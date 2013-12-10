package bank.test;

import junit.framework.TestCase;
import bank.Bank;
import bank.interfaces.BankCustomer;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import bank.test.mock.MockCustomerRole;
import base.ContactList;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;


public class TellerTest extends TestCase{
	
//	INTERFACES
	Bank mBank;
	int mBankNum = 0;
	Person mPerson;
	Person mPerson2;
	Person mPerson3;
	Person mCP1;
	Person mCP2;
	Person mCP3;
	BankTellerRole mTeller;
	MockCustomerRole mCustomer1;
	MockCustomerRole mCustomer2;
	MockCustomerRole mCustomer3;
	BankMasterTellerRole mMasterTeller;

	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		ContactList.setup();
 		mBank = ContactList.sBankList.get(mBankNum);
		//Testee
 		mPerson2 = new PersonAgent();
 		mMasterTeller = new BankMasterTellerRole(mPerson2);
		mPerson2.addRole((Role)mMasterTeller, true);
		ContactList.masterTeller = mMasterTeller;
		
		mPerson = new PersonAgent();
		mTeller = new BankTellerRole(mPerson, 0);
		mPerson.addRole((Role)mTeller, true);
		
		//Interfaces
		mCP1 = new PersonAgent();
		mCP2 = new PersonAgent();
		mCP3 = new PersonAgent();
		mCustomer1 = new MockCustomerRole();
		mCustomer1.setPerson(mCP1);
		mCustomer2 = new MockCustomerRole();
		mCustomer2.setPerson(mCP2);
		mCustomer3 = new MockCustomerRole();
		mCustomer3.setPerson(mCP3);
		
		//clear logs
	}
	
