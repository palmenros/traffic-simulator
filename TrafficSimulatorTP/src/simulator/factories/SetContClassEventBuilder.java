package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	private static final String TYPE = "set_cont_class";

	SetContClassEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
	
		List<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>();
		
		JSONArray jo = data.getJSONArray("info");
		for(int i = 0; i < jo.length(); i++) {
			
			JSONObject obj = jo.getJSONObject(i);
			Pair<String, Integer> pair = new Pair<String, Integer>(obj.getString("vehicle"), obj.getInt("class"));
			
			list.add(pair);
		}
		
		return new NewSetContClassEvent(time, list);
	}

	
}
