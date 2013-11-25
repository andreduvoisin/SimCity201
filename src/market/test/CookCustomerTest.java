package market.test;

import junit.framework.TestCase;
import market.roles.MarketCookCustomerRole;
import market.test.mock.*;
import market.*;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;

import java.util.*;

import base.Item.EnumItemType;
import base.PersonAgent;

public class CookCustomerTest extends TestCase {
	PersonAgent mPerson;
	MarketCookCustomerRole mCookCustomer;
	
	MockCashier mMockCashier;
	MockWorker mMockWorker;
	MockDeliveryTruck mMockDeliveryTruck;
	
 	Map<EnumItemType, Integer> mItems = new HashMap<EnumItemType, Integer>();
 	MarketOrder mOrder;
 	
 	public void setUp() throws Exception {
 		super.setUp();
 		
 		mPerson = new PersonAgent();
 		mCookCustomer = new MarketCookCustomerRole(mPerson);
 		
 		mMockCashier = new MockCashier();
 		mMockWorker = new MockWorker();
 		mMockDeliveryTruck = new MockDeliveryTruck();
 		
 		mItems.put(EnumItemType.CHICKEN, 3);
 		mItems.put(EnumItemType.STEAK, 1);
 	}
 	
 	/**
 	 * Test cook customer for a completed order.
 	 */
 	public void testCompletedOrder() {
 		
 	}
 	
 	/**
 	 * Test cook customer for a partial order.
 	 */
 	public void testPartialOrder() {
 		
 	}
 	
 	/**
 	 * Test cook customer for an unfulfilled order.
 	 */
 	public void testUnfulfilledOrder() {
 		
 	}
}
