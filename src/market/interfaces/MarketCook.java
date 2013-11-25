package market.interfaces;

import java.util.Map;

import base.Item.EnumItemType;
import market.*;

public interface MarketCook {
	
	public abstract void msgInvoiceToPerson(Map<EnumItemType,Integer> cannotFulfill, MarketInvoice invoice);
	
	public abstract void msgHereIsCookOrder(MarketOrder o);
}
