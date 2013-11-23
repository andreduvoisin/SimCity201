package market.roles;

import java.util.*;
import java.util.concurrent.Semaphore;

import market.gui.MarketWorkerGui;
import market.interfaces.*;
import market.*;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.interfaces.MarketDeliveryTruck;
import market.interfaces.MarketWorker;
import base.BaseRole;
import base.interfaces.Person;

/**
 * MarketWorkerRole for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketWorkerRole extends BaseRole implements MarketWorker {
	MarketWorkerGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	
	private List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	public MarketWorkerRole() {
	}
	
	public MarketWorkerRole(Person person){
		setPerson(person);
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
		stateChanged();
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
			if(order.mStatus == EnumOrderStatus.PAID && order.mEvent == EnumOrderEvent.ORDER_PAID) {
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
		DoGoToFront();
		((MarketCustomer)(o.mPersonRole)).msgHereIsCustomerOrder(o);
		mOrders.remove(o);
	}
	
	private void sendOrder(MarketOrder o) {
		DoGoToDeliveryTruck(o.mDeliveryTruck);
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
	
	private void DoGoToFront() {
		mGui.DoGoToMarket();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void DoGoToDeliveryTruck(MarketDeliveryTruck d) {
		//check to pass DeliveryTruck or MarketDeliveryTruckRole
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
}
