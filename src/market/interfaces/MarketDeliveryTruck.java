package market.interfaces;

import market.*;

/** 
 * DeliveryTruck interface for SimCity Markets.
 * 
 * @author Angelica Huyen Tran
 */

public interface MarketDeliveryTruck {
	public abstract void msgDeliverOrderToCook(MarketOrder o);
	
	public abstract void msgAnimationAtRestaurant(String r);
	
	public abstract void msgAnimationAtMarket();
	
	public abstract void msgAnimationLeftMarket();
}
