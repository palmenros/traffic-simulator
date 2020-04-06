package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class RoadTableModel extends AbstractTableModel implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller _controller;
	private List<RoadTransfer> _roads;
	
	//TODO: Review if these are the correct _colNames
	private String[] _colNames = { "Id", "Length", "Weather", "Max Speed", "Speed Limit", "Total C02", "Distance" };

	public RoadTableModel() {
		_roads=null;
	}

	public RoadTableModel(Controller ctrl) {
		_controller = ctrl;
		_controller.addObserver(this);
	}

	public void update() {
		// observar que si no refresco la tabla no se carga
		// La tabla es la represantación visual de una estructura de datos,
		// en este caso de un ArrayList, hay que notificar los cambios.
		
		// We need to notify changes, otherwise the table does not refresh.
		fireTableDataChanged();		
	}
	
	public void setRoadList(List<Road> roadList) {
		List<RoadTransfer> roads = new ArrayList<RoadTransfer>();
		for(Road r : roadList) {
			
			/*
			 * 	public RoadTransfer(String _id, Integer _length, Weather _weather, Integer _maxSpeed, Integer _speedLimit,
			Integer _totalC02, Integer _co2Limit) {
			 */
			roads.add(new RoadTransfer(
						r.getId(),
						r.getLength(),
						r.getWeather(),
						r.getMaxSpeed(),
						r.getSpeedLimit(),
						r.getTotalCO2(),
						r.getCO2Limit()
					));
		}
	
		_roads = roads;
		update();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	//si no pongo esto no coge el nombre de las columnas
	//
	//this is for the column header
	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	// método obligatorio, probad a quitarlo, no compila
	//
	// this is for the number of columns
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	// método obligatorio
	//
	// the number of row, like those in the events list
	public int getRowCount() {
		return _roads == null ? 0 : _roads.size();
	}

	@Override
	// método obligatorio
	// así es como se va a cargar la tabla desde el ArrayList
	// el índice del arrayList es el número de fila pq en este ejemplo
	// quiero enumerarlos.
	//
	// returns the value of a particular cell 
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _roads.get(rowIndex).getId();
			break;
		case 1:
			s = _roads.get(rowIndex).getLength();
			break;
		case 2:
			s = _roads.get(rowIndex).getWeather();
			break;
		case 3:
			s = _roads.get(rowIndex).getMaxSpeed();
			break;
		case 4:
			s = _roads.get(rowIndex).getSpeedLimit();
			break;
		case 5:
			s = _roads.get(rowIndex).getTotalC02();
			break;
		case 6:
			s = _roads.get(rowIndex).getCo2Limit();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setRoadList(map.getRoads());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		setRoadList(map.getRoads());		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		setRoadList(map.getRoads());		
	}

	@Override
	public void onError(String err) {		
	}
}
