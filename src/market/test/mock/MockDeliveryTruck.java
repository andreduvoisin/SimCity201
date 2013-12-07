package market.test.mock;

import market.MarketOrder;
import market.gui.MarketDeliveryTruckGui;
import market.interfaces.MarketDeliveryTruck;
import test.mock.LoggedEvent;
import test.mock.Mock;

public class MockDeliveryTruck extends Mock implements MarketDeliveryTruck {
	MarketDeliveryTruckGui mDeliveryTruckGui;
	
	public MockDeliveryTruck() {
		super();
	}

	public void msgDeliverOrderToCook(MarketOrder o) {
		log.add(new LoggedEvent("Received msgDeliverOrderToCook."));
	}
	
/** Animation Functions and Messages */
	public void msgAnimationAtRestaurant() {
		log.add(new LoggedEvent("Received msgAnimationAtRestaurant."));
		inTransit.release();
	}
	
	public void msgAnimationAtMarket() {
		log.add(new LoggedEvent("Received msgAnimationAtMarket."));
		inTransit.release();
	}
	
	public void msgAnimationLeftMarket() {
		log.add(new LoggedEvent("Received msgAnimationLeftMarket."));
		inTransit.release();
	}
	
	public void setGui(MarketDeliveryTruckGui g) {
		mDeliveryTruckGui = g;
	}
	
	public void DoGoToRestaurant(int n) {
		mDeliveryTruckGui.DoGoToRestaurant(n);
	}
	
	public void DoGoToMarket() {
		mDeliveryTruckGui.DoGoToMarket();
	}
	
	public void DoLeaveMarket() {
		mDeliveryTruckGui.DoLeaveMarket();
	}
}
