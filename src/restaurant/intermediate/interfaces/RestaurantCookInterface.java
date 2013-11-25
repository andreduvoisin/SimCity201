package restaurant.intermediate.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.MarketInvoice;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCook;
import base.Item.EnumItemType;
import base.ContactList;
import base.PersonAgent;
import base.BaseRole;

/** MarketCookCustomer for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public interface RestaurantCookInterface {
	public abstract void msgInvoiceToPerson(Map<EnumItemType,Integer> cannotFulfill, MarketInvoice invoice);
	
	public abstract void msgHereIsCookOrder(MarketOrder o);

	public abstract void setMarketCashier(MarketCashier c);
}
