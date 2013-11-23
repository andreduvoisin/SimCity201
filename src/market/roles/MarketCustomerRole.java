package market.roles;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;
import java.util.Map;
=======
>>>>>>> market

import java.util.*;
import java.util.concurrent.Semaphore;

import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;
import market.interfaces.*;
import market.gui.CustomerGui;
import base.*;

public class MarketCustomerRole extends BaseRole implements Customer{
	//DATA
	//mCash accessed from Person
	private CustomerGui mGui;
	private Semaphore inTransit = new Semaphore(0,true);
	
	List<Order> mOrders = Collections.synchronizedList(new ArrayList<Order>());
	List<Invoice> mInvoices	= Collections.synchronizedList(new ArrayList<Invoice>());

	Map<String, Integer> mItemInventory = new HashMap<String, Integer>();
	Map<String, Integer> mItemsDesired = new HashMap<String, Integer>();
	
	Map<String, Integer> mCannotFulfill = new HashMap<String, Integer>();

	Cashier mCashier;
<<<<<<< HEAD

	int mMarketToOrderFrom = 0; //SHANE: 4 use for market switching % Market.getNumMarkets
=======
	
	public MarketCustomerRole(PersonAgent person) {
		mPerson = person;
	}
>>>>>>> market
	
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
	
/* Animation Messages */
	public void msgAnimationAtMarket() {
		inTransit.release();
	}
	
	public void msgAnimationAtWaitingArea() {
		inTransit.release();
	}
	
	public void msgAnimationLeftRestaurant() {
		inTransit.release();
	}
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction(){
<<<<<<< HEAD

		//form order
		if (mPerson.getItemsDesired().size() > 0){
			formOrder();
		}

		for (Order iOrder : mOrders){
			if ((iOrder.mStatus == EnumOrderStatus.CARTED) && (true)){
				iOrder.mStatus = EnumOrderStatus.PLACED;
				placeOrder(iOrder);
=======
		for(Invoice invoice : mInvoices) {
			Order order = invoice.mOrder;
			if(order.mStatus == EnumOrderStatus.PAYING && order.mEvent == EnumOrderEvent.RECEIVED_INVOICE) {
				order.mStatus = EnumOrderStatus.PAID;
				payAndProcessOrder(invoice);
>>>>>>> market
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
<<<<<<< HEAD
	private void formOrder(){
		//Deep copy items desired...
		Map<EnumMarketItemType, Integer> desired = mPerson.getItemsDesired();
		mPerson.setItemsDesired(new HashMap<EnumMarketItemType, Integer>()); //clear desired items
		
		Order order = new Order(desired, this);
		mOrders.add(order);
=======
	private void createOrder(){
		Order o = new Order(mItemsDesired, this);
		
		for(String item : mItemsDesired.keySet()) {
			mItemsDesired.put(item,0);
		}
		
		mOrders.add(o);
>>>>>>> market
	}

	private void placeOrder(Order order){
		DoGoToMarket();
		mCashier.msgOrderPlacement(order);
	}

<<<<<<< HEAD
	private void payForOrder(Order order){
		Invoice invoice = getInvoice(order);
		if (invoice == null){
			//ANGELICA: throw error? or does this work?
			removeOrder(order);
			return;
		}

		if (invoice.mTotal > mPerson.getCash()){
			//ANGELICA: throw error?
		}
		

		
		mPerson.setCash(mPerson.getCash() - invoice.mTotal);
		invoice.mPayment += invoice.mTotal;

		//SHANE: 1 Pay by bank transfer?
		//REX: How do you do this?^^
=======
	private void payAndProcessOrder(Invoice invoice) {
		invoice.mPayment += invoice.mTotal;
		//check if cannot afford invoice
		//TODO: 1 How to write to bank / bank interactions
		//subtract money from cash
		
		for(String item : mCannotFulfill.keySet()) {
			mItemsDesired.put(item, mItemsDesired.get(item)+mCannotFulfill.get(item));
		}
		
>>>>>>> market
		mCashier.msgPayingForOrder(invoice);
		mInvoices.remove(invoice);
		DoWaitForOrder();
	}

<<<<<<< HEAD
	private void removeOrder(Order order){
		//remove from mOrders and mInvoices
		mOrders.remove(order);
		Invoice invoice = getInvoice(order);
		mInvoices.remove(invoice);
=======
	private void completeOrder(Order o) {
		for(String item : o.mItems.keySet()) {
			mItemInventory.put(item, mItemInventory.get(item)+o.mItems.get(item));
		}
		DoLeaveMarket();
>>>>>>> market
	}
	
/* Animation Actions */
	private void DoGoToMarket() {
		mGui.DoGoToMarket();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoWaitForOrder() {
		mGui.DoWaitForOrder();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoLeaveMarket() {
		mGui.DoLeaveMarket();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
/* Utilities */
	public void setGui(CustomerGui g) {
		mGui = g;
	}
}
