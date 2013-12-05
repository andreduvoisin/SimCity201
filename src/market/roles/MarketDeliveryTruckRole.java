package market.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import restaurant.intermediate.RestaurantCookRole;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.gui.MarketDeliveryTruckGui;
import market.interfaces.MarketDeliveryTruck;
import base.BaseRole;
import base.Location;
import base.PersonAgent;
import base.reference.ContactList;
import city.gui.SimCityGui;

/**
 * MarketDeliveryTruck for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketDeliveryTruckRole extends BaseRole implements MarketDeliveryTruck {

	MarketDeliveryTruckGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	int mMarketID;
	
	List<MarketOrder> mDeliveries = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	enum EnumDeliveryTruckStatus {Ready, Deliverying, Waiting};
	EnumDeliveryTruckStatus mStatus = EnumDeliveryTruckStatus.Waiting;
	//ANGELICA: fix delivery truck status thing
	public MarketDeliveryTruckRole(PersonAgent person, int marketID) {
		super(person);
		mMarketID = marketID;
		
		SimCityGui.getInstance().citypanel.masterMarketList.get(mMarketID).mDeliveryTruck = this;
		mGui = new MarketDeliveryTruckGui(this);
	}

/* Messages */
	public void msgDeliverOrderToCook(MarketOrder o) {
		mDeliveries.add(o);
		o.mEvent = EnumOrderEvent.TOLD_TO_DELIVER;
	}
	
	public void msgAnimationAtRestaurant(int n) {
		for(MarketOrder d : mDeliveries) {
			if(d.mRestaurantNumber == n)
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
		DoGoToRestaurant(o.mRestaurantNumber);
	}
	
	private void deliverOrder(MarketOrder o) {
		((RestaurantCookRole)o.mPersonRole).msgHereIsCookOrder(o);
		mDeliveries.remove(o);
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
