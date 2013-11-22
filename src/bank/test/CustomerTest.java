package bank.test;

import junit.framework.TestCase;
import bank.Action;
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
	PersonAgent mPerson;
	MockGuardRole mGuard;
	MockTellerRole mTeller;

	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		
		//Testee
		mCustomer = new BankCustomerRole();
		mPerson = new PersonAgent();
		mPerson.addRole((Role)mCustomer, true);
		mPerson.setSSN(1);
		mPerson.setName("Joe");
		
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
		mCustomer.mActions.add(new Action(EnumAction.Open, 100));
		
		//Check
		assertTrue("Customer guard is set", mCustomer.mGuard != null);
		assertTrue("Customer has one action", mCustomer.mActions.size() == 1);
		
		//2 : p.a.e.a. (waitInLine())
		assertTrue("PAEA: waitInLine()", mCustomer.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Customer state is waiting", mCustomer.mState == EnumState.Waiting);
		assertTrue("Customer event is none", mCustomer.mEvent == EnumEvent.None);
		assertTrue("Guard received msgNeedService", mGuard.log.containsString("msgNeedService"));
		assertTrue("PAEA: return false", !mCustomer.pickAndExecuteAnAction());
		
		//3 : assign teller (from guard)
		mCustomer.msgGoToTeller(mTeller);
		
		//Check
		assertTrue("Customer has a teller", mCustomer.mTeller == mTeller);
		assertTrue("Customer event is assigned", mCustomer.mEvent == EnumEvent.Assigned);
		
		//4 : p.a.e.a. (goToTeller())
		assertTrue("PAEA: goToTeller()", mCustomer.pickAndExecuteAnAction());
		
		//Check
		assertTrue("Customer state is moving", mCustomer.mState == EnumState.Moving);
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
		mTeller.log.getLastLoggedEvent().toString(), mTeller.log.containsString("msgOpen: 1 100 Joe"));
		//REX JERRY: get time is called and logged in mock teller. ???.
		
		
	}
	
	public void testTwo_TwoCustomers(){
		//setUp()
		
		//Preconditions
		
	}
	
	public void testThree_OneRobber(){
		//setUp()
		
		//Preconditions
		
	}
}

