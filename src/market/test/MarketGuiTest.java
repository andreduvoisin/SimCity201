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
		mCashier.DoGoToPosition();
	  //assert agent has received message
		try {
			mCashier.inTransit.acquire();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}

		//		while(mCashierGui.mCommand != MarketCashierGui.EnumCommand.noCommand) { 
		System.out.println(mCashierGui.mCommand);

//		}
		assertTrue("Cashier should have received msgAnimationAtPosition. Instead " + mCashier.log.getLastLoggedEvent().toString(),
				mCashier.log.containsString("Received msgAnimationAtPositon."));
	}
}
