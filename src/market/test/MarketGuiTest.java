package market.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.Market;
import market.MarketOrder;
import market.gui.MarketCashierGui;
import market.gui.MarketCustomerGui;
import market.gui.MarketItemsGui;
import market.gui.MarketPanel;
import market.gui.MarketWorkerGui;
import market.test.mock.MockCashier;
import market.test.mock.MockCustomer;
import market.test.mock.MockWorker;
import base.Item.EnumItemType;

/** 
 * This tests the gui functionality for each of the agents.
 * Market is set to a food market. Type of market (car or food)
 * does not affect gui movements.
 * 
 * @author Angelica Huyen Tran
 * 
 */
public class MarketGuiTest extends TestCase {
	Market mMarket;
	MarketPanel mMarketPanel;
	MarketItemsGui mMarketItems;
	int mMarketNum = 0;

	public void setUp() throws Exception {
		super.setUp();
		mMarket = new Market(mMarketNum);
		mMarketPanel = new MarketPanel(null,mMarket);
		mMarketItems = new MarketItemsGui();
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
		
	  //assert cashier received the message
		assertTrue("Cashier should have received msgAnimationLeftMarket. Instead " + mCashier.log.getLastLoggedEvent().toString(),
				mCashier.log.containsString("Received msgAnimationLeftMarket."));
	}
	
	public void testCustomerGui() {
		MockCustomer mCustomer = new MockCustomer();
		MarketCustomerGui mCustomerGui = new MarketCustomerGui(mCustomer);
		mCustomer.setGui(mCustomerGui);
		mMarketPanel.addGui(mCustomerGui);
		
	  //assert preconditions
		
		mCustomer.DoGoToMarket();
		try {
			mCustomer.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	  //assert
		assertTrue("Customer should have receievd msgAnimationAtMarket. Instead " +
				mCustomer.log.getLastLoggedEvent().toString(),
				mCustomer.log.containsString("Received msgAnimationAtMarket."));
		
		
		mCustomer.DoWaitForOrder();
		try {
			mCustomer.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	  //assert
		assertTrue("Customer should have received msgAnimationAtWaitingArea. Instead " + mCustomer.log.getLastLoggedEvent().toString(),
				mCustomer.log.containsString("Received msgAnimationAtWaitingArea."));
		
		
		mCustomer.DoLeaveMarket();
		try {
			mCustomer.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	  //assert
		assertTrue("Customer should have received msgAnimationLeftMarket. Instead " + 
				mCustomer.log.getLastLoggedEvent().toString(),
				mCustomer.log.containsString("Received msgAnimationLeftMarket."));
		
	}
	
	public void testWorkerGui() {
		MockWorker mWorker = new MockWorker();
		MarketWorkerGui mWorkerGui = new MarketWorkerGui(mWorker, 0);
		mWorker.setGui(mWorkerGui);
		mMarketPanel.addGui(mWorkerGui);
		
		Map<EnumItemType, Integer> items = new HashMap<EnumItemType, Integer>();
		items.put(EnumItemType.CHICKEN,2);
		items.put(EnumItemType.STEAK,1);
		MarketOrder order = new MarketOrder(items, null);
		
		
	  //assert preconditions
		
		
		mWorker.DoGoToMarket();	//also tests for DoGoToHomePosition
		try {
			mWorker.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	  //assert
		assertTrue("Worker should have received msgAnimationAtMarket. Instead " + mWorker.log.getLastLoggedEvent().toString(),
				mWorker.log.containsString("Received msgAnimationAtMarket."));
		
		
		mWorker.DoFulfillOrder(order);
		try {
			mWorker.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	  //assert
		assertTrue("Worker should have received msgOrderFulfilled. Instead "
				+ mWorker.log.getLastLoggedEvent().toString(),
				mWorker.log.containsString("Received msgOrderFulfilled."));
		
		
		mWorker.DoGoToCustomer();
		try {
			mWorker.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	  //assert
		assertTrue("Worker should have received msgAnimationAtCustomer. Instead " + mWorker.log.getLastLoggedEvent().toString(),
				mWorker.log.containsString("Received msgAnimationAtCustomer."));

		
		mWorker.DoGoToDeliveryTruck();
		try {
			mWorker.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	 //assert
		assertTrue("Worker should received msgAnimationAtDeliveryTruck. Instead " + mWorker.log.getLastLoggedEvent().toString(),
				mWorker.log.containsString("Received msgAnimationAtDeliveryTruck."));

		
		mWorker.DoLeaveMarket();
		try {
			mWorker.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	  //assert
		assertTrue("Worker should have received msgAnimationLeftMarket. Instead " + mWorker.log.getLastLoggedEvent().toString(),
				mWorker.log.containsString("Received msgAnimationLeftMarket."));
	}
}
