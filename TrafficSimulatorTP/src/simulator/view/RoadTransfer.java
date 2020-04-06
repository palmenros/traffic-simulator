package simulator.view;

import simulator.model.Weather;

public class RoadTransfer {

	private String _id;
	private Integer _length;
	private Weather _weather;
	private Integer _maxSpeed;
	private Integer _speedLimit;
	private Integer _totalC02;
	private Integer _co2Limit;
	
	public RoadTransfer(String _id, Integer _length, Weather _weather, Integer _maxSpeed, Integer _speedLimit,
			Integer _totalC02, Integer _co2Limit) {
		super();
		this._id = _id;
		this._length = _length;
		this._weather = _weather;
		this._maxSpeed = _maxSpeed;
		this._speedLimit = _speedLimit;
		this._totalC02 = _totalC02;
		this._co2Limit = _co2Limit;
	}

	public String getId() {
		return _id;
	}

	public Integer getLength() {
		return _length;
	}

	public Weather getWeather() {
		return _weather;
	}

	public Integer getMaxSpeed() {
		return _maxSpeed;
	}

	public Integer getSpeedLimit() {
		return _speedLimit;
	}

	public Integer getTotalC02() {
		return _totalC02;
	}

	public Integer getCo2Limit() {
		return _co2Limit;
	}
	
}
