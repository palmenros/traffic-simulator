package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

//Conceptually extending JToolbar would be a better fit.
//However, we did this way, extending JPanel because of 
//instructions given on statement.
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
	private JToolBar _toolbar;
	private JFileChooser _fileChooser;

	
	public ControlPanel(Controller controller)
	{
		_fileChooser = new JFileChooser();
		
		_controller = controller;
		_controller.addObserver(this);
		
		_toolbar = new JToolBar();
		
	
		_fileOpenButton = new JButton(new ImageIcon("resources/icons/open.png"));
		_fileOpenButton.setToolTipText("Open events file");
		//_fileOpenButton.setMaximumSize(new Dimension(50, 50));
		_fileOpenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadFile();
			}
		});
		
		_toolbar.add(_fileOpenButton);
		
		_toolbar.addSeparator();
			
		
		_contaminationButton = new JButton(new ImageIcon("resources/icons/co2class.png"));
		_contaminationButton.setToolTipText("Add a new C02 change event");
		//_contaminationButton.setMaximumSize(new Dimension(50, 50));
		_contaminationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeC02Class();
			}
		});
		
		_toolbar.add(_contaminationButton);		
		
		_weatherButton = new JButton(new ImageIcon("resources/icons/weather.png"));
		_weatherButton.setToolTipText("Add a new weather change event");
		//_weatherButton.setMaximumSize(new Dimension(50, 50));
		_weatherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeWeather();
			}
		});
		_toolbar.add(_weatherButton);
		
		_toolbar.addSeparator();
		
		_runButton = new JButton(new ImageIcon("resources/icons/run.png"));
		_runButton.setToolTipText("Run the simulation");
		//_runButton.setMaximumSize(new Dimension(50, 50));
		_runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = false;
				enableToolBar(false);
				run_sim((Integer)_tickSpinner.getValue());
			}			
		});
		
		_toolbar.add(_runButton);
		
		_stopButton = new JButton(new ImageIcon("resources/icons/stop.png"));
		_stopButton.setToolTipText("Stop the simulation");
		//_stopButton.setMaximumSize(new Dimension(50, 50));
		_stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = true;				
			}
			
		});
		_toolbar.add(_stopButton);
		
		_toolbar.addSeparator();
		
		_toolbar.add(new JLabel("Ticks:"));
		
		_tickSpinner = new JSpinner(new SpinnerNumberModel(10, 1, null, 1));
		_tickSpinner.setToolTipText("Ticks to advance");
		_tickSpinner.setMaximumSize(new Dimension(70, 35));
		_tickSpinner.setPreferredSize(new Dimension(70, 35));
		
		_toolbar.add(_tickSpinner);
				
		//Visual separator indication
		_toolbar.addSeparator();
		
		_closeButton = new JButton(new ImageIcon("resources/icons/exit.png"));
		//_closeButton.setMaximumSize(new Dimension(50, 50));
		
		_closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showOptionDialog(ControlPanel.this.getParent(),
						"Are sure you want to exit?", "Exit",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (n == 0) {
					System.exit(0);
				}
			}
			
		});

		_toolbar.add(Box.createGlue()); 
		_toolbar.add(_closeButton);
		
		setLayout(new BorderLayout(0, 0));
		add(_toolbar, BorderLayout.CENTER);
	}
	
	protected void changeWeather() {
		ChangeWeatherDialog dialog = new ChangeWeatherDialog(null, _controller);
		dialog.setVisible(true);		
	}

	protected void changeC02Class() {
		ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog(null, _controller);
		dialog.setVisible(true);
	}

	protected void loadFile() {		
		try {
			_fileChooser.setCurrentDirectory(new File("resources/examples"));
			int res = _fileChooser.showOpenDialog(this);
			
			switch(res)
			{
				case JFileChooser.CANCEL_OPTION:
					//Removed this, it was annoying and not mandatory
					//JOptionPane.showMessageDialog(this, "Cancelled", "Info", JOptionPane.INFORMATION_MESSAGE);
					break;
				case JFileChooser.APPROVE_OPTION:
					_controller.reset();
					_controller.loadEvents(new FileInputStream(_fileChooser.getSelectedFile()));
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

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_controller.run(1);
			} catch (Exception e) {
				// As an error dialog is created by whoever observes the controller onError, it is not necessary to do it here.  
				// If we wanted to have 2 dialogs, just uncomment the following lines.

				//JOptionPane.showMessageDialog(new JFrame(), "Error while executing simulation. Simulation aborted.", "Error",
				//        JOptionPane.ERROR_MESSAGE);
				enableToolBar(true);
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
		_fileOpenButton.setEnabled(b);
		_contaminationButton.setEnabled(b);
		_weatherButton.setEnabled(b);
		_runButton.setEnabled(b);
		_closeButton.setEnabled(b);
	}

	private void stop() {
		_stopped = true;
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
	}

	@Override
	public void onError(String err) {
	}
	
}
