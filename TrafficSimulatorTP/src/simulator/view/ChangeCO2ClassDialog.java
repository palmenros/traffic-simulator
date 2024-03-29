package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LinearGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog implements TrafficSimObserver {

	private JButton _cancelButton;
	private JButton _okButton;
	private JComboBox<String> _vehicleCombo;
	private JComboBox<String> _contaminationCombo;
	private JSpinner _tickSpinner;
	private RoadMap _map;
	private int _time;
	private Controller _controller;
	
	public ChangeCO2ClassDialog(Frame frame, Controller controller) {
		super(frame, false);	
		setVisible(false);
		setModal(true);
		_controller = controller;
		_controller.addObserver(this);
	}
	
	private void initGui()
	{
		setTitle("Change C02 class");
		
		//Padding
		
		JPanel contentPanel = new JPanel();

		int paddingSize = 5;
		Border padding = BorderFactory.createEmptyBorder(paddingSize, paddingSize, paddingSize, paddingSize);

		contentPanel.setBorder(padding);

		setContentPane(contentPanel);

		//Center dialog
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout(5, 5));
		
		JLabel text = new JLabel("<html>Schedule an event to change the C02 class of a vehicle after a given number of simulation ticks from now</html>");
		//text.setSize(new Dimension(100, 500));
		text.setPreferredSize(new Dimension(500, 40));
		
		add(text, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	
		panel.add(new JLabel("Vehicle: "));

		List<Vehicle> vehicleList = _map.getVehicles();
		String[] vehicleIds = new String[vehicleList.size()];
		int vehicleIndex = 0;
		
		for(Vehicle v : vehicleList) {
			vehicleIds[vehicleIndex] = v.toString();
			++vehicleIndex;
		}
		
		
		_vehicleCombo = new JComboBox<String>(vehicleIds);	
		panel.add(_vehicleCombo);
		
		panel.add(Box.createHorizontalStrut(10));
		panel.add(new JLabel("CO2 Class: "));
		
		String[] co2classes = new String[11];
		for(int i = 0; i <= 10; ++i) {
			co2classes[i] = Integer.toString(i);
		}
		
		_contaminationCombo = new JComboBox<String>(co2classes);
		panel.add(_contaminationCombo);
		
		panel.add(Box.createHorizontalStrut(10));
		panel.add(new JLabel("Ticks: "));
		
		_tickSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
		panel.add(_tickSpinner);
		
		add(panel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		_cancelButton = new JButton("Cancel");
		_cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeCO2ClassDialog.this.dispose();
			}
			
		});
		
		_okButton = new JButton("Ok");
		_okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					addEvent();
			}
			
		});
		
		buttonPanel.add(Box.createHorizontalGlue());
		
		buttonPanel.add(_cancelButton);
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(_okButton);

		buttonPanel.add(Box.createHorizontalGlue());
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		
	}

	public void addEvent() {
		String vehicleId = (String)_vehicleCombo.getSelectedItem();
		int contClass = _contaminationCombo.getSelectedIndex();
		int eventTick = _time + (Integer)_tickSpinner.getValue();
		
		if(vehicleId == null) {
			JOptionPane.showMessageDialog(this, "Vehicle ID not selected", "Error", JOptionPane.ERROR_MESSAGE);			
			return;
		}
		
		List<Pair<String, Integer>> l = new ArrayList<Pair<String, Integer>>();
		l.add(new Pair<String, Integer>(vehicleId, contClass));
		
		_controller.addEvent(new NewSetContClassEvent(eventTick, l));
		dispose();
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_map = map;
		_time = time;
		initGui();
	}

	@Override
	public void onError(String err) {		
	}
}
