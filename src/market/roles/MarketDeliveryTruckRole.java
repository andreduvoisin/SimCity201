package market.roles;

import java.util.*;
import java.util.concurrent.Semaphore;

import market.interfaces.*;
import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;
import market.gui.DeliveryTruckGui;
import base.*;

/**
 * MarketDeliveryTruck for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketDeliveryTruckRole extends BaseRole implements DeliveryTruck {

	DeliveryTruckGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	
	List<Order> mDeliveries = Collections.synchronizedList(new ArrayList<Order>());
	//FIX THIS MAP!!!
	Map<MarketCookCustomerRole, String>	mRestaurants = new HashMap<MarketCookCustomerRole, String>();
	
	enum EnumDeliveryTruckStatus {Ready, Deliverying, Waiting};
	EnumDeliveryTruckStatus mStatus = EnumDeliveryTruckStatus.Waiting;
	
	public MarketDeliveryTruckRole(PersonAgent person) {
		setPerson(person);
	}

/* Messages */
	public void msgDeliverOrderToCook(Order o) {
		mDeliveries.add(o);
		o.mEvent = EnumOrderEvent.TOLD_TO_DELIVER;
	}
	
	public void msgAnimationAtRestaurant(Order o) {
//		o.mEvent = EnumOrderEvent.TOLD_TO_DELIVER;
		//change event!!
		inTransit.release();
	}
	
	public void msgAnimationAtMarket() {
		mStatus = EnumDeliveryTruckStatus.Ready;
		inTransit.release();
		stateChanged();
	}
	
	public void msgAnimationLeftRestaurant() {
		inTransit.release();
	}
	
/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		for(Order delivery : mDeliveries) {
			if(delivery.mStatus == EnumOrderStatus.DELIVERING && delivery.mEvent == EnumOrderEvent.TOLD_TO_DELIVER) {				delivery.mStatus = EnumOrderStatus.DELIVERING;
				//change status!
				goToDeliverOrder(delivery);
				return true;
			}
		}
		for(Order delivery : mDeliveries) {
			//change status and event!
			if(delivery.mStatus == EnumOrderStatus.DELIVERING && delivery.mEvent == EnumOrderEvent.TOLD_TO_DELIVER) {
				delivery.mStatus = EnumOrderStatus.FULFILLING;
				deliverOrder(delivery);
				return true;
			}
		}
		if(mStatus == EnumDeliveryTruckStatus.Ready) {
			mStatus = EnumDeliveryTruckStatus.Waiting;
			notifyCashier();
			return true;
		}
		DoGoToMarket();
		/*
		 * if time for role change,
		 * DoLeaveMarket();
		 */
		return false;
	}

/* Actions */
	private void goToDeliverOrder(Order o) {
		DoGoToRestaurant(mRestaurants.get(o.mPersonRole));
	}
	
	private void deliverOrder(Order o) {
		((MarketCookCustomerRole)o.mPersonRole).msgHereIsCookOrder(o);
		mDeliveries.remove(o);
	}
	
	private void notifyCashier() {
		//message cashier that deliveryTruck is active
	}
	
/* Animation Actions */
	public void DoGoToRestaurant(String restaurant) {
		mGui.DoGoToRestaurant(restaurant);
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void DoGoToMarket() {
		mGui.DoGoToMarket();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void DoLeaveMarket() {
		mGui.DoLeaveMarket();
		try {
			inTransit.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
/* Utilities */
	public void setGui(DeliveryTruckGui g) {
		mGui = g;
	}
}
