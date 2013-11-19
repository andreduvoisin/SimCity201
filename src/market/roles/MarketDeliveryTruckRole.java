package market.roles;

import java.util.*;

import market.interfaces.*;
import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;
import base.*;

/**
 * MarketDeliveryTruck for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketDeliveryTruckRole extends Role implements DeliveryTruck {

//	DeliveryTruckGui gui;
	
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
		stateChanged();
	}
	
	public void msgAnimationAtMarket() {
		mStatus = EnumDeliveryTruckStatus.Ready;
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
		
	}
	
	public void DoGoToMarket() {
		
	}
	
/* Utilities */

}
