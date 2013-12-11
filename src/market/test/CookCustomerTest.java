package market.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.Market;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.test.mock.MockCashier;
import market.test.mock.MockRestaurantCashier;
import market.test.mock.MockWorker;
import restaurant.intermediate.RestaurantCookRole;
import base.ContactList;
import base.Item.EnumItemType;
import base.PersonAgent;

public class CookCustomerTest extends TestCase {
	PersonAgent mPerson;
	PersonAgent mPerson2;
	RestaurantCookRole mCookCustomer;
	MockCashier mMockCashier;
	
	MockWorker mMockWorker;
	MockRestaurantCashier mMockRestCashier;
	
 	Map<EnumItemType, Integer> mItems;
 	Map<EnumItemType, Integer> mCF;
 	MarketOrder mOrder;
 	
 	Market mMarket;
 	public void setUp() throws Exception {
 		super.setUp();

 		mMarket = new Market(0);
 		ContactList.setup();
 		mMarket = ContactList.sMarketList.get(0);

 		mPerson = new PersonAgent();
 		
 		
 		mMockCashier = new MockCashier();
 		
 		mMockWorker = new MockWorker();
 		mMockRestCashier = new MockRestaurantCashier();
 		
 		mMarket.mCashier = mMockCashier;
 		mMarket.mWorkers.add(mMockWorker);
 		
 		mItems = new HashMap<EnumItemType, Integer>();
 		mItems.put(EnumItemType.CHICKEN, 3);
 		mItems.put(EnumItemType.SALAD, 1);
 	
 		mCF = new HashMap<EnumItemType, Integer>();
 	}
 	
 	/**
 	 * Test cook customer for a completed order from coder's restaurant.
 	 */
 	//ANGELICA add in restaurant cashire mesage
 	public void testTranacRestaurant() {
 	  //set up the test
 		
 		mCookCustomer = new RestaurantCookRole(mPerson, 6); 		
 		mCookCustomer.setMarketCashier(mMockCashier);
 		mCookCustomer.setRestaurantCashier(mMockRestCashier);
 		
 		mCookCustomer.mItemsDesired.put(EnumItemType.CHICKEN,3);
 		mCookCustomer.mItemsDesired.put(EnumItemType.SALAD, 1);

 	//assert preconditions
 		
 		
 		mCookCustomer.pickAndExecuteAnAction();
 	  //assert number of orders
 		assertEquals("Cook should have one order.", mCookCustomer.mOrders.size(),1);
 	  //assert order state
 		assertEquals("Order state should be carted.",
 				mCookCustomer.mOrders.get(0).mStatus,EnumOrderStatus.CARTED);
 		
 		//create local pointer to order
 		MarketOrder o = mCookCustomer.mOrders.get(0);
	 	
	 		assertEquals("Order should have 3 chickens.",
 	 				(int)(o.mItems.get(EnumItemType.CHICKEN)),3);
 	 		assertEquals("Order should have 1 salads.",
 	 				(int)(o.mItems.get(EnumItemType.SALAD)),1);	

	 		
 		mCookCustomer.pickAndExecuteAnAction();
 		mCookCustomer.mOrders.get(0).mStatus = EnumOrderStatus.PLACED;
 	  //assert status of order
 		assertEquals("Order state should be placed.",
 				mCookCustomer.mOrders.get(0).mStatus,EnumOrderStatus.PLACED);
 	  //assert cashier has received message
 		assertTrue("Cashier should have received msgOrderPlacement. Instead " +
 				mMockCashier.log.getLastLoggedEvent().toString(),
 				mMockCashier.log.containsString("Received msgOrderPlacement"));

 		
 		o.mStatus = EnumOrderStatus.PAYING;
 		mCookCustomer.msgCannotFulfillItems(o,mCF);
 	  //assert order event
 		assertEquals("Order event should be received_invoice.",
 				o.mEvent, EnumOrderEvent.RECEIVED_INVOICE);
 		
 		
 		o.mStatus = EnumOrderStatus.FULFILLING;
 		
 		mCookCustomer.msgHereIsCookOrder(o);
 	  //assert order event
 		assertEquals("Order event should be received order.",
 				o.mEvent,EnumOrderEvent.RECEIVED_ORDER);
 		
 		mCookCustomer.pickAndExecuteAnAction();
 	  //assert order status
 		assertEquals("Order status should be done.",
 				o.mStatus,EnumOrderStatus.DONE);
 	  //assert number of orders
 		assertEquals("Cook should have 0 orders.",
 				mCookCustomer.mOrders.size(),0);
 	  //assert itemInventory
 		assertEquals("Cook should have 5 chickens.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.CHICKEN)),5);
 		assertEquals("Cook should have 3 salads.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.SALAD)),3);
 		assertEquals("Cook should have 2 steaks.",
				(int)(mCookCustomer.mItemInventory.get(EnumItemType.STEAK)),2);		
 		assertEquals("Cook should have 2 pizzas.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.PIZZA)),2);		
 	}
}	
