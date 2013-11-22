package reference.simcity.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import reference.trace.AlertLevel;
import reference.trace.AlertLog;
import reference.trace.AlertTag;

public class CityControlPanel extends JPanel implements ActionListener{

	SimCityGui city;
	public static final int CP_WIDTH = 1100, CP_HEIGHT = 100;
	JButton addRestaurant, addBank;

	//For managing traces
	JButton enableInfoButton;		//You could (and probably should) substitute a JToggleButton to replace both
	JButton disableInfoButton;		//of these, but I split it into enable and disable for clarity in the demo.
	JButton enableRestaurantTagButton;		
	JButton disableRestaurantTagButton;		
	JButton enableBankTagButton;
	JButton disableBankTagButton;
	
	String name = "Control Panel";

	public CityControlPanel(SimCityGui city) {
		this.city = city;
		this.setPreferredSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setVisible(true);

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		addRestaurant = new JButton("Add Restaurant");
		addRestaurant.addActionListener(this);
		c.gridx = 0; c.gridy = 0;
		add(addRestaurant, c);
		addBank = new JButton("Add Bank");
		addBank.addActionListener(this);
		c.gridx = 0; c.gridy = 1;
		add(addBank, c);

		//Trace panel buttons
		enableInfoButton = new JButton("Show Level: INFO");
		enableInfoButton.addActionListener(this);
		disableInfoButton = new JButton("Hide Level: INFO");
		disableInfoButton.addActionListener(this);
		enableRestaurantTagButton = new JButton("Show Tag: RESTAURANT");
		enableRestaurantTagButton.addActionListener(this);
		disableRestaurantTagButton = new JButton("Hide Tag: RESTAURANT");
		disableRestaurantTagButton.addActionListener(this);
		enableBankTagButton = new JButton("Show Tag: BANK");
		enableBankTagButton.addActionListener(this);
		disableBankTagButton = new JButton("Hide Tag: BANK");
		disableBankTagButton.addActionListener(this);
		
		c.gridx = 1; c.gridy = 0;
		this.add(enableInfoButton, c);
		c.gridx = 1; c.gridy = 1;
		this.add(disableInfoButton, c);
		c.gridx = 2; c.gridy = 0;
		this.add(enableRestaurantTagButton, c);
		c.gridx = 2; c.gridy = 1;
		this.add(disableRestaurantTagButton, c);
		c.gridx = 3; c.gridy = 0;
		this.add(enableBankTagButton, c);
		c.gridx = 3; c.gridy = 1;
		this.add(disableBankTagButton, c);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addRestaurant)) {
			city.city.addObject(CityComponents.RESTAURANT);
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New Restaurant");
		}
		else if (e.getSource().equals(addBank)) {
			AlertLog.getInstance().logInfo(AlertTag.BANK, this.name, "Adding New Bank");
			city.city.addObject(CityComponents.BANK);
		}
		else if(e.getSource().equals(enableInfoButton)) {
			city.tracePanel.showAlertsWithLevel(AlertLevel.INFO);
		}
		else if(e.getSource().equals(disableInfoButton)) {
			city.tracePanel.hideAlertsWithLevel(AlertLevel.INFO);
		}
		else if(e.getSource().equals(enableRestaurantTagButton)) {
			city.tracePanel.showAlertsWithTag(AlertTag.RESTAURANT);
		}
		else if(e.getSource().equals(disableRestaurantTagButton)) {
			city.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT);
		}
		else if(e.getSource().equals(enableBankTagButton)) {
			city.tracePanel.showAlertsWithTag(AlertTag.BANK);
		}
		else if(e.getSource().equals(disableBankTagButton)) {
			city.tracePanel.hideAlertsWithTag(AlertTag.BANK);
		}
	}
}
