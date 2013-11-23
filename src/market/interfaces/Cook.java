package market.interfaces;

import java.util.Map;

import base.Item.EnumMarketItemType;
import market.*;

public interface Cook {
	
	public abstract void msgInvoiceToPerson(Map<String,Integer> cannotFulfill, Invoice invoice);
	
	public abstract void msgHereIsCookOrder(Order o);
}
