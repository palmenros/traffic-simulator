package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent {

	@Override
	void execute(RoadMap map) {
		super.execute(map);
		
		map.addRoad( new CityRoad(_id, _srcJunction, _destJunction, _maxSpeed, _co2Limit, _length, _weather) );
	}
	
	public NewCityRoadEvent(int time, String id, String srcJun, String
			destJunc, int length, int co2Limit, int maxSpeed, Weather weather)
	{
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}
	
	@Override
	public String toString() {
		return "New City Road '" + _id + "'";
	}
	
}
