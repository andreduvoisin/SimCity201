package market.test.mock;

import java.util.concurrent.Semaphore;

import market.gui.MarketCashierGui;
import market.interfaces.MarketCashier;
import test.mock.*;
import market.*;

/**
 * MockCashier for unit testing.
 * 
 * @author Angelica Huyen Tran
 */
public class MockCashier extends Mock implements MarketCashier {
	MarketCashierGui mCashierGui;
	
	public MockCashier() {
		super();
	}
	
	public void msgOrderPlacement(MarketOrder order) {
		log.add(new LoggedEvent("Received msgOrderPlacement from " + order.mPersonRole));
	}

	public void msgPayingForOrder(MarketInvoice invoice) {
		log.add(new LoggedEvent("Received msgPayingForOrder " + invoice.mOrder.mPersonRole));
	}
	
	public void msgAnimationAtPosition() {
		log.add(new LoggedEvent("Received msgAnimationAtPositon."));
		inTransit.release();
	}
	
	public void msgAnimationLeftMarket() {
		log.add(new LoggedEvent("Received msgAnimationLeftMarket."));
		inTransit.release();
	}
	
	public void setGui(MarketCashierGui g) {
		mCashierGui = g;
	}
	
	public void DoGoToPosition() {
		mCashierGui.DoGoToPosition();
	}
	
	public void DoLeaveMarket() {
		mCashierGui.DoLeaveMarket();
	}
}
