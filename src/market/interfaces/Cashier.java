package market.interfaces;

import test.mock.EventLog;
import market.other.Invoice;
import market.other.Order;

public interface Cashier {
	
	public EventLog log = new EventLog();
	
	//Messages
	public abstract void msgOrderPlacement(Order order);
	public abstract void msgPayingForOrder(Invoice invoice);

	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();
	
	//Actions
	
	//Accessors
	public abstract int getNumWorkers();

}
