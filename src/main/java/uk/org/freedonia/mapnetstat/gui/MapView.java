package uk.org.freedonia.mapnetstat.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.MapNotInitializedException;
import com.lynden.gmapsfx.MapReadyListener;
import com.lynden.gmapsfx.javascript.JavaFxWebEngine;
import com.lynden.gmapsfx.javascript.JavascriptRuntime;
import com.lynden.gmapsfx.javascript.event.MapStateEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class MapView extends AnchorPane {

	
		public MapView(){}
	
		private boolean debug;
	
		@FXML
	 	private WebView webview;
		@FXML
		private HBox optionBox;
		@FXML
		private RadioButton tcpButton;
		
		private JeoData data;
	    protected JavaFxWebEngine webengine;
	    protected boolean initialized = false;
	    protected final CyclicBarrier barrier = new CyclicBarrier(2);
	    protected final List<MapComponentInitializedListener> mapInitializedListeners = new ArrayList<>();
	    protected final List<MapReadyListener> mapReadyListeners = new ArrayList<>();
	    protected GoogleMap map;
	    
	    public MapView(JeoData jeoGui, MapComponentInitializedListener mapInit ){
	    	this(jeoGui, mapInit, false);
	    }

	    
	    public MapView( JeoData data, MapComponentInitializedListener mapInit, boolean debug ) {
	    	webview = new WebView();
	        webengine = new JavaFxWebEngine(webview.getEngine());
	        JavascriptRuntime.setDefaultWebEngine( webengine );
	        
	        setTopAnchor(webview,0.0);
	        HBox optionBox = new HBox(10);
	        initRefreshButton( mapInit, optionBox );
	        optionBox.getChildren().addAll( getProtocolToggle( data ) );
	        optionBox.getChildren().add( getIsLocalCheckBox(data) );
		    VBox vbox = new VBox();
		    vbox.setFillWidth(true);
		    vbox.setMinWidth(500d);
		    vbox.setPadding(new Insets(0, 10, 10, 10));
		    vbox.setSpacing(10d);
		    vbox.getChildren().add(webview);
		    vbox.getChildren().add(optionBox);
		    vbox.getChildren().add(data.getTable());
		   
		    setBottomAnchor(vbox,0.0);
		    getChildren().add(vbox);
	        
	        
	        webview.widthProperty().addListener(e -> mapResized());
	        webview.heightProperty().addListener(e -> mapResized());
	        
	        webview.widthProperty().addListener(e -> mapResized());
	        webview.heightProperty().addListener(e -> mapResized());
	        webengine.getLoadWorker().stateProperty().addListener(
	                new ChangeListener<Worker.State>() {
	                    public void changed(ObservableValue<? extends State> ov, Worker.State oldState, Worker.State newState) {
	                        if (newState == Worker.State.SUCCEEDED) {
	                            setInitialized(true);
	                            fireMapInitializedListeners();
	                            
	                        }
	                    }
	                });
	        webengine.load(getClass().getResource(getHTMLFile()).toExternalForm());
	    	
	    }
	    
	    private void initRefreshButton( MapComponentInitializedListener mapInit, HBox optionBox ) {
	    	Button btn = new Button();
	        btn.setText("Refresh Results");
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	mapInit.mapInitialized();
	            }
	        });
	        optionBox.getChildren().add(btn);
	    }
	    
	    
	    private CheckBox getIsLocalCheckBox( JeoData data ) {
	    /**	CheckBox isLocal = new CheckBox( "Show Local Network Connections" );
	    	isLocal.setSelected( data.getResolverOptions().isDisplayLocalNetworkResults());
	    	isLocal.setTooltip(new Tooltip( "Display connections to private IP addresses." ));
	    	isLocal.selectedProperty().addListener(new ChangeListener<Boolean>() {
	    		@Override
	            public void changed(ObservableValue<? extends Boolean> ov,
	                    Boolean oldVal, Boolean newVal ) {
	    					data.getResolverOptions().setDisplayLocalNetworkResults(newVal);
	                }
	            });
	    	return isLocal;**/
	    	return null;
	    }
	    

	
	    private List<RadioButton> getProtocolToggle( JeoData data ) {
//	    	List<RadioButton> buttonList = new ArrayList<>();
//	    	ToggleGroup group = new ToggleGroup();
//	    	RadioButton tcpButton = new RadioButton("TCP");
//	    	tcpButton.setSelected(true);
//	    	buttonList.add( tcpButton );
//	    	tcpButton.setOnAction(new EventHandler<ActionEvent>() {
//	            @Override
//	            public void handle(ActionEvent event) {
//	            	data.getResolverOptions().setProtocolOptions(ProtocolOptions.TCP);
//	            }
//	        });
//	    	tcpButton.setTooltip(new Tooltip("Only display TCP connections"));
//	    	tcpButton.setToggleGroup(group);
//	    	RadioButton udpButton = new RadioButton("UDP");
//	    	buttonList.add( udpButton );
//	    	udpButton.setOnAction(new EventHandler<ActionEvent>() {
//	            @Override
//	            public void handle(ActionEvent event) {
//	            	data.getResolverOptions().setProtocolOptions(ProtocolOptions.UDP);
//	            }
//	        });
//	    	udpButton.setTooltip(new Tooltip("Only display UDP connections"));
//	    	udpButton.setToggleGroup(group);
//	    	RadioButton allButton = new RadioButton("All");
//	    	buttonList.add( allButton );
//	    	allButton.setOnAction(new EventHandler<ActionEvent>() {
//	            @Override
//	            public void handle(ActionEvent event) {
//	            	data.getResolverOptions().setProtocolOptions(ProtocolOptions.ALL);
//	            }
//	        });
//	    	allButton.setTooltip(new Tooltip("Displayes all connections"));
//	    	allButton.setToggleGroup(group);
//
//	    	return buttonList;
	    	return null;
	    }
	    
	    private String getHTMLFile() {
	    	String htmlFile;
	        if( debug ) {
	            htmlFile = "/html/maps-debug.html";
	        } else {
	            htmlFile = "/html/maps.html";
	        }
	        return htmlFile;
	    }
	    
	    private void mapResized() {
	        if (initialized) {
	            webengine.executeScript("google.maps.event.trigger("+map.getVariableName()+", 'resize')");
	        }
	    }
	    
	    public void setZoom(int zoom) {
	        checkInitialized();
	        map.setZoom(zoom);
	    }

	    public void setCenter(double latitude, double longitude) {
	        checkInitialized();
	        LatLong latLong = new LatLong(latitude, longitude);
	        map.setCenter(latLong);
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

	    public GoogleMap createMap() {
	        map = new GoogleMap();
	        return map;
	    }

	    public void addMapInializedListener(MapComponentInitializedListener listener) {
	        synchronized (mapInitializedListeners) {
	            mapInitializedListeners.add(listener);
	        }
	    }

	    public void removeMapInitializedListener(MapComponentInitializedListener listener) {
	        synchronized (mapInitializedListeners) {
	            mapInitializedListeners.remove(listener);
	        }
	    }
	    
	public void addMapReadyListener(MapReadyListener listener) {
	        synchronized (mapReadyListeners) {
	            mapReadyListeners.add(listener);
	        }
	    }

	    public void removeReadyListener(MapReadyListener listener) {
	        synchronized (mapReadyListeners) {
	            mapReadyListeners.remove(listener);
	        }
	    }
	    
	    public Point2D fromLatLngToPoint(LatLong loc) {
	        checkInitialized();
	        return map.fromLatLngToPoint(loc);
	    }
	    
	    public void panBy(double x, double y) {
	        checkInitialized();
	        map.panBy(x, y);
	    }
	    
	    protected void init() {

	    }

	    protected void setInitialized(boolean initialized) {
	        this.initialized = initialized;
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
	    
	    protected JSObject executeJavascript(String function) {
	        Object returnObject = webengine.executeScript(function);
	        return (JSObject) returnObject;
	    }

	    protected String getJavascriptMethod(String methodName, Object... args) {
	        StringBuilder sb = new StringBuilder();
	        sb.append(methodName).append("(");
	        for (Object arg : args) {
	            sb.append(arg).append(",");
	        }
	        sb.replace(sb.length() - 1, sb.length(), ")");

	        return sb.toString();
	    }

	    protected void checkInitialized() {
	        if (!initialized) {
	            throw new MapNotInitializedException();
	        }
	    }
	    
	    public class JSListener { 
	        public void log(String text){
	            System.out.println(text);
	        }
	    }

}
