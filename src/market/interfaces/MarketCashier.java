package market.interfaces;

import market.MarketInvoice;
import market.MarketOrder;

public interface MarketCashier {
		
	//Messages
	public abstract void msgOrderPlacement(MarketOrder order);

	public abstract void msgPayingForOrder(MarketInvoice invoice);

	//Scheduler
//	public abstract boolean pickAndExecuteAnAction();
	
	//Actions
	
	//Accessors
//	public abstract int getNumWorkers();

}
