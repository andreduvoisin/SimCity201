package city.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class CityControlPanel extends JPanel implements ActionListener{
	
	SimCityGui city;
	public static final int CP_WIDTH = 1100, CP_HEIGHT = 100;
	JButton addRestaurant, addBank;
	
	public CityControlPanel(SimCityGui city) {
		this.city = city;
		this.setPreferredSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setVisible(true);
		
		addRestaurant = new JButton("Add Restaurant");
		addRestaurant.addActionListener(this);
		add(addRestaurant);
		addBank = new JButton("Add Bank");
		addBank.addActionListener(this);
		add(addBank);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addRestaurant)) {
			city.city.addObject(CityComponents.RESTAURANT);
		}
		else if (e.getSource().equals(addBank)) {
			city.city.addObject(CityComponents.BANK);
		}
	}
}
