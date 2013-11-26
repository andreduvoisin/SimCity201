package market.test;

import junit.framework.TestCase;
import market.gui.*;
import market.gui.MarketPanel.EnumMarketType;
import market.test.mock.*;

/** 
 * This tests the gui functionality for each of the agents.
 * Market is set to a food market. Type of market (car or food)
 * does not affect gui movements.
 * 
 * @author Angelica Huyen Tran
 * 
 */
public class MarketGuiTest extends TestCase {
	MarketPanel mMarketPanel;
	MarketItemsGui mMarketItems;

	public void setUp() throws Exception {
		super.setUp();
		mMarketPanel = new MarketPanel(null, EnumMarketType.FOOD);
		mMarketItems = new MarketItemsGui(EnumMarketType.FOOD);
		mMarketPanel.addGui(mMarketItems);
	}
	
/**
 * This tests that cashier can enter and leave the gui.
 */
	public void testCashierGui() {
		MockCashier mCashier = new MockCashier();
		MarketCashierGui mCashierGui = new MarketCashierGui(mCashier);
		mCashier.setGui(mCashierGui);
		mMarketPanel.addGui(mCashierGui);
		
		//assert preconditions
		
		mCashier.DoGoToPosition();
	  //wait for animation to finish
		try {
			mCashier.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	  //assert cashier received the message
		assertTrue("Cashier should have received msgAnimationAtPosition. Instead " + mCashier.log.getLastLoggedEvent().toString(),
				mCashier.log.containsString("Received msgAnimationAtPositon."));

		mCashier.DoLeaveMarket();
	  //wait for animation to finish
		try {
			mCashier.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Do.");
	  //assert cashier received the message
		assertTrue("Cashier should have received msgAnimationLeftMarket. Instead " + mCashier.log.getLastLoggedEvent().toString(),
				mCashier.log.containsString("Received msgAnimationLeftMarket."));
	}
	
	public void testCustomerGui() {
		MockCustomer mCustomer = new MockCustomer();
		MarketCustomerGui mCustomerGui = new MarketCustomerGui(mCustomer);
		mCustomer.setGui(mCustomerGui);
		
	}
	
	public void testWorkerGui() {
		MockWorker mWorker = new MockWorker();
		MarketWorkerGui mWorkerGui = new MarketWorkerGui(mWorker);
		mWorker.setGui(mWorkerGui);
		
	}
	
	// ANGELICA: Finish deliveryTruckGui
	public void testDeliveryTruckGui() {
		MockDeliveryTruck mDeliveryTruck = new MockDeliveryTruck();
		MarketDeliveryTruckGui mDeliveryTruckGui = new MarketDeliveryTruckGui(mDeliveryTruck);
		mDeliveryTruck.setGui(mDeliveryTruckGui);
	}
}
