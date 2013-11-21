package restaurant_smileham.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import restaurant_smileham.Table;
import restaurant_smileham.agent.Agent;
import restaurant_smileham.gui.HostGui;
import restaurant_smileham.gui.RestaurantGui;
import restaurant_smileham.interfaces.Cook;
import restaurant_smileham.interfaces.Customer;
import restaurant_smileham.interfaces.Host;
import restaurant_smileham.interfaces.Waiter;

/**
 * Restaurant Host Agent
 */
public class HostAgent extends Agent implements Host{

	//Constants
	public static final int cNUM_TABLES = 3;
	public static final int cSPEED_LIMIT = 5;
	
	//Agents
	private List<Waiter> mWaiters;
	private List<Customer> mWaitingCustomers;
	
	//Regular Member Variables
	private String mName;
	private Collection<Table> mTables;

	//GUI
	private HostGui mHostGui;
	private RestaurantGui mGUI;
	
	
	//-----------------------------------------------CONSTRUCTOR-----------------------------------------------
	public HostAgent(String name, RestaurantGui gui) {
		super();
		mName = name;
		mGUI = gui;
		print("Constructor");
		
		//Set up Host
		mHostGui = new HostGui(this);
		mGUI.getAnimationPanel().addGui(mHostGui);
		
		//Add Waiters
		mWaiters = new ArrayList<Waiter>();
		
		//Add Customers
		mWaitingCustomers = new ArrayList<Customer>();
		
		//Add Tables
		mTables = new ArrayList<Table>(cNUM_TABLES);
		for (int iTables = 0; iTables < cNUM_TABLES; iTables++) {
			mTables.add(new Table(iTables));
		}
		
		startThread();
	}

	
	
	// -----------------------------------------------MESSAGES-------------------------------------------------------


	public void msgAddWaiter(Waiter waiter){
		mWaiters.add(waiter);
		if (mWaiters.size() == 1) stateChanged();
	}
	
	public void msgIWantFood(Customer customer) {
		print("Message: msgIWantFood()");
		mWaitingCustomers.add(customer);
		stateChanged();
	}

	public void msgLeavingTable(Customer cust) {
		print("Message: msgLeavingTable()");
		for (Table table : mTables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}
	
	public void msgLeavingRestaurant(Customer customer) {
		print("Message msgLeavingRestaurant()");
		mWaitingCustomers.remove(customer);
		//no state changed
	}
	
	//TODO Shane: 3 Waiter Break deprecated
	//Want to go on break
	public void msgWantToGoOnBreak(Waiter waiter){
		print("Message: msgWantToGoOnBreak");
//		waiter.msgBreakReply(getNumWorkingWaiters() != 0);
	}
	
	
	//-----------------------------------------------SCHEDULER-----------------------------------------------
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
		//let customer know if restaurant is full
		if (isRestaurantFull()){
			for (Customer iCustomer : mWaitingCustomers){
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
		private void seatCustomer(Customer customer, Table table) {
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
	
		public List<Customer> getWaitingCustomers() {
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
		
		public List<Waiter> getWaiters() {
			return mWaiters;
		}
		
		public int getNumWorkingWaiters(){
			int numWorkingWaiters = 0;
			for (Waiter iWaiter: mWaiters){
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
}

