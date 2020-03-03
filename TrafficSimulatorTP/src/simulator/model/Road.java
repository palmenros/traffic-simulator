package simulator.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

/**
 * @author Martín Gómez y Pedro Palacios
 *
 */
public abstract class Road extends SimulatedObject {

	/**
	 * Junction where the road begins
	 */
	private Junction _origin;
	
	/**
	 * Junction where the road ends
	 */
	private Junction _destination;
	
	/**
	 * Length of the road
	 */
	private int _length;
	
	/**
	 * Maximum speed of the road
	 */
	protected int _maxSpeedLimit;
	
	/**
	 * Current maximum speed (depending on pollution enz.)
	 */
	protected int _currentSpeedLimit;
	
	/**
	 * Threshold at which restriction are imposed
	 */
	protected int _pollutionThreshold;
	
	/**
	 * The current weather on the road
	 */
	protected Weather _currentWeather;
	
	/**
	 * Current pollution on the road
	 *
	 * At the beginning there have been no cars on the road, so there is no pollution
	 */
	protected int _totalPollution = 0;
	
	/**
	 * Current vehicles on the road, ordered depending on their position
	 */
	private List<Vehicle> _currentVehicles;
	
	/**
	 * Compare two vehicles by road position
	 */
	private final Comparator<Vehicle> _vehicleComparator = new Comparator<Vehicle>() {
		public int compare(Vehicle a, Vehicle b) {
			//Está mal; no debería ser en orden descendente? Si estás de acuerdo arréglalo
			//Ver página 7 del pdf
			//Martín @ 2/3/2020,17:40
			return  Integer.signum(a.getPosition() - b.getPosition());
		}
	};
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
			int contLimit, int length, Weather weather)
	{
		super(id);
		if (maxSpeed <= 0) {throw new IllegalArgumentException("maxSpeed is not positive");}
		if (contLimit < 0) {throw new IllegalArgumentException("contLimit is negative");}
		if (length <= 0) {throw new IllegalArgumentException("length is not positive");}
		if (srcJunc == null) {throw new IllegalArgumentException("srcJunc cannot be null");}
		if (destJunc == null) {throw new IllegalArgumentException("destJunc cannot be null");}
		if (weather == null) {throw new IllegalArgumentException("weather cannot be null");}
		
		_currentVehicles = new SortedArrayList<Vehicle>(_vehicleComparator);
		_origin = srcJunc;
		_destination = destJunc;
		_maxSpeedLimit = maxSpeed;
		_currentSpeedLimit = maxSpeed;
		_pollutionThreshold = contLimit;
		_length = length;
		_currentWeather = weather;
		_destination.addIncomingRoad(this);
		_origin.addOutGoingRoad(this);
	}
	
	public Junction getDestination()
	{
		return _destination;
	}
	
	public Junction getOrigin()
	{
		return _origin;
	}
	
	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		
		for(Vehicle v : _currentVehicles) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
		
		_currentVehicles.sort(_vehicleComparator);		
	}

	@Override
	public JSONObject report() {
		JSONObject result = new JSONObject();

		result.put("id", getId());
		result.put("speedlimit", _currentSpeedLimit);
		result.put("weather", _currentWeather);
		result.put("co2", _totalPollution);
		
		JSONArray ja = new JSONArray();
		for (Vehicle v : _currentVehicles)
		{
			ja.put(v.getId());
		}
		result.put("vehicles",ja);
		
		return result;
	}

	//TODO: Review visibility
	int getLength() {
		return _length;
		
	}

	void addContamination(int pollutionProduced) {
		if (pollutionProduced < 0) {throw new IllegalArgumentException("pollutionProduced cannot be negative");}
		_totalPollution += pollutionProduced;
		
	}
	
	abstract void reduceTotalContamination();

	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);
	
	void exit(Vehicle vehicle) {
		_currentVehicles.remove(vehicle);
	}

	void enter(Vehicle vehicle) {

		if(vehicle.getPosition() != 0 ) { throw new IllegalArgumentException("Vehicle position must be 0"); }
		else if(vehicle.getSpeed() != 0) {throw new IllegalArgumentException("Vehicle speed must be 0");  }
		
		_currentVehicles.add(vehicle);
	}

	
	void setWeather(Weather weather) {
		if(weather == null) { throw new IllegalArgumentException("Weather cannot be null"); }
		
		_currentWeather = weather;
	}
	
	//TODO: Review visibility
	//No parece que se use en ningún sitio
	//Pregunta a Victoria
	List<Vehicle> getVehicleList() {
		return Collections.unmodifiableList(_currentVehicles);
	}
}
