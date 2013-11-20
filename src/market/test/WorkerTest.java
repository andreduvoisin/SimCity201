package market.test;

import junit.framework.TestCase;
import market.interfaces.*;
import market.roles.MarketWorkerRole;
import base.PersonAgent;

public class WorkerTest extends TestCase {
	PersonAgent mPerson = new PersonAgent("WorkerRole");
	MarketWorkerRole mWorker = new MarketWorkerRole(mPerson);

//	MockCashier mMockCashier;
//	MockCustomer mMockCustomer;
// 	DeliveryTruck mMockDeliveyTruck;
	
	
	public void setUp() throws Exception {
		super.setUp();
	}
	
	public void testWorkerFunctionality() {
		
	}
}
