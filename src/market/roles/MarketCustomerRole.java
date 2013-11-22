package market.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;
import market.interfaces.*;
import base.*;

public class MarketCustomerRole extends BaseRole implements Customer{
	//DATA
	//mCash accessed from Person

	List<Order> mOrders = Collections.synchronizedList(new ArrayList<Order>());
	List<Invoice> mInvoices	= Collections.synchronizedList(new ArrayList<Invoice>());

	Map<String, Integer> mItemInventory = new HashMap<String, Integer>();
	Map<String, Integer> mItemsDesired = new HashMap<String, Integer>();
	
	Map<String, Integer> mCannotFulfill = new HashMap<String, Integer>();

	Cashier mCashier;

	int mMarketToOrderFrom = 0; //TODO: use for market switching % Market.getNumMarkets
	
	public MarketCustomerRole(PersonAgent person) {
		mPerson = person;
	}
	
	//MESSAGES
	public void msgInvoiceToPerson(Map<String, Integer> cannotFulfill, Invoice invoice) {
		mInvoices.add(invoice);
		mCannotFulfill = cannotFulfill;
		invoice.mOrder.mEvent = EnumOrderEvent.RECEIVED_INVOICE;
		stateChanged();
	}


	public void msgHereIsCustomerOrder(Order order){
		order.mEvent = EnumOrderEvent.RECEIVED_ORDER;
		stateChanged();
	}
	
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction(){
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
		for(String i : mItemsDesired.keySet()) {
			if(mItemsDesired.get(i) != 0) {
				createOrder();
				return true;
			}
		}

		return false;
	}
	
	
	//ACTIONS
	private void createOrder(){
		Order o = new Order(mItemsDesired, this);
		
		for(String item : mItemsDesired.keySet()) {
			mItemsDesired.put(item,0);
		}
		
		mOrders.add(o);
	}

	private void placeOrder(Order order){
		mCashier.msgOrderPlacement(order);
	}

	private void payAndProcessOrder(Invoice invoice) {
		invoice.mPayment += invoice.mTotal;
		//check if cannot afford invoice
		//TODO: 1 How to write to bank / bank interactions
		//subtract money from cash
		
		for(String item : mCannotFulfill.keySet()) {
			mItemsDesired.put(item, mItemsDesired.get(item)+mCannotFulfill.get(item));
		}
		
		mCashier.msgPayingForOrder(invoice);
		mInvoices.remove(invoice);
	}

	private void completeOrder(Order o) {
		for(String item : o.mItems.keySet()) {
			mItemInventory.put(item, mItemInventory.get(item)+o.mItems.get(item));
		}
	}
}
