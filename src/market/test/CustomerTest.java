package market.test;

import junit.framework.TestCase;
import market.roles.MarketCustomerRole;
import market.test.mock.*;
import market.*;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;

import java.util.*;

import base.Item.EnumItemType;
import base.PersonAgent;

public class CustomerTest extends TestCase {
	PersonAgent mPerson;
	MarketCustomerRole mCustomer;
	
	MockCashier mMockCashier;
	MockWorker mMockWorker;
	
 	Map<EnumItemType, Integer> mItems = new HashMap<EnumItemType, Integer>();
 	MarketOrder mOrder;
 
 	public void setUp() throws Exception {
 		super.setUp();
 		
 		mPerson = new PersonAgent();
 		mCustomer = new MarketCustomerRole(mPerson);
 		
 		mMockCashier = new MockCashier();
 		mMockWorker = new MockWorker();
 		
 		mItems.put(EnumItemType.CHICKEN, 3);
 		mItems.put(EnumItemType.STEAK, 1);
 	}
 	
 	/**
 	 * Test customer for a completed order.
 	 */
 	public void testCompletedOrder() {
 		
 	}
}
