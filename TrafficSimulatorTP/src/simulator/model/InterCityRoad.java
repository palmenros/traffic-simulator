package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		_totalPollution = (int)(((100.0 - _currentWeather.getInterCityContaminationCoeff()) / 100.0 ) * _totalPollution);
	}

	@Override
	void updateSpeedLimit() {
		_currentSpeedLimit =  (_totalPollution > _pollutionThreshold ? (int)(_maxSpeedLimit * 0.5) : _maxSpeedLimit); 
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (int) (_currentSpeedLimit * (_currentWeather == Weather.STORM ? 0.8 : 1 ));
	}

}
