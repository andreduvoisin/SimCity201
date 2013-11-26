package restaurant.intermediate.interfaces;

import java.util.*;

import market.*;
import market.interfaces.MarketCashier;
import base.Item.EnumItemType;


/** MarketCookCustomer for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public interface RestaurantCookInterface {
        public abstract void msgInvoiceToPerson(Map<EnumItemType,Integer> cannotFulfill, MarketInvoice invoice);
        
        public abstract void msgHereIsCookOrder(MarketOrder o);

        public abstract void setMarketCashier(MarketCashier c);
}