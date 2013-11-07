package restaurant.interfaces;

import java.util.Collection;
import java.util.List;

import restaurant.Table;
import restaurant.gui.HostGui;
import restaurant.test.mock.EventLog;

public interface Host {
	
	public EventLog log = new EventLog();
	
	//Messages
	public abstract void msgAddWaiter(Waiter waiter);
	public abstract void msgIWantFood(Customer customer);
	public abstract void msgLeavingTable(Customer cust);
	public abstract void msgLeavingRestaurant(Customer customer);
	public abstract void msgWantToGoOnBreak(Waiter waiter);
	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();
	
	//Actions
//	private void seatCustomer(Customer customer, Table table);
	
	//Accessors
	public abstract String getName();
	public abstract List<Customer> getWaitingCustomers();
	public abstract Collection<Table> getTables();
	public abstract void setGui(HostGui gui);
	public abstract HostGui getGui();
	public abstract String toString();
	public abstract List<Waiter> getWaiters();
	public abstract int getNumWorkingWaiters();
	public abstract boolean isRestaurantFull();

}
