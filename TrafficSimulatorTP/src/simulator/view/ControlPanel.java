package simulator.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import java.awt.Frame;
import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver {
	
	private Controller _controller;
	private boolean _stopped;
	
	private JButton _fileOpenButton;
	private JButton _contaminationButton;
	private JButton _weatherButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JSpinner _tickSpinner;
	private JButton _closeButton;
	
	public ControlPanel(Controller controller)
	{
		_controller = controller;
		_controller.addObserver(this);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		//TODO: Review if it is necessary to add here two separators at beginning
		
		_fileOpenButton = new JButton(new ImageIcon("resources/icons/open.png"));
		_fileOpenButton.setMaximumSize(new Dimension(50, 50));
		_fileOpenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadFile();
			}
		});
		
		add(_fileOpenButton);
		
		addSeparator();
				
		_contaminationButton = new JButton(new ImageIcon("resources/icons/co2class.png"));
		_contaminationButton.setMaximumSize(new Dimension(50, 50));
		_contaminationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeC02Class();
			}
		});
		
		add(_contaminationButton);		
		
		_weatherButton = new JButton(new ImageIcon("resources/icons/weather.png"));
		_weatherButton.setMaximumSize(new Dimension(50, 50));
		add(_weatherButton);
		
		addSeparator();
		
		_runButton = new JButton(new ImageIcon("resources/icons/run.png"));
		_runButton.setMaximumSize(new Dimension(50, 50));
		add(_runButton);
		
		_stopButton = new JButton(new ImageIcon("resources/icons/stop.png"));
		_stopButton.setMaximumSize(new Dimension(50, 50));
		add(_stopButton);
		
		addSeparator();
		
		add(new JLabel("Ticks:"));
		
		_tickSpinner = new JSpinner(new SpinnerNumberModel(10, 0, null, 1));
	
		_tickSpinner.setMaximumSize(new Dimension(70, 50));
		_tickSpinner.setPreferredSize(new Dimension(70, 50));
		
		add(_tickSpinner);
		
		//Actual graphical separation
		add(new JSeparator());
		
		//Visual separator indication
		addSeparator();
		
		_closeButton = new JButton(new ImageIcon("resources/icons/exit.png"));
		_closeButton.setMaximumSize(new Dimension(50, 50));
		
		_closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showOptionDialog(ControlPanel.this,
						"Are sure you want to exit?", "Exit",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (n == 0) {
					System.exit(0);
				}
			}
			
		});
		add(_closeButton);
	
	}
	
	protected void changeC02Class() {
		ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog(null);
		dialog.setVisible(true);
	}

	protected void loadFile() {
		JFileChooser fileChooser = new JFileChooser();
		
		try {
			fileChooser.setCurrentDirectory(new File("resources/examples"));
			int res = fileChooser.showOpenDialog(this);
			
			switch(res)
			{
				case JFileChooser.CANCEL_OPTION:
					JOptionPane.showMessageDialog(this, "Cancelled", "Info", JOptionPane.INFORMATION_MESSAGE);
					break;
				case JFileChooser.APPROVE_OPTION:
					_controller.reset();
					_controller.loadEvents(new FileInputStream(fileChooser.getSelectedFile()));
					break;
				default:
				case JFileChooser.ERROR_OPTION:
					//Show error message
					throw new Exception();
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	//TODO: Find if best way to add separator
	private void addSeparator()
	{
		add(Box.createHorizontalStrut(5));
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setMaximumSize(new Dimension(1, 50));
		add(separator);
		add(Box.createHorizontalStrut(5));
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
