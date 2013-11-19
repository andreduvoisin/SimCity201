package market.interfaces;

import market.other.*;

/**
 * Worker interface for SimCity Markets.
 * 
 * @author Angelica Huyen Tran
 */

public interface Worker {
	
	public abstract void msgFulfillOrder(Order o);
	
	public abstract void msgOrderFulfilled(Order o);
}
