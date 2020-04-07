package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {

	private List<Pair<String, Integer>> _cs;

	public NewSetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		this._cs = cs;
			
		if(cs == null) {
			throw new IllegalArgumentException("Contamination vehicle list cannot be null");
		}
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Integer> p : _cs) {
			Vehicle vehicle = map.getVehicle(p.getFirst());
			if(vehicle == null) {  
				throw new IllegalArgumentException("Vehicle in contamination list does not exist");
			}
			vehicle.setContaminationClass(p.getSecond());
		}

	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();	
		res.append("Change CO2 class: [");
		
		boolean first = true;
		for(Pair<String, Integer> pair : _cs) {
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
