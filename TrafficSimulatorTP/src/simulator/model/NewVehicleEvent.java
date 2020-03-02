package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {

	private String id;
	private int maxSpeed;
	private int contClass;
	private List<String> itineraryIds;

	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itineraryIds) {
		super(time);

		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itineraryIds = itineraryIds;
	}

	@Override
	void execute(RoadMap map) {

		ArrayList<Junction> itinerary = new ArrayList<Junction>();
		for(String id : itineraryIds) {
			itinerary.add(map.getJunction(id));
		}
		
		Vehicle vehicle = new Vehicle(id, maxSpeed, contClass, itinerary);
		map.addVehicle(vehicle);
		vehicle.moveToNextRoad();
	}

}
