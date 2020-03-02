package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	private static final String TYPE = "set_weather";

	SetWeatherEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
	
		List<Pair<String, Weather>> list = new ArrayList<Pair<String, Weather>>();
		
		JSONArray jo = data.getJSONArray("info");
		for(int i = 0; i < jo.length(); i++) {
			
			JSONObject obj = jo.getJSONObject(i);
			Pair<String, Weather> pair = new Pair<String, Weather>(obj.getString("road"), Weather.valueOf(obj.getString("weather")));
			
			list.add(pair);
		}
		
		return new SetWeatherEvent(time, list);
	}

}
