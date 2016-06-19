package uk.org.freedonia.mapnetstat.gui;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ConnectionTable {

	public static final String IP_ADD_TITLE = "IP Address";
	public static final String IP_ADD_PROP = "ipAddress";

	public static final String PORT_TITLE = "Port";
	public static final String PORT_PROP = "port";
	
	public static final String PID_TITLE = "PID";
	public static final String PID_PROP = "pid";

	public static final String PROC_TITLE = "Process Name";
	public static final String PROC_PROP = "procName";
	
	public static final String MEM_USAGE_TITLE = "Memory Usage";
	public static final String MEM_USAGE_PROP = "memUsage";
	
	public static final String COUNTRY_TITLE = "Country";
	public static final String COUNTRY_PROP = "country";
	
	public static final String CITY_TITLE = "City";
	public static final String CITY_PROP = "city";
	
	public static final String LAT_TITLE = "Latitude";
	public static final String LAT_PROP = "latitude";
	
	public static final String LONG_TITLE = "Longitude";
	public static final String LONG_PROP = "longitude";
	
	@SuppressWarnings("unchecked")
	public TableView<GeoResultProperty> getTableView( ObservableList<GeoResultProperty> data ) {
		TableView<GeoResultProperty> table = new TableView<>();
		double colWidth = 160d;
		TableColumn<GeoResultProperty, String> ipCol = getStringTable( IP_ADD_TITLE, IP_ADD_PROP, colWidth );
		TableColumn<GeoResultProperty, String> portCol = getStringTable( PORT_TITLE, PORT_PROP, colWidth );
		TableColumn<GeoResultProperty, String> pidCol = getStringTable( PID_TITLE, PID_PROP, colWidth );
		TableColumn<GeoResultProperty, String> processNameCol =  getStringTable( PROC_TITLE, PROC_PROP, colWidth );
		TableColumn<GeoResultProperty, String> memUsage = getStringTable( MEM_USAGE_TITLE, MEM_USAGE_PROP, colWidth );
		TableColumn<GeoResultProperty, String> countryCol = getStringTable( COUNTRY_TITLE, COUNTRY_PROP, colWidth );
		TableColumn<GeoResultProperty, String> cityCol = getStringTable( CITY_TITLE, CITY_PROP, colWidth );
		TableColumn<GeoResultProperty, Double> latCol = getDoubleTable( LAT_TITLE, LAT_PROP, colWidth );
		TableColumn<GeoResultProperty, Double> longCol = getDoubleTable( LONG_TITLE, LONG_PROP, colWidth );
		table.getColumns().addAll( ipCol, portCol, processNameCol, memUsage, pidCol, countryCol, cityCol, latCol, longCol );
		table.setItems( data );
		table.setMinWidth( 300d );
		table.setMaxHeight( 300d );
		return table;
	}
	
	private TableColumn<GeoResultProperty,String> getStringTable( String title, String propName, double colWidth ) {
		TableColumn<GeoResultProperty, String> tableCol = new TableColumn<>( title );
		tableCol.setCellValueFactory(
                new PropertyValueFactory<GeoResultProperty, String>( propName ) );
		tableCol.setMinWidth( colWidth );
		return tableCol;
	}
	
	private TableColumn<GeoResultProperty,Double> getDoubleTable( String title, String propName, double colWidth ) {
		TableColumn<GeoResultProperty, Double> tableCol = new TableColumn<>( title );
		tableCol.setCellValueFactory(
                new PropertyValueFactory<GeoResultProperty, Double>( propName ) );
		tableCol.setMinWidth( colWidth );
		return tableCol;
	}
	
	
}
