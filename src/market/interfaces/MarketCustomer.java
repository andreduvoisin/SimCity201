package market.interfaces;

import java.util.Map;

import base.Item.EnumItemType;
import market.*;

public interface MarketCustomer{
		
	//Messages
	public abstract void msgInvoiceToPerson(Map<EnumItemType, Integer> cannotFulfill, MarketInvoice invoice);
	
	public abstract void msgHereIsCustomerOrder(MarketOrder order);
	
	public abstract void msgAnimationAtMarket();
	
	public abstract void msgAnimationAtWaitingArea();
	
	public abstract void msgAnimationLeftRestaurant();
}