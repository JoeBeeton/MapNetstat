package uk.org.freedonia.mapnetstat.netstat.pidresolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import uk.org.freedonia.mapnetstat.netstat.ProcessResult;

public class WindowsPidInfoReader implements PidReader {
	
	private static final String PROC_NAME = "Image Name";
	private static final String PID_NAME = "PID";
	private static final String MEM_USAGE = "Mem Usage";
	

	@Override
	public Map<Integer, ProcessResult> parseResults( BufferedReader reader ) throws IOException {
		Map<Integer, ProcessResult> results = new HashMap<>();
        try ( CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader() ) ) {
			for ( CSVRecord record : parser ) {
				int pid = Integer.parseInt( record.get(PID_NAME) );
				results.put(pid, new ProcessResult( pid, record.get( PROC_NAME ), getMemFromString( record.get( MEM_USAGE ) ) ) );
			}
		}
		return results;
	}
	
	private long getMemFromString( String memString ) {
		long mem = 0;
		if ( memString != null ) {
			memString = memString.replaceAll(",", "");
			memString = memString.replaceAll("K", "");
			try {
				mem = Long.parseLong(memString.trim())*1024;
			} catch ( NumberFormatException e ) {
				System.err.println(e.getMessage());
			}
		}
		return mem;
	}


}
