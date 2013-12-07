package restaurant.intermediate.interfaces;

import java.util.Map;

import market.MarketOrder;
import base.Item.EnumItemType;

/** 
 * MarketCookCustomer for SimCity Market agents.
 * Needed as an interface for restaurant cooks
 * because those are the roles that place orders.
 * @author Angelica Huyen Tran
 */

public interface RestaurantCookInterface {        
        public abstract void msgCannotFulfillItems(MarketOrder o, Map<EnumItemType,Integer> cannotFulfill);
        
        public abstract void msgHereIsCookOrder(MarketOrder o);

        public abstract void setMarketCashier(int n);
}