package uk.org.freedonia.mapnetstat.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.MapNotInitializedException;
import com.lynden.gmapsfx.MapReadyListener;
import com.lynden.gmapsfx.javascript.JavaFxWebEngine;
import com.lynden.gmapsfx.javascript.JavascriptRuntime;
import com.lynden.gmapsfx.javascript.event.MapStateEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import uk.org.freedonia.mapnetstat.JeoNetstatUtil;
import uk.org.freedonia.mapnetstat.geo.GeoResult;
import uk.org.freedonia.mapnetstat.netstat.ProtocolOptions;

public class JeoGuiController implements  MapComponentInitializedListener {
	
	@FXML
 	private WebView mapView;
	@FXML
	private HBox optionBox;
	@FXML
	private RadioButton tcpButton;	
	@FXML
	private RadioButton udpButton;	
	@FXML
	private RadioButton allButton;
	@FXML
	private ToggleGroup protocolGroup;
	@FXML
	private CheckBox localCheckBox;
	@FXML
	private Button refreshButton;
	@FXML
	private VBox allBox;

	@FXML
	private TableView<GeoResultProperty> tableView;
	@FXML
	private ObservableList<GeoResultProperty> tableDataList;
	
	
    protected JavaFxWebEngine webengine;
    protected GoogleMap map;
    
    protected final List<MapComponentInitializedListener> mapInitializedListeners = new ArrayList<>();
    protected final List<MapReadyListener> mapReadyListeners = new ArrayList<>();


	
	private JeoData data;
	
	public JeoData getData() {
		return data;
	}

	public void setData(JeoData data) {
		this.data = data;
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

	private boolean initialized;
	
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
	    map = createMap(mapOptions); 
	    addMarkers();
	}
	
	   @FXML
	    public void initialize() {
	        tableDataList = tableView.getItems();
	        data.setData( tableDataList );
				initCheckBox();
				mapInitializedListeners.add(this);
		        webengine = new JavaFxWebEngine(mapView.getEngine());
		        webengine.load(getClass().getResource(getHTMLFile()).toExternalForm());
		        JavascriptRuntime.setDefaultWebEngine( webengine );
		        mapView.widthProperty().addListener(e -> mapResized());
			    mapView.heightProperty().addListener(e -> mapResized());
		        
			    mapView.widthProperty().addListener(e -> mapResized());
			    mapView.heightProperty().addListener(e -> mapResized());
		        webengine.getLoadWorker().stateProperty().addListener(
		                new ChangeListener<Worker.State>() {
		                    public void changed(ObservableValue<? extends State> ov, Worker.State oldState, Worker.State newState) {
		                        if (newState == Worker.State.SUCCEEDED) {
		                            setInitialized(true);
		                            fireMapInitializedListeners();
		                            
		                        }
		                    }
		                });
		}
	
    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    private void mapResized() {
        if (initialized) {
            webengine.executeScript("google.maps.event.trigger("+map.getVariableName()+", 'resize')");
        }
    }
    
    private String getHTMLFile() {
    	return "/html/maps.html";
    }
    
    public GoogleMap getMap() {
        checkInitialized();
        return map;
    }

    public GoogleMap createMap( MapOptions mapOptions ) {
        checkInitialized();
        map = new GoogleMap(mapOptions);
        map.addStateEventHandler(MapStateEventType.projection_changed, () -> {
            if (map.getProjection() != null) {
                mapResized();
                fireMapReadyListeners();
            }
        });
        
        return map;
    }
    
    protected void fireMapInitializedListeners() {
        synchronized (mapInitializedListeners) {
            for (MapComponentInitializedListener listener : mapInitializedListeners) {
                listener.mapInitialized();
            }
        }
    }

    protected void fireMapReadyListeners() {
        synchronized (mapReadyListeners) {
            for (MapReadyListener listener : mapReadyListeners) {
                listener.mapReady();
            }
        }
    }
    
    protected void checkInitialized() {
        if (!initialized) {
            throw new MapNotInitializedException();
        }
    }

    public GoogleMap createMap() {
        map = new GoogleMap();
        return map;
    }


    private void initCheckBox() {
    	if( data != null ) {
    		localCheckBox.setSelected( data.getResolverOptions().isDisplayLocalNetworkResults() );
    	}
	}

    
    @FXML
    protected void refreshResults() {
    	mapInitialized();
    }
    
    @FXML
    protected void handleCheckBox() {
    	data.getResolverOptions().setDisplayLocalNetworkResults(!data.getResolverOptions().isDisplayLocalNetworkResults());
    }


   

	@FXML
    protected void handleTCPRadioButton() {
    	data.getResolverOptions().setProtocolOptions(ProtocolOptions.TCP);
    }
    
    @FXML
    protected void handleUDPRadioButton() {
    	data.getResolverOptions().setProtocolOptions(ProtocolOptions.UDP);
    }
    
    @FXML
    protected void handleAllRadioButton() {
    	data.getResolverOptions().setProtocolOptions(ProtocolOptions.ALL);
    }

}
