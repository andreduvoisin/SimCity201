package market.test;

import junit.framework.TestCase;
import market.roles.MarketDeliveryTruckRole;
import market.test.mock.*;
import market.*;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;

import java.util.*;

import base.Item.EnumMarketItemType;
import base.PersonAgent;

public class DeliveryTruckTest extends TestCase {
	PersonAgent mPerson;
	MarketDeliveryTruckRole mDeliveryTruck;
	
	MockCashier mMockCashier;
	MockCookCustomer mMockCookCustomer;
	MockWorker mMockWorker;
	
 	Map<EnumMarketItemType, Integer> mItems = new HashMap<EnumMarketItemType, Integer>();
 	Order mOrder;
}
