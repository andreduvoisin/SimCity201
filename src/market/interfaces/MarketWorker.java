package market.interfaces;

import market.*;

/**
 * Worker interface for SimCity Markets.
 * 
 * @author Angelica Huyen Tran
 */

public interface MarketWorker {
	
	public abstract void msgFulfillOrder(MarketOrder o);
	
	public abstract void msgOrderFulfilled(MarketOrder o);
}
