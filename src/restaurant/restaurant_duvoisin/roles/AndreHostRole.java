package restaurant.restaurant_duvoisin.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_duvoisin.AndreRestaurant;
import restaurant.restaurant_duvoisin.interfaces.Customer;
import restaurant.restaurant_duvoisin.interfaces.Host;
import restaurant.restaurant_duvoisin.interfaces.Waiter;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class AndreHostRole extends BaseRole implements Host {
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public List<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	enum WaiterState { Working, RequestedBreak, OnBreak };
	enum CustomerState { Added, NotifiedFull };

	private String name;
	
	Boolean paused = false;

	public AndreHostRole(Person person) {
		super(person);

		this.name = "AndreHost";
		
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(AndreRestaurant.tgui.getNumTables()));
		synchronized(tables) {
			for (int i = 1; i <= AndreRestaurant.tgui.getNumTables(); i++) {
				tables.add(new Table(i));
			}
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List<MyCustomer> getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection<Table> getTables() {
		return tables;
	}
	
	public void addWaiter(Waiter wa) {
		synchronized(waiters) {
			waiters.add(new MyWaiter(wa, WaiterState.Working));
		}
	}
	// Messages

	public void msgIWantToEat(Customer cust, int waitingPosition) {
		print("msgIWantToEat received");
		waitingCustomers.add(new MyCustomer(cust, waitingPosition, CustomerState.Added));
		stateChanged();
	}
	
	public void msgLeavingBecauseRestaurantFull(Customer cust) {
		print("msgLeavingBecauseRestaurantFull received");
		synchronized(waitingCustomers) {
			for(MyCustomer mc : waitingCustomers)
				if(mc.customer == cust) {
					waitingCustomers.remove(mc);
					break;
				}
		}
		stateChanged();
	}

	public void msgTableIsFree(Waiter w, int table) {
		print("msgTableIsFree received");
		//Free table.
		synchronized(tables) {
			for(Table t : tables)
				if(t.tableNum == table) {
					t.isOccupied = false;
					synchronized(waiters) {
						for(MyWaiter mw : waiters)
							if(mw.waiter == w)
								mw.numCustomers--;
					}
					stateChanged();
				}
		}
	}
	
	public void msgRequestGoOnBreak(Waiter w) {
		print("msgRequestGoOnBreak received");
		synchronized(waiters) {
			for(MyWaiter mw : waiters)
				if(mw.waiter == w)
					mw.state = WaiterState.RequestedBreak;
		}
		stateChanged();
	}
	
	public void msgOffBreak(Waiter w) {
		print("msgOffBreak received");
		synchronized(waiters) {
			for(MyWaiter mw : waiters)
				if(mw.waiter == w)
					mw.state = WaiterState.Working;
		}
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
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(!paused) {
			synchronized(waiters) {
				for(MyWaiter mw : waiters)
					if(mw.state == WaiterState.RequestedBreak) {
						ProcessRequest(mw);
						return true;
					}
			}
			if(!waiters.isEmpty()) {
				synchronized(waitingCustomers) {
					synchronized(tables) {
						for(MyCustomer c : waitingCustomers) {
							for(Table t : tables) {
								if(t.isOccupied == false) {
									SeatCustomer(c, t);
									return true;
								}
							}
						}
					}
				}
			} else {
				stateChanged();
			}
			synchronized(waitingCustomers) {
				for(MyCustomer c : waitingCustomers)
					if(c.state == CustomerState.Added)
						if(checkTables()) {
							NotifyRestaurantFull(c);
							return true;
						}
			}
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void SeatCustomer(MyCustomer mc, Table table) {
		print("Doing SeatCustomer");
		// Which waiter to use?
		// Use the one with the least customers who is not on break.
			// Will still assign to waiters who have requested breaks, but have not been responded to.
			// This way, if all waiters are requesting breaks, the customer may still be seated.
			// Will also assign to waiters who have been told to request a break from the GUI, but
			// have not yet sent a message to the host.
		MyWaiter mw = waiters.get(0);
		synchronized(waiters) {
			for(MyWaiter w : waiters) {
				if(w.state == WaiterState.OnBreak)
					continue;
				else if(mw.state == WaiterState.OnBreak && w.state == WaiterState.Working)
					mw = w;
				else if(w.numCustomers < mw.numCustomers)
					mw = w;
			}
		}
		mw.waiter.msgSitAtTable(mc.customer, table.tableNum, mc.waitingPosition);
		mw.numCustomers++;
		waitingCustomers.remove(mc);
		table.isOccupied = true;
	}
	
	private void NotifyRestaurantFull(MyCustomer mc) {
		print("Doing NotifyRestaurantFull");
		mc.customer.msgRestaurantFull();
		mc.state = CustomerState.NotifiedFull;
	}
	
	private void ProcessRequest(MyWaiter waiter) {
		print("Doing ProcessRequest");
		// T = Yes, F = No
		Boolean response = false;
		synchronized(waiters) {
			for(MyWaiter mw : waiters)
				if(mw != waiter)
					if(mw.state == WaiterState.Working)
						response = true;
		}
		if(response)
			waiter.state = WaiterState.OnBreak;
		else
			waiter.state = WaiterState.Working;
		waiter.waiter.msgRespondToBreakRequest(response);
	}

	// The animation DoXYZ() routines

	//utilities
	private Boolean checkTables() {
		synchronized(tables) {
			for(Table t : tables)
				if(t.isOccupied == false)
					return false;
		}
		return true;
	}
	
	private class Table {
		int tableNum;
		Boolean isOccupied;
		
		Table(int tNum) {
			tableNum = tNum;
			isOccupied = false;
		}
	}
	
	public class MyWaiter {
		public Waiter waiter;
		public int numCustomers;
		WaiterState state;
		
		MyWaiter(Waiter w, WaiterState ws) {
			waiter = w;
			state = ws;
			numCustomers = 0;
		}
	}
	
	private class MyCustomer {
		Customer customer;
		CustomerState state;
		int waitingPosition;
		
		MyCustomer(Customer c, int wP, CustomerState cs) {
			customer = c;
			waitingPosition = wP;
			state = cs;
		}
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

