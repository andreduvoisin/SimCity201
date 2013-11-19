package market.interfaces;

import java.util.Map;
<<<<<<< HEAD
import market.other.*;
=======

import market.Invoice;
import market.Item.EnumMarketItemType;
import restaurant_smileham.Order;
import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.gui.CookGui;
import restaurant_smileham.test.mock.EventLog;
>>>>>>> d65a9f4f8f4e36d25ab48e460bae9382495de18b

public interface Cook {
	
	public abstract void msgInvoiceToPerson(Map<Item,Integer> cannotFulfill, Invoice invoice);
	
	public abstract void msgHereIsCookOrder(Order o);
}
