package restaurant.restaurant_smileham.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.gui.HostGui;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.interfaces.SmilehamCustomer;
import restaurant.restaurant_smileham.interfaces.SmilehamHost;
import restaurant.restaurant_smileham.interfaces.SmilehamWaiter;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;

/**
 * Restaurant Host Agent
 */
public class SmilehamHostRole extends BaseRole implements SmilehamHost{

	//Constants
	public static final int cNUM_TABLES = 3;
	public static final int cSPEED_LIMIT = 5;
	
	//Agents
	private List<SmilehamWaiter> mWaiters;
	private List<SmilehamCustomer> mWaitingCustomers;
	
	//Regular Member Variables
	private String mName;
	private Collection<Table> mTables;

	//GUI
	private HostGui mHostGui;
	private SmilehamAnimationPanel mAnimationPanel;
	
	
	//-----------------------------------------------CONSTRUCTOR-----------------------------------------------
	public SmilehamHostRole(Person person) {
		super(person);
		mName = person.getName();
		mAnimationPanel = SmilehamAnimationPanel.mInstance;
		print("Smileham Host Created");
		
		//Set up Host
		mHostGui = new HostGui(this);
		mAnimationPanel.addGui(mHostGui);
		
		//Add Waiters
		mWaiters = new ArrayList<SmilehamWaiter>();
		
		//Add Customers
		mWaitingCustomers = new ArrayList<SmilehamCustomer>();
		
		//Add Tables
		mTables = new ArrayList<Table>(cNUM_TABLES);
		for (int iTables = 0; iTables < cNUM_TABLES; iTables++) {
			mTables.add(new Table(iTables));
		}
	}

	
	
	// -----------------------------------------------MESSAGES-------------------------------------------------------


	public void msgAddWaiter(SmilehamWaiter waiter){
		mWaiters.add(waiter);
		if (mWaiters.size() == 1) stateChanged();
	}
	
	public void msgIWantFood(SmilehamCustomer customer) {
		print("Message: msgIWantFood()");
		mWaitingCustomers.add(customer);
		stateChanged();
	}

	public void msgLeavingTable(SmilehamCustomer cust) {
		print("Message: msgLeavingTable()");
		for (Table table : mTables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}
	
	public void msgLeavingRestaurant(SmilehamCustomer customer) {
		print("Message msgLeavingRestaurant()");
		mWaitingCustomers.remove(customer);
		//no state changed
	}
	
	//-----------------------------------------------SCHEDULER-----------------------------------------------
	public boolean pickAndExecuteAnAction() {
		
		//let customer know if restaurant is full
		if (isRestaurantFull()){
			for (SmilehamCustomer iCustomer : mWaitingCustomers){
				iCustomer.msgRestaurantFull();
			}
		}
		
		for (Table iTable : mTables) {
			if ((!iTable.isOccupied() && !mWaitingCustomers.isEmpty()) && (mWaiters.size() !=0)) {
				//If there is an open table and a waiting customer
				seatCustomer(mWaitingCustomers.get(0), iTable);
				return true;
			}
		}
		return false;
	}

	
	// -----------------------------------------------ACTIONS-----------------------------------------------

	//METHODS
		private void seatCustomer(SmilehamCustomer customer, Table table) {
			print("Action: seatCustomer()");
			table.setOccupant(customer);
			mWaitingCustomers.remove(customer);
			
			//distribute the work evenly, and choose the waiter who has the least number of tables covered
			int pickWaiter = table.getTableNumber() % mWaiters.size();
			while (!mWaiters.get(pickWaiter).isWorking()){
				pickWaiter = (pickWaiter + 1) % mWaiters.size();
			}
			mWaiters.get(pickWaiter).msgSeatCustomer(table, customer);
		}
	
	
	//ACCESSORS
		public String getName() {
			return mName;
		}
	
		public List<SmilehamCustomer> getWaitingCustomers() {
			return mWaitingCustomers;
		}
	
		public Collection<Table> getTables() {
			return mTables;
		}
		
		public void setGui(HostGui gui) {
			mHostGui = gui;
		}
	
		public HostGui getGui() {
			return mHostGui;
		}
		
		public String toString() {
			return "[Host " + getName() + "]";
		}
		
		public List<SmilehamWaiter> getWaiters() {
			return mWaiters;
		}
		
		public int getNumWorkingWaiters(){
			int numWorkingWaiters = 0;
			for (SmilehamWaiter iWaiter: mWaiters){
				if (iWaiter.isWorking()) numWorkingWaiters++;
			}
			return numWorkingWaiters;
		}
		
		public boolean isRestaurantFull(){
			for (Table iTable : mTables){
				if (!iTable.isOccupied()) return false;
			}
			return true;
		}
		
		@Override
		public Location getLocation() {
			return ContactList.cRESTAURANT_LOCATIONS.get(5);
		}
}

