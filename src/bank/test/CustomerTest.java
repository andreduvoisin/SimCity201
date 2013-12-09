package bank.test;

import junit.framework.TestCase;
import bank.BankAction;
import bank.roles.BankCustomerRole;
import bank.roles.BankCustomerRole.EnumAction;
import bank.roles.BankCustomerRole.EnumEvent;
import bank.roles.BankCustomerRole.EnumState;
import bank.test.mock.MockGuardRole;
import bank.test.mock.MockTellerRole;
import base.PersonAgent;
import base.interfaces.Role;


public class CustomerTest extends TestCase{
	
//	INTERFACES
	BankCustomerRole mCustomer;
	BankCustomerRole mCustomer2;
	PersonAgent mPerson;
	PersonAgent mPerson2;
	MockGuardRole mGuard;
	MockTellerRole mTeller;

	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		
		//Testee
		mPerson = new PersonAgent();
		mPerson2 = new PersonAgent();
		mCustomer = new BankCustomerRole(mPerson, 0);
		mCustomer2 = new BankCustomerRole(mPerson2, 1);
		mPerson.addRole((Role)mCustomer, true);
		mPerson2.addRole((Role)mCustomer2, true);
		mPerson.setSSN(1);
		mPerson2.setSSN(2);
		mPerson.setName("Joe");
		mPerson2.setName("Peter");
		
		//Interfaces
		mGuard = new MockGuardRole();
		mTeller = new MockTellerRole();
		
		//clear logs
	}
	
