package market.interfaces;

import market.Invoice;
import market.Order;

public interface Cashier {
		
	//Messages
	public abstract void msgOrderPlacement(Order order);

	public abstract void msgPayingForOrder(Invoice invoice);

	//Scheduler
//	public abstract boolean pickAndExecuteAnAction();
	
	//Actions
	
	//Accessors
//	public abstract int getNumWorkers();

}
