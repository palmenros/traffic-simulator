package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	
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
	
	/**
	 * List of observers
	 */
	private List<TrafficSimObserver> _observerList;
	
	
	public TrafficSimulator() {
		_roadMap = new RoadMap();
		_eventList = new SortedArrayList<Event>();
		_observerList = new ArrayList<TrafficSimObserver>();
	}
	
	//TODO: Where should we catch onError observer?
	//TODO: Should all functions have try and catch?
	
	public void addEvent(Event e) {
		try {
			_eventList.add(e);
			for(TrafficSimObserver o : _observerList) {
				//We pass the RoadMap because all of its getters return unmodifiable lists / objects
				o.onEventAdded(_roadMap, Collections.unmodifiableList(_eventList), e, _timeStep);
			}
		} catch(Exception exception) {
			for(TrafficSimObserver o : _observerList) {
				o.onError(exception.getMessage());
			}	
			throw exception;
		}
	}
	
	public void advance() {
		try {
			_timeStep++;
	
			for(TrafficSimObserver o : _observerList) {
				//We pass the RoadMap because all of its getters return unmodifiable lists / objects
				o.onAdvanceStart(_roadMap, Collections.unmodifiableList(_eventList), _timeStep);
			}
			
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
			
			for(TrafficSimObserver o : _observerList) {
				//We pass the RoadMap because all of its getters return unmodifiable lists / objects
				o.onAdvanceEnd(_roadMap, Collections.unmodifiableList(_eventList), _timeStep);
			}
		} catch(Exception exception) {
			for(TrafficSimObserver o : _observerList) {
				o.onError(exception.getMessage());
			}	
			throw exception;
		}
	}
	
	public void reset() {
		try {
			_roadMap.reset();
			_eventList.clear();
			_timeStep = 0;
			
			for(TrafficSimObserver o : _observerList) {
				//We pass the RoadMap because all of its getters return unmodifiable lists / objects
				o.onReset(_roadMap, Collections.unmodifiableList(_eventList), _timeStep);
			}
		} catch(Exception exception) {
			for(TrafficSimObserver o : _observerList) {
				o.onError(exception.getMessage());
			}	
			throw exception;
		}
	}
	
	public JSONObject report() {
		try {
			JSONObject result = new JSONObject();
	
			result.put("time", _timeStep);
			result.put("state", _roadMap.report());
			return result;
			
		} catch(Exception exception) {
			for(TrafficSimObserver o : _observerList) {
				o.onError(exception.getMessage());
			}	
			throw exception;
		}
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		try {
			_observerList.add(o);
			
			//TODO: Call on register before or after adding new observer?
			//TODO: Call on register on all observers or only one?
			for(TrafficSimObserver obj : _observerList) {
				//We pass the RoadMap because all of its getters return unmodifiable lists / objects
				obj.onRegister(_roadMap, Collections.unmodifiableList(_eventList), _timeStep);
			}
		} catch(Exception exception) {
			for(TrafficSimObserver obj : _observerList) {
				obj.onError(exception.getMessage());
			}	
			throw exception;
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		try {
			_observerList.remove(o);
		} catch(Exception exception) {
			for(TrafficSimObserver obj : _observerList) {
				obj.onError(exception.getMessage());
			}	
			throw exception;
		}
	}
}