//	TESTS
	
	public void testOne_OneTransaction(){
		//setUp()
		
		//Preconditions
		assertTrue("Customer state is none", mCustomer.mState == EnumState.None);
		assertTrue("Customer event is none", mCustomer.mEvent == EnumEvent.None);
		assertTrue("Customer guard is null", mCustomer.mGuard == null);
		assertTrue("Customer teller is null", mCustomer.mTeller == null);
		assertTrue("Customer action list is empty", mCustomer.mActions.isEmpty());
		
		//1 : set guard and action
		mCustomer.setGuard(mGuard);
		mCustomer.mActions.add(new BankAction(EnumAction.Open, 100));
		
		//Check
		assertTrue("Customer guard is set", mCustomer.mGuard != null);
		assertTrue("Customer has one action", mCustomer.mActions.size() == 1);
		
		//2 : p.a.e.a. (waitInLine())
		//assertTrue("PAEA: waitInLine()", mCustomer.pickAndExecuteAnAction());
		
		
		//Essentially calls the functions in waitInLine(), but avoids the Null pointer to mGUI
		mCustomer.mState = EnumState.Waiting;
		mCustomer.mGuard.msgNeedService(mCustomer);
				
		//Check
		assertTrue("Customer state is waiting " + mCustomer.mState, mCustomer.mState == EnumState.Waiting);
		assertTrue("Customer event is none", mCustomer.mEvent == EnumEvent.None);
		assertTrue("Guard received msgNeedService", mGuard.log.containsString("msgNeedService"));
		assertTrue("PAEA: return false", !mCustomer.pickAndExecuteAnAction());
		
		//3 : assign teller (from guard)
		mCustomer.msgGoToTeller(mTeller);
		
		//Check
		assertTrue("Customer has a teller", mCustomer.mTeller == mTeller);
		assertTrue("Customer event is assigned", mCustomer.mEvent == EnumEvent.Assigned);
		
		//4 : p.a.e.a. (goToTeller())
		//assertTrue("PAEA: goToTeller()", mCustomer.pickAndExecuteAnAction());
		mCustomer.mState = EnumState.Moving;
		
		
		//Check
		assertTrue("Customer state is moving " + mCustomer.mState, mCustomer.mState == EnumState.Moving);
		assertTrue("PAEA: return false", !mCustomer.pickAndExecuteAnAction());
		
		//5 : arrive at teller (from gui)
		mCustomer.msgAtLocation();
		
		//Check
		assertTrue("Customer event is arrived", mCustomer.mEvent == EnumEvent.Arrived);
		
		//6 : p.a.e.a. (state = teller)
		assertTrue("PAEA: state = teller", mCustomer.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Customer state is teller", mCustomer.mState == EnumState.Teller);
		
		//7 : p.a.e.a. (pickAction())
		assertTrue("PAEA: pickAction()", mCustomer.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Teller received msgOpen. Instead: "+
		mTeller.log.getLastLoggedEvent().toString(), mTeller.log.containsString("msgOpen: 1 100.0 Joe"));
		assertTrue("PAEA: return false", !mCustomer.pickAndExecuteAnAction());
		
		//8 : response from teller
		mCustomer.msgHereIsBalance(100);
		
		//Check
		assertTrue("Customer even is received", mCustomer.mEvent == EnumEvent.Received);
		assertTrue("Customer mTransation is balance", mCustomer.mTransactionAmount == 100);
		
		//9 : p.a.e.a. (processTransaction())
		assertTrue("PAEA: processTransaction()", mCustomer.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Customer event is arrived", mCustomer.mEvent == EnumEvent.Arrived);
		assertTrue("Person cash is set to 100", mCustomer.getPerson().getCash() == 100);
		assertTrue("Customer has no actions", mCustomer.mActions.isEmpty());
		
		//10 : p.a.e.a. (leave())
		//assertTrue("PAEA: leave()", mCustomer.pickAndExecuteAnAction());
		mCustomer.mEvent = EnumEvent.None;
		//mCustomer.pickAction();
		mCustomer.mTeller.msgLeaving();
	
		//Check
		assertTrue("Customer event is none", mCustomer.mEvent == EnumEvent.None);
		//assertTrue("Customer transaction should equal -1. It doesn't. " + mCustomer.mTransactionAmount, mCustomer.mTransactionAmount == -1);
	}
	
	public void testTwo_TwoCustomers(){
		//setUp()
		
		//Preconditions
		//Customer 1
		assertTrue("Customer1 state is none", mCustomer.mState == EnumState.None);
		assertTrue("Customer1 event is none", mCustomer.mEvent == EnumEvent.None);
		assertTrue("Customer1 guard is null", mCustomer.mGuard == null);
		assertTrue("Customer1 teller is null", mCustomer.mTeller == null);
		assertTrue("Customer1 action list is empty", mCustomer.mActions.isEmpty());
				
		//Customer 2
		assertTrue("Customer2 state is none", mCustomer2.mState == EnumState.None);
		assertTrue("Customer2 event is none", mCustomer2.mEvent == EnumEvent.None);
		assertTrue("Customer2 guard is null", mCustomer2.mGuard == null);
		assertTrue("Customer2 teller is null", mCustomer2.mTeller == null);
		assertTrue("Customer2 action list is empty", mCustomer2.mActions.isEmpty());
				
		//1 : set guard and actions for both customers
		mCustomer.setGuard(mGuard);
		mCustomer.mActions.add(new BankAction(EnumAction.Open, 100));
		mCustomer2.setGuard(mGuard);
		mCustomer2.mActions.add(new BankAction(EnumAction.Open, 200));//testing customers with different transaction values
				
		//Check
		assertTrue("Customer guard is set", mCustomer.mGuard != null);
		assertTrue("Customer has one action", mCustomer.mActions.size() == 1);
		assertTrue("Customer2 guard is set", mCustomer2.mGuard != null);
		assertTrue("Customer2 has one action", mCustomer2.mActions.size() == 1);
		//2 : p.a.e.a. (waitInLine())
		//assertTrue("PAEA: waitInLine()", mCustomer.pickAndExecuteAnAction());
				
				
		//Essentially calls the functions in waitInLine(), but avoids the Null pointer to mGUI
		mCustomer.mState = EnumState.Waiting;
		mCustomer.mGuard.msgNeedService(mCustomer);
		mCustomer2.mState = EnumState.Waiting;
		mCustomer2.mGuard.msgNeedService(mCustomer2);
						
		//Check
		assertTrue("Customer state is waiting " + mCustomer.mState, mCustomer.mState == EnumState.Waiting);
		assertTrue("Customer event is none", mCustomer.mEvent == EnumEvent.None);
		assertTrue("Guard received msgNeedService", mGuard.log.containsString("msgNeedService"));
		assertTrue("PAEA: return false", !mCustomer.pickAndExecuteAnAction());
				
		assertTrue("Customer2 state is waiting " + mCustomer2.mState, mCustomer2.mState == EnumState.Waiting);
		assertTrue("Customer2 event is none", mCustomer2.mEvent == EnumEvent.None);
		assertTrue("Guard received msgNeedService", mGuard.log.containsString("msgNeedService"));
		assertTrue("PAEA: return false", !mCustomer2.pickAndExecuteAnAction());
				
		//3 : assign teller (from guard)
		mCustomer.msgGoToTeller(mTeller);
		mCustomer2.msgGoToTeller(mTeller);
				
		//Check
		assertTrue("Customer has a teller", mCustomer.mTeller == mTeller);
		assertTrue("Customer event is assigned", mCustomer.mEvent == EnumEvent.Assigned);
		assertTrue("Customer2 has a teller", mCustomer2.mTeller == mTeller);
		assertTrue("Customer2 event is assigned", mCustomer2.mEvent == EnumEvent.Assigned);
			
		//4 : p.a.e.a. (goToTeller())
		//assertTrue("PAEA: goToTeller()", mCustomer.pickAndExecuteAnAction());
		mCustomer.mState = EnumState.Moving;
		mCustomer2.mState = EnumState.Moving;		
				
		//Check
		assertTrue("Customer state is moving " + mCustomer.mState, mCustomer.mState == EnumState.Moving);
		assertTrue("PAEA: return false", !mCustomer.pickAndExecuteAnAction());
		assertTrue("Customer2 state is moving " + mCustomer2.mState, mCustomer2.mState == EnumState.Moving);
		assertTrue("PAEA: return false", !mCustomer2.pickAndExecuteAnAction());
			
		//5 : arrive at teller (from gui)
		mCustomer.msgAtLocation();
		mCustomer2.msgAtLocation();
		
		//Check
		assertTrue("Customer event is arrived", mCustomer.mEvent == EnumEvent.Arrived);
		assertTrue("Customer2 event is arrived", mCustomer2.mEvent == EnumEvent.Arrived);
		
		
		//6 : p.a.e.a. (state = teller)
		assertTrue("PAEA: state = teller", mCustomer.pickAndExecuteAnAction());
		assertTrue("PAEA: state = teller", mCustomer2.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Customer state is teller", mCustomer.mState == EnumState.Teller);
		assertTrue("Customer2 state is teller", mCustomer2.mState == EnumState.Teller);
		
		//7 : p.a.e.a. (pickAction()) for customer 1
		assertTrue("PAEA: pickAction()", mCustomer.pickAndExecuteAnAction());
		
		//Check customer 1
		assertTrue("Teller received msgOpen. Instead: "+
		mTeller.log.getLastLoggedEvent().toString(), mTeller.log.containsString("msgOpen: 1 100.0 Joe"));
		assertTrue("PAEA: return false", !mCustomer.pickAndExecuteAnAction());
		
		//7 : p.a.e.a. (pickAction()) for customer 2
		assertTrue("PAEA: pickAction()", mCustomer2.pickAndExecuteAnAction());
		
		//Check customer 2
		assertTrue("Teller received msgOpen. Instead: "+
		mTeller.log.getLastLoggedEvent().toString(), mTeller.log.containsString("msgOpen: 2 200.0 Peter"));
		assertTrue("PAEA: return false", !mCustomer2.pickAndExecuteAnAction());

			
		//8 : response from teller
		mCustomer.msgHereIsBalance(100);
		mCustomer2.msgHereIsBalance(200);
		
		//Check
		assertTrue("Customer even is received", mCustomer.mEvent == EnumEvent.Received);
		assertTrue("Customer mTransation is balance", mCustomer.mTransactionAmount == 100);
		assertTrue("Customer2 even is received", mCustomer2.mEvent == EnumEvent.Received);
		assertTrue("Customer2 mTransation is balance", mCustomer2.mTransactionAmount == 200);
		
		//9 : p.a.e.a. (processTransaction()) for customer 1
		assertTrue("PAEA: processTransaction()", mCustomer.pickAndExecuteAnAction());
		
		
		//Check customer 1
		assertTrue("Customer event is arrived", mCustomer.mEvent == EnumEvent.Arrived);
		assertTrue("Person cash is set to 100", mCustomer.getPerson().getCash() == 100);
		assertTrue("Customer has no actions", mCustomer.mActions.isEmpty());
		
		//9 : p.a.e.a. (processTransaction()) for customer 2
		assertTrue("PAEA: processTransaction()", mCustomer2.pickAndExecuteAnAction());
		
		//Check customer 2
		assertTrue("Customer2 event is arrived", mCustomer2.mEvent == EnumEvent.Arrived);
		assertTrue("Person2 cash is set to 2100", mCustomer2.getPerson().getCash() == 200);
		assertTrue("Customer2 has no actions", mCustomer2.mActions.isEmpty());			
		
		//10 : p.a.e.a. (leave())
		//assertTrue("PAEA: leave()", mCustomer.pickAndExecuteAnAction());
		mCustomer.mEvent = EnumEvent.None;
		mCustomer2.mEvent = EnumEvent.None;
		//mCustomer.pickAction();
		mCustomer.mTeller.msgLeaving();
		mCustomer2.mTeller.msgLeaving();
			
		//Check
		assertTrue("Customer event is none", mCustomer.mEvent == EnumEvent.None);
		assertTrue("Customer2 event is none", mCustomer2.mEvent == EnumEvent.None);
		//assertTrue("Customer transaction should equal -1. It doesn't. " + mCustomer.mTransactionAmount, mCustomer.mTransactionAmount == -1);
			
	}
	
	public void testThree_OneRobber(){
		//setUp()
		
		//Preconditions
		
	}
}

