package market.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.MarketOrder;
import market.roles.MarketDeliveryTruckRole;
import market.test.mock.MockCashier;
import market.test.mock.MockCookCustomer;
import market.test.mock.MockWorker;
import base.Item.EnumItemType;
import base.ContactList;
import base.PersonAgent;

public class DeliveryTruckTest extends TestCase {
	PersonAgent mPerson;
	MarketDeliveryTruckRole mDeliveryTruck;
	
	MockCashier mMockCashier;
	MockCookCustomer mMockCookCustomer;
	MockWorker mMockWorker;
	
 	Map<EnumItemType, Integer> mItems = new HashMap<EnumItemType, Integer>();
 	MarketOrder mOrder;
 	
 	public void setUp() throws Exception {
 		super.setUp();
 		ContactList.setup();
 		mPerson = new PersonAgent();
 		mDeliveryTruck = new MarketDeliveryTruckRole(mPerson, 0);
 		
 		mMockCashier = new MockCashier();
 		mMockCookCustomer = new MockCookCustomer();
 		mMockWorker = new MockWorker();
 		
 		mItems.put(EnumItemType.CHICKEN, 3);
 		mItems.put(EnumItemType.STEAK, 1);
 		mOrder = new MarketOrder(mItems,mMockCookCustomer);
 		
 		//byapss animation
 		PersonAgent p = (PersonAgent)mDeliveryTruck.getPerson();
 		p.msgAnimationDone();
 		p.msgAnimationDone();
 		p.msgAnimationDone();
 		p.msgAnimationDone();
 		p.msgAnimationDone();

 	}
 	
 	/**
 	 * Test delivery truck functionality for open restaurant.
 	 */
 	public void testDeliveryTruck() {
 		//assert preconditions
 		assertEquals("Delivery truck has no deliveries.",
 				mDeliveryTruck.mPendingDeliveries.size(),0);
 		assertEquals("Delivery truck has no pending deliveries.",
 				mDeliveryTruck.mDeliveries.size(),0);
 		mDeliveryTruck.msgDeliverOrderToCook(mOrder);
 		//assert size of pending deliveries
 		assertEquals("Delivery truck should have one pending order.",
 				mDeliveryTruck.mPendingDeliveries.size(),1);
 		
 		mDeliveryTruck.pickAndExecuteAnAction();
 		//assert size of deliveries
 		assertEquals("Delivery truck should have one order.",
 				mDeliveryTruck.mDeliveries.size(),1);
 		//assert size of pending deliveries
 		assertEquals("Delivery truck should have no pending orders.",
 				mDeliveryTruck.mPendingDeliveries.size(),0);
 
 	}
}
