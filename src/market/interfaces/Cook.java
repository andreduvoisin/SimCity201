package market.interfaces;

import java.util.Map;
import market.*;
import market.Item.EnumMarketItemType;

public interface Cook {
	
	public abstract void msgInvoiceToPerson(Map<EnumMarketItemType,Integer> cannotFulfill, Invoice invoice);
	
	public abstract void msgHereIsCookOrder(Order o);
}
