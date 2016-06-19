package uk.org.freedonia.mapnetstat.netstat.pidresolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import uk.org.freedonia.mapnetstat.netstat.ProcessResult;

public class LinuxPidInfoReader implements PidReader {
	
	private static final String PID_NAME = "pid";
	
	
	public Map<Integer, ProcessResult> parseResults( BufferedReader reader ) throws IOException {
		ProcConsumer consumer = new ProcConsumer();
		reader.lines().forEach(consumer);
		return consumer.getResults();
	}
	
	private class ProcConsumer implements Consumer<String> {
		private Map<Integer, ProcessResult> results = new HashMap<>();
		@Override
		public void accept(String line) {
			if ( line != null ) {
				String trimmedLine = line.trim();
				if ( !trimmedLine.toLowerCase().startsWith( PID_NAME ) ) {
					String[] elements = trimmedLine.split(" +");
					if ( elements.length >= 4 ) {
						String pidString = elements[0];
						String memString = elements[1];
						String cmdString = elements[3];
						Integer pid = Integer.parseInt(pidString);
						Long mem = Long.parseLong(memString);
						results.put( pid, new ProcessResult( pid, cmdString, mem ) );
					}
				}
			}
		}

		public Map<Integer, ProcessResult> getResults() {
			return results;
		}

	}


	
	

}
