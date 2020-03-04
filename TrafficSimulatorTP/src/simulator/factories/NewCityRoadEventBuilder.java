package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder {
	
	private static final String TYPE = "new_city_road";

	public NewCityRoadEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		fillData(data);
		return new NewCityRoadEvent(time, id, src, dest, length, co2limit, maxSpeed, weather);
	}

}
