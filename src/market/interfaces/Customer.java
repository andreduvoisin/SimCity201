package market.interfaces;

import java.util.Map;

import base.Item.EnumMarketItemType;
import market.Invoice;
import restaurant_smileham.test.mock.EventLog;

public interface Customer{
	
	public EventLog log = new EventLog();
	
	//Messages
	
	//Animation
	
	//Scheduler
	public abstract boolean pickAndExecuteAnAction();
	//Actions

	public abstract void msgInvoiceToPerson(Map<EnumMarketItemType, Integer> canFulfill, Invoice invoice);
	
	//Accessors
}