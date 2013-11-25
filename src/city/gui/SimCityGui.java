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
	static SimCityGui instance = null;
	public CityPanel citypanel;
	public InfoPanel infopanel;
	public CityView cityview;
	CityControlPanel CP;
	GridBagConstraints mGridBagConstraints = new GridBagConstraints();
	
	public static SimCityGui getInstance() {
		return instance;
	}

	public SimCityGui() throws HeadlessException, IOException {
		instance = this;
		CP = new CityControlPanel(this);
		citypanel = new CityPanel(this);
		cityview = new CityView(this);
		infopanel = new InfoPanel(this);
		
		this.setLayout(new GridBagLayout());
		
		Time globaltime = new Time(citypanel.masterPersonList);
		
		mGridBagConstraints.gridx = 0; mGridBagConstraints.gridy = 0;
		mGridBagConstraints.gridwidth = 2; mGridBagConstraints.gridheight = 6;
		this.add(CP, mGridBagConstraints);
		
		mGridBagConstraints.gridx = 2; mGridBagConstraints.gridy = 0;
		mGridBagConstraints.gridwidth = 6; mGridBagConstraints.gridheight = 6;
		this.add(citypanel, mGridBagConstraints);
		
		mGridBagConstraints.gridx = 8; mGridBagConstraints.gridy = 0;
		mGridBagConstraints.gridwidth = 5; mGridBagConstraints.gridheight = 1;
		this.add(infopanel, mGridBagConstraints);

		mGridBagConstraints.gridx = 8; mGridBagConstraints.gridy = 1;
		mGridBagConstraints.gridwidth = 5; mGridBagConstraints.gridheight = 5;
		this.add(cityview, mGridBagConstraints);
		
		ConfigParser config = ConfigParser.getInstanceOf();
		config.readFileCreatePersons(this);
		
		Person person = citypanel.masterPersonList.get(0);
		if (person instanceof PersonAgent){
			((PersonAgent) person).msgAnimationDone();
			((PersonAgent) person).getCar();
			((PersonAgent) person).msgAnimationDone();
		}
		
/*		if (person instanceof PersonAgent){
			//((PersonAgent) person).msgAnimationDone();
//			((PersonAgent) person).getCar();
//			((PersonAgent) person).msgAnimationDone();
//			((PersonAgent) person).pickAndExecuteAnAction();
//			((PersonAgent) person).pickAndExecuteAnAction();
		}*/
		
/*		if (person instanceof PersonAgent){
			//Housing
			//((PersonAgent) person).invokeMaintenance();
			//((PersonAgent) person).mHouseRole.setHouse(cityview.house1);
			//((PersonAgent) person).mHouseRole.msgEatAtHome();
			//((PersonAgent) person).startThread();
//			((PersonAgent) person).eatFood();
		}*/
		
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
