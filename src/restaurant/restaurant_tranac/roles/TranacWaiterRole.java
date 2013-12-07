package restaurant.restaurant_tranac.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import restaurant.restaurant_tranac.TranacCheck;
import restaurant.restaurant_tranac.TranacMenu;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.gui.TranacWaiterGui;
import restaurant.restaurant_tranac.interfaces.TranacCashier;
import restaurant.restaurant_tranac.interfaces.TranacCook;
import restaurant.restaurant_tranac.interfaces.TranacCustomer;
import restaurant.restaurant_tranac.interfaces.TranacHost;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;

/**
 * Restaurant Waiter Agent
 */

public class TranacWaiterRole extends BaseRole implements TranacWaiter{
	public TranacWaiterGui waiterGui = null;
	private Timer timer = new Timer();	//used for breaks
	
	//agent correspondents
	private TranacHost mHost = null; 
	private TranacCook mCook = null;
	private TranacCashier mCashier = null;
	
	//synchronized list to make it "thread-safe"
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public enum CustomerState {Waiting, Seated, ReadyToOrder, OutOfFood, Reordering, Ordered, WaitingForFood, FoodDone, Eating, ReadyForCheck, WaitingForCheck, ReceivingCheck, ReceivedCheck, Paying, Done};

	private enum WaiterState {Active, WantToGoOnBreak, CanGoOnBreak, NoBreak, OnBreak};
	
	private WaiterState mState = WaiterState.Active;
	
	//semaphores to prevent action while waiting for another message
	private Semaphore inTransit = new Semaphore(0,true);		//used for animation
	private Semaphore waitingForOrder = new Semaphore(0,true);	//used for asking for order
	private Semaphore askingForBreak = new Semaphore(0,true);
	
	public TranacWaiterRole(Person p) {
		super(p);
		waiterGui = new TranacWaiterGui(this, TranacRestaurant.getNumWaiters());
		TranacRestaurant.getInstance().addPerson(this);
		
		mHost = TranacRestaurant.getHost();
		mCook = TranacRestaurant.getCook();
		mCashier = TranacRestaurant.getCashier();
	}
	
	/** Messages */

	public void msgPleaseSeatCustomer(TranacCustomer c, int n, int table) {
		synchronized(customers) {
			customers.add(new MyCustomer(c,n,table,CustomerState.Waiting));
		}
		stateChanged();
	}

