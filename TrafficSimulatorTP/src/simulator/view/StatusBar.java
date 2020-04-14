package simulator.view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {

	private Controller _controller;
	
	private JLabel _timeLabel;
	private JLabel _statusLabel;
	
	public StatusBar(Controller controller) {
		_controller = controller;
		_controller.addObserver(this);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(Box.createHorizontalStrut(5));
		
		add(new JLabel("Time: "));
		
		_timeLabel = new JLabel("0");
			
		//TODO: Give _timeLabel a minimum size so there is a space between
		add(_timeLabel);
		
		add(Box.createHorizontalStrut(5));
		
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setMaximumSize(new Dimension(1, 50));
		add(separator);

		add(Box.createHorizontalStrut(5));
		
		_statusLabel = new JLabel("Welcome!");
		add(_statusLabel);
		
		setPreferredSize(new Dimension(0, 20));
		
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_timeLabel.setText(Integer.toString(time));
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_statusLabel.setText("Event added (" + e.toString() + ")");
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_timeLabel.setText(Integer.toString(time));		
		_statusLabel.setText("Welcome!");
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
