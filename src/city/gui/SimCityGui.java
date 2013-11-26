package city.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.swing.JFrame;

import base.ConfigParser;
import base.Event;
import base.Event.EnumEventType;
import base.Time;
import base.interfaces.Person;
//import base.Time;

@SuppressWarnings("serial")
public class SimCityGui extends JFrame {
	static SimCityGui instance = null;
	public CityPanel citypanel;
	public InfoPanel infopanel;
	public CityView cityview;
	CityControlPanel CP;
	GridBagConstraints mGridBagConstraints = new GridBagConstraints();
	
	public static int TESTNUM = 7; //ALL SHANE: 0TESTNUM
	
	public static SimCityGui getInstance() {
		return instance;
	}

	public SimCityGui() throws HeadlessException, IOException {
		instance = this;
		CP = new CityControlPanel(this);
		cityview = new CityView(this);
		citypanel = new CityPanel(this);
		infopanel = new InfoPanel(this);
		
		this.setLayout(new GridBagLayout());
		
		Time globaltime = new Time(citypanel.masterPersonList); //starts the static timer
		
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
		
		ConfigParser config = ConfigParser.getInstanceOf();
		config.readFileCreatePersons(this);
		
//		Timer updateAnimationTimer = new Timer();
//		updateAnimationTimer.scheduleAtFixedRate(new TimerTask() {
//			public void run() {
//				for (CityCard card : cityview.cards.values()) {
//					if (!card.isVisible()) {
//						card.update(card.getGraphics());
//					}
//				}
//			}
//		}, 1000, 30);
		
		/*
		 * Hack Restaurant DAVID
		 */
				
//		PersonAgent testHost = new PersonAgent();
//		testHost.setName("testHost");
//		RestaurantHostRole hostRole = new RestaurantHostRole(testHost);
//		hostRole.setPerson(testHost);
//		hostRole.setRestaurant(1);
//		testHost.addRole(hostRole, true);
//		testHost.startThread();
//		
//		PersonAgent testWaiter = new PersonAgent();
//		testWaiter.setName("testWaiter");
//		RestaurantWaiterRole waiterRole = new RestaurantWaiterRole(testWaiter);
//		waiterRole.setPerson(testWaiter);
//		waiterRole.setRestaurant(1);
//		testWaiter.addRole(waiterRole, true);
//		testWaiter.startThread();
//		
//		PersonAgent testCustomer = new PersonAgent();
//		testCustomer.setName("testCustomer");
//		RestaurantCustomerRole customerRole = new RestaurantCustomerRole(testCustomer);
//		customerRole.setPerson(testCustomer);
//		customerRole.setRestaurant(1);
//		testCustomer.addRole(customerRole, true);
//		testCustomer.startThread();
//		
//		PersonAgent testCashier = new PersonAgent();
//		testCashier.setName("testCashier");
//		RestaurantCashierRole cashierRole = new RestaurantCashierRole(testCashier);
//		cashierRole.setPerson(testCashier);
//		cashierRole.setRestaurant(1);
//		testCashier.addRole(cashierRole, true);
//		testCashier.startThread();
//		
//		PersonAgent testCook = new PersonAgent();
//		testCook.setName("testCook");
//		RestaurantCookRole cookRole = new RestaurantCookRole(testCook);
//		cookRole.setPerson(testCook);
//		cookRole.setRestaurant(1);
//		testCook.addRole(cookRole, true);
//		testCook.startThread();
		
		Person person = citypanel.masterPersonList.get(0);
		person.msgAddEvent(new Event(EnumEventType.JOB, 0));
//		if (person instanceof PersonAgent){
//			((PersonAgent) person).msgAnimationDone();
//			((PersonAgent) person).getCar();
//			((PersonAgent) person).eatFood();
//			((PersonAgent) person).msgAnimationDone();
//		}
		
//		if (person instanceof PersonAgent){
//			((PersonAgent) person).msgAnimationDone();
//			((PersonAgent) person).getCar();
//			((PersonAgent) person).msgAnimationDone();
//			((PersonAgent) person).pickAndExecuteAnAction();
//			((PersonAgent) person).pickAndExecuteAnAction();
//		}
		
//		if (person instanceof PersonAgent){
//			Housing
//			((PersonAgent) person).invokeMaintenance();
//			((PersonAgent) person).mHouseRole.setHouse(cityview.house1);
//			((PersonAgent) person).mHouseRole.msgEatAtHome();
//			((PersonAgent) person).startThread();
//			((PersonAgent) person).eatFood();
//		}
//		
//		for (int i=0; i<10; i++) {
//			((PersonAgent) citypanel.masterPersonList.get(i)).goToJob();
//		}
//		
//		for (int i=10; i<citypanel.masterPersonList.size(); i++) {
//			((PersonAgent) citypanel.masterPersonList.get(i)).eatFood();
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
