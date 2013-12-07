package market.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.MarketOrder;
import market.MarketOrder.EnumOrderStatus;
import market.roles.MarketCashierRole;
import market.test.mock.MockCookCustomer;
import market.test.mock.MockCustomer;
import market.test.mock.MockDeliveryTruck;
import base.Item.EnumItemType;
import base.PersonAgent;
import base.reference.ContactList;
import base.reference.Market;

public class CashierTest extends TestCase {
	Market mMarket;
	int mMarketNum = 1;
	PersonAgent mPerson;
	MarketCashierRole mCashier;
	
	MockCustomer mMockCustomer;
	MockCookCustomer mMockCookCustomer;
	MockDeliveryTruck mMockDeliveryTruck;
	
 	Map<EnumItemType, Integer> mItems = new HashMap<EnumItemType, Integer>();
 	MarketOrder mOrder;
 	
 	public void setUp() throws Exception {
 		super.setUp();
 		//set up base market
 		ContactList.setup();
 		mMarket = ContactList.sMarketList.get(0);
 		mPerson = new PersonAgent();
 		
 		mMockCustomer = new MockCustomer();
 		mMockCookCustomer = new MockCookCustomer();
 		mMockDeliveryTruck = new MockDeliveryTruck();
 		
 		mItems.put(EnumItemType.CHICKEN, 3);
 		mItems.put(EnumItemType.STEAK, 1);
 		
 		//create order
 		mOrder = new MarketOrder(mItems, mMockCustomer);
 		mOrder.mStatus = EnumOrderStatus.PLACED;
 	}
 	
 	/**
 	 * Test food market cashier with customer.
 	 */
 	public void testCashierCustomer() {
 		mCashier = new MarketCashierRole(mPerson,mMarketNum);
 		
 	  //ANGELICA: assert preconditions
 		
 		mCashier.msgOrderPlacement(mOrder);
 	  //assert number of orders
 		assertEquals("Cashier should have one order.",mCashier.mOrders.size(),1);
 		
 		mCashier.pickAndExecuteAnAction();
 	  //assert inventory
 		
 	  //assert number of invoices
 		
 	  //assert customer has received the message
 		
 	}
 	/*
 	/**
 	 * Test market cashier with cook customer.
 	 *
 	public void testCashierCookCustomer() {
 		mCashier = new MarketCashierRole(mPerson,mMarketNum);
 	}
 	
 	/**
 	 * Test market cashier with two different customers.
 	 *
 	public void testCashierTwoCustomers() {
 		mCashier = new MarketCashierRole(mPerson,mMarketNum);
 	}*/
}
