package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		ArrayList<Vehicle> lista = new ArrayList<Vehicle>();
		//Cuidado: si la cola está vacía no hacemos nada
		if (q.size()>0)
		{
			lista.add(q.get(0));
		}
		return Collections.unmodifiableList(lista);
	}

}
