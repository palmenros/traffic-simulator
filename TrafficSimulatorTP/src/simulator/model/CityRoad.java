package simulator.model;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		_totalPollution = Math.max(0, _totalPollution - _currentWeather.getCityContaminationCoeff());
	}

	@Override
	void updateSpeedLimit() {
		//Speed limit never changes it is always max speed
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (int)(((11.0 - v.getPollutionMeasure()) /11.0) * _currentSpeedLimit );
	}

}
