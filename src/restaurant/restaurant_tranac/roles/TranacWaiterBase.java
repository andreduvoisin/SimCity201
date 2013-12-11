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
import base.ContactList;
import base.Location;
import base.PersonAgent.EnumJobType;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

/**
 * Restaurant Waiter Agent
 */

public abstract class TranacWaiterBase extends BaseRole implements TranacWaiter{
	public TranacWaiterGui waiterGui = null;

	//agent correspondents
	protected TranacHost mHost = null; 
	protected TranacCook mCook = null;
	protected TranacCashier mCashier = null;
	
	//synchronized list to make it "thread-safe"
	protected List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public enum CustomerState {Waiting, Seated, ReadyToOrder, OutOfFood, Reordering, Ordered, WaitingForFood, FoodDone, Eating, ReadyForCheck, WaitingForCheck, ReceivingCheck, ReceivedCheck, Paying, Done};
	
	//semaphores to prevent action while waiting for another message
	protected Semaphore inTransit = new Semaphore(0,true);		//used for animation
	protected Semaphore waitingForOrder = new Semaphore(0,true);	//used for asking for order

	public TranacWaiterBase(Person p) {
		super(p);
		waiterGui = new TranacWaiterGui(this, TranacRestaurant.getNumWaiters());
		TranacRestaurant.addPerson(this);
		TranacRestaurant.addGui(waiterGui);
		
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
//		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.c == c) {
					mc.s = CustomerState.ReadyToOrder;
					stateChanged();
				}
			}	
//		}
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
	//	synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.table == table) {
					mc.orderNum = n;
					mc.s = CustomerState.FoodDone;
					stateChanged();
				}
			}
	//	}
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

	/** Animation Messages*/
	public void msgAnimationDone() {
		inTransit.release();
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
		DoGoToHome();
		return false;
	}

	/** Actions */
	protected void seatCustomer(MyCustomer c) {
		print("Seating customer");
		DoGoToWaitingArea(c.n);
		c.c.msgFollowMe(new TranacMenu(), this);	//tell customer to follow
		mHost.msgCustomerSeated(c.c);		//tell host customer is seated
		DoSeatCustomer(c);
		c.s = CustomerState.Seated;
	}
	
	protected void takeOrder(MyCustomer c) {
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

	protected void tellCustomerOutOfFood(MyCustomer c) {
		print("Telling customer out of food.");
		DoGoToTable(c.table);
		c.c.msgOutOfChoice();
		c.s = CustomerState.Reordering;
	}
	
	protected abstract void sendOrder(MyCustomer c);
	
	protected void deliverFood(MyCustomer c) {
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
	
	protected void computeCheck(MyCustomer c) {
		print("Asking cashier for check");
		c.s = CustomerState.WaitingForCheck;
		DoGoToCashier();
		mCashier.msgComputeCheck(this,c.c,c.choice);
	}
	
	protected void giveCheck(MyCustomer c) {
		print("Giving check to customer.");
		c.s = CustomerState.Paying;
		DoGoToCashier();
		waiterGui.setDeliveringCheck();
		DoGoToTable(c.table);
		waiterGui.setClear();
		c.c.msgHereIsCheck(c.check);
	}
	
	protected void freeTable(MyCustomer c) {
		print("Freeing table.");
		DoGoToTable(c.table);			//"clean" table
		DoGoToHost();					//"tell" the host table is free
		mHost.msgTableIsFree(c.table);
		customers.remove(c);
	}

	/** Animation Actions */
	protected void DoGoToWaitingArea(int n) {
		waiterGui.DoGoToWaitingArea(n);
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void DoSeatCustomer(MyCustomer customer) {
		waiterGui.DoBringToTable(customer.c,customer.table);
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void DoGoToTable(int table) {
		waiterGui.DoGoToTable(table);
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void DoGoToHost() {
		waiterGui.DoGoToHost();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void DoGoToCook() {
		waiterGui.DoGoToCook();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void DoGoToGetOrder(int n) {
		waiterGui.DoGoToGetOrder(n);
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void DoGoToCashier() {
		waiterGui.DoGoToCashier();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void DoGoToHome() {
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
	class MyCustomer {
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
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.R6);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R6);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R6, e);
	}
	
	public void fuckYou(){
		waiterGui.setFired(true);
		
		mPerson.msgRoleFinished();
		mPerson.assignNextEvent();
		
		mPerson.removeRole(this);
		mPerson.setJobType(EnumJobType.NONE);
	}
}
