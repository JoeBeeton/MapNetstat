package uk.org.freedonia.mapnetstat.gui;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import uk.org.freedonia.mapnetstat.geo.GeoResult;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResolverOptions;

public class JeoData {

	private List<GeoResult> geoResults;
	private ObservableList<GeoResultProperty> data = FXCollections.observableArrayList();
	private ConnectionResolverOptions resolverOptions;
	private TableView<GeoResultProperty> table = new TableView<GeoResultProperty>();
	
	
	public ConnectionResolverOptions getResolverOptions() {
		return resolverOptions;
	}
	public void setResolverOptions(ConnectionResolverOptions resolverOptions) {
		this.resolverOptions = resolverOptions;
	}
	public TableView<GeoResultProperty> getTable() {
		return table;
	}
	public void setTable(TableView<GeoResultProperty> table) {
		this.table = table;
	}
	public ObservableList<GeoResultProperty> getData() {
		return data;
	}
	public void setData(ObservableList<GeoResultProperty> data) {
		this.data = data;
	}
	
	
	
}
