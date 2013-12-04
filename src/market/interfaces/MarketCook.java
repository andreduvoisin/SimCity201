package market.interfaces;

import java.util.Map;

import market.MarketInvoice;
import market.MarketOrder;
import base.Item.EnumItemType;

public interface MarketCook {
	
	public abstract void msgInvoiceToPerson(Map<EnumItemType,Integer> cannotFulfill, MarketInvoice invoice);
	
	public abstract void msgHereIsCookOrder(MarketOrder o);
}
