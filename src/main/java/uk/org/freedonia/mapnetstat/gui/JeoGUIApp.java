package uk.org.freedonia.mapnetstat.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResolverOptions;

public class JeoGUIApp extends  Application {

	
	public static void main(String[] args) {
        Application.launch(JeoGUIApp.class, args);
    }
	


	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("MapNetstat");
		StackPane myGroup = null;
	    JeoGuiController controller = new JeoGuiController();
	    JeoData data = new JeoData();
		data.setResolverOptions(new ConnectionResolverOptions());
        controller.setData(data);
	        try{
	        	FXMLLoader loader = new FXMLLoader(JeoGUIApp.class.getResource("/Jeo_Gui.xml.fxml"));
	        	loader.setController(controller);
	            myGroup = (StackPane) loader.load();
	        }catch (IOException ioe){
	            ioe.printStackTrace();
	        }
	   if (myGroup != null) {
		   Scene scene = new Scene( myGroup );
	       primaryStage.setScene(scene); 
	       primaryStage.show();
	   }
	}

}
