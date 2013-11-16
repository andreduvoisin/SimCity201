package market.interfaces;

import market.other.Order;
import restaurant_smileham.test.mock.EventLog;

public interface Worker {
	
	public EventLog log = new EventLog(); //TODO: 1 is this allowed?
	
	//Messages
	public abstract void msgFulfillOrder(Order order);

	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();

	
	//Accessors
	
}
