package uk.org.freedonia.mapnetstat.gui;

import java.io.IOException;
import java.util.List;

import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import uk.org.freedonia.mapnetstat.JeoNetstatUtil;
import uk.org.freedonia.mapnetstat.geo.GeoResult;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResolverOptions;


public class JeoGui extends Application implements MapComponentInitializedListener {




	private MapView mapView;
	private GoogleMap map;
	
	private JeoData data = new JeoData();

	
	
	@Override
	public void start(Stage stage) throws Exception {
		data = new JeoData();
		data.setResolverOptions(new ConnectionResolverOptions());
		initTable();
	    mapView = new MapView( data, this );
	    mapView.addMapInializedListener(this);
	    ScrollPane pane = new ScrollPane(mapView);
	    Scene scene = new Scene(pane);
	    stage.setTitle("JeoNetStatMap");
	    stage.setScene(scene);
	    stage.show();
	}


	@Override
	public void mapInitialized() {
	    MapOptions mapOptions = new MapOptions();
	
	    mapOptions.center(new LatLong(0,0))
	            .overviewMapControl(false)
	            .panControl(false)
	            .rotateControl(false)
	            .scaleControl(false)
	            .streetViewControl(false)
	            .zoomControl(true)
	            .zoom(2);
	    map = mapView.createMap(mapOptions);
	    addMarkers();
	}
	
	private void initTable() {
		data.setTable( new ConnectionTable().getTableView( data.getData() ) );
	}

	private void addMarkers() {
		try {
			List<GeoResult> results = JeoNetstatUtil.getCurrentConnections( data.getResolverOptions() );
			data.getData().removeAll(data.getData());
			for ( GeoResult result : results ) {
				if ( result.getLatitude() != null && result.getLongitude() != null ) {
					LatLong location = new LatLong(result.getLatitude(), result.getLongitude());
					MarkerOptions option = new MarkerOptions();
					option.position(location).title(getTitle( result ) );
			        Marker marker = new Marker(option);
			        map.addMarker( marker );
				}
				data.getData().add(new GeoResultProperty( result ));
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String getTitle( GeoResult result ) {
		StringBuilder msg = new StringBuilder();
		msg.append(result.getIp());
		msg.append( "\n" );
		msg.append(result.getCountryName());
		if ( result.getCity() != null ) {
			msg.append( "\n" );
			msg.append(result.getCity());
		}
		if ( result.getConnResult() != null && result.getConnResult().getProcessName() != null && !result.getConnResult().getProcessName().isEmpty() ) {
			msg.append( "\n" );
			msg.append( result.getConnResult().getProcessName() );
		}
		if ( result.getConnResult() != null ) {
			msg.append( "\n" );
			msg.append( "PID : " + result.getConnResult().getPid() );
		}
		return msg.toString();
	}


	public static void main(String[] args) {
	    launch(args);
	}
	

}