package market.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.gui.MarketWorkerGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketWorker;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;
import city.gui.trace.AlertTag;

/**
 * MarketWorkerRole for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketWorkerRole extends BaseRole implements MarketWorker {
	MarketWorkerGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	int mMarketID;
	
	private List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	public MarketWorkerRole(Person person, int marketID){
		super(person);
		mMarketID = marketID;
		
		//ANGELICA: add worker to list of cashier workesr or change cashier to check market
		
		//Add guis to list
		mGui = new MarketWorkerGui(this);
		ContactList.sMarketList.get(mMarketID).mWorkers.add(this);
		ContactList.sMarketList.get(mMarketID).mGuis.add(mGui);
		ContactList.sMarketList.get(mMarketID).mWorkerGuis.add(mGui);
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
		Do("d");
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
		DoFulfillOrder(o);
	}
	
	private void fulfillOrder(MarketOrder o) {
		DoGoToCustomer();
		((MarketCustomer)(o.mPersonRole)).msgHereIsCustomerOrder(o);
		mOrders.remove(o);
	}
	
	private void sendOrder(MarketOrder o) {
		DoGoToDeliveryTruck();
		o.mDeliveryTruck.msgDeliverOrderToCook(o);
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
}
