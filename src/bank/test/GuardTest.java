package bank.test;

import junit.framework.TestCase;
import bank.interfaces.Customer;
import bank.interfaces.Teller;
import bank.roles.BankGuardRole;
import bank.test.mock.MockCustomerRole;
import bank.test.mock.MockTellerRole;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;


public class GuardTest extends TestCase{
	
//	INTERFACES
	BankGuardRole mGuard;
	Person mPerson;
	Customer mCustomer1;
	Customer mCustomer2;
	Teller mTeller1;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		//Testee
		mGuard = new BankGuardRole();
		mPerson = new PersonAgent("Person");
		mPerson.addRole((Role)mGuard);
		
		//Interfaces
		mCustomer1 = new MockCustomerRole();
		mCustomer2 = new MockCustomerRole();
		mTeller1 = new MockTellerRole();
		
		//clear logs
	}
	
//	TESTS
	
	public void testOne_OneCustomer(){
		//setUp()
		
		//Preconditions
		assertTrue("Guard has no customers.", mGuard.mCustomers.size() == 0);
		assertTrue("Guard has no tellers.", mGuard.mTellers.isEmpty());
		
		//1 : add customer & teller
		mGuard.msgReadyToWork(mTeller1);
		mGuard.msgNeedService(mCustomer1);
		
		//Check
		assertTrue("Guard has one customer.", mGuard.mCustomers.size() == 1);
		assertTrue("Guard has one teller.", !mGuard.mTellers.isEmpty());
		assertTrue("Teller is available.", mGuard.mTellers.get(mTeller1));
		
		//2 : p.a.e.a. (provideService(c,t))
		assertTrue("PAEA: provideService(c,t)", mGuard.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Guard has no customers.", mGuard.mCustomers.size() == 0);
		assertTrue("Teller is unavailable.", !mGuard.mTellers.get(mTeller1));
	}
}

