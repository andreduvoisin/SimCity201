package reference.simcity.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import reference.trace.AlertLog;
import reference.trace.TracePanel;

public class SimCityGui extends JFrame {
	
	CityPanel city;
	InfoPanel info;
	CityView view;
	CityControlPanel CP;
	TracePanel tracePanel;
	GridBagConstraints c = new GridBagConstraints();

	public SimCityGui() throws HeadlessException {
		CP = new CityControlPanel(this);
		
		tracePanel = new TracePanel();
		tracePanel.setPreferredSize(new Dimension(CP.getPreferredSize().width, (int)(1.4*CP.getPreferredSize().height)));
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();

		
		city = new CityPanel(this);
		
		view = new CityView(this);
		
		info = new InfoPanel(this);
		
		this.setLayout(new GridBagLayout());
		
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 6; c.gridheight = 6;
		this.add(city, c);
		
		c.gridx = 6; c.gridy = 0;
		c.gridwidth = 5; c.gridheight = 1;
		this.add(info, c);

		c.gridx = 6; c.gridy = 1;
		c.gridwidth = 5; c.gridheight = 5;
		this.add(view, c);

		c.gridx = 0; c.gridy = 6;
		c.gridwidth = 11; c.gridheight = 1;
		this.add(CP, c);
		
		c.gridx = 0; c.gridy = 7;
		c.gridwidth = 11; c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		this.add(tracePanel, c);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimCityGui test = new SimCityGui();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setResizable(false);
		test.pack();
		test.setVisible(true);

	}

}
