package bank.test;

import test.mock.MockPerson;
import junit.framework.TestCase;
import bank.Account;
import bank.roles.BankMasterTellerRole;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;


public class MasterTellerTest extends TestCase{
	
//	INTERFACES
	BankMasterTellerRole mMasterTeller;
	Person mPerson;
	MockPerson mPerson1;
	MockPerson mPerson2;
	MockPerson mPerson3;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		
		//Testee
		mMasterTeller = new BankMasterTellerRole();
		mPerson = new PersonAgent("Person");
		mPerson.addRole((Role)mMasterTeller, true);
		
		//Interfaces
		mPerson1 = new MockPerson("Person 1");
		mPerson2 = new MockPerson("Person 2");
		mPerson3 = new MockPerson("Person 3");
		
		//clear logs
	}
	
//	TESTS
	
	public void testOne_OneTransaction(){
		//setUp()
		
		//Preconditions
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has no accounts", mMasterTeller.mAccounts.isEmpty());
		assertTrue("MT has no account indices", mMasterTeller.mAccountIndex.isEmpty());
		
		//1 : add accounts (name, loan, balance)
		mMasterTeller.mAccounts.add(new Account("Person 1", 0, 10)); 
		mMasterTeller.mAccountIndex.put(1, mMasterTeller.mAccounts.size()-1);
		mMasterTeller.mAccounts.add(new Account("Person 2", 0, 20));
		mMasterTeller.mAccountIndex.put(2, mMasterTeller.mAccounts.size()-1);
		mMasterTeller.mAccounts.add(new Account("Person 3", 0, 30));
		mMasterTeller.mAccountIndex.put(3, mMasterTeller.mAccounts.size()-1);
		
		//Check
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has three accounts", mMasterTeller.mAccounts.size() == 3);
		assertTrue("MT has three account indices", mMasterTeller.mAccountIndex.size() == 3);
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
		assertTrue("Person 1 has balance of 10", mMasterTeller.mAccounts.get(0).balance == 10);
		assertTrue("Person 2 has balance of 20", mMasterTeller.mAccounts.get(1).balance == 20);
		assertTrue("Person 3 has balance of 30", mMasterTeller.mAccounts.get(2).balance == 30);
		
		//2 : add one transaction (sender, receiver, amount)
		mMasterTeller.msgSendPayment(1, 2, 5);
		
		//Check
		assertTrue("MT has one transaction", mMasterTeller.mTransactions.size() == 1);
		
		//3 : p.a.e.a. (processTransaction(t))
		assertTrue("PAEA: processTransaction(t)", mMasterTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
		assertTrue("Person 1 has balance of 5", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(1)).balance == 5);
		assertTrue("Person 2 has balance of 25", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(2)).balance == 25);
		assertTrue("Person 3 has balance of 30", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(3)).balance == 30);
		
		//4 : message receiver (usually called from processTransaction) (sender, amount)
		mPerson2.msgHereIsPayment(1, 5);
		
		//Check
		assertTrue("Person 2 received payment method", mPerson2.log.containsString("Received 5 from 1"));
	}
	
	public void testTwo_MultipleTransactions(){
		//setUp()
		
		//Preconditions
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has no accounts", mMasterTeller.mAccounts.isEmpty());
		assertTrue("MT has no account indices", mMasterTeller.mAccountIndex.isEmpty());
		
		//1 : add accounts (name, loan, balance)
		mMasterTeller.mAccounts.add(new Account("Person 1", 0, 10)); 
		mMasterTeller.mAccountIndex.put(1, mMasterTeller.mAccounts.size()-1);
		mMasterTeller.mAccounts.add(new Account("Person 2", 0, 20));
		mMasterTeller.mAccountIndex.put(2, mMasterTeller.mAccounts.size()-1);
		mMasterTeller.mAccounts.add(new Account("Person 3", 0, 30));
		mMasterTeller.mAccountIndex.put(3, mMasterTeller.mAccounts.size()-1);
		
		//Check
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has three accounts", mMasterTeller.mAccounts.size() == 3);
		assertTrue("MT has three account indices", mMasterTeller.mAccountIndex.size() == 3);
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
		assertTrue("Person 1 has balance of 10", mMasterTeller.mAccounts.get(0).balance == 10);
		assertTrue("Person 2 has balance of 20", mMasterTeller.mAccounts.get(1).balance == 20);
		assertTrue("Person 3 has balance of 30", mMasterTeller.mAccounts.get(2).balance == 30);
		
		//2 : add multiple transactions (sender, receiver, amount)
		mMasterTeller.msgSendPayment(1, 2, 5);
		mMasterTeller.msgSendPayment(2, 3, 10);
		mMasterTeller.msgSendPayment(3, 1, 20);
		
		//Check
		assertTrue("MT has three transactions", mMasterTeller.mTransactions.size() == 3);
		
		//3 : p.a.e.a. (processTransaction(t))
		assertTrue("PAEA: processTransaction(t)", mMasterTeller.pickAndExecuteAnAction());
		assertTrue("PAEA: processTransaction(t)", mMasterTeller.pickAndExecuteAnAction());
		assertTrue("PAEA: processTransaction(t)", mMasterTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
		assertTrue("Person 1 has balance of 25", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(1)).balance == 25);
		assertTrue("Person 2 has balance of 15", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(2)).balance == 15);
		assertTrue("Person 3 has balance of 20", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(3)).balance == 20);
		
		//4 : message receiver (usually called from processTransaction) (sender, amount)
		mPerson2.msgHereIsPayment(1, 5);
		mPerson3.msgHereIsPayment(2, 10);
		mPerson1.msgHereIsPayment(3, 20);
		
		//Check
		assertTrue("Person 2 received payment message", mPerson2.log.containsString("Received 5 from 1"));
		assertTrue("Person 3 received payment message", mPerson3.log.containsString("Received 10 from 2"));
		assertTrue("Person 1 received payment message", mPerson1.log.containsString("Received 20 from 3"));
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
	}
	
	public void testThree_Interweaving(){
		//setUp()
		
		//Preconditions
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has no accounts", mMasterTeller.mAccounts.isEmpty());
		assertTrue("MT has no account indices", mMasterTeller.mAccountIndex.isEmpty());
		
		//1 : add accounts (name, loan, balance)
		mMasterTeller.mAccounts.add(new Account("Person 1", 0, 10)); 
		mMasterTeller.mAccountIndex.put(1, mMasterTeller.mAccounts.size()-1);
		
		//Check
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has one accounts", mMasterTeller.mAccounts.size() == 1);
		assertTrue("MT has one account index", mMasterTeller.mAccountIndex.size() == 1);
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
		assertTrue("Person 1 has balance of 10", mMasterTeller.mAccounts.get(0).balance == 10);
		
		//2 : add one transaction (sender, receiver, amount)
		mMasterTeller.msgSendPayment(1, 2, 5);
		
		//Check
		assertTrue("MT has one transaction", mMasterTeller.mTransactions.size() == 1);
		
		//3 : add new accounts
		mMasterTeller.mAccounts.add(new Account("Person 2", 0, 20));
		mMasterTeller.mAccountIndex.put(2, mMasterTeller.mAccounts.size()-1);
		mMasterTeller.mAccounts.add(new Account("Person 3", 0, 30));
		mMasterTeller.mAccountIndex.put(3, mMasterTeller.mAccounts.size()-1);
		
		//Check
		assertTrue("MT has one transactions", mMasterTeller.mTransactions.size() == 1);
		assertTrue("MT has three accounts", mMasterTeller.mAccounts.size() == 3);
		assertTrue("MT has three account indices", mMasterTeller.mAccountIndex.size() == 3);
		assertTrue("Person 1 has balance of 10", mMasterTeller.mAccounts.get(0).balance == 10);
		assertTrue("Person 2 has balance of 20", mMasterTeller.mAccounts.get(1).balance == 20);
		assertTrue("Person 3 has balance of 30", mMasterTeller.mAccounts.get(2).balance == 30);
		
		//4 : p.a.e.a. (processTransaction(t))
		assertTrue("PAEA: processTransaction(t)", mMasterTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
		assertTrue("Person 1 has balance of 5", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(1)).balance == 5);
		assertTrue("Person 2 has balance of 25", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(2)).balance == 25);
		assertTrue("Person 3 has balance of 30", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(3)).balance == 30);
		
		//4 : message receiver (usually called from processTransaction) (sender, amount)
		mPerson2.msgHereIsPayment(1, 5);
		
		//Check
		assertTrue("Person 2 received payment method", mPerson2.log.containsString("Received 5 from 1"));
		
	}
}

