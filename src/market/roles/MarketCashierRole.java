package market.roles;

import java.util.*;

import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;
import market.gui.CashierGui;
import market.interfaces.*;
import base.*;
import base.interfaces.Role;

public class MarketCashierRole extends BaseRole implements Cashier{
	PersonAgent mPerson;
	Market mMarket;
	
	CashierGui mGui;
	int mNumWorkers = 0;
	
	Map<String, Integer> mInventory;
	
	List<Worker> mWorkers = Collections.synchronizedList(new ArrayList<Worker>());
	static int mWorkerIndex;
	
	
	int mCash;

	List<Order> mOrders = Collections.synchronizedList(new ArrayList<Order>());
	List<Invoice> mInvoices = Collections.synchronizedList(new ArrayList<Invoice>());
	
	public MarketCashierRole(PersonAgent person, Market m) {
		mPerson = person;
		mMarket = m;
	}
	
//	Messages
	public void msgOrderPlacement(Order order){
		mOrders.add(order);
		order.mEvent = EnumOrderEvent.ORDER_PLACED;
		stateChanged();
	}

	public void msgPayingForOrder(Invoice invoice){
		if (invoice.mTotal == invoice.mPayment){
			invoice.mOrder.mEvent = EnumOrderEvent.ORDER_PAID;
		}
		else{
//			throw error?
		}
		stateChanged();
	}
	
//	Scheduler
	public boolean pickAndExecuteAnAction(){
		/*
		 * if cashier has just started, go to position
		 */
		if (mOrders.size() > 0){
			for (Order iOrder : mOrders){
				if ((iOrder.mStatus == EnumOrderStatus.PLACED) && (iOrder.mEvent == EnumOrderEvent.ORDER_PLACED)){
					iOrder.mStatus = EnumOrderStatus.PAYING;
					processOrderAndNotifyPerson(iOrder);
					return true;
				}
			}
			for (Order iOrder : mOrders){
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
	private void processOrderAndNotifyPerson(Order order){
		Map<String, Integer> cannotFulfill = new HashMap<String, Integer>();
		int cost = 0;

/*		//set cost
		for (EnumMarketItemType iItemType : order.mItems.keySet()){
			int amountCanFulfill = Math.max(order.mItems.get(iItemType), mInventory.get(iItemType));
			canFulfill.put(iItemType, amountCanFulfill);
			cost += Item.sPrices.get(iItemType) * amountCanFulfill;
		}
*/
		List<MarketItem> mInventory = mMarket.getInventory();
		for(String item : order.mItems.keySet()) {
			if(mMarket.getInventory(item) < order.mItems.get(item)) {
				cannotFulfill.put(item,order.mItems.get(item)-mMarket.getInventory(item));
				mMarket.setInventory(item,0);
				cost += mMarket.getCost(item) * mMarket.getInventory(item);
			}
			else {
				mMarket.setInventory(item, mMarket.getInventory(item)-order.mItems.get(item));
				cost += mMarket.getCost(item) * order.mItems.get(item);
			}
		}
		
		Role personRole = order.mPersonRole;
		Invoice invoice = new Invoice(order, cost);

		//if a cook
		if (personRole instanceof Cook){
			Cook cook = (Cook) order.mPersonRole;
			cook.msgInvoiceToPerson(cannotFulfill, invoice);
		}

		//if a customer
		else if (personRole instanceof Customer){
			Customer customer = (Customer) order.mPersonRole;
			customer.msgInvoiceToPerson(cannotFulfill, invoice);
		}
	}

	void fulfillOrder(Order order){
		order.mWorker = mWorkers.get(mWorkerIndex++ % mNumWorkers); //TODO: here or in worker?
		order.mWorker.msgFulfillOrder(order);
	}
	
/* Animation Actions */
	private void DoLeaveMarket() {
		mGui.DoLeaveMarket();
	}
	
	private void DoGoToPosition() {
		mGui.DoGoToPosition();
	}
	
/* Utilities */
	public void setGui(CashierGui gui) {
		mGui = gui;
	}
	
	public int getNumWorkers(){
		return mNumWorkers;
	}
	
	public void addWorker(Worker w) {
		mWorkers.add(w);
	}
}
