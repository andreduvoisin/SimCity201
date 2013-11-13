package restaurant_tranac.agents;

import restaurant_tranac.Check;
import restaurant_tranac.Menu;
import base.Agent;
import restaurant_tranac.gui.WaiterGui;
import restaurant_tranac.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */

public class WaiterAgent extends Agent implements Waiter{
	private String name;
	public WaiterGui waiterGui = null;
	private Timer timer = new Timer();	//used for breaks
	
	//agent correspondents
	private Host host = null; 
	private Cook cook = null;
	private Cashier cashier = null;
	
	//synchronized list to make it "thread-safe"
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public enum CustomerState {Waiting, Seated, ReadyToOrder, OutOfFood, Reordering, Ordered, WaitingForFood, FoodDone, Eating, ReadyForCheck, WaitingForCheck, ReceivingCheck, ReceivedCheck, Paying, Done};

	private enum WaiterState {Active, WantToGoOnBreak, CanGoOnBreak, NoBreak, OnBreak};
	
	private WaiterState state;
	
	//semaphores to prevent action while waiting for another message
	private Semaphore inTransit = new Semaphore(0,true);		//used for animation
	private Semaphore waitingForOrder = new Semaphore(0,true);	//used for asking for order
	private Semaphore askingForBreak = new Semaphore(0,true);
	
	public WaiterAgent(String n, Host h, Cook c, Cashier cash) {
		super();

		name = n;
		host = h;
		cook = c;
		cashier = cash;
		
		state = WaiterState.Active;
	}
	
	/** Messages */

	public void msgPleaseSeatCustomer(Customer c, int n, int table) {
		synchronized(customers) {
			customers.add(new MyCustomer(c,n,table,CustomerState.Waiting));
		}
		stateChanged();
	}

	public void msgReadyToOrder(Customer c) {
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
	
	public void msgReceivedOrder(Customer c, String choice) {
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
	
	public void msgAskingForCheck(Customer c) {
		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.c == c) {
					mc.s = CustomerState.ReadyForCheck;
					stateChanged();
				}
			}
		}
	}
	
	public void msgHereIsCheck(Check c) {
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
	
	public void msgDoneEating(Customer c) {
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
		Do("Want to go on break.");
		state = WaiterState.WantToGoOnBreak;
		stateChanged();
	}
	
	public void msgGoOnBreak() {
		Do("Go on break.");
		state = WaiterState.CanGoOnBreak;
		askingForBreak.release();
		stateChanged();
	}
	
	public void msgNoBreak() {
		Do("No break for me.");
		state = WaiterState.NoBreak;
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
	protected boolean pickAndExecuteAnAction() {
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
		if(state == WaiterState.WantToGoOnBreak) {
			askToGoOnBreak();
			return true;
		}
		if(state == WaiterState.NoBreak) {
			setNoBreak();
			return true;
		}
		//check if no customers; go on break
		if(customers.isEmpty() && state == WaiterState.CanGoOnBreak) {
			goOnBreak();
			return true;
		}
		DoGoToHome();
		return false;
	}

	/** Actions */
	private void seatCustomer(MyCustomer c) {
		Do("Seating customer");
		DoGoToWaitingArea(c.n);
		c.c.msgFollowMe(new Menu(), this);	//tell customer to follow
		host.msgCustomerSeated(c.c);		//tell host customer is seated
		DoSeatCustomer(c);
		c.s = CustomerState.Seated;
	}
	
	private void takeOrder(MyCustomer c) {
		DoGoToTable(c.table);
		Do("What do you want to eat, " + c.getName() + "?");
		c.c.msgWhatDoYouWant();
		try {
			waitingForOrder.acquire();		//wait for customer to reply
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void tellCustomerOutOfFood(MyCustomer c) {
		Do("Telling customer out of food.");
		DoGoToTable(c.table);
		c.c.msgOutOfChoice();
		c.s = CustomerState.Reordering;
	}
	
	private void sendOrder(MyCustomer c) {
		Do("Sending order.");
		DoGoToCook();
		c.s = CustomerState.WaitingForFood;
		cook.msgHereIsOrder(this, c.choice, c.table);
	}
	
	private void deliverFood(MyCustomer c) {
		Do("Delivering food to " + c.getName() + ".");
		DoGoToGetOrder(c.orderNum);
		Do("Testing");
		cook.msgOrderPickedUp(this, c.choice);
		
		waiterGui.setFood(c.choice);	//set gui to show food
		waiterGui.setDeliveringFood();
		DoGoToTable(c.table);
		waiterGui.setClear();		//remove food from waiter gui
		c.c.msgHereIsFood();
		c.s = CustomerState.Eating;
	}
	
	private void computeCheck(MyCustomer c) {
		Do("Asking cashier for check");
		c.s = CustomerState.WaitingForCheck;
		DoGoToCashier();
		cashier.msgComputeCheck(this,c.c,c.choice);
	}
	
	private void giveCheck(MyCustomer c) {
		Do("Giving check to customer.");
		c.s = CustomerState.Paying;
		DoGoToCashier();
		waiterGui.setDeliveringCheck();
		DoGoToTable(c.table);
		waiterGui.setClear();
		c.c.msgHereIsCheck(c.check);
	}
	
	private void freeTable(MyCustomer c) {
		Do("Freeing table.");
		DoGoToTable(c.table);			//"clean" table
		DoGoToHost();					//"tell" the host table is free
		host.msgTableIsFree(c.table);
		customers.remove(c);
	}

	private void askToGoOnBreak() {
		Do("Asking to go on break.");
		host.msgWantToGoOnBreak(this);
		try {
			askingForBreak.acquire();		//wait for customer to reply
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void goOnBreak() {
		Do("Going on break.");
		state = WaiterState.OnBreak;
		DoGoToHome();
		timer.schedule(new TimerTask() {
			public void run() {
				goOffBreak();
			}
		},
		3000);								//MAGIC NUMBER!
	}

	private void goOffBreak() {
		Do("Coming off of break.");
		state = WaiterState.Active;
		waiterGui.setBreak();
		host.msgBackFromBreak(this);
	}
	
	private void setNoBreak() {
		waiterGui.setBreak();
	}

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
		return name;
	}
	
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}

	public void setHost(Host h) {
		host = h;
	}
	
	public void setCook(Cook c) {
		cook = c;
	}

	public List<MyCustomer> getCustomers() {
		return customers;
	}
	
	/** Classes */
	private class MyCustomer {
		Customer c;
		int n;
		int table;
		String choice;
		int orderNum;
		CustomerState s;
		Check check;
		
		MyCustomer(Customer c, int n, int t, CustomerState s) {
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
}
