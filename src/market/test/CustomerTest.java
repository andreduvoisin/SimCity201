package market.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.Market;
import market.MarketOrder;
import market.roles.MarketCustomerRole;
import market.test.mock.MockCashier;
import market.test.mock.MockWorker;
import base.ContactList;
import base.Item.EnumItemType;
import base.PersonAgent;

public class CustomerTest extends TestCase {
	Market mMarket;
	int mMarketNum = 0;
	
	PersonAgent mPerson;
	MarketCustomerRole mCustomer;
	
	MockCashier mMockCashier;
	MockWorker mMockWorker;
	
 	Map<EnumItemType, Integer> mItems = new HashMap<EnumItemType, Integer>();
 	MarketOrder mOrder;
 	
 	int mNumItemOne = 1;
 	int mNumItemTwo = 3;
 
 	public void setUp() throws Exception {
 		super.setUp();
 		
 		ContactList.setup();
 		mMarket = ContactList.sMarketList.get(mMarketNum);
 		mPerson = new PersonAgent();
 		mCustomer = new MarketCustomerRole(mPerson, mMarketNum);
 		
 		mMockCashier = new MockCashier();
 		mMockWorker = new MockWorker();
 		
 		mCustomer.mItemsDesired.put(EnumItemType.CHICKEN, 3);
 		mCustomer.mItemsDesired.put(EnumItemType.STEAK, 1);
 	}
 	
 	/**
 	 * Test customer for a completed order.
 	 */
 	public void testCompletedOrder() {
 	  //assert preconditions
 		assertEquals("MockWorker shouldn't have any logged events, but it does.", 0, mMockWorker.log.size());
 		assertEquals("MocCashier shouldn't have any logged events, but it does.", 0, mMockCashier.log.size());
 		assertEquals("Customer shouldn't have any orders, but he does.", 0, mCustomer.getOrderList().size());
 		assertEquals("Customer shouldn't have any invoices, but he does.", 0, mCustomer.getInvoiceList().size());
 		assertEquals("Customer mItemInventory should have 5 different types of items, but it does not.", 5, mCustomer.mItemInventory.size());
 		assertEquals("Customer mItemsDesired should have 2 different types of items, but it does not.", 2, mCustomer.mItemsDesired.size());
		
 		mCustomer.mCashier = mMockCashier;
 		
 		//Create order
 		mCustomer.pickAndExecuteAnAction();
 		
 		assertEquals("Customer should have one order, but he doesn't.", 1, mCustomer.getOrderList().size());
 		assertEquals("Customer mItemsDesired should still only have 2 different types of items, but it does not.", 2, mCustomer.mItemsDesired.size());
 		assertEquals("Order state should be CARTED, but it's not.", mCustomer.getOrderList().get(0).mStatus.CARTED, mCustomer.getOrderList().get(0).mStatus);
 		
 		//Place order
 		mCustomer.pickAndExecuteAnAction();
 		assertEquals("Customer should still have only one order, but he doesn't.", 1, mCustomer.getOrderList().size());
 		assertEquals("Order state should be PLACED, but it's not.", mCustomer.getOrderList().get(0).mStatus.PLACED, mCustomer.getOrderList().get(0).mStatus);
 		
 		mCustomer.msgAnimationAtMarket();
 		assertTrue("The mock cashier log should ")
 	}
 	/**
 	 * Test customer for a partial order.
 	 */
 	public void testPartialOrder() {
 	  //assert preconditions
 		
 		
 		
 	}
}
