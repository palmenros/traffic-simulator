package simulator.model;

public abstract class NewRoadEvent extends Event {


	private String _srcJunId;
	private String _destJuncId;
	
	protected Junction _srcJunction;
	protected Junction _destJunction;
	
	protected String _id;	
	protected int _length;
	protected int _co2Limit;
	protected int _maxSpeed;
	protected Weather _weather;

	public NewRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time);

		this._id = id;
		this._srcJunId = srcJun;
		this._destJuncId = destJunc;
		this._length = length;
		this._co2Limit = co2Limit;
		this._maxSpeed = maxSpeed;
		this._weather = weather;
	}

	@Override
	void execute(RoadMap map) {
		_srcJunction = map.getJunction(_srcJunId);
		_destJunction = map.getJunction(_destJuncId);
	}

}
