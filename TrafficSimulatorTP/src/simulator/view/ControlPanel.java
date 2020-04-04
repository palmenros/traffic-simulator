package simulator.view;

import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver {
	
	private Controller _controller;
	private boolean _stopped;
	
	public ControlPanel(Controller controller)
	{
		_controller = controller;
		_controller.addObserver(this);
	}

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				//TODO: Uncomment and view correct parameters for _controller.run()
				//_controller.run(1);
			} catch (Exception e) {
				// TODO show error message
				_stopped = true;
				return;
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});	
		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}
	
	private void enableToolBar(boolean b) {
		// TODO Auto-generated method stub
		
	}

	private void stop() {
		_stopped = true;
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	
}
