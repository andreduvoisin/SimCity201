package market.roles;

import market.interfaces.*;
import market.other.*;
import market.other.Order.EnumOrderEvent;
import market.other.Order.EnumOrderStatus;
import base.*;

import java.util.*;

/** MarketCookCustomer for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketCookCustomerRole extends Role implements Cook {
	//RestaurantCashierRole mRestaurantCashier;

	Map<Item, Integer> mItemInventory = new HashMap<Item, Integer>();
	Map<Item, Integer> mItemsDesired = new HashMap<Item, Integer>();
	
	Map<Item, Integer> mCannotFulfill = new HashMap<Item, Integer>();
	
	List<Order> mOrders = Collections.synchronizedList(new ArrayList<Order>());
	List<Invoice> mInvoices	= Collections.synchronizedList(new ArrayList<Invoice>());
	
	Cashier mMarketCashier;
	
	public MarketCookCustomerRole(PersonAgent person) {
		setPerson(person);
	}
	
/* Messages */
	public void msgInvoiceToPerson(Map<Item,Integer> cannotFulfill, Invoice invoice) {
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
		for(Item i : mItemsDesired.keySet()) {
			if(mItemsDesired.get(i) != 0) {
				createOrder();
				return true;
			}
		}
		return false;
	}

/* Actions */
	private void createOrder() {
		
	}
	
	private void placeOrder(Order o) {
		mMarketCashier.msgOrderPlacement(o);
	}
	
	private void payAndProcessOrder(Invoice i) {
		
	}
	
	private void completeOrder(Order o) {
		
	}
	
/* Utilities */
	public void setMarketCashier(Cashier c) {
		mMarketCashier = c;
	}
}
