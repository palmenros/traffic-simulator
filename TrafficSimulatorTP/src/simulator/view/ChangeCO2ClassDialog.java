package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LinearGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class ChangeCO2ClassDialog extends JDialog {

	JButton _cancelButton;
	JButton _okButton;
	
	public ChangeCO2ClassDialog(Frame frame) {
		super(frame, false);	
		
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
		
		buttonPanel.add(Box.createHorizontalGlue());
		
		buttonPanel.add(_cancelButton);
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(_okButton);

		buttonPanel.add(Box.createHorizontalGlue());
		//TODO: Center buttons
		
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		
	}
	
}
