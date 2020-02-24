package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	/**
	 *  Junction list
	 */
	private List<Junction> _junctionList;
	
	/**
	 * Road list
	 */
	private List<Road> _roadList;

	/**
	 * Vehicle list
	 */
	private List<Vehicle> _vehicleList;
	
	/**
	 * Junction id to junction map
	 */
	private Map<String, Junction> _junctionMap;

	/**
	 * Road id to road map
	 */
	private Map<String, Road> _roadMap;
	
	/**
	 * Vehicle id to vehicle map
	 */
	private Map<String, Vehicle> _vehicleMap;
	
	//TODO: Revisar tener actualizadas las listas y los mapas
	
	RoadMap() {
		_junctionList = new ArrayList<Junction>();
		_roadList = new ArrayList<Road>();
		_vehicleList = new ArrayList<Vehicle>();
		
		_junctionMap = new TreeMap<String, Junction>();
		_roadMap = new TreeMap<String, Road>();
		_vehicleMap = new TreeMap<String, Vehicle>();
	}
	
	void addJunction(Junction j) {
		if(_junctionMap.get(j.getId()) != null) { throw new IllegalArgumentException("There is already a junction with given identifier"); }
		_junctionList.add(j);
		_junctionMap.put(j.getId(), j);
	}
	
	void addRoad(Road r) {
		if(_roadMap.get(r.getId()) != null) { throw new IllegalArgumentException("There is already a road with given identifier"); }
		
		Junction o = r.getOrigin(), d = r.getDestination();
		if( _junctionMap.get(o.getId()) == null || _junctionMap.get(d.getId()) == null) { throw new IllegalArgumentException("Given road junctions are not included in road map"); };
			
		_roadList.add(r);
		_roadMap.put(r.getId(), r);
	}
	
	void addVehicle(Vehicle v) {
		if(_vehicleMap.get(v.getId()) != null) { throw new IllegalArgumentException("There is already a vehicle with given identifier"); }

		//Test that the vehicle itinerary is valid: There exist roads connecting consecutive junctions of its itinerary
		
		List<Junction> itinerary = v.getItinerary();
		
		for(int i = 0; i < itinerary.size() - 1; i++) {
			Road r = itinerary.get(i).roadTo(itinerary.get(i + 1));
			if(r == null || _roadMap.get(r.getId()) == null) { throw new IllegalArgumentException("Vehicle itinerary is not valid"); }	
		}
		
		_vehicleList.add(v);
		_vehicleMap.put(v.getId(), v);
	}
	
	public Junction getJunction(String id) {
		return _junctionMap.get(id);
	}
	
	public Road getRoad(String id) {
		return _roadMap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return _vehicleMap.get(id);
	}
	
	public List<Junction> getJunctions() {
		return Collections.unmodifiableList(_junctionList);
	}
	
	public List<Road> getRoads() {
		return Collections.unmodifiableList(_roadList);
	}
	
	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(_vehicleList);
	}
	
	void reset() {
		_junctionList.clear();
		_roadList.clear();
		_vehicleList.clear();
		
		_junctionMap.clear();
		_roadMap.clear();
		_vehicleMap.clear();		
	}
	
	public JSONObject report() {
		JSONObject result = new JSONObject();
		
		JSONArray junctionArray = new JSONArray();
		for (Junction j : _junctionList)
		{
			junctionArray.put(j.report());
		}
		result.put("junctions", junctionArray);
		
		JSONArray roadArray = new JSONArray();
		for (Road r : _roadList)
		{
			roadArray.put(r.report());
		}
		result.put("road", roadArray);
		
		JSONArray vehicleArray = new JSONArray();
		for (Vehicle v : _vehicleList)
		{
			vehicleArray.put(v.report());
		}
		result.put("vehicles", vehicleArray);	
		
		return result;
	}
	
}
