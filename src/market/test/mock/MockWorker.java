package market.test.mock;

import market.MarketOrder;
import market.gui.MarketWorkerGui;
import market.interfaces.MarketWorker;
import test.mock.*;

public class MockWorker extends Mock implements MarketWorker {
	MarketWorkerGui mWorkerGui;
	
	public MockWorker() {
		super();
	}
	
	public void msgFulfillOrder(MarketOrder o) {
		log.add(new LoggedEvent("Received msgFulfillOrder for " + o.mPersonRole));
	}
	
	public void msgOrderFulfilled(MarketOrder o) {
		log.add(new LoggedEvent("Received msgOrderFulfilled."));
	}
	
/** Animation Functions and Messages */
	
	public void setGui(MarketWorkerGui g) {
		mWorkerGui = g;
	}
	
	public void msgAnimationAtMarket() {
		log.add(new LoggedEvent("Received msgAnimationAtMarket."));
		inTransit.release();
	}
	
	public void msgAnimationAtDeliveryTruck() {
		log.add(new LoggedEvent("Received msgAnimationAtDeliveryTruck."));
		inTransit.release();
	}
	
	public void msgAnimationAtCustomer() {
		log.add(new LoggedEvent("Received msgAnimationAtCustomer."));
		inTransit.release();
	}
	
	public void msgAnimationLeftMarket() {
		log.add(new LoggedEvent("Received msgAnimationLeftMarket."));
		inTransit.release();
	}
	
	public void DoFulfillOrder(MarketOrder o) {
		mWorkerGui.DoFulfillOrder(o);
	}
	
	//also used for DoGoToHomePosition
	public void DoGoToMarket() {
		mWorkerGui.DoGoToMarket();
	}
	
	public void DoGoToCustomer() {
		mWorkerGui.DoGoToCustomer();
	}
	
	public void DoGoToDeliveryTruck() {
		mWorkerGui.DoGoToDeliveryTruck();
	}
	
	public void DoLeaveMarket() {
		mWorkerGui.DoLeaveMarket();
	}
}
