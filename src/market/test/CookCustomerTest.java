package market.test;

import junit.framework.TestCase;
import market.roles.MarketCookCustomerRole;
import market.test.mock.*;
import market.*;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;

import java.util.*;

import restaurant.intermediate.RestaurantCookRole;
import base.Item.EnumItemType;
import base.PersonAgent;

public class CookCustomerTest extends TestCase {
	PersonAgent mPerson;
	RestaurantCookRole mCookCustomer;
	
	MockCashier mMockCashier;
	MockWorker mMockWorker;
	MockDeliveryTruck mMockDeliveryTruck;
	
 	Map<EnumItemType, Integer> mItems = new HashMap<EnumItemType, Integer>();
 	MarketOrder mOrder;
 	
 	public void setUp() throws Exception {
 		super.setUp();
 		
 		mPerson = new PersonAgent();
 		mCookCustomer = new RestaurantCookRole(mPerson);
 		
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
}
