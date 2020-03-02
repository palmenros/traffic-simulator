package simulator.model;

import java.util.List;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator {
	
	/**
	 * Road map where all simulation objects are stored
	 */
	private RoadMap roadMap;
	
	/**
	 * List of events to be executed. Ordered by event time.
	 */
	private List<Event> eventList;
	
	/**
	 * Simulation time step
	 */
	private int timeStep = 0;
	
	
	public TrafficSimulator() {
		roadMap = new RoadMap();
		eventList = new SortedArrayList<Event>();
	}
	
	public void addEvent(Event e) {
		eventList.add(e);
	}
	
	public void advance() {
		timeStep++;

		while(!eventList.isEmpty() && eventList.get(0).getTime() == timeStep) {
			Event e = eventList.get(0);
			e.execute(roadMap);
			eventList.remove(0);
		}

		for(Junction junction : roadMap.getJunctions()) {
			junction.advance(timeStep);
		}
		
		for(Road road : roadMap.getRoads()) {
			road.advance(timeStep);
		}
	}
	
	public void reset() {
		roadMap.reset();
		eventList.clear();
		timeStep = 0;
	}
	
	public JSONObject report() {
		JSONObject result = new JSONObject();

		result.put("time", timeStep);
		result.put("state", roadMap.report());
		
		return result;
	}
}
