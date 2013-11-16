package market.interfaces;

import java.util.Map;

import market.other.Invoice;
import market.other.Item.EnumMarketItemType;
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