package market.interfaces;

import market.MarketOrder;

/** 
 * DeliveryTruck interface for SimCity Markets.
 * 
 * @author Angelica Huyen Tran
 */

public interface MarketDeliveryTruck {
	public abstract void msgDeliverOrderToCook(MarketOrder o);
	
	public abstract void msgAnimationAtRestaurant();
	
	public abstract void msgAnimationAtMarket();
	
//	public abstract void msgAnimationLeftMarket();
}