//	TESTS
	
	public void testOne_OpenTransaction(){
		//setUp()
		
		//Preconditions
		assertTrue("Teller accounts should not be null ", mTeller.mAccounts != null);
		assertTrue("Teller account indicies is null", mTeller.mAccountIndex != null);
		
		//1 : set teller database
		mTeller.setMaster(mMasterTeller);
		mTeller.setAccounts();
		mTeller.setAccountIndex();
		
		//Check
		assertTrue("Teller has no customer", mTeller.mCustomer == null);
		assertTrue("Teller has no accounts", mTeller.mAccounts.isEmpty());
		assertTrue("Teller has no account indices", mTeller.mAccountIndex.isEmpty());
		
		//2 : add customer - open
		mTeller.msgOpen((BankCustomer)mCustomer1, 1, 100.00, (PersonAgent)mCustomer1.mPerson);
		
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
	
	public void testTwo_Normatives(){
		//setUp()
		
		//Preconditions
		assertTrue("Teller accounts should not be null ", mTeller.mAccounts != null);
		assertTrue("Teller account indicies is null", mTeller.mAccountIndex != null);
		
		//1 : set teller database
		mTeller.setMaster(mMasterTeller);
		mTeller.setAccounts();
		mTeller.setAccountIndex();
		
		//Check
		assertTrue("Teller has no customer", mTeller.mCustomer == null);
		assertTrue("Teller has no accounts", mTeller.mAccounts.isEmpty());
		assertTrue("Teller has no account indices", mTeller.mAccountIndex.isEmpty());
		
		//2 : add customer1 - open
		mTeller.msgOpen(mCustomer1, 1, 100, mCustomer1.mPerson);
		
		//Check
		assertTrue("Teller has customer", mTeller.mCustomer.customer == mCustomer1);
		
		//3 : p.a.e.a. (open())
		assertTrue("PAEA: open()", mTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Teller has one account", mTeller.mAccounts.size() == 1);
		assertTrue("Teller has one account index", mTeller.mAccountIndex.size() == 1);
		assertTrue("Teller account has person", mTeller.mAccounts.get(0).person == mCustomer1.mPerson);
		assertTrue("Customer has balance of 100", mTeller.mAccounts.get(mTeller.mAccountIndex.get(1)).balance == 100);
		assertTrue("Customer received msgHereIsBalance", mCustomer1.log.containsString("msgHereIsBalance: 100"));
		
		//4 : add customer2 - open
		mTeller.msgOpen(mCustomer2, 2, 200, mCustomer2.mPerson);
		
		//Check
		assertTrue("Teller has customer", mTeller.mCustomer.customer == mCustomer2);
		
	
		//5 : p.a.e.a. (open())
		assertTrue("PAEA: open()", mTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Teller has two accounts", mTeller.mAccounts.size() == 2);
		assertTrue("Teller has two account index", mTeller.mAccountIndex.size() == 2);
		assertTrue("Customer has balance of 200", mTeller.mAccounts.get(mTeller.mAccountIndex.get(2)).balance == 200);
		assertTrue("Customer received msgHereIsBalance", mCustomer2.log.containsString("msgHereIsBalance: 200"));
		
		//6 : DEPOSIT - reuse customer1
		mTeller.msgDeposit(mCustomer1, 1, 100);
		assertTrue("PAEA: deposit()", mTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Customer 1 has balance of 200", mTeller.mAccounts.get(mTeller.mAccountIndex.get(1)).balance == 200);
		assertTrue("Customer 1 received msgHereIsBalance", mCustomer1.log.containsString("msgHereIsBalance: 200"));
		assertTrue("Customer 1 has loan of 0", mTeller.mAccounts.get(mTeller.mAccountIndex.get(1)).loan == 0);
		
		//7 : LOAN - reuse customer2
		mTeller.msgLoan(mCustomer2, 2, 50);
		assertTrue("PAEA: loan()", mTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Customer 2 has a loan of 50", mTeller.mAccounts.get(mTeller.mAccountIndex.get(2)).loan == 50);
		//assertTrue("Customer 2 received msgHereIsLoan", mCustomer2.log.containsString("msgHereIsLoan: 50"));
		
		//8 : LOAN (second) - reuse customer 2
		mTeller.msgLoan(mCustomer2, 2, 50);
		assertTrue("PAEA: loan()", mTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Customer 2 has a loan of 100", mTeller.mAccounts.get(mTeller.mAccountIndex.get(2)).loan == 100);
		//assertTrue("Customer 2 received msgHereIsLoan", mCustomer2.log.containsString("msgHereIsLoan: 50"));
		
		//9 : LOAN (rejection) - reuse customer 2
		mTeller.msgLoan(mCustomer2, 2, 50);
		assertTrue("PAEA: loan()", mTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Customer 2 has a loan of 100", mTeller.mAccounts.get(mTeller.mAccountIndex.get(2)).loan == 100);
		//assertTrue("Customer 2 received msgHereIsLoan", mCustomer2.log.containsString("msgHereIsLoan: 0"));
		
		//10 : PAYMENT - reuse customer 2
		mTeller.msgPayment(mCustomer2, 2, 75);
		assertTrue("PAEA: payment()", mTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Customer 2 has a loan of 25", mTeller.mAccounts.get(mTeller.mAccountIndex.get(2)).loan == 25);
		assertTrue("Customer 2 received msgHereIsLoan", mCustomer2.log.containsString("msgHereIsLoan: 0"));
		/*
		//11 : LOAN - reuse customer3
		mTeller.msgLoan(mCustomer3, 3, 200);
		assertTrue("PAEA: loan()", mTeller.pickAndExecuteAnAction());
				
		//Check
		assertTrue("Customer 3 has a loan of 200", mTeller.mAccounts.get(mTeller.mAccountIndex.get(3)).loan == 200);
		assertTrue("Customer 3 received msgHereIsLoan", mCustomer3.log.containsString("msgHereIsLoan: 200"));
			
		//12 : LOAN (second) - reuse customer 3
		mTeller.msgLoan(mCustomer3, 3, 200);
		assertTrue("PAEA: loan()", mTeller.pickAndExecuteAnAction());
				
		//Check
		assertTrue("Customer 3 has a loan of 200", mTeller.mAccounts.get(mTeller.mAccountIndex.get(3)).loan == 200);
		assertTrue("Customer 3 received msgHereIsLoan", mCustomer3.log.containsString("msgHereIsLoan: 200"));
				
		//13 : LOAN (rejection) - reuse customer 3
		mTeller.msgLoan(mCustomer3, 3, 200);
		assertTrue("PAEA: loan()", mTeller.pickAndExecuteAnAction());
				
		//Check
		assertTrue("Customer 3 has a loan of 200", mTeller.mAccounts.get(mTeller.mAccountIndex.get(3)).loan == 200);
		assertTrue("Customer 3 received msgHereIsLoan", mCustomer3.log.containsString("msgHereIsLoan: 0"));
				
		//14 : PAYMENT - reuse customer 2
		mTeller.msgPayment(mCustomer2, 2, 75);
		assertTrue("PAEA: payment()", mTeller.pickAndExecuteAnAction());
				
		//Check
		assertTrue("Customer 2 has a loan of 25", mTeller.mAccounts.get(mTeller.mAccountIndex.get(2)).loan == 25);
		assertTrue("Customer 2 received msgHereIsLoan", mCustomer2.log.containsString("msgHereIsLoan: 0"));*/
	}
	
	public void testThree_NonNorms(){
		//Robbery
		//setUp()
		
		//Preconditions
		assertTrue("Teller accounts should not be null ", mTeller.mAccounts != null);
		assertTrue("Teller account indicies is null", mTeller.mAccountIndex != null);
		
		//1 : set teller database
		mTeller.setMaster(mMasterTeller);
		mTeller.setAccounts();
		mTeller.setAccountIndex();
		
		//Check
		assertTrue("Teller has no customer", mTeller.mCustomer == null);
		assertTrue("Teller has no accounts", mTeller.mAccounts.isEmpty());
		assertTrue("Teller has no account indices", mTeller.mAccountIndex.isEmpty());
		
		
		//Rejected Loan
	}
}

