package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {

	private String _id;
	private int _maxSpeed;
	private int _contClass;
	private List<String> _itineraryIds;

	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itineraryIds) {
		super(time);

		this._id = id;
		this._maxSpeed = maxSpeed;
		this._contClass = contClass;
		this._itineraryIds = itineraryIds;
	}

	@Override
	void execute(RoadMap map) {

		ArrayList<Junction> itinerary = new ArrayList<Junction>();
		for(String id : _itineraryIds) {
			itinerary.add(map.getJunction(id));
		}
		
		Vehicle vehicle = new Vehicle(_id, _maxSpeed, _contClass, itinerary);
		map.addVehicle(vehicle);
		vehicle.moveToNextRoad();
	}

}
