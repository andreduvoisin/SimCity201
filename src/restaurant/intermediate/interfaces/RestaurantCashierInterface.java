package restaurant.intermediate.interfaces;

import java.util.Map;

import base.Item.EnumItemType;
import market.MarketInvoice;
import market.MarketOrder;
import market.interfaces.MarketCashier;

/**
 * Interface/functionality to allow restaurant cashiers
 * to verify and pay for market orders placed by the cook.
 * @author Angelica Huyen Tran
 */

public interface RestaurantCashierInterface {
	public abstract void msgPlacedMarketOrder(MarketOrder o, MarketCashier c);
	
	public abstract void msgInvoiceToPerson(Map<EnumItemType,Integer> cannotFulfill, MarketInvoice invoice);
}
