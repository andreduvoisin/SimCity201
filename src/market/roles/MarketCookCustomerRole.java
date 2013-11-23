package market.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.MarketInvoice;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCook;
import base.Item.EnumMarketItemType;
import base.PersonAgent;
import base.BaseRole;

/** MarketCookCustomer for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketCookCustomerRole extends BaseRole implements MarketCook {
	//RestaurantCashierRole mRestaurantCashier;

	Map<EnumMarketItemType, Integer> mItemInventory = new HashMap<EnumMarketItemType, Integer>();
	Map<EnumMarketItemType, Integer> mItemsDesired = new HashMap<EnumMarketItemType, Integer>();
	
	Map<EnumMarketItemType, Integer> mCannotFulfill = new HashMap<EnumMarketItemType, Integer>();
	
	List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	List<MarketInvoice> mInvoices	= Collections.synchronizedList(new ArrayList<MarketInvoice>());
	
	MarketCashier mMarketCashier;
	int mMarketSSN;			///for paying
	
	public MarketCookCustomerRole(PersonAgent person) {
		mPerson = person;
	}
	
/* Messages */
	public void msgInvoiceToPerson(Map<EnumMarketItemType,Integer> cannotFulfill, MarketInvoice invoice) {
		mInvoices.add(invoice);
		mCannotFulfill = cannotFulfill;
		invoice.mOrder.mEvent = EnumOrderEvent.RECEIVED_INVOICE;
		stateChanged();
	}
	
	public void msgHereIsCookOrder(MarketOrder o) {
		o.mEvent = EnumOrderEvent.RECEIVED_ORDER;
		stateChanged();
	}
	
	
/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		for(MarketInvoice invoice : mInvoices) {
			MarketOrder order = invoice.mOrder;
			if(order.mStatus == EnumOrderStatus.PAYING && order.mEvent == EnumOrderEvent.RECEIVED_INVOICE) {
				order.mStatus = EnumOrderStatus.PAID;
				payAndProcessOrder(invoice);
				return true;
			}
		}
		for(MarketOrder order : mOrders) {
			if(order.mStatus == EnumOrderStatus.FULFILLING && order.mEvent == EnumOrderEvent.RECEIVED_ORDER) {
				order.mStatus = EnumOrderStatus.DONE;
				completeOrder(order);
				return true;
			}
		}
		for(MarketOrder order : mOrders) {
			if(order.mStatus == EnumOrderStatus.CARTED) {
				order.mStatus = EnumOrderStatus.PLACED;
				placeOrder(order);
				return true;
			}
		}
		//check efficiency of method
		for(EnumMarketItemType i : mItemsDesired.keySet()) {
			if(mItemsDesired.get(i) != 0) {
				createOrder();
				return true;
			}
		}
		return false;
	}

/* Actions */
	private void createOrder() {
		MarketOrder o = new MarketOrder(mItemsDesired, this);
		
		for(EnumMarketItemType item : mItemsDesired.keySet()) {
			mItemsDesired.put(item,0);
		}
		
		mOrders.add(o);
	}
	
	private void placeOrder(MarketOrder o) {
		mMarketCashier.msgOrderPlacement(o);
	}
	
	private void payAndProcessOrder(MarketInvoice i) {
		i.mPayment = i.mTotal;
		//check if cannot afford invoice
		
		for(EnumMarketItemType item : mCannotFulfill.keySet()) {
			mItemsDesired.put(item, mItemsDesired.get(item)+mCannotFulfill.get(item));
		}
		
		//ANGELICA REX: bank interactions?
		mMarketCashier.msgPayingForOrder(i);
		mInvoices.remove(i);
	}
	
	private void completeOrder(MarketOrder o) {
		for(EnumMarketItemType item : o.mItems.keySet()) {
			mItemInventory.put(item, mItemInventory.get(item)+o.mItems.get(item));
		}
	}
	
/* Utilities */
	public void setMarketCashier(MarketCashier c) {
		mMarketCashier = c;
	}
}
