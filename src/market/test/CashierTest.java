package market.test;

import junit.framework.TestCase;
import market.roles.MarketCashierRole;
import market.test.mock.*;
import market.*;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;

import java.util.*;

import base.Item.EnumItemType;
import base.PersonAgent;

public class CashierTest extends TestCase {
	PersonAgent mPerson;
	MarketCashierRole mCashier;
	
	MockCustomer mMockCustomer;
	MockCookCustomer mMockCookCustomer;
	MockDeliveryTruck mMockDeliveryTruck;
	
 	Map<EnumItemType, Integer> mItems = new HashMap<EnumItemType, Integer>();
 	MarketOrder mOrder;
 	
 	public void setUp() throws Exception {
 		super.setUp();
 		
 		mPerson = new PersonAgent();
 		mCashier = new MarketCashierRole(mPerson);
 		
 		mMockCustomer = new MockCustomer();
 		mMockCookCustomer = new MockCookCustomer();
 		mMockDeliveryTruck = new MockDeliveryTruck();
 		
 		mItems.put(EnumItemType.CHICKEN, 3);
 		mItems.put(EnumItemType.STEAK, 1);
 	}
 	
 	/**
 	 * Test cashier with customer.
 	 */
 	public void testCashierCustomer() {
 		
 	}
 	
 	/**
 	 * Test cashier with cook customer.
 	 */
 	public void testCashierCookCustomer() {
 		
 	}
 	
 	/**
 	 * Test cashier with two different customers.
 	 */
 	public void testCashierTwoCustomers() {
 		
 	}
}
