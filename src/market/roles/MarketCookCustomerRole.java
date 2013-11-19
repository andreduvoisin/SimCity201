package market.roles;

import market.interfaces.*;
import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;
import base.*;
import market.Item.EnumMarketItemType;

import java.util.*;

/** MarketCookCustomer for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketCookCustomerRole extends Role implements Cook {
	//RestaurantCashierRole mRestaurantCashier;

	Map<EnumMarketItemType, Integer> mItemInventory = new HashMap<EnumMarketItemType, Integer>();
	Map<EnumMarketItemType, Integer> mItemsDesired = new HashMap<EnumMarketItemType, Integer>();
	
	Map<EnumMarketItemType, Integer> mCannotFulfill = new HashMap<EnumMarketItemType, Integer>();
	
	List<Order> mOrders = Collections.synchronizedList(new ArrayList<Order>());
	List<Invoice> mInvoices	= Collections.synchronizedList(new ArrayList<Invoice>());
	
	Cashier mMarketCashier;
	
	public MarketCookCustomerRole(PersonAgent person) {
		setPerson(person);
	}
	
/* Messages */
	public void msgInvoiceToPerson(Map<EnumMarketItemType,Integer> cannotFulfill, Invoice invoice) {
		mInvoices.add(invoice);
		mCannotFulfill = cannotFulfill;
		invoice.mOrder.mEvent = EnumOrderEvent.RECEIVED_INVOICE;
		stateChanged();
	}
	
	public void msgHereIsCookOrder(Order o) {
		o.mEvent = EnumOrderEvent.RECEIVED_ORDER;
	}
	
	
/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		for(Invoice invoice : mInvoices) {
			Order order = invoice.mOrder;
			if(order.mStatus == EnumOrderStatus.PAYING && order.mEvent == EnumOrderEvent.RECEIVED_INVOICE) {
				order.mStatus = EnumOrderStatus.PAID;
				payAndProcessOrder(invoice);
				return true;
			}
		}
		for(Order order : mOrders) {
			if(order.mStatus == EnumOrderStatus.FULFILLING && order.mEvent == EnumOrderEvent.RECEIVED_ORDER) {
				order.mStatus = EnumOrderStatus.DONE;
				completeOrder(order);
				return true;
			}
		}
		for(Order order : mOrders) {
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
		Order o = new Order(mItemsDesired, this);
		
		for(EnumMarketItemType item : mItemsDesired.keySet()) {
			mItemsDesired.put(item,0);
		}
		
		mOrders.add(o);
	}
	
	private void placeOrder(Order o) {
		mMarketCashier.msgOrderPlacement(o);
	}
	
	private void payAndProcessOrder(Invoice i) {
		i.mPayment = i.mTotal;
		//check how to get payment from restaurant cashier
		
		for(EnumMarketItemType item : mCannotFulfill.keySet()) {
			mItemsDesired.put(item, mItemsDesired.get(item)+mCannotFulfill.get(item));
		}
		
//		mRestaurantCashier.msgHereIsInvoice(i);
		mMarketCashier.msgPayingForOrder(i);
		mInvoices.remove(i);
	}
	
	private void completeOrder(Order o) {
		for(EnumMarketItemType item : o.mItems.key
	}
	
/* Utilities */
	public void setMarketCashier(Cashier c) {
		mMarketCashier = c;
	}
}
