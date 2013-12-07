package market.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.MarketInvoice;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.roles.MarketCashierRole;
import market.test.mock.MockCookCustomer;
import market.test.mock.MockCustomer;
import market.test.mock.MockDeliveryTruck;
import market.test.mock.MockWorker;
import base.Item.EnumItemType;
import base.PersonAgent;
import base.reference.ContactList;
import base.reference.Market;

public class CashierTest extends TestCase {
	Market mMarket;
	int mMarketNum = 0;
	PersonAgent mPerson;
	MarketCashierRole mCashier;
	
	MockCustomer mMockCustomer;
	MockCookCustomer mMockCookCustomer;
	MockWorker mMockWorker;
	
 	Map<EnumItemType, Integer> mItems = new HashMap<EnumItemType, Integer>();
 	MarketOrder mOrder;
 	
 	int mNumItemOne = 1;
 	int mNumItemTwo = 3;
 	public void setUp() throws Exception {
 		super.setUp();
 		//set up base market
 		ContactList.setup();
 		mMarket = ContactList.sMarketList.get(0);
 		mPerson = new PersonAgent();
 		
 		mMockCustomer = new MockCustomer();
 		mMockCookCustomer = new MockCookCustomer();
 		mMockWorker = new MockWorker();
 		mMarket.mWorkers.add(mMockWorker);
 		
 		mItems.put(EnumItemType.STEAK, mNumItemOne);
 		mItems.put(EnumItemType.CHICKEN, mNumItemTwo);
 		
 		//create order
 		mOrder = new MarketOrder(mItems, mMockCustomer);
 		mOrder.mStatus = EnumOrderStatus.PLACED;
 	}
 	
 	/**
 	 * Test food market cashier with customer.
 	 */
 	public void testCashierCustomer() {
 		mCashier = new MarketCashierRole(mPerson,mMarketNum);

 	  //assert preconditions
 		assertEquals("Cashier should have no orders.",mCashier.mOrders.size(),0);
 		assertEquals("Cashier should have no invoices.",mCashier.mInvoices.size(),0);
 		assertEquals("Cashier should have base number of steak.",(int)mCashier.mInventory.get(EnumItemType.STEAK),(int)mCashier.mBaseInventory);
 		assertEquals("Cashier should have base number of chicken.",(int)mCashier.mInventory.get(EnumItemType.CHICKEN),(int)mCashier.mBaseInventory);
 		
 		
 		mCashier.msgOrderPlacement(mOrder);
 	  //assert number of orders
 		assertEquals("Cashier should have one order.",mCashier.mOrders.size(),1);
 	  //assert order event
 		assertEquals("Order event should be order placed.",mOrder.mEvent,EnumOrderEvent.ORDER_PLACED);
 		
 		mCashier.pickAndExecuteAnAction();
 	  //assert order status
 		assertEquals("Order status should be paying.",mOrder.mStatus,EnumOrderStatus.PAYING);
 	  //assert inventory
 		assertEquals("Cashier should have less number of steak.",
 				(int)mCashier.mInventory.get(EnumItemType.STEAK),
 				(int)mCashier.mBaseInventory-mNumItemOne);
 		assertEquals("Cashier should have less number of chicken.",
 				(int)mCashier.mInventory.get(EnumItemType.CHICKEN),
 				(int)mCashier.mBaseInventory-mNumItemTwo);		
 	  //assert number of invoices
 		assertEquals("Cashier should have one invoice.",mCashier.mInvoices.size(),1);
 	  //assert customer has received the message
 		assertTrue("MockCustomer has received msgInvoiceToPerson.",
 				mMockCustomer.log.containsString("Received msgInvoiceToPerson."));
 		
 		//get invoice information; set state of order
 		MarketInvoice invoice = mCashier.mInvoices.get(0);
 		invoice.mPayment = invoice.mTotal;
 		mOrder.mStatus = EnumOrderStatus.PAID;
 		
 		mCashier.msgPayingForOrder(invoice);
 	  //assert order event
 		assertEquals("Order event should be order paid.",mOrder.mEvent,EnumOrderEvent.ORDER_PAID);
 		
 		
 		mCashier.pickAndExecuteAnAction();
 	  //assert number of orders
 		assertEquals("Cashier should have no orders.",mCashier.mOrders.size(),0);
 	  //assert order status
 		assertEquals("Order status should be sent.",mOrder.mStatus,EnumOrderStatus.SENT);
 	  //assert worker has received the message
 		assertTrue("MockWorker should have receievd msgFulfillOrder.",
 				mMockWorker.log.containsString("Received msgFulfillOrder."));
 	}
 	/*
 	/**
 	 * Test market cashier with cook customer.
 	 *
 	public void testCashierCookCustomer() {
 		mCashier = new MarketCashierRole(mPerson,mMarketNum);
 	}
 	
 	/**
 	 * Test market cashier with two different customers.
 	 *
 	public void testCashierTwoCustomers() {
 		mCashier = new MarketCashierRole(mPerson,mMarketNum);
 	}*/
}
