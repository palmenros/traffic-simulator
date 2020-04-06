package simulator.view;

public class EventTransfer {

	private Integer _time;
	private String _description;
	
	public EventTransfer(Integer time, String description) {
		_time = time;
		_description = description;
	}

	public int getTime() {
		return _time;
	}

	public String getDescription() {
		return _description;
	}
	
}
