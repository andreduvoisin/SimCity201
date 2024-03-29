package restaurant.restaurant_tranac.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.gui.TranacHostGui;
import restaurant.restaurant_tranac.interfaces.TranacCustomer;
import restaurant.restaurant_tranac.interfaces.TranacHost;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;


/**
 * Restaurant Host Agent
 */

public class TranacHostRole extends BaseRole implements TranacHost{
	private TranacHostGui hostGui;
	
	static final int NTABLES = 3;		//number of tables in rest
	static final int NWAITINGAREA = 20;
	public Collection<Table> tables;

	//list of agents interacting in the restaurant
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());

	private Map<Integer, MyCustomer> waitingAreas = new HashMap<Integer, MyCustomer>(NWAITINGAREA);
	//MyCustomerState
	enum CustomerState {Arrived, WillWait, Walking, Waiting, Debating, Leaving, Seating, Seated, Eating, Finished, Done};
	//MyWaiterState
	enum WaiterState {Active, WantToGoOnBreak, OnBreak};
	
	public TranacHostRole(Person mPerson) {
		super(mPerson);
		hostGui = new TranacHostGui(this);
		TranacRestaurant.addPerson(this);
		TranacRestaurant.addGui(hostGui);

		//create the list of tables
		tables = new ArrayList<Table>(NTABLES);
		for(int i=1;i<= NTABLES;i++) {
			tables.add(new Table(i));
		}
		
		for(int i=0;i<NWAITINGAREA;i++) {
			waitingAreas.put(i,null);
		}
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
		return false;
	}

	/** Actions */
	void showCustomerWaitingArea(MyCustomer c) {
		print("Please wait here.");
		c.s = CustomerState.Walking;
		for(Integer i : waitingAreas.keySet()) {
			if(waitingAreas.get(i) == null) {
				waitingAreas.put(i, c);
				c.n = i;
				c.c.msgPleaseWaitHere(c.n);
				break;
			}
		}
	}
	
	void seatCustomer(MyCustomer c, Table t) {
		if(waiters.isEmpty())
			return;
		//checks if there is an available waiter
		print("Seating " + c.getName() + " at " + t.tableNumber);
		c.s = CustomerState.Seating;
		t.setOccupant(c);
		//finds waiter with the lowest customer count
		MyWaiter waiter = waiters.get(0);
		synchronized(waiters) {
			for(MyWaiter w : waiters) {
				if((w.getCount() < waiter.getCount()))
					waiter = w;
			}
		}
		c.w = waiter;
		waiter.w.msgPleaseSeatCustomer(c.c,c.n,t.getNum());
		waiter.increaseCount();
	}
	
	void removeWaitingCustomer(MyCustomer c) {
		print("Removing customer off waitlist.");
		c.s = CustomerState.Eating;
		waitingAreas.put(c.n,null);
	}
	
	void tellCustomerRestaurantIsFull(MyCustomer c) {
		print("Telling customer restaurant is full.");
		c.s = CustomerState.Debating;
		c.c.msgRestaurantFull();
	}
	
	void removeLeavingCustomer(MyCustomer c) {
		print("Removing customer who left.");
		customers.remove(c);
	}
	
	void removeCustomer(MyCustomer c) {
		//changes customer status to done, sets table unoccupied
		print("Removing " + c.getName() + ".");
		c.s = CustomerState.Done;
		c.w.decreaseCount();
		for(Table t : tables) {
			if(t.getOccupant() == c) {
				t.setUnoccupied();
			}
		}
	}
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
		@SuppressWarnings("unused")
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
		
		MyWaiter(TranacWaiter w) {
			this.w = w;
			customerCount = 0;
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
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.R6);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R6);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R6, e);
	}
}

