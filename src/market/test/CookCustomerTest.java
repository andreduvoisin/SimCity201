package market.test;

import junit.framework.TestCase;
import market.roles.MarketCookCustomerRole;
import market.test.mock.*;
import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;

import java.util.*;

import base.Item.EnumMarketItemType;
import base.PersonAgent;

public class CookCustomerTest extends TestCase {
	PersonAgent mPerson;
	MarketCookCustomerRole mCookCustomer;
	
	MockCashier mMockCashier;
	MockWorker mMockWorker;
	MockDeliveryTruck mMockDeliveryTruck;
	
 	Map<EnumMarketItemType, Integer> mItems = new HashMap<EnumMarketItemType, Integer>();
 	Order mOrder;
}
