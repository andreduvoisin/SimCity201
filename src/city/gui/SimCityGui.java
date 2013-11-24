package city.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JFrame;

import base.ConfigParser;
import base.PersonAgent;
import base.interfaces.Person;

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
		
		Person person = city.masterPersonList.get(0);
		if (person instanceof PersonAgent){
			((PersonAgent) person).msgAnimationDone();
			((PersonAgent) person).getCar();
			((PersonAgent) person).msgAnimationDone();
			((PersonAgent) person).pickAndExecuteAnAction();
			((PersonAgent) person).pickAndExecuteAnAction();
//			((PersonAgent) person).eatFood();
		}
		
//		for (Person person: city.masterPersonList) {
//			((PersonAgent) person).msgAnimationDone();
//			((PersonAgent) person).startThread();
//			((PersonAgent) person).msgAnimationDone();
//			((PersonAgent) person).eatFood();
//			((PersonAgent) person).msgAnimationDone();
//		}
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
