package market.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.MarketOrder;
import market.roles.MarketCashierRole;
import market.test.mock.MockCookCustomer;
import market.test.mock.MockCustomer;
import market.test.mock.MockDeliveryTruck;
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
 		
 		mMockCustomer = new MockCustomer();
 		mMockCookCustomer = new MockCookCustomer();
 		mMockDeliveryTruck = new MockDeliveryTruck();
 		
 		mItems.put(EnumItemType.CHICKEN, 3);
 		mItems.put(EnumItemType.STEAK, 1);
 	}
 	
 	/**
 	 * Test food market cashier with customer.
 	 */
 	public void testCashierCustomer() {
 		mCashier = new MarketCashierRole(mPerson);
 	}
 	
 	/**
 	 * Test food market cashier with cook customer.
 	 */
 	public void testCashierCookCustomer() {
 		mCashier = new MarketCashierRole(mPerson, EnumMarketType.BOTH);
 	}
 	
 	/**
 	 * Test food market cashier with two different customers.
 	 */
 	public void testCashierTwoCustomers() {
 		mCashier = new MarketCashierRole(mPerson, EnumMarketType.BOTH);
 	}

 	/**
 	 * Test car market cashier with customer.
 	 */
 	public void testCarMarketCashier() {
 		mCashier = new MarketCashierRole(mPerson, EnumMarketType.CAR);
 	}
}
