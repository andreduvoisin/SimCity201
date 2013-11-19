package market.interfaces;

import java.util.Map;
import market.other.*;

public interface Cook {
	
	public abstract void msgInvoiceToPerson(Map<Item,Integer> cannotFulfill, Invoice invoice);
	
	public abstract void msgHereIsCookOrder(Order o);
}
