package restaurant.restaurant_smileham.interfaces;

import java.util.Collection;
import java.util.List;

import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.gui.HostGui;
import restaurant.restaurant_smileham.test.mock.EventLog;

public interface SmilehamHost {
	
	public EventLog log = new EventLog();
	
	//Messages
	public abstract void msgAddWaiter(SmilehamWaiter waiter);
	public abstract void msgIWantFood(SmilehamCustomer customer);
	public abstract void msgLeavingTable(SmilehamCustomer cust);
	public abstract void msgLeavingRestaurant(SmilehamCustomer customer);
	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();
	
	//Actions
//	private void seatCustomer(Customer customer, Table table);
	
	//Accessors
	public abstract String getName();
	public abstract List<SmilehamCustomer> getWaitingCustomers();
	public abstract Collection<Table> getTables();
	public abstract void setGui(HostGui gui);
	public abstract HostGui getGui();
	public abstract String toString();
	public abstract List<SmilehamWaiter> getWaiters();
	public abstract int getNumWorkingWaiters();
	public abstract boolean isRestaurantFull();

}
