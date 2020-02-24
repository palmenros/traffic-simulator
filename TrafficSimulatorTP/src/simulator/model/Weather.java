package simulator.model;

public enum Weather {
	
	//Intercity contamination coefficient, city contamination coefficient
	
	SUNNY (2, 2), 
	CLOUDY (3, 2), 
	RAINY (10, 2), 
	WINDY (15, 10), 
	STORM (20, 10);
	
	private int interCityContaminationCoeff;
	private int cityContaminationCoeff;

	private Weather(int interCityContaminationCoeff, int cityContaminationCoeff) {
		this.cityContaminationCoeff = cityContaminationCoeff;
		this.interCityContaminationCoeff = interCityContaminationCoeff;	
	}

	public int getInterCityContaminationCoeff() {
		return interCityContaminationCoeff;
	}

	public int getCityContaminationCoeff() {
		return cityContaminationCoeff;
	}

}
