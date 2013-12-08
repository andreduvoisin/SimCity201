package market.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import market.Market;
import market.MarketOrder;
import market.roles.MarketCustomerRole;
import market.test.mock.MockCashier;
import market.test.mock.MockWorker;
import base.ContactList;
import base.Item.EnumItemType;
import base.PersonAgent;

public class CustomerTest extends TestCase {
	Market mMarket;
	int mMarketNum = 0;
	
	PersonAgent mPerson;
	MarketCustomerRole mCustomer;
	
	MockCashier mMockCashier;
	MockWorker mMockWorker;
	
 	Map<EnumItemType, Integer> mItems = new HashMap<EnumItemType, Integer>();
 	MarketOrder mOrder;
 	
 	int mNumItemOne = 1;
 	int mNumItemTwo = 3;
 
 	public void setUp() throws Exception {
 		super.setUp();
 		
 		ContactList.setup();
 		mMarket = ContactList.sMarketList.get(mMarketNum);
 		mPerson = new PersonAgent();
 		mCustomer = new MarketCustomerRole(mPerson, mMarketNum);
 		
 		mMockCashier = new MockCashier();
 		mMockWorker = new MockWorker();
 		
 		mCustomer.mItemsDesired.put(EnumItemType.CHICKEN, 3);
 		mCustomer.mItemsDesired.put(EnumItemType.STEAK, 1);
 	}
 	
 	/**
 	 * Test customer for a completed order.
 	 */
 	public void testCompletedOrder() {
 	  //assert preconditions
 		
 		
 		
 	}
 	
 	/**
 	 * Test customer for a partial order.
 	 */
 	public void testPartialOrder() {
 	  //assert preconditions
 		
 		
 		
 	}
}
