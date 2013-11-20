package market.interfaces;

import java.util.Map;

import base.Item.EnumMarketItemType;
import market.*;

public interface Customer{
		
	//Messages
	public abstract void msgInvoiceToPerson(Map<EnumMarketItemType, Integer> cannotFulfill, Invoice invoice);
	
	public abstract void msgHereIsCustomerOrder(Order order);
	
	//Animation
	
	//Scheduler
//	public abstract boolean pickAndExecuteAnAction();
	//Actions
	
	//Accessors
}