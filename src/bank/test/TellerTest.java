package bank.test;

import junit.framework.TestCase;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import bank.test.mock.MockCustomerRole;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;


public class TellerTest extends TestCase{
	
//	INTERFACES
	Person mPerson;
	BankTellerRole mTeller;
	MockCustomerRole mCustomer1;
	MockCustomerRole mCustomer2;
	BankMasterTellerRole mMasterTeller;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		
		//Testee
		mTeller = new BankTellerRole();
		mPerson = new PersonAgent("Person");
		mPerson.addRole((Role)mTeller);
		
		//Interfaces
		mCustomer1 = new MockCustomerRole();
		mCustomer2 = new MockCustomerRole();
		mMasterTeller = new BankMasterTellerRole();
		
		//clear logs
	}
	
//	TESTS
	
	public void testOne_OneTransaction(){
		//setUp()
		
		//Preconditions
		assertTrue("Teller accounts is null", mTeller.mAccounts == null);
		assertTrue("Teller account indicies is null", mTeller.mAccountIndex == null);
		
		//1 : set teller database
		mTeller.setMaster(mMasterTeller);
		mTeller.setAccounts();
		mTeller.setAccountIndex();
		
		//Check
		assertTrue("Teller has no customer", mTeller.mCustomer == null);
		assertTrue("Teller has no accounts", mTeller.mAccounts.isEmpty());
		assertTrue("Teller has no account indices", mTeller.mAccountIndex.isEmpty());
		
		//2 : add customer - open
		mTeller.msgOpen(mCustomer1, 1, 100, "Joe");
		
		//Check
		assertTrue("Teller has customer", mTeller.mCustomer.customer == mCustomer1);
		
		//3 : p.a.e.a. (open())
		assertTrue("PAEA: open()", mTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Teller has one account", mTeller.mAccounts.size() == 1);
		assertTrue("Teller has one account index", mTeller.mAccountIndex.size() == 1);
		assertTrue("Customer has balance of 100", mTeller.mAccounts.get(mTeller.mAccountIndex.get(1)).balance == 100);
		assertTrue("Customer received msgHereIsBalance", mCustomer1.log.containsString("msgHereIsBalance: 100"));
		
		
	}
	
	public void testTwo_TwoCustomers(){
		//setUp()
		
		//Preconditions
		
	}
	
	public void testThree_OneRobber(){
		
	}
}

