package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {

	private static final String TYPE = "round_robin_lss";
	
	RoundRobinStrategyBuilder() {
		super(TYPE);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int timeSlot = 1;
			
		if(data.has("timeslot")) {
			timeSlot = data.getInt("timeslot");
		}
				
		return new RoundRobinStrategy(timeSlot);
	}

}
