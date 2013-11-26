package market.test;

import junit.framework.TestCase;
import market.test.mock.*;
import market.*;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;

import java.util.*;

import restaurant.intermediate.RestaurantCookRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_tranac.gui.TranacCookGui;
import restaurant.restaurant_tranac.gui.TranacRestaurantPanel;
import restaurant.restaurant_tranac.roles.TranacRestaurantCookRole;
import base.Item.EnumItemType;
import base.PersonAgent;

public class CookCustomerTest extends TestCase {
	PersonAgent mPerson;
	RestaurantCookRole mCookCustomer;
	
	MockCashier mMockCashier;
	MockWorker mMockWorker;
	
 	Map<EnumItemType, Integer> mItems;
 	Map<EnumItemType, Integer> mCF;
 	MarketOrder mOrder;
 	
 	public void setUp() throws Exception {
 		super.setUp();
 		
 		mPerson = new PersonAgent();
 		mCookCustomer = new RestaurantCookRole(mPerson); 		
 		
 		mMockCashier = new MockCashier();
 		mMockWorker = new MockWorker();
 	
 		mCookCustomer.setMarketCashier(mMockCashier);
 		
 		mCookCustomer.mItemsDesired.put(EnumItemType.CHICKEN,3);
 		mCookCustomer.mItemsDesired.put(EnumItemType.SALAD, 1);
 		
 		mItems = new HashMap<EnumItemType, Integer>();
 		mItems.put(EnumItemType.CHICKEN, 3);
 		mItems.put(EnumItemType.SALAD, 1);
 	
 		mCF = new HashMap<EnumItemType, Integer>();
 	}
 	
