package restaurant_all.roles.Host;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_davidmca.MyWaiter;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.agents.HostAgent.WaiterState;
import restaurant.restaurant_davidmca.gui.HostGui;
import restaurant.restaurant_davidmca.interfaces.Cook;
import restaurant.restaurant_davidmca.interfaces.Host;
import restaurant.restaurant_davidmca.interfaces.Waiter;
import restaurant_all.interfaces.Customer.Customer_dc;
import restaurant_all.interfaces.Host.Host_dc;
import base.Agent;
import base.BaseRole;

/**
 * Restaurant Host Agent
 */

public class HostRole_dc extends BaseRole implements Host_dc {
	static final int NTABLES = 4;// a global for the number of tables.
	public List<Customer_dc> waitingCustomer_dcs = Collections
			.synchronizedList(new ArrayList<Customer_dc>());
	public List<Customer_dc> indecisiveCustomer_dcs = Collections
			.synchronizedList(new ArrayList<Customer_dc>());
	public Collection<Table> tables;
	public Collection<MyWaiter> waiters = Collections
			.synchronizedList(new ArrayList<MyWaiter>());
	private int workingWaiters = 0;
	private int index = 0;
	public Cook cook = null;

	private String name;
	// Table positions
	private int[] xpositions = { 0, 100, 200, 300, 200 };
	private int[] ypositions = { 0, 300, 200, 300, 400 };

	public class MyWaiter {
		public Waiter w;
		public int numCustomers;
		public WaiterState state;

		public MyWaiter(Waiter waiter) {
			this.w = waiter;
			numCustomers = 0;
			state = WaiterState.Normal;
		}
	};
	
	public enum WaiterState {
		Normal, BreakRequested, OnBreak
	};

	public HostGui hostGui = null;

	public HostRole_dc(String name) {
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

	public List<Customer_dc> getWaitingCustomer_dcs() {
		return waitingCustomer_dcs;
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
		if (opentables == 0 || (waitingCustomer_dcs.size() >= tables.size())) {
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

	public void msgCheckAvailability(Customer_dc cust) {
		indecisiveCustomer_dcs.add(cust);
		stateChanged();
	}

	public void msgIWantFood(Customer_dc cust) {
		index++;
		waitingCustomer_dcs.add(cust);
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
		synchronized (indecisiveCustomer_dcs) {
			for (Customer_dc cust : indecisiveCustomer_dcs) {
				cust.msgAvailability(getAvailability());
				indecisiveCustomer_dcs.remove(cust);
				return true;
			}
		}
		synchronized (tables) {
			for (Table table : tables) {
				if (!table.isOccupied() && !waitingCustomer_dcs.isEmpty()
						&& !waiters.isEmpty()) {
					MyWaiter waiterToUse = getWaiter();
					waiterToUse.numCustomers++;
					seatCustomer_dc(waitingCustomer_dcs.get(0), table, waiterToUse.w);
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

	private void seatCustomer_dc(Customer_dc customer, Table table, Waiter waiter) {
		print("seat Customer_dc");
		waiter.msgSeatAtTable(customer, table, customer.getGui().getHomeLocation());
		table.setOccupant(customer);
		waitingCustomer_dcs.remove(customer);
	}

	// utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	public void setCook(Cook cook) {
		this.cook = cook;

	}

	public boolean AreWaiters() {
		return !waiters.isEmpty();
	}

	public int getCustomer_dcIndex() {
		return index;
	}
}
