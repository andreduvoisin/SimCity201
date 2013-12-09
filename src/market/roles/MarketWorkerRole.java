package market.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import market.Market;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.gui.MarketWorkerGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketWorker;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

/**
 * MarketWorkerRole for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketWorkerRole extends BaseRole implements MarketWorker {
	MarketWorkerGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	public Market mMarket;
	int mMarketID;
	
	private List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	public MarketWorkerRole(Person person, int marketID){
		super(person);
		mMarket = ContactList.sMarketList.get(marketID);
		mMarketID = marketID;
		
		mGui = new MarketWorkerGui(this,mMarket.mWorkers.size());
		mMarket.mWorkers.add(this);
		mMarket.mGuis.add(mGui);
	}
	
/* Messages */
	public void msgFulfillOrder(MarketOrder o) {
		mOrders.add(o);
		o.mEvent = EnumOrderEvent.ORDER_PAID;
		stateChanged();
	}
	
/* Animation Message */
	public void msgOrderFulfilled(MarketOrder o) {
		if(o.mPersonRole instanceof MarketCustomer)
			o.mEvent = EnumOrderEvent.TOLD_TO_FULFILL;
		else
			o.mEvent = EnumOrderEvent.TOLD_TO_SEND;
		inTransit.release();
	//	stateChanged();
	}

	public void msgAnimationAtMarket() {
		inTransit.release();
	}
	
	public void msgAnimationAtDeliveryTruck() {
		inTransit.release();
	}

	public void msgAnimationAtCustomer() {
		inTransit.release();
	}

	public void msgAnimationLeftMarket() {
		inTransit.release();
	}
	
/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		for(MarketOrder order : mOrders) {
			if(order.mStatus == EnumOrderStatus.SENT && order.mEvent == EnumOrderEvent.ORDER_PAID) {
				order.mStatus = EnumOrderStatus.ORDERING;
				processOrder(order);
				return true;
			}
		}
		for(MarketOrder order : mOrders) {
			if(order.mStatus == EnumOrderStatus.ORDERING && order.mEvent == EnumOrderEvent.TOLD_TO_FULFILL) {
				order.mStatus = EnumOrderStatus.FULFILLING;
				fulfillOrder(order);
				return true;
			}
		}
		for(MarketOrder order : mOrders) {
			if(order.mStatus == EnumOrderStatus.ORDERING && order.mEvent == EnumOrderEvent.TOLD_TO_SEND) {
				order.mStatus = EnumOrderStatus.DELIVERING;
				sendOrder(order);
				return true;
			}
		}
		DoGoToHomePosition();
		/*
		 * if time shift expires, leave restaurant
		 */
		return false;
	}

/* Actions */
	private void processOrder(MarketOrder o) {
		print("Processing order.");
		DoFulfillOrder(o);
	}
	
	private void fulfillOrder(MarketOrder o) {
		print("Fulfilling order.");
		DoGoToCustomer();
		((MarketCustomer)(o.mPersonRole)).msgHereIsCustomerOrder(o);
		mOrders.remove(o);
	}
	
	private void sendOrder(MarketOrder o) {
		print("Sending order.");
		DoGoToDeliveryTruck();
		mMarket.mDeliveryTruck.msgDeliverOrderToCook(o);
		mOrders.remove(o);
	}

/* Animation Actions */
	private void DoFulfillOrder(MarketOrder o) {
		mGui.DoFulfillOrder(o);
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToMarket() {
		mGui.DoGoToMarket();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToCustomer() {
		mGui.DoGoToCustomer();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void DoGoToDeliveryTruck() {
		mGui.DoGoToDeliveryTruck();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToHomePosition() {
		mGui.DoGoToHome();
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
	public int getNumOrders() {
		return mOrders.size();
	}
	
	public MarketOrder getOrder(int n) {
		return mOrders.get(n);
	}
	
	public void setGui(MarketWorkerGui g) {
		mGui = g;
	}
	
	public void setPerson(Person p) {
		mPerson = p;
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
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.MARKET);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.MARKET);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.MARKET, e);
	}
	
	public String toString() {
		if(mPerson != null) {
			return mPerson.getName();
		}
		else
			return "";
	}
}
