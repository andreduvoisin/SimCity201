package market.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import market.MarketOrder;
import market.gui.MarketDeliveryTruckGui;
import market.interfaces.MarketDeliveryTruck;
import restaurant.intermediate.RestaurantCookRole;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;
import city.gui.trace.AlertTag;

/**
 * MarketDeliveryTruck for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

/* ANGELICA: fix this shit up
 * -change to allow multiple orders
 * -pick up orders at one time from market
 * -delivery all orders at one time
 * -check if restaurants are open
 */

public class MarketDeliveryTruckRole extends BaseRole implements MarketDeliveryTruck {
	MarketDeliveryTruckGui mGui;
	Semaphore inTransit = new Semaphore(0,true);
	int mMarketID;
	
	List<MarketOrder> mPendingDeliveries = Collections.synchronizedList(new ArrayList<MarketOrder>());
	List<MarketOrder> mDeliveries = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	public MarketDeliveryTruckRole(Person person, int marketID) {
		super(person);
		mMarketID = marketID;
		
		
		mGui = new MarketDeliveryTruckGui(this);
		ContactList.sMarketList.get(mMarketID).mDeliveryTruck = this;
//		//ANGELICA: add delivery truck to city view gui
	}
	
/* Messages */
	public void msgDeliverOrderToCook(MarketOrder o) {
		synchronized(mPendingDeliveries) {
			mPendingDeliveries.add(o);
		}
		stateChanged();
	}
	
	public void msgAnimationAtRestaurant() {
		inTransit.release();
	}
	
	public void msgAnimationAtMarket() {
		inTransit.release();
	}

/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		if(mDeliveries.size() != 0) {
			deliverOrders();
			return true;
		}
		if(mPendingDeliveries.size() != 0) {
			pickUpOrdersFromMarket();
			return true;
		}
		return false;
	}
	
/* Actions */
	public void deliverOrders() {
		//check all the restaurants
		for(int i=0;i<8;i++) {
			for(MarketOrder o : mDeliveries) {
				if(o.mRestaurantNumber == i) { //ANGELICA: check if restaurant is open
					DoGoToRestaurant(i);
					((RestaurantCookRole)o.mPersonRole).msgHereIsCookOrder(o);
					mDeliveries.remove(o);
				}
			}
		}
	}
	
	public void pickUpOrdersFromMarket() {
		DoGoToMarket();
		synchronized(mPendingDeliveries) {
		for(MarketOrder o : mPendingDeliveries) {
			mDeliveries.add(o);
		}
		mPendingDeliveries.clear();
		}
	}
	
	public void waitAtMarket() {
		DoGoToMarket();
	}

/* Animation Actions */
	private void DoGoToRestaurant(int n) {
		mGui.DoGoToRestaurant(n);
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
	
/* Utilities */

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