package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller _controller;
	private List<EventTransfer> _events;
	
	//TODO: Review if these are the correct _colNames
	private String[] _colNames = { "Time", "Description" };

	public EventsTableModel(Controller ctrl) {
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
	
	public void setEventsList(List<Event> eventList) {
		List<EventTransfer> events = new ArrayList<EventTransfer>();
		for(Event e : eventList) {
			events.add(new EventTransfer(e.getTime(), e.toString()));
		}
		
		_events = events;
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
		return _events == null ? 0 : _events.size();
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
			s = _events.get(rowIndex).getTime();
			break;
		case 1:
			s = _events.get(rowIndex).getDescription();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setEventsList(events);		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setEventsList(events);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		setEventsList(events);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		setEventsList(events);		
	}

	@Override
	public void onError(String err) {		
	}
}
