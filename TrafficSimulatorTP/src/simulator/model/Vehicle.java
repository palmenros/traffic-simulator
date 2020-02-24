package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

/**
 * @author Martín Gómez y Pedro Palacios
 *
 */
public class Vehicle extends SimulatedObject {

	
	/**
	 * List of junctions the vehicle must go through
	 */
	private List<Junction> _itinerary;
	/**
	 * Max speed of the vehicle
	 */
	private int _maxSpeed;
	/**
	 * Current speed of the vehicle
	 */
	private int _currentSpeed = 0;
	/**
	 * Current status of the vehicle
	 */
	private VehicleStatus _status = VehicleStatus.PENDING;
	/**
	 * Current road the vehicle is in
	 */
	private Road _currentRoad=null;
	/**
	 * Current position inside the road
	 */
	private int _currentDistanceInRoad=0;
	/**
	 * To what extent the vehicle pollutes
	 */
	private int _pollutionMeasure;
	/**
	 * Total pollution produced
	 */
	private int _totalPollution=0;
	/**
	 * Total distance traveled
	 */
	private int _totalDistance=0;
	
	/**
	 * Index of last visited junction
	 * -1 if vehicle hasn't entered yet any junction
	 */
	private int _lastJunctionIndex = -1;
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary)
	{
		super(id);
		
		if(maxSpeed <= 0) {
			throw new IllegalArgumentException("maxSpeed in Vehicle must be greater than 0");
		}
		_maxSpeed = maxSpeed;
		
		if(!( contClass >= 0 && contClass <= 10 )) {
			throw new IllegalArgumentException("contClass in Vehicle must be between 0 and 10 (both included)");			
		}
		_pollutionMeasure = contClass;	
		
		if(itinerary.size() < 2) {
			throw new IllegalArgumentException("itinerary in Vehicle must have at least two junctions");
		}
		_itinerary = Collections.unmodifiableList(new ArrayList<Junction>(itinerary));
	}
	
	void setSpeed(int s) {
		if(s < 0) {
			throw new IllegalArgumentException("Vehicle speed must be non negative");
		}
		_currentSpeed = Math.min(s, _maxSpeed);
	}
	
	void setContaminationClass(int c) {
		if(!( c >= 0 && c <= 10 )) {
			throw new IllegalArgumentException("Vehicle contamination class must be between 0 and 10 (both included)");			
		}
		_pollutionMeasure = c;
	}
	
	@Override
	void advance(int time) {
		
		//TODO: Review
		
		if (_status!=VehicleStatus.TRAVELING) {
			return;
		}
		
		int previousPosition = _currentDistanceInRoad;
		_currentDistanceInRoad = Math.min(_currentDistanceInRoad+_currentSpeed, _currentRoad.getLength());
		
		int pollutionProduced = _pollutionMeasure*(_currentDistanceInRoad-previousPosition);
		_totalPollution+=pollutionProduced;
		_currentRoad.addContamination(pollutionProduced);
		
		if (_currentDistanceInRoad == _currentRoad.getLength()) {
			
			//Set vehicle state
			_status = VehicleStatus.WAITING;	
			_currentSpeed = 0;
						
			//Enter junction
			_lastJunctionIndex++;
			_itinerary.get(_lastJunctionIndex).enter(this);
		}

	}

	void moveToNextRoad() {
	
		//TODO: Review
		
		if(_status == VehicleStatus.PENDING)
		{
			_lastJunctionIndex = 0;
			Junction junction = _itinerary.get(_lastJunctionIndex);
			_currentRoad = junction.roadTo(_itinerary.get(_lastJunctionIndex+1));
			_currentRoad.enter(this);
			_currentDistanceInRoad = 0;
			_status = VehicleStatus.TRAVELING;
			
		} else if(_status == VehicleStatus.WAITING) {
			//Exit road
			_currentRoad.exit(this);

			//Enter new road	
			Junction junction = _itinerary.get(_lastJunctionIndex);
			_currentRoad = junction.roadTo(_itinerary.get(_lastJunctionIndex+1));
			
			if(_currentRoad != null) {
				_currentRoad.enter(this);
				_currentDistanceInRoad = 0;	
				_status = VehicleStatus.TRAVELING;
			} else {
				_status = VehicleStatus.ARRIVED;
			}
			
		} else {
			//TODO: Find more appropiate exception subclass
			throw new IllegalStateException("Vehicle must be waiting or pending");
		}
		
		
	}
	
	@Override
	public JSONObject report() {
		
		JSONObject result = new JSONObject();

		result.put("id", getId());
		result.put("speed", _currentSpeed);
		result.put("distance", _totalDistance);
		result.put("co2", _totalPollution);
		result.put("class", _pollutionMeasure);
		result.put("status", _status.toString());
		
		if(_status != VehicleStatus.PENDING && _status != VehicleStatus.ARRIVED) {
			result.put("road", _currentRoad.getId());
			result.put("location", _currentDistanceInRoad);			
		}
		
		return result;
	}

	int getPosition() {
		return _currentDistanceInRoad;
	}

	int getSpeed() {
		return _currentSpeed;
	}

	Road getCurrentRoad() {
		return _currentRoad;
	}

	List<Junction> getItinerary() {
		return Collections.unmodifiableList(_itinerary);
	}

	public int getPollutionMeasure() {
		return _pollutionMeasure;
	}


}
