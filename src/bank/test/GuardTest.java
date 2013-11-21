package bank.test;

import junit.framework.TestCase;
//	INTERFACES
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
	MockCustomerRole mCustomer1;
	MockCustomerRole mCustomer2;
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
		mPerson.addRole((Role)mGuard, true);
		
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
		assertTrue("Customer1 has an empty log.", mCustomer1.log.size() == 0);
		
		//1 : add customer 
		mGuard.msgNeedService(mCustomer1);
		
		//Check
		assertTrue("Guard has one customer.", mGuard.mCustomers.size() == 1);
		assertTrue("PAEA: return false", !mGuard.pickAndExecuteAnAction());
		
		//2 : add teller
		mGuard.msgReadyToWork(mTeller1);
		
		//Check
		assertTrue("Guard has one teller.", !mGuard.mTellers.isEmpty());
		assertTrue("Teller is available.", mGuard.mTellers.get(mTeller1));
		
		//3 : p.a.e.a. (provideService(c,t))
		assertTrue("PAEA: provideService(c,t)", mGuard.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Guard has no customers.", mGuard.mCustomers.size() == 0);
		assertTrue("Teller is unavailable.", !mGuard.mTellers.get(mTeller1));
		assertTrue("Customer received msgGoToTeller.", mCustomer1.log.containsString("msgGoToTeller"));
		assertTrue("PAEA: return false", !mGuard.pickAndExecuteAnAction());
	}
	
	public void testTwo_TwoCustomers(){
		//setUp()
		
		//Preconditions
		assertTrue("Guard has no customers.", mGuard.mCustomers.size() == 0);
		assertTrue("Guard has no tellers.", mGuard.mTellers.isEmpty());
		assertTrue("Customer1 has an empty log.", mCustomer1.log.size() == 0);
		
		//1 : add customer 
		mGuard.msgNeedService(mCustomer1);
		mGuard.msgNeedService(mCustomer2);
		
		//Check
		assertTrue("Guard has two customers.", mGuard.mCustomers.size() == 2);
		assertTrue("PAEA: return false", !mGuard.pickAndExecuteAnAction());
		
		//2 : add teller
		mGuard.msgReadyToWork(mTeller1);
		
		//Check
		assertTrue("Guard has one teller.", mGuard.mTellers.keySet().size() == 1);
		assertTrue("Teller is available.", mGuard.mTellers.get(mTeller1));
		
		//3 : p.a.e.a. (provideService(c,t))
		assertTrue("PAEA: provideService(c,t)", mGuard.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Guard has one customers.", mGuard.mCustomers.size() == 1);
		assertTrue("Teller is unavailable.", !mGuard.mTellers.get(mTeller1));
		assertTrue("Customer received msgGoToTeller.", mCustomer1.log.containsString("msgGoToTeller"));
		assertTrue("PAEA: return false", !mGuard.pickAndExecuteAnAction());
		
		//4 : make Teller available
		mGuard.msgReadyToWork(mTeller1);
		
		//Check
		assertTrue("Teller is available.", mGuard.mTellers.get(mTeller1));
		
		//5 : p.a.e.a. (provideService(c,t))
		assertTrue("PAEA: provideService(c,t)", mGuard.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Guard has no customers.", mGuard.mCustomers.size() == 0);
		assertTrue("Teller is unavailable.", !mGuard.mTellers.get(mTeller1));
		assertTrue("Customer received msgGoToTeller.", mCustomer2.log.containsString("msgGoToTeller"));
		assertTrue("PAEA: return false", !mGuard.pickAndExecuteAnAction());
	}
	
	public void testThree_OneRobber(){
		//setUp()
		
		//Preconditions
		assertTrue("Guard has no customers.", mGuard.mCustomers.size() == 0);
		assertTrue("Guard has no tellers.", mGuard.mTellers.isEmpty());
		
		//1 : add teller
		mGuard.msgRobberAlert(mCustomer1);
				
		//Check
		assertTrue("Guard has one criminal.", mGuard.mCriminals.size() == 1);
		
		//2 : p.a.e.a. ()
		//TODO Rex: fill in this non-norm of killRobber
	}
}

