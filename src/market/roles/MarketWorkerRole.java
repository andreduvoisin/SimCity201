package market.roles;

import java.util.*;

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
	//MarketWorkerGui gui;
	//Semaphore inTransit = new Semaphore(0,true);
	
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
	
	public void msgOrderFulfilled(Order o) {
		if(o.mPersonRole instanceof Customer)
			o.mEvent = EnumOrderEvent.TOLD_TO_FULFILL;
		else
			o.mEvent = EnumOrderEvent.TOLD_TO_SEND;
		stateChanged();
		//release animation semaphore
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
		
	}
	
	private void DoGoToFront() {
		
	}
	
	private void DoGoToDeliveryTruck(DeliveryTruck d) {
		//check to pass DeliveryTruck or MarketDeliveryTruckRole
	}
	
	private void DoGoToHomePosition() {
		
	}
	
/* Utilities */
	public int getNumOrders() {
		return mOrders.size();
	}
	
	public Order getOrder(int n) {
		return mOrders.get(n);
	}
}
