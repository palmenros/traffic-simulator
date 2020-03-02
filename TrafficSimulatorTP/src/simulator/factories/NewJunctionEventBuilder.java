package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {

	private static final String TYPE = "new_junction";
	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;

	NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super(TYPE);
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		
		int time = data.getInt("time");
		String id = data.getString("id");
		
		JSONArray jo = data.getJSONArray("coor");
		int x = jo.getInt(0);
		int y = jo.getInt(1);
		
		LightSwitchingStrategy ls = lssFactory.createInstance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy ds = dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
		
		return new NewJunctionEvent(time, id, ls, ds, x, y);
	}

}