 	/**
 	 * Test cook customer for a completed order.
 	 */
 	public void testTranacRestaurant() {
 	  //set up the test
 		TranacRestaurantPanel mPanel = new TranacRestaurantPanel(null);
 		TranacCookGui mCookGui = new TranacCookGui((TranacRestaurantCookRole) mCookCustomer.subRole);
 		mCookCustomer.setRestaurant(6);
 		TranacRestaurantCookRole r = (TranacRestaurantCookRole)(mCookCustomer.subRole);
 		r.setGui(mCookGui);
 		
 	//assert preconditions
 		
 		
 		
 		mCookCustomer.pickAndExecuteAnAction();
 	  //assert number of orders
 		assertEquals("Cook should have one order.", mCookCustomer.mOrders.size(),1);
 	  //assert order state
 		assertEquals("Order state should be carted.",
 				mCookCustomer.mOrders.get(0).mStatus,EnumOrderStatus.CARTED);
 		
 		//create local pointer to order
 		MarketOrder o = mCookCustomer.mOrders.get(0);
	 	
	 		assertEquals("Cook should have 8 chickens.",
 	 				(int)(o.mItems.get(EnumItemType.CHICKEN)),3);
 	 		assertEquals("Cook should have 6 salads.",
 	 				(int)(o.mItems.get(EnumItemType.SALAD)),1);	

	 		
 		mCookCustomer.pickAndExecuteAnAction();
 	  //assert status of order
 		assertEquals("Order state should be placed.",
 				mCookCustomer.mOrders.get(0).mStatus,EnumOrderStatus.PLACED);
 	  //assert cashier has received message
 		assertTrue("Cashier should have received msgOrderPlacement. Instead " +
 				mMockCashier.log.getLastLoggedEvent().toString(),
 				mMockCashier.log.containsString("Received msgOrderPlacement"));
 		
 		
 		o.mStatus = EnumOrderStatus.PAYING;
 		MarketInvoice i = new MarketInvoice(o,50,-1);
 		
 		mCookCustomer.msgInvoiceToPerson(mCF,i);
 	  //assert order event
 		assertEquals("Order event should be received_invoice.",
 				o.mEvent, EnumOrderEvent.RECEIVED_INVOICE);
 	  //assert number of invoices
 		assertEquals("Cook should have one invoice.", mCookCustomer.mInvoices.size(),1);
 		
 		mCookCustomer.pickAndExecuteAnAction();
 	  //assert order status
 		assertEquals("Order status should be paid.",
 				o.mStatus, EnumOrderStatus.PAID);
 	  //assert invoice payment
 		assertEquals("Invoice payment should be equal to cost.",
 				i.mPayment, i.mTotal);
 	  //assert cashier has received messages
 		assertTrue("Cashier should have received msgPayingForOrder. Instead " +
 				mMockCashier.log.getLastLoggedEvent().toString(),
 				mMockCashier.log.containsString("Received msgPayingForOrder"));
 	  //assert number of invoices
 		assertEquals("Cook should have 0 invoices.",
 				mCookCustomer.mInvoices.size(),0);

 		o.mStatus = EnumOrderStatus.FULFILLING;
 		
 		mCookCustomer.msgHereIsCookOrder(o);
 	  //assert order event
 		assertEquals("Order event should be received order.",
 				o.mEvent,EnumOrderEvent.RECEIVED_ORDER);
 		
 		mCookCustomer.pickAndExecuteAnAction();
 	  //assert order status
 		assertEquals("Order status should be done.",
 				o.mStatus,EnumOrderStatus.DONE);
 	  //assert number of orders
 		assertEquals("Cook should have 0 orders.",
 				mCookCustomer.mOrders.size(),0);
 	  //assert itemInventory
 		assertEquals("Cook should have 8 chickens.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.CHICKEN)),8);
 		assertEquals("Cook should have 6 salads.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.SALAD)),6);		
 		assertEquals("Cook should have 5 steaks.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.STEAK)),5);		
 		assertEquals("Cook should have 5 pizzas.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.PIZZA)),5);		
 	}
 	
 	public void testSmilehamRestaurant() {
 	  //set up the test
 		SmilehamAnimationPanel mPanel = new SmilehamAnimationPanel(null);
 		mCookCustomer.setRestaurant(5);
	
 	  //assert preconditions
 		
 		
 		
 		mCookCustomer.pickAndExecuteAnAction();
 	  //assert number of orders
 		assertEquals("Cook should have one order.", mCookCustomer.mOrders.size(),1);
 	  //assert order state
 		assertEquals("Order state should be carted.",
 				mCookCustomer.mOrders.get(0).mStatus,EnumOrderStatus.CARTED);
 		
 		//create local pointer to order
 		MarketOrder o = mCookCustomer.mOrders.get(0);
	 	
	 		assertEquals("Cook should have 8 chickens.",
 	 				(int)(o.mItems.get(EnumItemType.CHICKEN)),3);
 	 		assertEquals("Cook should have 6 salads.",
 	 				(int)(o.mItems.get(EnumItemType.SALAD)),1);	

	 		
 		mCookCustomer.pickAndExecuteAnAction();
 	  //assert status of order
 		assertEquals("Order state should be placed.",
 				mCookCustomer.mOrders.get(0).mStatus,EnumOrderStatus.PLACED);
 	  //assert cashier has received message
 		assertTrue("Cashier should have received msgOrderPlacement. Instead " +
 				mMockCashier.log.getLastLoggedEvent().toString(),
 				mMockCashier.log.containsString("Received msgOrderPlacement"));
 		
 		
 		o.mStatus = EnumOrderStatus.PAYING;
 		MarketInvoice i = new MarketInvoice(o,50,-1);
 		
 		mCookCustomer.msgInvoiceToPerson(mCF,i);
 	  //assert order event
 		assertEquals("Order event should be received_invoice.",
 				o.mEvent, EnumOrderEvent.RECEIVED_INVOICE);
 	  //assert number of invoices
 		assertEquals("Cook should have one invoice.", mCookCustomer.mInvoices.size(),1);
 		
 		mCookCustomer.pickAndExecuteAnAction();
 	  //assert order status
 		assertEquals("Order status should be paid.",
 				o.mStatus, EnumOrderStatus.PAID);
 	  //assert invoice payment
 		assertEquals("Invoice payment should be equal to cost.",
 				i.mPayment, i.mTotal);
 	  //assert cashier has received messages
 		assertTrue("Cashier should have received msgPayingForOrder. Instead " +
 				mMockCashier.log.getLastLoggedEvent().toString(),
 				mMockCashier.log.containsString("Received msgPayingForOrder"));
 	  //assert number of invoices
 		assertEquals("Cook should have 0 invoices.",
 				mCookCustomer.mInvoices.size(),0);

 		o.mStatus = EnumOrderStatus.FULFILLING;
 		
 		mCookCustomer.msgHereIsCookOrder(o);
 	  //assert order event
 		assertEquals("Order event should be received order.",
 				o.mEvent,EnumOrderEvent.RECEIVED_ORDER);
 		
 		mCookCustomer.pickAndExecuteAnAction();
 	  //assert order status
 		assertEquals("Order status should be done.",
 				o.mStatus,EnumOrderStatus.DONE);
 	  //assert number of orders
 		assertEquals("Cook should have 0 orders.",
 				mCookCustomer.mOrders.size(),0);
 	  //assert itemInventory
 		assertEquals("Cook should have 8 chickens.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.CHICKEN)),8);
 		assertEquals("Cook should have 6 salads.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.SALAD)),6);		
 		assertEquals("Cook should have 5 steaks.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.STEAK)),5);		
 		assertEquals("Cook should have 5 pizzas.",
 				(int)(mCookCustomer.mItemInventory.get(EnumItemType.PIZZA)),5);			
 	}
}
