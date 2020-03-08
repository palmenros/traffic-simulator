package simulator.model;

import java.util.List;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator {
	
	/**
	 * Road map where all simulation objects are stored
	 */
	private RoadMap _roadMap;
	
	/**
	 * List of events to be executed. Ordered by event time.
	 */
	private List<Event> _eventList;
	
	/**
	 * Simulation time step
	 */
	private int _timeStep = 0;
	
	
	public TrafficSimulator() {
		_roadMap = new RoadMap();
		_eventList = new SortedArrayList<Event>();
	}
	
	public void addEvent(Event e) {
		_eventList.add(e);
	}
	
	public void advance() {
		_timeStep++;

		while(!_eventList.isEmpty() && _eventList.get(0).getTime() == _timeStep) {
			Event e = _eventList.get(0);
			e.execute(_roadMap);
			_eventList.remove(0);
		}
		for(Junction junction : _roadMap.getJunctions()) {
			junction.advance(_timeStep);
		}
		
		for(Road road : _roadMap.getRoads()) {
			road.advance(_timeStep);
		}
	}
	
	public void reset() {
		_roadMap.reset();
		_eventList.clear();
		_timeStep = 0;
	}
	
	public JSONObject report() {
		JSONObject result = new JSONObject();

		result.put("time", _timeStep);
		result.put("state", _roadMap.report());
		
		return result;
	}
}
