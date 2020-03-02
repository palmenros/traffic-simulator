package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	private static final String TYPE = "new_vehicle";
	
	NewVehicleEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int contClass = data.getInt("class");
		
		List<String> itinerary = new ArrayList<String>();
		JSONArray jo = data.getJSONArray("itinerary");
		for(int i = 0; i < jo.length(); i++) {
			itinerary.add(jo.getString(i));
		}
		
		return new NewVehicleEvent(time, id, maxSpeed, contClass, itinerary);
	}

}
