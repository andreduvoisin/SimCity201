package restaurant_all.roles.Host;

import restaurant.restaurant_cwagoner.interfaces.*;

import java.util.*;

import base.BaseRole;

/**
 * "Greets" customers as they enter.
 * Decides which empty table each customer will occupy.
 * Assigns waiting customers to a waiter
 */
public class HostRole_cw extends BaseRole implements Host {

	public HostRole_cw(String hostName) {
		name = hostName;
	}
	
	
	// DATA

	private String name;
	private List<Table> Tables =
			Collections.synchronizedList(new ArrayList<Table>());
	private List<MyCustomer> Customers =
			Collections.synchronizedList(new ArrayList<MyCustomer>());
	private List<MyWaiter> Waiters =
			Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	
	// MESSAGES

	/** 
	 * Message from a customer entering the restaurant.
	 * Indicates to the host that he/she is hungry.
	 * @param cust CustomerAgent that gets placed on host's customer list.
	 */
	public void msgIWantFood(Customer c) {
		print("Received msgIWantFood(" + c.getName() + ")");

		Customers.add(new MyCustomer(c));
		stateChanged();
	}

	/**
	 * Message from a waiter that a customer has left and
	 * the table can be assigned
	 * @param c CustomerAgent who has left the restaurant
	 * @param tableNum Table number of that customer
	 */
	public void msgCustomerGoneTableEmpty(Customer c, int tableNum) {
		print("Received msgCustomerGoneTableEmpty(" + c.getName() + ", table " + tableNum + ")");
		
		synchronized(Customers) {
			for (MyCustomer mc : Customers) {
				if (mc.customer.equals(c)) {
					Customers.remove(mc);
					break;
				}
			}
		}
		
		Tables.get(tableNum).occupied = false;
	}
	
	/** From a waiter asking to go on break ("Ask for break" button has been triggered)
	 * @param w WaiterAgent asking for a break
	 */
	public void msgCanIGoOnBreak(Waiter w) {
		print("Received msgCanIGoOnBreak(" + w.getName() + ")");
		
		synchronized(Waiters) {
			for (MyWaiter mw : Waiters) {
				if (mw.waiter.equals(w)) {
					mw.state = MyWaiter.State.askedForBreak;
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgOffBreak(Waiter w) {
		print("Received msgOffBreak(" + w.getName() + ")");
		
		synchronized(Waiters) {
			for (MyWaiter mw : Waiters) {
				if (mw.waiter.equals(w)) {
					mw.state = MyWaiter.State.working;
					stateChanged();
					return;
				}
			}
		}
	}
	
	
	// SCHEDULER
	/**
	 * Reviews waiters' break requests first. Denies if only one working waiter.
	 * 
	 * If there is a customer waiting to be seated,
	 * an unoccupied table,
	 * and at least one waiter in the restaurant,
	 * the host assigns the customer to that table, and
	 * marks the table occupied
	 */
	public boolean pickAndExecuteAnAction() {
		
		synchronized(Waiters) {
			for (MyWaiter w: Waiters) {
				if (w.state.equals(MyWaiter.State.askedForBreak)) {
					DecideBreak(w);
					return true;
				}
			}
		}
		
		// A waiter with state askedForBreak CANNOT make it past the first rule,
		// so the next rule only checks for state working (the alternative is onBreak)
		
		// If there is a customer waiting
		synchronized(Customers) {
			for (MyCustomer c : Customers) {
				if (c.state.equals(MyCustomer.State.waiting)) {
					
					// And an unoccupied table
					for (Table t : Tables) {
						if (! t.occupied) {
	
							// And at least one waiter working
							for (MyWaiter w : Waiters) {
								if (w.state.equals(MyWaiter.State.working)) {
									// Assign the customer to that table
									AssignCustomer(c, t);
									t.occupied = true;
									return true;
								}
							}
						}
						else if (t.tableNum == Tables.size() - 1) {
							TellRestaurantIsFull(c);
							return true;
						}
					}
				}
			}
		}
	
		return false;
	}

	
	// ACTIONS

	void DecideBreak(MyWaiter w) {
		print("DecideBreak(" + w.waiter.getName() + ")");
		
		// As long as there are multiple waiters WORKING, allow one to go on break
		int numWorking = 0;
		
		synchronized(Waiters) {
			for (MyWaiter mw : Waiters) {
				if (! mw.equals(w)) {
					if (! mw.state.equals(MyWaiter.State.onBreak)) {
						// 'working' and 'askedForBreak' both count as working
						numWorking++;
					}
				}
			}
		}
		
		if (numWorking > 0) {
			w.state = MyWaiter.State.onBreak;
			w.waiter.msgGoOnBreak(true);
			stateChanged();
			return;
		}
		// No working waiters
		w.state = MyWaiter.State.working;
		w.waiter.msgGoOnBreak(false);
		stateChanged();
	}
	
	void AssignCustomer(MyCustomer c, Table t) {
		print("AssignCustomer(" + c.customer.getName() + ", table " + t.tableNum + ")");
		
		// Find the WORKING waiter with the fewest assigned customers
		int min = 0, index = 0;
		
		synchronized(Waiters) {
			for (MyWaiter w : Waiters) {
				if (! w.state.equals(MyWaiter.State.onBreak) && 
					w.waiter.numCustomers() < Waiters.get(min).waiter.numCustomers()) {
					min = index;
				}
				index++;
			}
		}
		
		// Assign the given customer to the given table and the found waiter
		Waiters.get(min).waiter.msgSeatCustomer(c.customer, t.tableNum);
		c.state = MyCustomer.State.assignedToWaiter;
		stateChanged();
	}
	
	void TellRestaurantIsFull(MyCustomer c) {
		print("TellRestaurantIsFull(" + c.customer.getName() + ")");

		c.customer.msgRestaurantFull();
		c.state = MyCustomer.State.toldRestaurantFull;
		stateChanged();
	}
	
	
	// ACCESSORS
	
	public String getName() {
		return name;
	}
	
	public void addWaiter(Waiter w) {
		Waiters.add(new MyWaiter(w));
		stateChanged();
	}

	public List<Table> getTables() {
		return Tables;
	}
	
	// From GUI. Initializes host's table list
	public void setNumTables(int numTables) {
		for (int i = 0; i < numTables; i++) {
			Tables.add(new Table(i));
		}
	}
	
	
	// CLASSES
	
	/**
	 * Holds its own table number and an occupant
	 */
	private class Table {
		boolean occupied;
		int tableNum;

		Table(int tableNumber) {
			tableNum = tableNumber;
		}
	}
	
	/**
	 * Allows Host to keep track of customers waiting to be seated,
	 * those who have been assigned to a waiter,
	 * and those who have left the restaurant. 
	 */
	private static class MyCustomer {
		Customer customer;
		public enum State { waiting, toldRestaurantFull, assignedToWaiter }
		State state;
		
		MyCustomer(Customer c) {
			customer = c;
			state = State.waiting;
		}
	}
	
	private static class MyWaiter {
		Waiter waiter;
		public enum State { working, askedForBreak, onBreak }
		State state;
		
		MyWaiter(Waiter w) {
			waiter = w;
			state = State.working;
		}
	}
}

