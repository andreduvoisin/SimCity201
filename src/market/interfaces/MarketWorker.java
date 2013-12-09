package market.interfaces;

import market.Market;
import market.MarketOrder;

/**
 * Worker interface for SimCity Markets.
 * 
 * @author Angelica Huyen Tran
 */

public interface MarketWorker {

	Market mMarket = null;

	public abstract void msgFulfillOrder(MarketOrder o);
	
	public abstract void msgOrderFulfilled(MarketOrder o);
	
	public abstract void msgAnimationAtMarket();
	
	public abstract void msgAnimationAtDeliveryTruck();
	
	public abstract void msgAnimationAtCustomer();
	
	public abstract void msgAnimationLeftMarket();
}
