package market.roles;

import java.util.*;
import java.util.concurrent.Semaphore;

import market.gui.WorkerGui;
import market.interfaces.*;
import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;
import base.*;

/**
 * MarketWorkerRole for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketWorkerRole extends BaseRole implements Worker {
	WorkerGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	
	private List<Order> mOrders = Collections.synchronizedList(new ArrayList<Order>());
	
	public MarketWorkerRole(PersonAgent person) {
		setPerson(person);
	}
	
/* Messages */
	public void msgFulfillOrder(Order o) {
		mOrders.add(o);
		o.mEvent = EnumOrderEvent.ORDER_PAID;
		stateChanged();
	}
	
/* Animation Message */
	public void msgOrderFulfilled(Order o) {
		if(o.mPersonRole instanceof Customer)
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
		for(Order order : mOrders) {
			if(order.mStatus == EnumOrderStatus.PAID && order.mEvent == EnumOrderEvent.ORDER_PAID) {
				order.mStatus = EnumOrderStatus.ORDERING;
				processOrder(order);
				return true;
			}
		}
		for(Order order : mOrders) {
			if(order.mStatus == EnumOrderStatus.ORDERING && order.mEvent == EnumOrderEvent.TOLD_TO_FULFILL) {
				order.mStatus = EnumOrderStatus.FULFILLING;
				fulfillOrder(order);
				return true;
			}
		}
		for(Order order : mOrders) {
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
	private void processOrder(Order o) {
		DoFulfillOrder(o);
	}
	
	private void fulfillOrder(Order o) {
		DoGoToFront();
		((Customer)(o.mPersonRole)).msgHereIsCustomerOrder(o);
		mOrders.remove(o);
	}
	
	private void sendOrder(Order o) {
		DoGoToDeliveryTruck(o.mDeliveryTruck);
		o.mDeliveryTruck.msgDeliverOrderToCook(o);
		mOrders.remove(o);
	}

/* Animation Actions */
	private void DoFulfillOrder(Order o) {
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
	
	private void DoGoToDeliveryTruck(DeliveryTruck d) {
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
	
	public Order getOrder(int n) {
		return mOrders.get(n);
	}
	
	public void setGui(WorkerGui g) {
		mGui = g;
	}
}
