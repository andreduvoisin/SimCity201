package restaurant.restaurant_duvoisin.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.restaurant_duvoisin.AndreRestaurant;
import restaurant.restaurant_duvoisin.Menu;
import restaurant.restaurant_duvoisin.gui.WaiterGui;
import restaurant.restaurant_duvoisin.interfaces.Customer;
import restaurant.restaurant_duvoisin.interfaces.Waiter;
import restaurant.restaurant_duvoisin.test.mock.EventLog;
import restaurant.restaurant_duvoisin.test.mock.LoggedEvent;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;
import city.gui.trace.AlertTag;

/**
 * Restaurant Waiter Agent
 */
public class AndreSharedWaiterRole extends BaseRole implements Waiter {	
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	public enum CustomerState { Waiting, Seated, ReadyToOrder, AskedToOrder, Ordered, OutOfFood, FoodCooking, FoodReady, Eating, RequestedCheck, WaitingForCheck, CheckComputed, Paying, Done };
	
	public enum MyState { Working, RequestBreak, RequestedBreak, OnBreakPending, OnBreak, OffBreak };
	
	private String name;

	public Semaphore atCustomer = new Semaphore(0, true);
	public Semaphore atTable = new Semaphore(0, true);
	public Semaphore atCook = new Semaphore(0, true);
	public Semaphore atCashier = new Semaphore(0, true);

	public WaiterGui waiterGui = null;
	
	public Boolean paused = false;
	
	public MyState state = MyState.Working;
	
	static final long breakTime = 20000;
	public Timer timer = new Timer();
	
	public EventLog log = new EventLog();
	
	public static int sharedWaiterNumber = 0;

	public AndreSharedWaiterRole(Person person) {
		super(person);
		//this.name = person.getName();
			this.name = "AndreSharedWaiter" + sharedWaiterNumber;
			sharedWaiterNumber++;
	}

	public String getName() {
		return name;
	}
	
	// Messages
	
	public void msgSitAtTable(Customer c, int table, int waitingPosition) {
		print("msgSitAtTable received");
		customers.add(new MyCustomer(c, table, waitingPosition, CustomerState.Waiting));
		stateChanged();
	}
	
	public void msgImReadyToOrder(Customer c) {
		print("msgImReadyToOrder received");
		synchronized(customers) {
			for(MyCustomer mc : customers)
				if(mc.customer == c) {
					mc.state = CustomerState.ReadyToOrder;
					stateChanged();
				}
		}
	}
	
	public void msgHereIsMyChoice(Customer c, String choice) {
		print("msgHereIsMyChoice received");
		synchronized(customers) {
			for(MyCustomer mc : customers)
				if(mc.customer == c) {
					mc.state = CustomerState.Ordered;
					mc.choice = choice;
					stateChanged();
				}
		}
	}
	
	public void msgOrderIsReady(String choice, int table, int position) {
		print("msgOrderIsReady received");
		synchronized(customers) {
			for(MyCustomer mc : customers)
				if(mc.choice != null && mc.choice.equals(choice) && mc.table == table) {
					mc.state = CustomerState.FoodReady;
					mc.foodPosition = position;
					stateChanged();
				}
		}
	}
	
	public void msgDoneEatingAndLeaving(Customer c) {
		print("msgDoneEatingAndLeaving received");
		synchronized(customers) {
			for(MyCustomer mc : customers)
				if(mc.customer == c) {
					mc.state = CustomerState.Done;
					stateChanged();
				}
		}
	}
	
	public void msgOutOfFood(int table, String choice) {
		print("msgOutOfFood received");
		synchronized(customers) {
			for(MyCustomer mc : customers)
				if(mc.table == table && mc.choice == choice) {
					mc.state = CustomerState.OutOfFood;
					stateChanged();
				}
		}
	}
	
	public void msgRequestBreak() { //from animation
		print("msgRequestBreak() called");
		state = MyState.RequestBreak;
		stateChanged();
	}
	
	public void msgRespondToBreakRequest(Boolean answer) {
		print("msgRespondToBreakRequest received");
		// T = Yes, F = No
		if(answer)
			state = MyState.OnBreakPending;
		else {
			state = MyState.Working;
			waiterGui.enableButton();	//fixes animation button
		}
		stateChanged();
	}
	
	public void msgRequestCheck(Customer c) {
		print("msgRequestCheck received");
		synchronized(customers) {
			for(MyCustomer mc : customers)
				if(mc.customer == c)
					mc.state = CustomerState.RequestedCheck;
		}
		stateChanged();
	}
	
	public void msgHereIsCheck(Customer c, double amount) {
		print("msgHereIsCheck received");
		synchronized(customers) {
			for(MyCustomer mc : customers)
				if(mc.customer == c) {
					mc.amountOwed = amount;
					mc.state = CustomerState.CheckComputed;
				}
		}
		stateChanged();
	}
	
	public void msgAtCustomer() { //from animation
		print("msgAtEntrance() called");
		atCustomer.release();
		stateChanged();
	}
	
	public void msgAtTable() { //from animation
		print("msgAtTable() called");
		atTable.release();
		stateChanged();
	}
	
	public void msgAtCook() { //from animation
		print("msgAtCook() called");
		atCook.release();
		stateChanged();
	}
	
	public void msgAtCashier() {
		atCashier.release();
		stateChanged();
	}
	
	public void msgPauseScheduler() {
		paused = true;
	}
	
