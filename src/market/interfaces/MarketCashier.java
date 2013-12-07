package market.interfaces;

import base.Item.EnumItemType;
import market.MarketInvoice;
import market.MarketOrder;

public interface MarketCashier {
		
	//Messages
	public abstract void msgOrderPlacement(MarketOrder order);

	public abstract void msgPayingForOrder(MarketInvoice invoice);

	//Animation Messages
	public abstract void msgAnimationAtPosition();
	
	public abstract void msgAnimationLeftMarket();

	public abstract void setInventory(EnumItemType i, int n);
}
