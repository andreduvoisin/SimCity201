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
 		
 		mPerson = new PersonAgent();
 		mDeliveryTruck = new MarketDeliveryTruckRole(mPerson, 0);
 		
 		mMockCashier = new MockCashier();
 		mMockCookCustomer = new MockCookCustomer();
 		mMockWorker = new MockWorker();
 		
 		mItems.put(EnumItemType.CHICKEN, 3);
 		mItems.put(EnumItemType.STEAK, 1);
 		mOrder = new MarketOrder(mItems,mMockCookCustomer);
 		
 	}
 	
 	/**
 	 * Test delivery truck functionality.
 	 */
 	public void testDeliveryTruck() {
 		
 	}
}
