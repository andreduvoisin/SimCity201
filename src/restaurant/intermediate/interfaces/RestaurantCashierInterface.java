package restaurant.intermediate.interfaces;

import java.util.Map;

import base.Item.EnumItemType;
import market.MarketInvoice;
import market.MarketOrder;

public interface RestaurantCashierInterface {
	public abstract void msgPlacedMarketOrder(MarketOrder o);
	
	public abstract void msgInvoiceToPerson(Map<EnumItemType,Integer> cannotFulfill, MarketInvoice invoice);
}
