package market.interfaces;

import market.MarketInvoice;
import market.MarketOrder;

public interface MarketCashier {
		
	//Messages
	public abstract void msgOrderPlacement(MarketOrder order);

	public abstract void msgPayingForOrder(MarketInvoice invoice);

	//Animation Messages
	public abstract void msgAnimationAtPosition();
	
	public abstract void msgAnimationLeftMarket();
}