	public void msgResumeScheduler() {
		paused = false;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try {
			if(!paused) {
				if(state == MyState.RequestBreak) {
					RequestToTakeBreak();
					return true;
				}
				if(state == MyState.OffBreak) {
					GoOffBreak();
					return true;
				}
				for(MyCustomer c : customers)
					if(c.state == CustomerState.Waiting) {
						SeatCustomer(c, c.table);
						return true;
					}
				for(MyCustomer c : customers)
					if(c.state == CustomerState.ReadyToOrder) {
						TakeOrder(c);
						return true;
					}
				for(MyCustomer c : customers)
					if(c.state == CustomerState.Ordered) {
						GiveOrderToCook(c);
						return true;
					}
				for(MyCustomer c : customers)
					if(c.state == CustomerState.OutOfFood) {
						WeAreOutOfChoice(c);
						return true;
					}
				for(MyCustomer c : customers)
					if(c.state == CustomerState.FoodReady) {
						TakeFoodToCustomer(c);
						return true;
					}
				for(MyCustomer c : customers)
					if(c.state == CustomerState.RequestedCheck) {
						AskCashierToComputeCheck(c);
						return true;
					}
				for(MyCustomer c : customers)
					if(c.state == CustomerState.CheckComputed) {
						GiveCustomerCheck(c);
						return true;
					}
				for(MyCustomer c : customers)
					if(c.state == CustomerState.Done) {
						CleanTable(c);
						return true;
					}
				if(state == MyState.OnBreakPending && customers.isEmpty()) {
					GoOnBreak();
					return true;
				}
			}
		} catch (ConcurrentModificationException cme) { return false; }
		return false;
	}

	// Actions
	private void RequestToTakeBreak() {
		print("Doing RequestToTakeBreak");
		state = MyState.RequestedBreak;
		AndreRestaurant.host.msgRequestGoOnBreak(this);
	}
	
	private void SeatCustomer(MyCustomer c, int table) {
		print("Doing SeatCustomer");
		waiterGui.DoGoToCustomer(c.waitingPosition);
		try {
			atCustomer.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.customer.msgFollowMeToTable(this, c.menu, table);
		waiterGui.DoGoToTable(table);
		c.state = CustomerState.Seated;
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.DoGoWait();
	}
	
	private void TakeOrder(MyCustomer c) {
		print("Doing TakeOrder");
		waiterGui.DoGoToTable(c.table);	//animation
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.customer.msgWhatWouldYouLike();
		c.state = CustomerState.AskedToOrder;
	}
	
	private void GiveOrderToCook(MyCustomer c) {
		print("Doing GiveOrderToCook");
		log.add(new LoggedEvent("Doing GiveOrderToCook"));
		waiterGui.DoGoToRevolvingStand();	//animation
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized(AndreRestaurant.cook.getRevolvingStand()) {
			AndreRestaurant.cook.getRevolvingStand().add(AndreRestaurant.cook.new Order(this, c.choice, c.table, AndreCookRole.OrderState.Pending));
		}
		waiterGui.DoGoWait();
		c.state = CustomerState.FoodCooking;
	}
	
	private void WeAreOutOfChoice(MyCustomer c) {
		print("Doing WeAreOutOfChoice");
		c.menu.removeMenuOption(c.choice);
		waiterGui.DoGoToTable(c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.customer.msgOutOfChoice(c.menu);
		c.state = CustomerState.AskedToOrder;
	}
	
	private void TakeFoodToCustomer(MyCustomer c) {
		print("Doing TakeFoodToCustomer");
		waiterGui.DoGoToCook(c.foodPosition);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		AndreRestaurant.cook.msgGotFood(c.foodPosition);
		waiterGui.DoGoToTable(c.table);	//animation
		waiterGui.setCurrentOrder(c.choice);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.setCurrentOrder("");
		c.customer.msgHereIsYourFood(c.choice);
		c.state = CustomerState.Eating;
		waiterGui.DoGoWait();
	}
	
	private void AskCashierToComputeCheck(MyCustomer c) {
		print("Doing AskCashierToComputeCheck");
		AndreRestaurant.cashier.msgComputeBill(this, c.customer, c.choice);
		c.state = CustomerState.WaitingForCheck;
	}
	
	private void GiveCustomerCheck(MyCustomer c) {
		print("Doing GiveCustomerCheck");
		waiterGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.DoGoToTable(c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.customer.msgHereIsCheck(c.amountOwed);
		c.state = CustomerState.Paying;
		waiterGui.DoGoWait();
	}
	
	private void CleanTable(MyCustomer c) {
		print("Doing CleanTable");
		AndreRestaurant.host.msgTableIsFree(this, c.table);
		customers.remove(c);
	}
	
	private void GoOnBreak() {
		print("Doing GoOnBreak");
		state = MyState.OnBreak;
		waiterGui.DoGoOnBreak(); // Animation
		timer.schedule(new TimerTask() {
			public void run() {
				state = MyState.OffBreak;
				stateChanged();
			}
		}, breakTime);
	}
	
	private void GoOffBreak() {
		print("Doing GoOffBreak");
		waiterGui.DoGoOffBreak(); // Animation
		AndreRestaurant.host.msgOffBreak(this);
		state = MyState.Working;
		stateChanged();
	}

	// The animation DoXYZ() routines
	/*
	private void DoSeatCustomer(Customer customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(customer, table); 
	}
	*/

	//utilities
	class MyCustomer {
		Customer customer;
		int table;
		String choice;
		CustomerState state;
		double amountOwed;
		Menu menu;
		int waitingPosition;
		int foodPosition;
		
		MyCustomer(Customer ca, int t, int wP, CustomerState s) {
			customer = ca;
			table = t;
			waitingPosition = wP;
			state = s;
			menu = new Menu();
		}
	}
	
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
		waiterGui.DoGoWait();
	}

	public WaiterGui getGui() {
		return waiterGui;
	}

	@Override
	public void pauseBaseAgent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeBaseAgent() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(0);
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.R0);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R0);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R0, e);
	}
}