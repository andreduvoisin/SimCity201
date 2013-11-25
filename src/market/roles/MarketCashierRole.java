package market.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import market.MarketInvoice;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.gui.MarketCashierGui;
import market.gui.MarketPanel.EnumMarketType;
import market.interfaces.MarketCashier;
import market.interfaces.MarketCook;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketDeliveryTruck;
import market.interfaces.MarketWorker;
import base.BaseRole;
import base.Item;
import base.Item.EnumMarketItemType;
import base.interfaces.Person;
import base.interfaces.Role;

/*
 	SHANE ANGELICA: Check to make sure all of these apply
 	1) Each market has its own owner/cashier who handles money.
	2) Each market can have any restaurant/person as a client. 
	3) Restaurants are delivered to, persons must go to the market.
	4) Markets can run out of inventory. They can be resupplied from the gui.
 */
public class MarketCashierRole extends BaseRole implements MarketCashier{
	
	MarketCashierGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	EnumMarketType mMarketType;
	
	int mNumWorkers = 0;
	
	Map<EnumMarketItemType, Integer> mInventory = new HashMap<EnumMarketItemType, Integer>();
	int mBaseInventory = 5;
	
	List<MarketWorker> mWorkers = Collections.synchronizedList(new ArrayList<MarketWorker>());
	static int mWorkerIndex;
	
	List<MarketDeliveryTruck> mDeliveryTrucks = Collections.synchronizedList(new ArrayList<MarketDeliveryTruck>());
	
	int mCash; // no longer needed?
	int mMarketSSN;

	List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	List<MarketInvoice> mInvoices = Collections.synchronizedList(new ArrayList<MarketInvoice>());
	
	public MarketCashierRole(Person person, EnumMarketType type) {
		super(person);
		mPerson = person;
		mMarketType = type;
		
		//populate inventory
		if(mMarketType == EnumMarketType.FOOD) {
		mInventory.put(EnumMarketItemType.STEAK, mBaseInventory);
		mInventory.put(EnumMarketItemType.SALAD, mBaseInventory);
		mInventory.put(EnumMarketItemType.CHICKEN, mBaseInventory);
		mInventory.put(EnumMarketItemType.PIZZA, mBaseInventory);
		}
		else {
			mInventory.put(EnumMarketItemType.CAR, mBaseInventory);
		}
	}
	
//	Messages
	public void msgOrderPlacement(MarketOrder order){
		mOrders.add(order);
		order.mEvent = EnumOrderEvent.ORDER_PLACED;
		stateChanged();
	}

	public void msgPayingForOrder(MarketInvoice invoice){
		if (invoice.mTotal == invoice.mPayment){
			invoice.mOrder.mEvent = EnumOrderEvent.ORDER_PAID;
		}
		else{
			//SHANE ANGELICA: What do we do if they can't pay? throw error?
		}
		stateChanged();
	}
	
/* Animation Messages */
	public void msgAnimationLeftRestaurant() {
		inTransit.release();
	}
	
	public void msgAnimationAtPosition() {
		inTransit.release();
	}
	
//	Scheduler
	public boolean pickAndExecuteAnAction(){
		/*
		 * if cashier has just started, go to position
		 */
		if (mOrders.size() > 0){
			for (MarketOrder iOrder : mOrders){
				//notify customer if an order has been placed
				if ((iOrder.mStatus == EnumOrderStatus.PLACED) && (iOrder.mEvent == EnumOrderEvent.ORDER_PLACED)){
					iOrder.mStatus = EnumOrderStatus.PAYING;
					processOrderAndNotifyPerson(iOrder);
					return true;
				}
			}
			for (MarketOrder iOrder : mOrders){
				if ((iOrder.mStatus == EnumOrderStatus.PAID) && (iOrder.mEvent == EnumOrderEvent.ORDER_PAID)){
					iOrder.mStatus = EnumOrderStatus.ORDERING;
					fulfillOrder(iOrder);
					return true;
				}
			}
		}
		/*
		 * if time for role change
		 * 	DoLeaveMarket();
		 */
		return false;
	}
	
//	Actions
	private void processOrderAndNotifyPerson(MarketOrder order){
		Map<EnumMarketItemType, Integer> cannotFulfill = new HashMap<EnumMarketItemType, Integer>();
		int cost = 0;

		for(EnumMarketItemType item : order.mItems.keySet()) {
			if(mInventory.get(item) < order.mItems.get(item)) {
				cannotFulfill.put(item,order.mItems.get(item)-mInventory.get(item));
				mInventory.put(item,0);
				cost += getPrice(item) * mInventory.get(item);
			}
			else {
				mInventory.put(item, mInventory.get(item)-order.mItems.get(item));
				cost += getPrice(item) * order.mItems.get(item);
			}
		}
		
		Role personRole = order.mPersonRole;
		MarketInvoice invoice = new MarketInvoice(order, cost);

		//if a cook
		if (personRole instanceof MarketCook){
			MarketCook cook = (MarketCook) order.mPersonRole;
			cook.msgInvoiceToPerson(cannotFulfill, invoice);
		}

		//if a customer
		else if (personRole instanceof MarketCustomer){
			MarketCustomer customer = (MarketCustomer) order.mPersonRole;
			customer.msgInvoiceToPerson(cannotFulfill, invoice);
		}
	}

	void fulfillOrder(MarketOrder order){
		order.mWorker = mWorkers.get(mWorkerIndex++ % mNumWorkers);
		order.mWorker.msgFulfillOrder(order);
	}
	
/* Animation Actions */
	private void DoLeaveMarket() {
		mGui.DoLeaveMarket();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToPosition() {
		mGui.DoGoToPosition();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
/* Utilities */
	public void setGui(MarketCashierGui gui) {
		mGui = gui;
	}
	
	public int getNumWorkers(){
		return mNumWorkers;
	}
	
	public void addWorker(MarketWorker w) {
		mWorkers.add(w);
	}
	
	public double getPrice(EnumMarketItemType item) {
		return Item.cMARKET_PRICES.get(item);
	}
	
	public int getInventory(EnumMarketItemType item) {
		return mInventory.get(item);
	}
}