	public void msgReadyToOrder(TranacCustomer c) {
		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.c == c) {
					mc.s = CustomerState.ReadyToOrder;
					stateChanged();
				}
			}	
		}
	}
	
	public void msgOutOfFood(String choice, int table) {
		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.table == table) {
					mc.s = CustomerState.OutOfFood;
					stateChanged();
				}
			}
		}
	}
	
	public void msgReceivedOrder(TranacCustomer c, String choice) {
	//no synchronization because of the semaphore
			for(MyCustomer mc : customers) {
				if(mc.c == c) {
					mc.s = CustomerState.Ordered;
					mc.choice = choice;
					waitingForOrder.release();
					stateChanged();
				}
			}
	}
	
	public void msgOrderDone(String choice, int table, int n) {
		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.table == table) {
					mc.orderNum = n;
					mc.s = CustomerState.FoodDone;
					stateChanged();
				}
			}
		}
	}
	
	public void msgAskingForCheck(TranacCustomer c) {
		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.c == c) {
					mc.s = CustomerState.ReadyForCheck;
					stateChanged();
				}
			}
		}
	}
	
	public void msgHereIsCheck(TranacCheck c) {
		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.c == c.getCustomer()) {
					mc.s = CustomerState.ReceivingCheck;
					mc.check = c;
					stateChanged();
				}
			}
		}
	}
	
	public void msgDoneEating(TranacCustomer c) {
		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.c == c) {
					mc.s = CustomerState.Done;
					stateChanged();
				}
			}
		}
	}

	public void msgWantToGoOnBreak() {
		print("Want to go on break.");
		mState = WaiterState.WantToGoOnBreak;
		stateChanged();
	}
	
	public void msgGoOnBreak() {
		print("Go on break.");
		mState = WaiterState.CanGoOnBreak;
		askingForBreak.release();
		stateChanged();
	}
	
	public void msgNoBreak() {
		print("No break for me.");
		mState = WaiterState.NoBreak;
		askingForBreak.release();
		stateChanged();
	}

	/** Animation Messages*/
	public void msgAnimationAtWaitingArea() {
		inTransit.release();
		stateChanged();
	}
	
	public void msgAnimationAtTable() {
		inTransit.release();
		stateChanged();
	}
	
	public void msgAnimationAtCook() {
		inTransit.release();
		stateChanged();
	}
	
	public void msgAnimationAtOrderPickup() {
		inTransit.release();
		stateChanged();
	}
	
	public void msgAnimationAtHost() {
		inTransit.release();
		stateChanged();
	}

	public void msgAnimationAtCashier() {
		inTransit.release();
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(customers) {
			for(MyCustomer c : customers) {
				if(c.s == CustomerState.Waiting)
				{
					seatCustomer(c);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer c : customers) {
				if(c.s == CustomerState.ReadyToOrder)
				{

					takeOrder(c);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer c : customers) {
				if(c.s == CustomerState.Ordered)
				{
					sendOrder(c);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer c: customers) {
				if(c.s == CustomerState.OutOfFood)
				{
					tellCustomerOutOfFood(c);
					return true;
				}
			}
		}
		
		synchronized(customers) {
			for(MyCustomer c : customers) {
				if(c.s == CustomerState.FoodDone)
				{
					deliverFood(c);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer c: customers) {
				if(c.s == CustomerState.ReadyForCheck)
				{
					computeCheck(c);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer c: customers) {
				if(c.s == CustomerState.ReceivingCheck)
				{
					giveCheck(c);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer c : customers) {
				if(c.s == CustomerState.Done)
				{
					freeTable(c);
					return true;
				}
			}
		}
/*		if(mState == WaiterState.WantToGoOnBreak) {
			askToGoOnBreak();
			return true;
		}
		if(mState == WaiterState.NoBreak) {
			setNoBreak();
			return true;
		}
		//check if no customers; go on break
		if(customers.isEmpty() && mState == WaiterState.CanGoOnBreak) {
			goOnBreak();
			return true;
		}
*/		DoGoToHome();
		return false;
	}

	/** Actions */
	private void seatCustomer(MyCustomer c) {
		print("Seating customer");
		DoGoToWaitingArea(c.n);
		c.c.msgFollowMe(new TranacMenu(), this);	//tell customer to follow
		mHost.msgCustomerSeated(c.c);		//tell host customer is seated
		DoSeatCustomer(c);
		c.s = CustomerState.Seated;
	}
	
	private void takeOrder(MyCustomer c) {
		DoGoToTable(c.table);
		print("What do you want to eat, " + c.getName() + "?");
		c.c.msgWhatDoYouWant();
		try {
			waitingForOrder.acquire();		//wait for customer to reply
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void tellCustomerOutOfFood(MyCustomer c) {
		print("Telling customer out of food.");
		DoGoToTable(c.table);
		c.c.msgOutOfChoice();
		c.s = CustomerState.Reordering;
	}
	
	private void sendOrder(MyCustomer c) {
		print("Sending order.");
		DoGoToCook();
		c.s = CustomerState.WaitingForFood;
		mCook.msgHereIsOrder(this, c.choice, c.table);
	}
	
	private void deliverFood(MyCustomer c) {
		print("Delivering food to " + c.getName() + ".");
		DoGoToGetOrder(c.orderNum);
		mCook.msgOrderPickedUp(this, c.choice);
		
		waiterGui.setFood(c.choice);	//set gui to show food
		waiterGui.setDeliveringFood();
		DoGoToTable(c.table);
		waiterGui.setClear();		//remove food from waiter gui
		c.c.msgHereIsFood();
		c.s = CustomerState.Eating;
	}
	
	private void computeCheck(MyCustomer c) {
		print("Asking cashier for check");
		c.s = CustomerState.WaitingForCheck;
		DoGoToCashier();
		mCashier.msgComputeCheck(this,c.c,c.choice);
	}
	
	private void giveCheck(MyCustomer c) {
		print("Giving check to customer.");
		c.s = CustomerState.Paying;
		DoGoToCashier();
		waiterGui.setDeliveringCheck();
		DoGoToTable(c.table);
		waiterGui.setClear();
		c.c.msgHereIsCheck(c.check);
	}
	
	private void freeTable(MyCustomer c) {
		print("Freeing table.");
		DoGoToTable(c.table);			//"clean" table
		DoGoToHost();					//"tell" the host table is free
		mHost.msgTableIsFree(c.table);
		customers.remove(c);
	}
/*
	private void askToGoOnBreak() {
		print("Asking to go on break.");
		mHost.msgWantToGoOnBreak(this);
		try {
			askingForBreak.acquire();		//wait for customer to reply
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void goOnBreak() {
		print("Going on break.");
		mState = WaiterState.OnBreak;
		DoGoToHome();
		timer.schedule(new TimerTask() {
			public void run() {
				goOffBreak();
			}
		},
		3000);								//MAGIC NUMBER!
	}

	private void goOffBreak() {
		print("Coming off of break.");
		mState = WaiterState.Active;
		waiterGui.setBreak();
		mHost.msgBackFromBreak(this);
	}
	
	private void setNoBreak() {
		waiterGui.setBreak();
	}
*/
	/** Animation Actions */
	private void DoGoToWaitingArea(int n) {
		waiterGui.DoGoToWaitingArea(n);
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoSeatCustomer(MyCustomer customer) {
		waiterGui.DoBringToTable(customer.c,customer.table);
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToTable(int table) {
		waiterGui.DoGoToTable(table);
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToHost() {
		waiterGui.DoGoToHost();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToCook() {
		waiterGui.DoGoToCook();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToGetOrder(int n) {
		waiterGui.DoGoToGetOrder(n);
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToCashier() {
		waiterGui.DoGoToCashier();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void DoGoToHome() {
		waiterGui.DoGoToHome();
	}
	
	/** Utilities */
	public String getName() {
		return mPerson.getName();
	}
	
	public void setGui(TranacWaiterGui gui) {
		waiterGui = gui;
	}

	public TranacWaiterGui getGui() {
		return waiterGui;
	}

	public void setHost(TranacHost h) {
		mHost = h;
	}
	
	public void setCook(TranacCook c) {
		mCook = c;
	}

	public void setCashier(TranacCashier c) {
		mCashier = c;
	}
	public List<MyCustomer> getCustomers() {
		return customers;
	}
	
	/** Classes */
	private class MyCustomer {
		TranacCustomer c;
		int n;
		int table;
		String choice;
		int orderNum;
		CustomerState s;
		TranacCheck check;
		
		MyCustomer(TranacCustomer c, int n, int t, CustomerState s) {
			this.c = c;
			this.n = n;
			this.table = t;
			this.s = s;
			orderNum = 0;
			check = null;
		}
		
		public String getName() {
			return c.getName();
		}
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(6);
	}
}
