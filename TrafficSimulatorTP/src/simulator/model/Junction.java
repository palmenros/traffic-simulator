package simulator.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {

	/**
	 * 	List of incoming roads
	 */
	private List<Road> _incomingRoads;
	
	
	/**
	 *  Map of outgoing roads
	 */
	private Map<Junction, Road> _outGoingRoads;
	
	/**
	 *	List of queues for incoming roads
	 */
	private List<List<Vehicle>> _queueList;
	
	/**
	 * Map of lists accessed by road, for more efficient search
	 */
	private Map<Road,List<Vehicle>> _roadQueueMap;
	
	/**
	 *	Index of incoming road whose light is green
	 *	-1 means that all incoming roads have their lights red
	 */
	private int _greenLightIndex; //No habría que inicializarlo a -1?
	
	/**
	 * Step when greenLightIndex has changed
	 */
	private int _lastLightChangeStep = 0;
	
	/**
	 * Light switching strategy
	 */
	private LightSwitchingStrategy _lightSwitchingStrategy;
	
	/**
	 * Dequeueing strategy
	 */
	private DequeuingStrategy _dequeuingStrategy;
	
	/**
	 * Junction coordinates in screen
	 */
	private int _x, _y;
	
	//TODO: Revisar tener actualizadas las listas y los mapas
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);

		if(lsStrategy == null) {  throw new IllegalArgumentException("Light switching strategy cannot be null"); }
		else if (dqStrategy == null) {  throw new IllegalArgumentException("Dequeueing strategy cannot be null"); }
		else if (xCoor < 0 || yCoor < 0 ) { throw new IllegalArgumentException("Coordinates cannot be negative"); }
		
		_lightSwitchingStrategy = lsStrategy;
		_dequeuingStrategy = dqStrategy;
		_x = xCoor;
		_y = yCoor;
		
		_incomingRoads = new LinkedList<Road>();
		_outGoingRoads = new HashMap<Junction, Road>();
		_queueList = new LinkedList<List<Vehicle>>();
		_roadQueueMap = new HashMap<Road,List<Vehicle>>();	
	}
	
	@Override
	void advance(int time) {
		
		//TODO: Review
		
		//Dequeuing
		
		if(_greenLightIndex != -1) {
			//There is a road with a green light
			
			List<Vehicle> dequeuedVehicles = _dequeuingStrategy.dequeue( _queueList.get(_greenLightIndex) );
			for(Vehicle v : dequeuedVehicles) {
				//Esto está mal; falta retirar los vehículos de las colas con:
				//_queueList.get(_greenLightIndex).remove(v);
				//Según el PDF (página 11 abajo) va aquí
				//Si estás de acuerdo descoméntalo
				v.moveToNextRoad();
			}
		}

		//Light switching
		int newIndex = _lightSwitchingStrategy.chooseNextGreen(_incomingRoads, _queueList, _greenLightIndex, _lastLightChangeStep, time);
		if(_greenLightIndex != newIndex) {
			_greenLightIndex = newIndex;
			_lastLightChangeStep = time;
		}
	}

	@Override
	public JSONObject report() {

		JSONObject result = new JSONObject();

		result.put("id", getId());
		
		String greenId;
		
		if(_greenLightIndex == -1) {
			greenId = "none";
		} else {
			greenId = _incomingRoads.get(_greenLightIndex).getId();
		}
		
		result.put("green", greenId);
		
		JSONArray ja = new JSONArray();
		
		for (Road r : _incomingRoads)
		{
			JSONObject queueJSON = new JSONObject();
			queueJSON.put("road", r.getId());

			JSONArray vehicleArray = new JSONArray();
			for(Vehicle v : _roadQueueMap.get(r)) {
				vehicleArray.put(v.getId());
			}
			queueJSON.put("vehicles", vehicleArray);
			
			ja.put(queueJSON);
		}
		result.put("queues",ja);
		
		return result;
	}

	void enter(Vehicle vehicle) {
		_roadQueueMap.get(vehicle.getCurrentRoad()).add(vehicle);
	}

	Road roadTo(Junction junction) {
		return _outGoingRoads.get(junction);
	}
	
	void addOutGoingRoad(Road r)
	{
		//We try to extract a road with the same destination, which shouldn't exist. If it does, throw an exception
		Road road = _outGoingRoads.get(r.getDestination());
		if (road != null) {throw new IllegalArgumentException("there is another road going to this road's destination");}
		if (r.getOrigin() != this) {throw new IllegalArgumentException("the current road's origin is not the current junction");}
		_outGoingRoads.put(r.getDestination(), r);
	}
	
	void addIncomingRoad(Road r)
	{
		if (r.getDestination()!=this) {throw new IllegalArgumentException("the current road's destination is not the current junction");}
		_incomingRoads.add(r);
		
		List<Vehicle> list = new LinkedList<Vehicle>();
		_queueList.add(list);
		_roadQueueMap.put(r, list);
	}

}
