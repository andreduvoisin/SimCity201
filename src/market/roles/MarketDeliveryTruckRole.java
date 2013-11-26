package market.roles;

import java.util.*;
import java.util.concurrent.Semaphore;

import market.interfaces.*;
import market.*;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.gui.MarketDeliveryTruckGui;
import base.*;

/**
 * MarketDeliveryTruck for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketDeliveryTruckRole extends BaseRole implements MarketDeliveryTruck {

	MarketDeliveryTruckGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	
	List<MarketOrder> mDeliveries = Collections.synchronizedList(new ArrayList<MarketOrder>());

	Map<Integer, MarketCookCustomerRole>	mRestaurants = new HashMap<Integer, MarketCookCustomerRole>();
	
	enum EnumDeliveryTruckStatus {Ready, Deliverying, Waiting};
	EnumDeliveryTruckStatus mStatus = EnumDeliveryTruckStatus.Waiting;
	
	public MarketDeliveryTruckRole(PersonAgent person) {
		super(person);
	}

/* Messages */
	public void msgDeliverOrderToCook(MarketOrder o) {
		mDeliveries.add(o);
		o.mEvent = EnumOrderEvent.TOLD_TO_DELIVER;
	}
	
	public void msgAnimationAtRestaurant(int n) {
		for(MarketOrder d : mDeliveries) {
			if(d.mPersonRole == mRestaurants.get(n))
			d.mEvent = EnumOrderEvent.READY_TO_DELIVER;
		}
		inTransit.release();
	}
	
	public void msgAnimationAtMarket() {
		mStatus = EnumDeliveryTruckStatus.Ready;
		inTransit.release();
		stateChanged();
	}
	
	public void msgAnimationLeftMarket() {
		inTransit.release();
	}
	
/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		for(MarketOrder delivery : mDeliveries) {
			if(delivery.mStatus == EnumOrderStatus.BEING_DELIVERED && delivery.mEvent == EnumOrderEvent.READY_TO_DELIVER) {
				delivery.mStatus = EnumOrderStatus.FULFILLING;
				deliverOrder(delivery);
				return true;
			}
		}
		for(MarketOrder delivery : mDeliveries) {
			if(delivery.mStatus == EnumOrderStatus.DELIVERING && delivery.mEvent == EnumOrderEvent.TOLD_TO_DELIVER) {
				delivery.mStatus = EnumOrderStatus.BEING_DELIVERED;
				goToDeliverOrder(delivery);
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
	private void goToDeliverOrder(MarketOrder o) {
		int n = 0;
		for(int r : mRestaurants.keySet()) {
			if(mRestaurants.get(n) == o.mPersonRole) {
				n = r;
				break;
			}
		}
		DoGoToRestaurant(n);
	}
	
	private void deliverOrder(MarketOrder o) {
		((MarketCookCustomerRole)o.mPersonRole).msgHereIsCookOrder(o);
		mDeliveries.remove(o);
	}
	
	//not necessary for V1?
	private void notifyCashier() {
		//ANGELICA: message cashier that deliveryTruck is active
	}
	
/* Animation Actions */
	public void DoGoToRestaurant(int n) {
//		mGui.DoGoToRestaurant(restaurant);
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
	public void setGui(MarketDeliveryTruckGui g) {
		mGui = g;
	}

	@Override
	public void msgAnimationAtRestaurant(String r) {
		// TODO Auto-generated method stub
		
	}
}
