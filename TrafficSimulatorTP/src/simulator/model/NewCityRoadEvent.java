package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent {

	@Override
	void execute(RoadMap map) {
		super.execute(map);
		
		map.addRoad( new CityRoad(id, srcJunction, destJunction, maxSpeed, co2Limit, length, weather) );
	}
	
	public NewCityRoadEvent(int time, String id, String srcJun, String
			destJunc, int length, int co2Limit, int maxSpeed, Weather weather)
	{
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}
	
}
