package restaurant.restaurant_tranac.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.restaurant_tranac.gui.TranacHostGui;
import restaurant.restaurant_tranac.interfaces.TranacCustomer;
import restaurant.restaurant_tranac.interfaces.TranacHost;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;
import base.BaseRole;
import base.ContactList;
import base.Location;


/**
 * Restaurant Host Agent
 */

public class TranacRestaurantHostRole extends BaseRole implements TranacHost{
	private TranacHostGui hostGui;
	
	static final int NTABLES = 4;		//number of tables in rest
	static final int NWAITINGAREA = 20;
	public Collection<Table> tables;
	private int numWaiters;

	//list of agents interacting in the restaurant
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());

	private Map<Integer, MyCustomer> waitingAreas = new HashMap<Integer, MyCustomer>(NWAITINGAREA);
	//MyCustomerState
	enum CustomerState {Arrived, WillWait, Walking, Waiting, Debating, Leaving, Seating, Seated, Eating, Finished, Done};
	//MyWaiterState
	enum WaiterState {Active, WantToGoOnBreak, OnBreak};
	
	public TranacRestaurantHostRole() {
		super(null);

		//create the list of tables
		tables = new ArrayList<Table>(NTABLES);
		for(int i=1;i<= NTABLES;i++) {
			tables.add(new Table(i));
		}
		
		for(int i=0;i<NWAITINGAREA;i++) {
			waitingAreas.put(i,null);
		}
		numWaiters = 0;
	}

	/** Messages */
	public void msgIWantFood(TranacCustomer c) {
		//check if the customer is a returning customer
		synchronized(customers) {
			for(MyCustomer customer : customers) {
				if(customer.c == c) {
					customer.s = CustomerState.Arrived;
					stateChanged();
					return;
				}
			}
		}
		//else, create a new MyCustomer
		synchronized(customers) {
			customers.add(new MyCustomer(c,CustomerState.Arrived));
		}
		stateChanged();
	}
	
	public void msgWillWait(TranacCustomer c) {
		synchronized(customers) {
			for(MyCustomer customer: customers) {
				if(customer.c == c) {
					customer.s = CustomerState.WillWait;
					stateChanged();
				}
			}
		}
	}
	
	public void msgLeavingEarly(TranacCustomer c) {
		synchronized(customers) {
			for(MyCustomer customer: customers) {
				if(customer.c == c) {
					customer.s = CustomerState.Leaving;
					stateChanged();
				}
			}
		}
	}
	
	public void msgAtWaitingArea(TranacCustomer c) {
		for(MyCustomer customer : customers) {
			if(customer.c == c) {
				customer.s = CustomerState.Waiting;
				stateChanged();
			}
		}
	}
	public void msgCustomerSeated(TranacCustomer c) {
		for(MyCustomer customer : customers) {
			if(customer.c == c) {
				customer.s = CustomerState.Seated;
				stateChanged();
			}
		}
	}
	
	public void msgTableIsFree(int t) {
		//set customer state to finished
		for(Table table : tables) {
			if(table.getNum() == t) {
				MyCustomer c= table.getOccupant();
				c.s = CustomerState.Finished;
			}
		}
		stateChanged();
	}
	
	public void msgWantToGoOnBreak(TranacWaiter w) {
		synchronized(waiters) {
			for(MyWaiter waiter : waiters) {
				if(waiter.w == w) {
					waiter.s = WaiterState.WantToGoOnBreak;
					stateChanged();
				}
			}
		}
	}

	public void msgBackFromBreak(TranacWaiter w) {
		synchronized(waiters) {
			for(MyWaiter waiter : waiters) {
				if(waiter.w == w) {
					waiter.s = WaiterState.Active;
					numWaiters++;
					stateChanged();
				}
			}
		}
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(customers) {
			for(MyCustomer customer : customers) {
				if(customer.s == CustomerState.Leaving) {
					removeLeavingCustomer(customer);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer customer : customers) {
				if(customer.s == CustomerState.Finished) {
					removeCustomer(customer);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer customer : customers) {
				if(customer.s == CustomerState.Arrived) {
					if(restaurantFull())
						tellCustomerRestaurantIsFull(customer);
					else
						showCustomerWaitingArea(customer);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer customer : customers) {
				if(customer.s == CustomerState.Seated) {
					removeWaitingCustomer(customer);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer customer : customers) {
				if(customer.s == CustomerState.WillWait) {
					showCustomerWaitingArea(customer);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(MyCustomer customer : customers) {
				if(customer.s == CustomerState.Waiting)  {
					for(Table table : tables) {
						if(!table.isOccupied()) {
							seatCustomer(customer,table);
							return true;
						}
					}
				}
			}
		}
	/*	synchronized(waiters) {
			for(MyWaiter waiter : waiters) {
				if(waiter.s == WaiterState.WantToGoOnBreak) {
					checkIfBreakPossible(waiter);
					return true;
				}
			}
		}
	*/	return false;
	}

	/** Actions */
	void showCustomerWaitingArea(MyCustomer c) {
		Do("Please wait here.");
		c.s = CustomerState.Walking;
		for(Integer i : waitingAreas.keySet()) {
			if(waitingAreas.get(i) == null) {
				waitingAreas.put(i, c);
				c.n = i;
				c.c.msgPleaseWaitHere(c.n);
				break;
			}
		}
		/*
		synchronized(waitingPositions) {
		for(MyCustomer customer : waitingPositions) {
			if(customer == null) {
				customer = c;
				c.n = waitingPositions.indexOf(customer);
				c.c.msgPleaseWaitHere(c.n);
				return;
			}
		}
		}
		waitingPositions.add(c);
		c.n = waitingPositions.indexOf(c);*/

	}
	
	void seatCustomer(MyCustomer c, Table t) {
		if(waiters.isEmpty())
			return;
		//checks if there is an available waiter
		Do("Seating " + c.getName() + " at " + t.tableNumber);
		c.s = CustomerState.Seating;
		t.setOccupant(c);
		//finds waiter with the lowest customer count
		MyWaiter waiter = waiters.get(0);
		//switches first waiter to second if 1 on break
		if(waiter.s == WaiterState.OnBreak)
			waiter = waiters.get(1);
		synchronized(waiters) {
			for(MyWaiter w : waiters) {
				if(w.s == WaiterState.Active) {
					if((w.getCount() < waiter.getCount()))
						waiter = w;
				}
			}
		}
		c.w = waiter;
		waiter.w.msgPleaseSeatCustomer(c.c,c.n,t.getNum());
		waiter.increaseCount();
	}
	
	void removeWaitingCustomer(MyCustomer c) {
		Do("Removing customer off waitlist.");
		c.s = CustomerState.Eating;
		waitingAreas.put(c.n,null);
	}
	
	void tellCustomerRestaurantIsFull(MyCustomer c) {
		Do("Telling customer restaurant is full.");
		c.s = CustomerState.Debating;
		c.c.msgRestaurantFull();
	}
	
	void removeLeavingCustomer(MyCustomer c) {
		Do("Removing customer who left.");
		customers.remove(c);
	}
	
	void removeCustomer(MyCustomer c) {
		//changes customer status to done, sets table unoccupied
		Do("Removing " + c.getName() + ".");
		c.s = CustomerState.Done;
		c.w.decreaseCount();
		for(Table t : tables) {
			if(t.getOccupant() == c) {
				t.setUnoccupied();
			}
		}
	}
/*
	void checkIfBreakPossible(MyWaiter w) {
		Do("Checking if waiter can go on break.");
		if(numWaiters == 1) {
			//send message saying no
			w.s = WaiterState.Active;
			w.w.msgNoBreak();
		}
		else {
			//send message saying yes
			w.s = WaiterState.OnBreak;
			numWaiters--;
			w.w.msgGoOnBreak();
		}
	}
	*/
	/** Utilities */
	public String getName() {
		return mPerson.getName();
	}

	public List<MyCustomer> getCustomers() {
		return customers;
	}

	public Collection<Table> getTables() {
		return tables;
	}

	public void addWaiter(TranacWaiter w) {		//add hack for waiters currently
		synchronized(waiters) {
			waiters.add(new MyWaiter(w));
		}
		numWaiters++;
	}
	
	private boolean restaurantFull() {
		for(Table table : tables) {
			if(!table.isOccupied())
				return false;
		}
		return true;
	}
	
	public TranacHostGui getGui() {
		return hostGui;
	}
	
	public void setGui(TranacHostGui g) {
		hostGui = g;
	}
	
	/** Classes */
	
	private class MyCustomer {
		TranacCustomer c;
		CustomerState s;
		MyWaiter w;
		boolean willWait = false;
		int n;
		
		MyCustomer(TranacCustomer c, CustomerState s) {
			this.c = c;
			this.w = null;
			this.s = s;
			n = 0;
		}
		
		String getName() {
			return c.getName();
		}
	}
	
	private class MyWaiter {
		TranacWaiter w;
		int customerCount;
		WaiterState s;
		
		MyWaiter(TranacWaiter w) {
			this.w = w;
			customerCount = 0;
			s = WaiterState.Active;
		}

		void increaseCount() {
			customerCount++;
		}
		
		void decreaseCount() {
			customerCount--;
		}
		
		int getCount() {
			return customerCount;
		}
	}
	
	private class Table {
		MyCustomer occupiedBy;
		int tableNumber;
		
		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(MyCustomer c) {
			occupiedBy = c;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		MyCustomer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		int getNum() {
			return tableNumber;
		}
		
		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(6);
	}
}

