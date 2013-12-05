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
import market.interfaces.MarketCashier;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketDeliveryTruck;
import market.interfaces.MarketWorker;
import restaurant.intermediate.interfaces.RestaurantCookInterface;
import base.BaseRole;
import base.ContactList;
import base.Item;
import base.Item.EnumItemType;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.SimCityGui;

/*
 	SHANE ANGELICA: Check to make sure all of these apply
 	1) Each market has its own owner/cashier who handles money.
 		-taken care of
	2) Each market can have any restaurant/person as a client. 
		-taken care of
	3) Restaurants are delivered to, persons must go to the market.
		-taken care of (functionality at least)
	4) Markets can run out of inventory. They can be resupplied from the gui.
		-taken care of; gui control panel needs to call MarketPanel.setInventory();
 */
public class MarketCashierRole extends BaseRole implements MarketCashier{
	
	MarketCashierGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	int mMarketID;
	
	int mNumWorkers = 0;
	
	Map<EnumItemType, Integer> mInventory = new HashMap<EnumItemType, Integer>();
	int mBaseInventory = 100;
	
	List<MarketWorker> mWorkers = Collections.synchronizedList(new ArrayList<MarketWorker>());
	static int mWorkerIndex;
	
	MarketDeliveryTruck mDeliveryTruck;
//	List<MarketDeliveryTruck> mDeliveryTrucks = Collections.synchronizedList(new ArrayList<MarketDeliveryTruck>());
//	Map<MarketDeliveryTruck,Boolean> mDeliveryTrucks = new HashMap<MarketDeliveryTruck,Boolean>();
	
	int mBankAccount;

	List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	List<MarketInvoice> mInvoices = Collections.synchronizedList(new ArrayList<MarketInvoice>());
	
	public MarketCashierRole(Person person, int marketID) {
		super(person);
		mMarketID = marketID;
		
		SimCityGui.getInstance().citypanel.masterMarketList.get(mMarketID).mCashier = this;
		mGui = new MarketCashierGui(this);
		SimCityGui.getInstance().citypanel.masterMarketList.get(mMarketID).mCashierGui = mGui;
		SimCityGui.getInstance().citypanel.masterMarketList.get(mMarketID).addGui(mGui);		
		
		if(person != null)
			mBankAccount = person.getSSN();
		
		//populate inventory
		mInventory.put(EnumItemType.STEAK, mBaseInventory);
		mInventory.put(EnumItemType.SALAD, mBaseInventory);
		mInventory.put(EnumItemType.CHICKEN, mBaseInventory);
		mInventory.put(EnumItemType.PIZZA, mBaseInventory);
		mInventory.put(EnumItemType.CAR, mBaseInventory);
	}
	
//	Messages
	public void msgOrderPlacement(MarketOrder order){
		mOrders.add(order);
		order.mEvent = EnumOrderEvent.ORDER_PLACED;
		stateChanged();
	}

	public void msgPayingForOrder(MarketInvoice invoice){
		if (invoice.mTotal == invoice.mPayment)
			invoice.mOrder.mEvent = EnumOrderEvent.ORDER_PAID;
		stateChanged();
	}
	
/* Animation Messages */
	public void msgAnimationLeftMarket() {
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
					iOrder.mStatus = EnumOrderStatus.SENT;
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
		Map<EnumItemType, Integer> cannotFulfill = new HashMap<EnumItemType, Integer>();
		int cost = 0;

		for(EnumItemType item : order.mItems.keySet()) {
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
		MarketInvoice invoice = new MarketInvoice(order, cost, mBankAccount);

		//if a cook
        if (personRole instanceof RestaurantCookInterface){
        	order.mDeliveryTruck = mDeliveryTruck;
            RestaurantCookInterface cook = (RestaurantCookInterface) order.mPersonRole;
            cook.msgInvoiceToPerson(cannotFulfill, invoice);
            /* ANGELICA: send invoice to cashier, send cannotFulfill
             * to cook
             * 
             * 
             */
        }
        
		//if a customer
		else if (personRole instanceof MarketCustomer){
			MarketCustomer customer = (MarketCustomer) order.mPersonRole;
			customer.msgInvoiceToPerson(cannotFulfill, invoice);
		}
	}

	void fulfillOrder(MarketOrder order){
		int n = (int) (Math.random() % mWorkers.size());
		order.mWorker = mWorkers.get(n);
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
	
	public double getPrice(EnumItemType item) {
		return Item.cMARKET_PRICES.get(item);
	}
	
	public int getInventory(EnumItemType item) {
		return mInventory.get(item);
	}
	
	public void setInventory(EnumItemType i, int n) {
		mInventory.put(i, n);
	}
	
	public void addDeliveryTruck(MarketDeliveryTruck d) {
		mDeliveryTrucks.add(d);
	}
	
	public void setPerson(Person p) {
		mPerson = p;
		mBankAccount = p.getSSN();
	}

	@Override
	public Location getLocation() {
		if (mMarketID == 1) {
			return ContactList.cMARKET1_LOCATION;
		}
		else if (mMarketID == 2) {
			return ContactList.cMARKET2_LOCATION;
		}
		return null;
	}
}
