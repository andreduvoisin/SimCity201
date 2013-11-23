package market.test;

import junit.framework.TestCase;
import market.roles.MarketWorkerRole;
import market.test.mock.*;
import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;

import java.util.*;

import base.Item.EnumMarketItemType;
import base.PersonAgent;

public class WorkerTest extends TestCase {
	PersonAgent mPerson;
	MarketWorkerRole mWorker;

	MockCashier mMockCashier;
	MockCustomer mMockCustomer;
	MockCookCustomer mMockCookCustomer;
 	MockDeliveryTruck mMockDeliveryTruck;
 	
 	Map<String, Integer> mItems = new HashMap<String, Integer>();
 	Order mOrder;
	
	public void setUp() throws Exception {
		super.setUp();
		
		mPerson = new PersonAgent("WorkerRole");
		mWorker = new MarketWorkerRole(mPerson);
		
		mMockCashier = new MockCashier();
		mMockCustomer = new MockCustomer();		
		mMockCookCustomer = new MockCookCustomer();
		mMockDeliveryTruck = new MockDeliveryTruck();

		mItems.put("Chicken", 3);
		mItems.put("Steak", 1);
	}
	
/** 
 * Test Worker Functionality (minus animation gathering items)
 * for a regular customer.
 */
	public void testWorkerCustomer() {
		System.out.println("Test Worker Functions");
	  //create order; set state of
	  //order to beginning of worker obligations
		mOrder = new Order(mItems, mMockCustomer);
		mOrder.mStatus = EnumOrderStatus.PAID;
		mOrder.mCashier = mMockCashier;
		mOrder.mWorker = mWorker;

	  //check preconditions
		assertEquals("Worker should have 0 orders.",
				mWorker.getNumOrders(), 0);
		assertEquals("Order state should be paid.",
				mOrder.mStatus, EnumOrderStatus.PAID);
		assertEquals("Order event should be none.",
				mOrder.mEvent, EnumOrderEvent.NONE);
		
		mWorker.msgFulfillOrder(mOrder);
	  //assert number of orders
		assertEquals("Worker should have 1 order.",
				mWorker.getNumOrders(), 1);
	  //assert order event
		assertEquals("Order event should be order paid.",
				mWorker.getOrder(0).mEvent, EnumOrderEvent.ORDER_PAID);
		
		
		mWorker.pickAndExecuteAnAction();
	  //assert order state
		assertEquals("Order state should be ordering.",
				mWorker.getOrder(0).mStatus, EnumOrderStatus.ORDERING);
		
		
		mWorker.msgOrderFulfilled(mOrder);
	  //assert order event
		assertEquals("Order event should be told to fulfill.",
				mWorker.getOrder(0).mEvent, EnumOrderEvent.TOLD_TO_FULFILL);
		
		
		mWorker.pickAndExecuteAnAction();
	  //assert order state and event
		assertEquals("Order state should be fulfilling.",
				mOrder.mStatus, EnumOrderStatus.FULFILLING);
	  //assert number of orders
		assertEquals("Worker should have 0 orders.",
				mWorker.getNumOrders(), 0);
	  //assert customer has received message
		assertTrue("MockCustomer should have received message.",
				mMockCustomer.log.containsString("Received msgHereIsCustomerOrder."));
	}
	
	/** 
	 * Test Worker Functionality (minus animation gathering items)
	 * for a cook customer
	 */
		public void testWorkerCookCustomer() {
			System.out.println("Test Worker Functions");
		  //create order; set state of order to beginning of
		  //worker obligations; 
			mOrder = new Order(mItems, mMockCookCustomer);
			mOrder.mStatus = EnumOrderStatus.PAID;
			mOrder.mDeliveryTruck = mMockDeliveryTruck;
			mOrder.mCashier = mMockCashier;
			mOrder.mWorker = mWorker;

		  //check preconditions
			assertEquals("Worker should have 0 orders.",
					mWorker.getNumOrders(), 0);
			assertEquals("Order state should be paid.",
					mOrder.mStatus, EnumOrderStatus.PAID);
			assertEquals("Order event should be none.",
					mOrder.mEvent, EnumOrderEvent.NONE);
			
			mWorker.msgFulfillOrder(mOrder);
		  //assert number of orders
			assertEquals("Worker should have 1 order.",
					mWorker.getNumOrders(), 1);
		  //assert order event
			assertEquals("Order event should be order paid.",
					mWorker.getOrder(0).mEvent, EnumOrderEvent.ORDER_PAID);
			
			
			mWorker.pickAndExecuteAnAction();
		  //assert order state
			assertEquals("Order state should be ordering.",
					mWorker.getOrder(0).mStatus, EnumOrderStatus.ORDERING);
			
			
			mWorker.msgOrderFulfilled(mOrder);
		  //assert order event
			assertEquals("Order event should be told to send.",
					mWorker.getOrder(0).mEvent, EnumOrderEvent.TOLD_TO_SEND);
			
			
			mWorker.pickAndExecuteAnAction();
		  //assert order state and event
			assertEquals("Order state should be delivering.",
					mOrder.mStatus, EnumOrderStatus.DELIVERING);
		  //assert number of orders
			assertEquals("Worker should have 0 orders.",
					mWorker.getNumOrders(), 0);
		  //assert delivery truck has received message
			assertTrue("MockDeliveryTruck should have received message."
					+ "Instead his log reads: " + mMockDeliveryTruck.log.getLastLoggedEvent().toString(),
					mMockDeliveryTruck.log.containsString("Received msgDeliverOrderToCook."));
		}
}
