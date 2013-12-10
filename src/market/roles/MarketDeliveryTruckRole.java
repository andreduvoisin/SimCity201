package market.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import market.MarketOrder;
import market.MarketOrder.EnumOrderStatus;
import market.interfaces.MarketDeliveryTruck;
import restaurant.intermediate.RestaurantCookRole;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.PersonAgent;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

/**
 * MarketDeliveryTruck for SimCity Market agents.
 * 
 * @author Angelica Huyen Tran
 */

public class MarketDeliveryTruckRole extends BaseRole implements MarketDeliveryTruck {
	Semaphore inTransit = new Semaphore(0,true);
	int mMarketID;
	boolean atMarket = true;
	
	List<MarketOrder> mPendingDeliveries = Collections.synchronizedList(new ArrayList<MarketOrder>());
	List<MarketOrder> mDeliveries = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	public MarketDeliveryTruckRole(Person person, int marketID) {
		super(person);
		mMarketID = marketID;
		
		ContactList.sMarketList.get(mMarketID).mDeliveryTruck = this;
	}
	
/* Messages */
	public void msgDeliverOrderToCook(MarketOrder o) {
		synchronized(mPendingDeliveries) {
			mPendingDeliveries.add(o);
		}
		stateChanged();
	}

/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		synchronized(mDeliveries) {
		if(mDeliveries.size() != 0) {
			deliverOrders();
			return true;
		}
		}
		synchronized(mPendingDeliveries) {
		if(mPendingDeliveries.size() != 0) {
			pickUpOrdersFromMarket();
			return true;
		}
		}
		if(!atMarket)
			waitAtMarket();
		return false;
	}
	
/* Actions */
	public void deliverOrders() {
		//check all the restaurants
		for(int i=0;i<8;i++) {
			synchronized(mDeliveries) {
				synchronized(ContactList.sOpenPlaces) {
					for(MarketOrder o : mDeliveries) {
						Location location = ContactList.cRESTAURANT_LOCATIONS.get(i);
						if(o.mRestaurantNumber == i && ContactList.sOpenPlaces.get(location)) {
							DoGoToRestaurant(i);
							print("Delivering order.");
							o.mStatus = EnumOrderStatus.FULFILLING;
							((RestaurantCookRole)o.mPersonRole).msgHereIsCookOrder(o);
							mDeliveries.remove(o);
							break;
						}
					}
				}
			}
		}
	}
	
	public void pickUpOrdersFromMarket() {
		DoGoToMarket();
		print("Picking up orders from market!");
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
		atMarket = false;
		if(mPerson instanceof PersonAgent) {
			Location location = ContactList.cRESTAURANT_LOCATIONS.get(n);
			PersonAgent p = (PersonAgent) mPerson;
			p.mPersonGui.mDeliverying = true;
			p.mPersonGui.DoGoToDestination(location);
			p.acquireSemaphore(p.semAnimationDone);
			p.mPersonGui.mDeliverying = false;
		}
	}
	
	private void DoGoToMarket() {
		atMarket = true;
		if(mPerson instanceof PersonAgent) {
			Location location = null;
			if(mMarketID == 0)
				location = ContactList.cMARKET1_LOCATION;
			else
				location = ContactList.cMARKET2_LOCATION;
			PersonAgent p = (PersonAgent) mPerson;
			p.mPersonGui.mDeliverying = true;
			p.mPersonGui.DoGoToDestination(location);
			p.acquireSemaphore(p.semAnimationDone);
			p.mPersonGui.mDeliverying = false;
		}
	}
	
/* Utilities */

	@Override
	public Location getLocation() {
		if (mMarketID == 0) {
			return ContactList.cMARKET1_LOCATION;
		}
		else if (mMarketID == 1) {
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