package city.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JFrame;

import base.ContactList;
import base.SortingHat;
import base.Time;

@SuppressWarnings("serial")
public class SimCityGui extends JFrame {
	public static boolean TESTING = true;

	public static int TESTNUM = 3;
	
	public static boolean GRADINGVIEW = true;

	
	static SimCityGui instance = null;
	public CityPanel citypanel;
	public InfoPanel infopanel;
	public CityView cityview;
	CityControlPanel CP;
	GridBagConstraints mGridBagConstraints = new GridBagConstraints();
	
	
	public static SimCityGui getInstance() {
		return instance;
	}

	public SimCityGui(String title) throws HeadlessException, IOException {
		super(title);
		
		instance = this;
		CP = new CityControlPanel(this);
		citypanel = new CityPanel(this);
		cityview = new CityView(this);
		infopanel = new InfoPanel(this);
		
		this.setLayout(new GridBagLayout());
		
		Time globaltime = new Time(); //starts the static timer
		
		//Create Grid/Gui
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
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws HeadlessException 
	 */
	public static void main(String[] args) throws HeadlessException, IOException {
		ContactList.setup();
		SortingHat.InstantiateBaseRoles();
	
		SimCityGui test = new SimCityGui("SimCity201 - Team 28");
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setResizable(false);
		test.pack();
		test.setVisible(true);	
	}
	
}
