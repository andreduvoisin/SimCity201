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
	PersonAgent mPerson;
	MarketCashierRole mCashier;
	
	MockCustomer mMockCustomer;
	MockCookCustomer mMockCookCustomer;
	MockDeliveryTruck mMockDeliveryTruck;
	
 	Map<EnumMarketItemType, Integer> mItems = new HashMap<EnumMarketItemType, Integer>();
 	Order mOrder;
}
