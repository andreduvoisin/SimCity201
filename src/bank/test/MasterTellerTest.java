package bank.test;

import junit.framework.TestCase;
import test.mock.MockPerson;
import bank.BankAccount;
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
	PersonAgent mp1;
	PersonAgent mp2;
	PersonAgent mp3;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		
		//Testee
		mPerson = new PersonAgent();
		mMasterTeller = new BankMasterTellerRole(mPerson);
		mPerson.addRole((Role)mMasterTeller, true);
		
		//Interfaces
		mPerson1 = new MockPerson("Person 1");
		mPerson2 = new MockPerson("Person 2");
		mPerson3 = new MockPerson("Person 3");
		
		mp1 = new PersonAgent();
		mp2 = new PersonAgent();
		mp3 = new PersonAgent();
		
		mPerson1.setPerson(mp1);
		mPerson2.setPerson(mp2);
		mPerson3.setPerson(mp3);
		
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
		mMasterTeller.mAccounts.add(new BankAccount(0, 10, (Person)mPerson1)); 
		mMasterTeller.mAccountIndex.put(1, mMasterTeller.mAccounts.size()-1);
		mMasterTeller.mAccounts.add(new BankAccount(0, 20, (Person)mPerson2));
		mMasterTeller.mAccountIndex.put(2, mMasterTeller.mAccounts.size()-1);
		mMasterTeller.mAccounts.add(new BankAccount(0, 30, (Person)mPerson3));
		mMasterTeller.mAccountIndex.put(3, mMasterTeller.mAccounts.size()-1);
		
		//Check
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has three accounts", mMasterTeller.mAccounts.size() == 3);
		assertTrue("MT has three account indices", mMasterTeller.mAccountIndex.size() == 3);
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
		assertTrue("Person 1 has balance of 10", mMasterTeller.mAccounts.get(0).balance == 10);
		assertTrue("Person 2 has balance of 20", mMasterTeller.mAccounts.get(1).balance == 20);
		assertTrue("Person 3 has balance of 30", mMasterTeller.mAccounts.get(2).balance == 30);
		assertTrue("Account 1 has a person agent", mMasterTeller.mAccounts.get(0).person == mPerson1);
		assertTrue("Account 2 has a person agent", mMasterTeller.mAccounts.get(1).person == mPerson2);
		assertTrue("Account 3 has a person agent", mMasterTeller.mAccounts.get(2).person == mPerson3);
		
		
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
		//mPerson2.msgHereIsPayment(1, 5);
		
		//Check
		assertTrue("Person 2 received payment method", mPerson2.log.containsString("SenderSSN: 1. Amount received: 5.0"));
	}
	
	public void testTwo_MultipleTransactions(){
		//setUp()
		
		//Preconditions
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has no accounts", mMasterTeller.mAccounts.isEmpty());
		assertTrue("MT has no account indices", mMasterTeller.mAccountIndex.isEmpty());
		
		//1 : add accounts (name, loan, balance)
		mMasterTeller.mAccounts.add(new BankAccount(0, 10, (Person)mPerson1)); 
		mMasterTeller.mAccountIndex.put(1, mMasterTeller.mAccounts.size()-1);
		mMasterTeller.mAccounts.add(new BankAccount(0, 20, (Person)mPerson2));
		mMasterTeller.mAccountIndex.put(2, mMasterTeller.mAccounts.size()-1);
		mMasterTeller.mAccounts.add(new BankAccount(0, 30, (Person)mPerson3));
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
		/*mPerson2.msgHereIsPayment(1, 5);
		mPerson3.msgHereIsPayment(2, 10);
		mPerson1.msgHereIsPayment(3, 20);*/
		
		//Check
		assertTrue("Person 2 received payment message. Instead: "+
					mPerson2.log.getLastLoggedEvent().toString(), 
					mPerson2.log.containsString("SenderSSN: 1. Amount received: 5.0"));
		assertTrue("Person 3 received payment message", mPerson3.log.containsString("SenderSSN: 2. Amount received: 10.0"));
		assertTrue("Person 1 received payment message", mPerson1.log.containsString("SenderSSN: 3. Amount received: 20.0"));
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
	}
	
	public void testThree_Interweaving(){
		//setUp()
		
		//Preconditions
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has no accounts", mMasterTeller.mAccounts.isEmpty());
		assertTrue("MT has no account indices", mMasterTeller.mAccountIndex.isEmpty());
		
		//1 : add accounts (name, loan, balance)
		mMasterTeller.mAccounts.add(new BankAccount(0, 10, (Person)mPerson1)); 
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
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).sender == 1);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).receiver == 2);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).amount == 5);
		
		
		//3 : add new accounts
		mMasterTeller.mAccounts.add(new BankAccount(0, 20, (Person) mPerson2));
		mMasterTeller.mAccountIndex.put(2, mMasterTeller.mAccounts.size()-1);
		mMasterTeller.mAccounts.add(new BankAccount(0, 30, (Person) mPerson3));
		mMasterTeller.mAccountIndex.put(3, mMasterTeller.mAccounts.size()-1);
		
		//Check
		assertTrue("MT has one transactions", mMasterTeller.mTransactions.size() == 1);
		assertTrue("MT has three accounts", mMasterTeller.mAccounts.size() == 3);
		assertTrue("MT has three account indices", mMasterTeller.mAccountIndex.size() == 3);
		assertTrue("Person 1 has balance of 10", mMasterTeller.mAccounts.get(0).balance == 10);
		assertTrue("Person 2 has balance of 20", mMasterTeller.mAccounts.get(1).balance == 20);
		assertTrue("Person 3 has balance of 30", mMasterTeller.mAccounts.get(2).balance == 30);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).sender == 1);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).receiver == 2);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).amount == 5);
		
		//4 : p.a.e.a. (processTransaction(t))
		assertTrue("PAEA: processTransaction(t)", mMasterTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
		assertTrue("Person 1 has balance of 5. Instead: "+
				mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(1)).balance,
				mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(1)).balance == 5);
		assertTrue("Person 2 has balance of 25", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(2)).balance == 25);
		assertTrue("Person 3 has balance of 30", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(3)).balance == 30);
		
		//4 : message receiver (called from processTransaction) (sender, amount)
		
		//Check
		assertTrue("Person 2 received payment method. Instead: "+
					mPerson2.log.getLastLoggedEvent().toString(), 
					mPerson2.log.containsString("SenderSSN: 1. Amount received: 5.0"));
		
	}
	
	public void testFour_Overdrawn(){
		//setUp()
		
		//Preconditions
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has no accounts", mMasterTeller.mAccounts.isEmpty());
		assertTrue("MT has no account indices", mMasterTeller.mAccountIndex.isEmpty());
		
		//1 : add accounts (name, loan, balance)
		mMasterTeller.mAccounts.add(new BankAccount(0, 10, (Person)mPerson1)); 
		mMasterTeller.mAccountIndex.put(1, mMasterTeller.mAccounts.size()-1);
		
		//Check
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("MT has one accounts", mMasterTeller.mAccounts.size() == 1);
		assertTrue("MT has one account index", mMasterTeller.mAccountIndex.size() == 1);
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
		assertTrue("Person 1 has balance of 10", mMasterTeller.mAccounts.get(0).balance == 10);
		
		//2 : add one transaction (sender, receiver, amount)
		mMasterTeller.msgSendPayment(1, 2, 15);
		
		//Check
		assertTrue("MT has one transaction", mMasterTeller.mTransactions.size() == 1);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).sender == 1);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).receiver == 2);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).amount == 15);
		
		
		//3 : add new accounts
		mMasterTeller.mAccounts.add(new BankAccount(0, 20, (Person) mPerson2));
		mMasterTeller.mAccountIndex.put(2, mMasterTeller.mAccounts.size()-1);
		
		//Check
		assertTrue("MT has one transactions", mMasterTeller.mTransactions.size() == 1);
		assertTrue("MT has two accounts", mMasterTeller.mAccounts.size() == 2);
		assertTrue("MT has two account indices", mMasterTeller.mAccountIndex.size() == 2);
		assertTrue("Person 1 has balance of 10", mMasterTeller.mAccounts.get(0).balance == 10);
		assertTrue("Person 2 has balance of 20", mMasterTeller.mAccounts.get(1).balance == 20);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).sender == 1);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).receiver == 2);
		assertTrue("MT transaction is valid", mMasterTeller.mTransactions.get(0).amount == 15);
		
		//4 : p.a.e.a. (processTransaction(t))
		assertTrue("PAEA: processTransaction(t)", mMasterTeller.pickAndExecuteAnAction());
		
		//Check
		assertTrue("MT has no transactions", mMasterTeller.mTransactions.isEmpty());
		assertTrue("PAEA: return false", !mMasterTeller.pickAndExecuteAnAction());
		assertTrue("Person 1 has balance of 0", mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(1)).balance == 0);
		assertTrue("Person 1 has loan of 5. Instead: "+
					mMasterTeller.mAccounts.get(0).loan, 
					mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(1)).loan == 5);
		assertTrue("Person 2 has balance of 35. Instead: "+
					mMasterTeller.mAccounts.get(1).balance, 
					mMasterTeller.mAccounts.get(mMasterTeller.mAccountIndex.get(2)).balance == 35);
		
		//4a: message receiver  (HereIsPayment called from processTransaction) (sender, amount)
		//4b: message sender 	(OverdrawnAccount called from processTransaction) (excess)
		
		//Check
		assertTrue("Person 2 received payment method. Instead: "+
					mPerson2.log.getLastLoggedEvent().toString(), 
					mPerson2.log.containsString("SenderSSN: 1. Amount received: 15.0"));
		assertTrue("Person 1 received overdrawn message", mPerson1.log.containsString("Loan amount: 5.0"));
	}
	
	
	
	
}

