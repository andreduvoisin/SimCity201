package market.test;

import junit.framework.TestCase;
import market.roles.MarketDeliveryTruckRole;
import market.test.mock.*;
import market.*;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;

import java.util.*;

import base.Item.EnumMarketItemType;
import base.PersonAgent;

public class DeliveryTruckTest extends TestCase {
	PersonAgent mPerson;
	MarketDeliveryTruckRole mDeliveryTruck;
	
	MockCashier mMockCashier;
	MockCookCustomer mMockCookCustomer;
	MockWorker mMockWorker;
	
 	Map<EnumMarketItemType, Integer> mItems = new HashMap<EnumMarketItemType, Integer>();
 	MarketOrder mOrder;
 	
 	public void setUp() throws Exception {
 		super.setUp();
 		
 		mPerson = new PersonAgent();
 		mDeliveryTruck = new MarketDeliveryTruckRole(mPerson);
 		
 		mMockCashier = new MockCashier();
 		mMockCookCustomer = new MockCookCustomer();
 		mMockWorker = new MockWorker();
 		
 		mItems.put(EnumMarketItemType.CHICKEN, 3);
 		mItems.put(EnumMarketItemType.STEAK, 1);
 	}
 	
 	/**
 	 * Test delivery truck functionality.
 	 */
 	public void testDeliveryTruck() {
 		
 	}
}
