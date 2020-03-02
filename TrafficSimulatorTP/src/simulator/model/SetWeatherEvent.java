package simulator.model;

import java.util.List;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event {

	private List<Pair<String, Weather>> ws;

	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		this.ws = ws;
			
		if(ws == null) {
			throw new IllegalArgumentException("Weather road list cannot be null");
		}
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Weather> p : ws) {
			Road road = map.getRoad(p.getFirst());
			if(road == null) {  
				throw new IllegalArgumentException("Road in weather list does not exist");
			}
			road.setWeather(p.getSecond());
		}

	}

}
