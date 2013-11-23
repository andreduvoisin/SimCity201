package market.interfaces;

import java.util.Map;

import base.Item.EnumMarketItemType;
import market.*;

public interface MarketCustomer{
		
	//Messages
	public abstract void msgInvoiceToPerson(Map<String, Integer> cannotFulfill, MarketInvoice invoice);
	
	public abstract void msgHereIsCustomerOrder(MarketOrder order);
	
	//Animation
	
	//Scheduler
//	public abstract boolean pickAndExecuteAnAction();
	//Actions
	
	//Accessors
}