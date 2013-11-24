package restaurant.restaurant_davidmca.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_davidmca.MyWaiter;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.gui.HostGui;
import restaurant.restaurant_davidmca.interfaces.Customer;
import restaurant.restaurant_davidmca.interfaces.Host;
import restaurant.restaurant_davidmca.interfaces.Waiter;
import base.Agent;
import base.BaseRole;

/**
 * Restaurant Host Agent
 */

public class HostRole extends BaseRole implements Host {
	static final int NTABLES = 4;// a global for the number of tables.
	public List<CustomerRole> waitingCustomers = Collections
			.synchronizedList(new ArrayList<CustomerRole>());
	public List<CustomerRole> indecisiveCustomers = Collections
			.synchronizedList(new ArrayList<CustomerRole>());
	public Collection<Table> tables;
	public Collection<MyWaiter> waiters = Collections
			.synchronizedList(new ArrayList<MyWaiter>());
	private int workingWaiters = 0;
	private int index = 0;
	public CookRole cook = null;

	private String name;
	// Table positions
	private int[] xpositions = { 0, 100, 200, 300, 200 };
	private int[] ypositions = { 0, 300, 200, 300, 400 };

	public enum WaiterState {
		Normal, BreakRequested, OnBreak
	};

	public HostGui hostGui = null;

	public HostRole(String name) {
		super();

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		// use array of positions
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix, xpositions[ix], ypositions[ix], 1));
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void addWaiter(Waiter newWaiter) {
		waiters.add(new MyWaiter(newWaiter));
		workingWaiters++;
		stateChanged();
	}

	public List<CustomerRole> getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection<Waiter> getWaitersList() {
		List<Waiter> returnWaiters = Collections
				.synchronizedList(new ArrayList<Waiter>());
		synchronized (waiters) {
			for (MyWaiter myw : waiters) {
				returnWaiters.add(myw.w);
			}
		}
		return returnWaiters;
	}

	public Collection<Table> getTables() {
		return tables;
	}

	public Table getAvailableTable() {
		synchronized (tables) {
			for (Table table : tables) {
				if (!table.isOccupied()) {
					return table;
				}
			}
		}
		return null;
	}

	private boolean getAvailability() {
		int opentables = 0;
		synchronized (tables) {
			for (Table t : tables) {
				if (!t.isOccupied()) {
					opentables++;
				}
			}
		}
		if (opentables == 0 || (waitingCustomers.size() >= tables.size())) {
			return false;
		} else {
			return true;
		}
	}

	public MyWaiter getWaiter() {
		int lowestCustomers = 1000;
		MyWaiter leastBusy = null;
		synchronized (waiters) {
			for (MyWaiter w : waiters) {
				if (w.state == WaiterState.Normal) {
					if (w.numCustomers < lowestCustomers) {
						lowestCustomers = w.numCustomers;
						leastBusy = w;
					}
				}
			}
		}
		return leastBusy;
	}

	// Messages

	public void msgCheckAvailability(CustomerRole cust) {
		indecisiveCustomers.add(cust);
		stateChanged();
	}

	public void msgIWantFood(CustomerRole cust) {
		index++;
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgTableIsEmpty(Table t) {
		t.setUnoccupied();
		stateChanged();
	}

	public void msgGoOnBreak(Waiter waiter) {
		print("Go on Break Request Received");
		synchronized (waiters) {
			for (MyWaiter myw : waiters) {
				if (myw.w == waiter) {
					myw.state = WaiterState.BreakRequested;
				}
			}
		}
		stateChanged();
	}

	public void msgGoOffBreak(Waiter waiter) {
		print(waiter.getName() + " is off break");
		synchronized (waiters) {
			for (MyWaiter myw : waiters) {
				if (myw.w == waiter) {
					myw.state = WaiterState.Normal;
					workingWaiters++;
				}
			}
		}
		stateChanged();
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized (indecisiveCustomers) {
			for (CustomerRole cust : indecisiveCustomers) {
				cust.msgAvailability(getAvailability());
				indecisiveCustomers.remove(cust);
				return true;
			}
		}
		synchronized (tables) {
			for (Table table : tables) {
				if (!table.isOccupied() && !waitingCustomers.isEmpty()
						&& !waiters.isEmpty()) {
					MyWaiter waiterToUse = getWaiter();
					waiterToUse.numCustomers++;
					seatCustomer(waitingCustomers.get(0), table, waiterToUse.w);
					return true;
				}
			}
		}
		synchronized (waiters) {
			for (MyWaiter myw : waiters) {
				if (myw.state == WaiterState.BreakRequested) {
					if (workingWaiters > 1) {
						myw.state = WaiterState.OnBreak;
						workingWaiters--;
						myw.w.msgBreakReply(true);
						return true;
					} else {
						myw.w.msgBreakReply(false);
						return true;
					}
				}
			}
		}
		return false;
	}

	// Actions

	private void seatCustomer(Customer customer, Table table, Waiter waiter) {
		print("seat customer");
		waiter.msgSeatAtTable(customer, table, customer.getGui().getHomeLocation());
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
	}

	// utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	public void setCook(CookRole cook) {
		this.cook = cook;

	}

	public boolean AreWaiters() {
		return !waiters.isEmpty();
	}

	public int getCustomerIndex() {
		return index;
	}
}
