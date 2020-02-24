package simulator.model;

import java.util.Collections;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		//Con devolver la propia lista, protegiéndola, basta
		return Collections.unmodifiableList(q);
	}

}
