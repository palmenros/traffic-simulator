package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {

	protected int time;
	protected String id;
	protected String src;
	protected String dest;
	protected int length;
	protected int co2limit;
	protected int maxSpeed;
	protected Weather weather;
		
	NewRoadEventBuilder(String type) {
		super(type);
	}
	
	void fillData(JSONObject data) {
		time = data.getInt("time");
		id = data.getString("id");
		src = data.getString("src");
		dest = data.getString("dest");
		length = data.getInt("length");
		co2limit = data.getInt("co2limit");
		maxSpeed = data.getInt("maxspeed");
		weather = Weather.valueOf(data.getString("weather"));
	}
	
}
