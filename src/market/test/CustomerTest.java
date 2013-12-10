package market.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.Market;
import market.MarketInvoice;
import market.MarketOrder;
import market.MarketOrder.EnumOrderStatus;
import market.roles.MarketCustomerRole;
import market.test.mock.MockCashier;
import market.test.mock.MockWorker;
import base.ContactList;
import base.Item.EnumItemType;
import base.PersonAgent;

@SuppressWarnings("static-access")
public class CustomerTest extends TestCase {
	Market mMarket;
	int mMarketNum = 0;
	
	PersonAgent mPerson;
	MarketCustomerRole mCustomer;
	
	MockCashier mMockCashier;
	MockWorker mMockWorker;
	
 	Map<EnumItemType, Integer> mItems = new HashMap<EnumItemType, Integer>();
 	Map<EnumItemType, Integer> mCannotFulfill = new HashMap<EnumItemType, Integer>();
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
 		
 		//Place order and avoid GUI deadlock 
 		//mCustomer.pickAndExecuteAnAction();
 		mCustomer.getOrderList().get(0).mStatus = EnumOrderStatus.PLACED;
		mMockCashier.msgOrderPlacement(mCustomer.getOrderList().get(0));
 		assertEquals("Customer should still have only one order, but he doesn't.", 1, mCustomer.getOrderList().size());
 		assertEquals("Order state should be PLACED, but it's not.", mCustomer.getOrderList().get(0).mStatus.PLACED, mCustomer.getOrderList().get(0).mStatus);
 		
 		//mCustomer.msgAnimationAtMarket();
 		assertEquals("Mock cashier should have one logged event, but doesn't.", 1, mMockCashier.log.size());
 		
 		//Invoice from cashier
 		MarketInvoice invoice = new MarketInvoice(mCustomer.getOrderList().get(0), 18, 0);
 		mCustomer.msgInvoiceToPerson(null, invoice);
 		assertEquals("Customer should have one invoice, but he doesn't.", 1, mCustomer.getInvoiceList().size());
 		assertEquals("The invoice to value should amount to 18 dollars, but doesn't.", 18,mCustomer.getInvoiceList().get(0).mTotal);
 		assertEquals("Invoice event should be RECIEVED_INVOICE, but isn't.", invoice.mOrder.mEvent.RECEIVED_INVOICE, mCustomer.getInvoiceList().get(0).mOrder.mEvent);

 		mCustomer.pickAndExecuteAnAction();
 		//assertTrue("Customer sched should have returned true, but didn't." + mCustomer.getOrderList().get(0).mStatus, mCustomer.pickAndExecuteAnAction());
 		//assertEquals("Customer should have one invoice, but he doesn't.", 0, mCustomer.getInvoiceList().size());
 	
 		
 		
 	}
 	/**
 	 * Test customer for a partial order.
 	 */
 	public void testPartialOrder() {
 	  //assert preconditions
 		assertEquals("MockWorker shouldn't have any logged events, but it does.", 0, mMockWorker.log.size());
 		assertEquals("MocCashier shouldn't have any logged events, but it does.", 0, mMockCashier.log.size());
 		assertEquals("Customer shouldn't have any orders, but he does.", 0, mCustomer.getOrderList().size());
 		assertEquals("Customer shouldn't have any invoices, but he does.", 0, mCustomer.getInvoiceList().size());
 		assertEquals("Customer mItemInventory should have 5 different types of items, but it does not.", 5, mCustomer.mItemInventory.size());
 		assertEquals("Customer mItemsDesired should have 2 different types of items, but it does not.", 2, mCustomer.mItemsDesired.size());
		
 		//adding unavailable items to the map
 		mCannotFulfill.put(EnumItemType.CHICKEN, 2);
 		mCustomer.mCashier = mMockCashier;
 		
 		//Create order
 		mCustomer.pickAndExecuteAnAction();
 		
 		assertEquals("Customer should have one order, but he doesn't.", 1, mCustomer.getOrderList().size());
 		assertEquals("Customer mItemsDesired should still only have 2 different types of items, but it does not.", 2, mCustomer.mItemsDesired.size());
 		assertEquals("Order state should be CARTED, but it's not.", mCustomer.getOrderList().get(0).mStatus.CARTED, mCustomer.getOrderList().get(0).mStatus);
 		
 		//Place order and avoid GUI deadlock 
 		mCustomer.getOrderList().get(0).mStatus = EnumOrderStatus.PLACED;
		mMockCashier.msgOrderPlacement(mCustomer.getOrderList().get(0));
 		assertEquals("Customer should still have only one order, but he doesn't.", 1, mCustomer.getOrderList().size());
 		assertEquals("Order state should be PLACED, but it's not.", mCustomer.getOrderList().get(0).mStatus.PLACED, mCustomer.getOrderList().get(0).mStatus);
 		assertEquals("Mock cashier should have one logged event, but doesn't.", 1, mMockCashier.log.size());
 		
 		//Invoice from cashier
 		MarketInvoice invoice = new MarketInvoice(mCustomer.getOrderList().get(0), 10, 0);
 		mCustomer.msgInvoiceToPerson(mCannotFulfill, invoice);
 		assertEquals("Customer CannotFulFill should have 1 item in it, but it doesn't.", 1, mCustomer.getCannotFulFillMap().size());
 		assertEquals("Customer CannotFulFill should have item type Chicken in it, but it doesn't.", 2, mCustomer.getCannotFulFillMap().get(EnumItemType.CHICKEN));
 		
 		
 	}
}
