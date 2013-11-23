package market.interfaces;

import java.util.Map;

import base.Item.EnumMarketItemType;
import market.*;

public interface MarketCook {
	
	public abstract void msgInvoiceToPerson(Map<EnumMarketItemType,Integer> cannotFulfill, MarketInvoice invoice);
	
	public abstract void msgHereIsCookOrder(MarketOrder o);
}
