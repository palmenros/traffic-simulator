package simulator.view;

import java.util.List;

public class VehicleTransfer {
	//	private String[] _colNames = { "Id", "Location", "Itinerary", "C02 Class", "Max Speed", "Speed","Total C02", "Distance" };
	private String _id;
	private String _location;
	private List<String> _itinerary;
	private Integer _co2Class;
	private Integer _maxSpeed;
	private Integer _speed;
	private Integer _totalCO2;
	private Integer _distance;
	
	public VehicleTransfer(String _id, String _location, List<String> _itinerary, Integer _co2Class, Integer _maxSpeed,
			Integer _speed, Integer _totalCO2, Integer _distance) {
		super();
		this._id = _id;
		this._location = _location;
		this._itinerary = _itinerary;
		this._co2Class = _co2Class;
		this._maxSpeed = _maxSpeed;
		this._speed = _speed;
		this._totalCO2 = _totalCO2;
		this._distance = _distance;
	}

	public String getId() {
		return _id;
	}

	public String getLocation() {
		return _location;
	}

	public List<String> getItinerary() {
		return _itinerary;
	}

	public Integer getCo2Class() {
		return _co2Class;
	}

	public Integer getMaxSpeed() {
		return _maxSpeed;
	}

	public Integer getSpeed() {
		return _speed;
	}

	public Integer getTotalCO2() {
		return _totalCO2;
	}

	public Integer getDistance() {
		return _distance;
	}


	
	
	
}
