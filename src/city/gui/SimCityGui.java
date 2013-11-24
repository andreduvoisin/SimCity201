package city.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JFrame;

import base.ConfigParser;
import base.PersonAgent;
import base.Time;
import base.interfaces.Person;

public class SimCityGui extends JFrame {
	
	public CityPanel citypanel;
	public InfoPanel infopanel;
	public CityView cityview;
	CityControlPanel CP;
	GridBagConstraints c = new GridBagConstraints();

	static CityPanel cityPanel;

	public SimCityGui() throws HeadlessException, IOException {
		CP = new CityControlPanel(this);
		citypanel = new CityPanel(this);
		cityview = new CityView(this);
		infopanel = new InfoPanel(this);
		
		this.setLayout(new GridBagLayout());
		
		Time globaltime = new Time(citypanel.masterPersonList);
		
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 2; c.gridheight = 6;
		this.add(CP, c);
		
		c.gridx = 2; c.gridy = 0;
		c.gridwidth = 6; c.gridheight = 6;
		this.add(citypanel, c);
		
		c.gridx = 8; c.gridy = 0;
		c.gridwidth = 5; c.gridheight = 1;
		this.add(infopanel, c);

		c.gridx = 8; c.gridy = 1;
		c.gridwidth = 5; c.gridheight = 5;
		this.add(cityview, c);
		
		ConfigParser config = ConfigParser.getInstanceOf();
		config.readFileCreatePersons(citypanel);
		
		Person person = citypanel.masterPersonList.get(0);
		if (person instanceof PersonAgent){
			((PersonAgent) person).msgAnimationDone();
			((PersonAgent) person).getCar();
			((PersonAgent) person).msgAnimationDone();
		}
		
		if (person instanceof PersonAgent){
			((PersonAgent) person).msgAnimationDone();
//			((PersonAgent) person).getCar();
//			((PersonAgent) person).msgAnimationDone();
//			((PersonAgent) person).pickAndExecuteAnAction();
//			((PersonAgent) person).pickAndExecuteAnAction();
		}
		
		if (person instanceof PersonAgent){
			//Housing
			//((PersonAgent) person).invokeMaintenance();
			((PersonAgent) person).mHouseRole.setHouse(cityview.house1);
			//((PersonAgent) person).mHouseRole.msgEatAtHome();
			((PersonAgent) person).startThread();
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
