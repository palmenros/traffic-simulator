package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int _timeSlot;
	
	public MostCrowdedStrategy(int timeSlot)
	{
		_timeSlot=timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if (roads.size() == 0) {return -1;}
		else if (currGreen==-1)
		{
			int appropriateIndex = 0;
			int lengthOfAppropriate = 0;
			for (int i = 0;i<roads.size();i++)
			{
				if (qs.get(i).size()>lengthOfAppropriate)
				{
					appropriateIndex=i;
					lengthOfAppropriate=qs.get(i).size();
				}
			}
			return appropriateIndex;
		}
		else if (currTime-lastSwitchingTime<_timeSlot)
		{
			return currGreen;
		}
		else //currTime = _timeSlot+lastSwitchingTIme
		{
			//Para evitar problemas de haber pasado por
			//un sitio, o no, o módulos, vamos a
			//utilizar un índice de 0 a roads.size()-1
			//corregido sumándole algo
			int appropriateIndex = 0; //No es el de verdad
			//Cuando appropriateIndex vale 0 el elemento
			//apuntado es el siguiente
			int lengthOfAppropriate = 0;
			for (int i = 0;i<roads.size();i++)
			{
				if (qs.get((currGreen+1+i)%roads.size()).size()>lengthOfAppropriate)
				{
					appropriateIndex=i;
					lengthOfAppropriate=qs.get((currGreen+1+i)%roads.size()).size();
				}
			}
			return appropriateIndex+currGreen+1;
		}
	}

}
