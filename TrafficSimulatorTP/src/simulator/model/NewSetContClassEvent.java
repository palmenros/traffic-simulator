package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {

	private List<Pair<String, Integer>> cs;

	public NewSetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		this.cs = cs;
			
		if(cs == null) {
			throw new IllegalArgumentException("Contamination vehicle list cannot be null");
		}
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Integer> p : cs) {
			Vehicle vehicle = map.getVehicle(p.getFirst());
			if(vehicle == null) {  
				throw new IllegalArgumentException("Vehicle in contamination list does not exist");
			}
			vehicle.setContaminationClass(p.getSecond());
		}

	}

}
