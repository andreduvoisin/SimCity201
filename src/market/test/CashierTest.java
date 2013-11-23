package market.test;

import junit.framework.TestCase;
import market.roles.MarketCashierRole;
import market.test.mock.*;
import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;

import java.util.*;

import base.Item.EnumMarketItemType;
import base.PersonAgent;

public class CashierTest extends TestCase {
	Market mMarket;
	PersonAgent mPerson;
	MarketCashierRole mCashier;
	
	MockCustomer mMockCustomer;
	MockCookCustomer mMockCookCustomer;
	MockDeliveryTruck mMockDeliveryTruck;
	
 	Map<EnumMarketItemType, Integer> mItems = new HashMap<EnumMarketItemType, Integer>();
 	Order mOrder;
 	
 	public void setUp() throws Exception {
 		super.setUp();
 		
 		mMarket = new Market();
 		mPerson = new PersonAgent();
 		mCashier = new MarketCashierRole(mPerson,mMarket);
 		
 		mMockCustomer = new MockCustomer();
 		mMockCookCustomer = new MockCookCustomer();
 		mMockDeliveryTruck = new MockDeliveryTruck();
 		
 		mItems.put(EnumMarketItemType.CHICKEN, 3);
 		mItems.put(EnumMarketItemType.STEAK, 1);
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
