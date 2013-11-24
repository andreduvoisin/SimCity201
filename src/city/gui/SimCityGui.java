package city.gui;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import restaurant.restaurant_davidmca.gui.RestaurantGui;
import base.ConfigParser;

public class SimCityGui extends JFrame {
	
	CityPanel city;
	InfoPanel info;
	CityView view;
	CityControlPanel CP;
	GridBagConstraints c = new GridBagConstraints();

	public SimCityGui() throws HeadlessException, IOException {
		CP = new CityControlPanel(this);
		city = new CityPanel(this);
		view = new CityView(this);
		info = new InfoPanel(this);
		
		this.setLayout(new GridBagLayout());
		
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 2; c.gridheight = 6;
		this.add(CP, c);
		
		c.gridx = 2; c.gridy = 0;
		c.gridwidth = 6; c.gridheight = 6;
		this.add(city, c);
		
		c.gridx = 8; c.gridy = 0;
		c.gridwidth = 5; c.gridheight = 1;
		this.add(info, c);

		c.gridx = 8; c.gridy = 1;
		c.gridwidth = 5; c.gridheight = 5;
		this.add(view, c);
		
		ConfigParser config = ConfigParser.getInstanceOf();
		config.readFileCreatePersons(city);
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws HeadlessException 
	 */
	public static void main(String[] args) throws HeadlessException, IOException {
		SimCityGui test = new SimCityGui();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setResizable(false);
		test.pack();
		test.setVisible(true);
	}
	
}
