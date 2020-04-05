package simulator.model;

import java.util.List;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event {

	private List<Pair<String, Weather>> _ws;

	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		this._ws = ws;
			
		if(ws == null) {
			throw new IllegalArgumentException("Weather road list cannot be null");
		}
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Weather> p : _ws) {
			Road road = map.getRoad(p.getFirst());
			if(road == null) {  
				throw new IllegalArgumentException("Road in weather list does not exist");
			}
			road.setWeather(p.getSecond());
		}

	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();	
		res.append("Set Weather [");
		
		boolean first = true;
		for(Pair<String, Weather> pair : _ws) {
			if(first) {
				first = false;
			} else {
				res.append(", ");
			}
			
			//Print pair
			res.append("(");
			res.append(pair.getFirst());
			res.append(",");
			res.append(pair.getSecond());
			res.append(")");
		}
		
		res.append("]");
		return res.toString();
	}
	
}
