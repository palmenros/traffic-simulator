package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

/**
 * @author Mart�n G�mez y Pedro Palacios
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
		if (_status==VehicleStatus.TRAVELING)
		{
			_currentSpeed = Math.min(s, _maxSpeed);
		}
	}
	
	void setContaminationClass(int c) {
		if(!( c >= 0 && c <= 10 )) {
			throw new IllegalArgumentException("Vehicle contamination class must be between 0 and 10 (both included)");			
		}
		_pollutionMeasure = c;
	}
	
	@Override
	void advance(int time) {
				
		if (_status!=VehicleStatus.TRAVELING) {
			return;
		}
		
		int previousPosition = _currentDistanceInRoad;
		_currentDistanceInRoad = Math.min(_currentDistanceInRoad+_currentSpeed, _currentRoad.getLength());
		
		int deltaPosition = _currentDistanceInRoad-previousPosition;
		_totalDistance += deltaPosition;
		
		int pollutionProduced = _pollutionMeasure*(deltaPosition);
		_totalPollution+=pollutionProduced;
		_currentRoad.addContamination(pollutionProduced);
		
		if (_currentDistanceInRoad >= _currentRoad.getLength()) { //Same as == but the directives say >=
			
			//Set vehicle state
			_status = VehicleStatus.WAITING;	
			_currentSpeed = 0;
						
			//Enter junction
			_lastJunctionIndex++;
			_itinerary.get(_lastJunctionIndex).enter(this);
		}

	}

	void moveToNextRoad() {
			
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

			//Check if the vehicle is at the end of the itinerary
			if(_lastJunctionIndex + 1 < _itinerary.size()) {
				//Enter new road
				Junction junction = _itinerary.get(_lastJunctionIndex);
				_currentRoad = junction.roadTo(_itinerary.get(_lastJunctionIndex+1));
				
				_currentDistanceInRoad = 0;	
				_currentRoad.enter(this);
				
				_status = VehicleStatus.TRAVELING;
			} else {
				_currentRoad = null;
				_status = VehicleStatus.ARRIVED;
			}
			
		} else {
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

	public int getSpeed() {
		return _currentSpeed;
	}

	public Road getCurrentRoad() {
		return _currentRoad;
	}

	public List<Junction> getItinerary() {
		return Collections.unmodifiableList(_itinerary);
	}

	public int getPollutionMeasure() {
		return _pollutionMeasure;
	}

	public Integer getLocation() {
		return _currentDistanceInRoad;
	}

	public Integer getContClass() {
		return _pollutionMeasure;
	}

	public VehicleStatus getStatus() {
		return _status;
	}

	public Integer getMaxSpeed() {
		return _maxSpeed;
	}

	public Integer getTotalPollution() {
		return _totalPollution;
	}

	public Integer getTotalDistance() {
		return _totalDistance;
	}


}
