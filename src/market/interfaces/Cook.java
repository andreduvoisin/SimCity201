package market.interfaces;

import java.util.Map;

import market.other.Invoice;
import market.other.Item.EnumMarketItemType;
import restaurant_smileham.Order;
import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.gui.CookGui;
import restaurant_smileham.test.mock.EventLog;

public interface Cook {
	
	public EventLog log = new EventLog();
	
	//Messages
	public abstract void msgInvoiceToPerson(Map<EnumMarketItemType, Integer> canFulfill, Invoice invoice);

	//Scheduler
	public abstract boolean pickAndExecuteAnAction();

	
	//Actions
	
	//Accessors

}
