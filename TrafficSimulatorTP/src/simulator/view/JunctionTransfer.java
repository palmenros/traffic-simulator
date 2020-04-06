package simulator.view;

public class JunctionTransfer {
	//private String[] _colNames = { "Id", "Green", "Queues" };
	private String _id;
	private String _green;
	private String _queues;
	
	public JunctionTransfer(String _id, String _green, String _queues) {
		super();
		this._id = _id;
		this._green = _green;
		this._queues = _queues;
	}

	public String getId() {
		return _id;
	}

	public String getGreen() {
		return _green;
	}

	public String getQueues() {
		return _queues;
	}
	
	
}
