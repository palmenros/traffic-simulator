package simulator.model;

public abstract class NewRoadEvent extends Event {


	private String srcJunId;
	private String destJuncId;
	
	protected Junction srcJunction;
	protected Junction destJunction;
	
	protected String id;	
	protected int length;
	protected int co2Limit;
	protected int maxSpeed;
	protected Weather weather;

	public NewRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time);

		this.id = id;
		this.srcJunId = srcJun;
		this.destJuncId = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}

	@Override
	void execute(RoadMap map) {
		srcJunction = map.getJunction(srcJunId);
		destJunction = map.getJunction(destJuncId);
	}

}
