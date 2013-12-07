package test;

import housing.interfaces.HousingBase;
import junit.framework.TestCase;
import market.test.mock.MockCustomer;
import bank.test.mock.MockTellerRole;
import base.ContactList;
import base.Event;
import base.Event.EnumEventType;
import base.PersonAgent;
import base.PersonAgent.EnumJobType;
import base.SortingHat;
import city.gui.CityPerson;

public class PersonAgentTest extends TestCase {

	PersonAgent mPerson;
	HousingBase mRenterRole;
	MockTellerRole mTellerRole;
	MockCustomer mCustomerRole;
	CityPerson mGui;
	
	public void setUp() throws Exception {
		super.setUp();
//		mPerson = new PersonAgent();
//		mPerson.semAnimationDone.release(1000);
//		SimCityGui.TESTING = true;
		//mGui = new MockPersonGui();
		//mPerson.setGui(mGui);
	}
	
	public void testShaneOne() throws Exception{
		setUp();
		ContactList.setup();
		SortingHat.InstantiateBaseRoles();

		
		int cash = 100;
		String name = "Dobby";
		mPerson = new PersonAgent(EnumJobType.MARKET, cash, name);
		mPerson.msgAddEvent(new Event(EnumEventType.EAT, 0));
		
		mPerson.msgTimeShift();
		mPerson.msgAnimationDone();
		mPerson.pickAndExecuteAnAction();
		mPerson.addCash(0);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void testOne_BasicSetUp () {
	//	setUp()
		
	//	Preconditions
//		assertTrue("Person has no events", mPerson.mEvents.isEmpty());
//		assertTrue("Person has no roles", mPerson.mRoles.isEmpty());
//		assertTrue("Person has no job", mPerson.mJobType == null);
//		assertTrue("Person has no cash", mPerson.getCash() == 0);
//		assertTrue("Person has no name", mPerson.getName() == null);
//		//assertTrue("Person SSN is 0. Instead: "+ mPerson.getSSN(), mPerson.getSSN() == 0);
//		assertTrue("Person has no loan", mPerson.getLoan() == 0);
//		//assertTrue("Person timeShift is 0", mPerson.getTimeShift() == 0);
//		assertTrue("Person has no housing role", mPerson.mHouseRole == null);
//		assertTrue("Role finished is true", mPerson.mRoleFinished == true);
//		assertTrue("Person is not at job", mPerson.mAtJob == false);
//		assertTrue("paea: return false", !mPerson.pickAndExecuteAnAction());
//		
//		//Set Time
//		Time.sGlobalShift = 0;
//		assertTrue("Time is currently 0", Time.GetShift() == 0);
//		Time.sGlobalDate = 0;
//		assertTrue("It is not weekend", !Time.IsWeekend());
//		
//		//Add Events
//		mPerson.mEvents.add(new Event(EnumEventType.JOB, 0));
//		assertTrue("Person has one event", mPerson.mEvents.size() == 1);
//		mPerson.mJobRole = mTellerRole;
//		assertTrue("Person has job role", mPerson.mJobRole != null);
//		mPerson.setName("Joe");
//		assertTrue("Person is named Joe", mPerson.getName().equals("Joe"));
		
	}
	
	public void testTwo_RoleSwitch(){
		//This test relies on the success of the first test and cannot be run alone. You must 
		//run both tests in order to successfully complete this test
		testOne_BasicSetUp();

		//Start of Test 2
//		mPerson.mJobLocation = new Location(-1,-1);
//		//paea: processEvent() -> goToJob()
//		assertTrue("paea: processEvent", mPerson.pickAndExecuteAnAction());
//		
//		assertTrue("New event in 24", mPerson.mEvents.first().mTime == 24);
//		assertTrue("Person is at job", mPerson.mAtJob == true);
////		assertTrue("Person is at location", mGui.log.containsString("DoGoToDestination"));
////		assertTrue("Person is not visible", mGui.log.containsString("setPresent: false"));
//		assertTrue("Person is set in job", mTellerRole.log.containsString("setPerson: Joe"));
//		assertTrue("Person has a role", mPerson.mRoles.size() == 1);
//		assertTrue("Person has active role", mPerson.mRoles.get(mTellerRole) == true);
//		
//		//Conditions for continual PAEA
//		assertTrue("Job role is set with person", mTellerRole.getPerson() != null);
//		
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		
//		//Role Switch
//		Time.sGlobalShift =2;
//		assertTrue("Time shift is currently 2", Time.GetShift() == 2);
//		assertTrue("Time is currently 0", Time.GetTime() == 0);
//		assertTrue("Your job shift is 1", mPerson.mTimeShift == 1);
//		mPerson.msgTimeShift();
//		
//		assertTrue("Person is not at job anymore", mPerson.mAtJob == false);
//		assertTrue("Role is finished", mPerson.mRoleFinished == true);
////		assertTrue("PersonGui is visible again", mGui.log.containsString("setPresent: true"));
//		
//		assertTrue("paea: return false (break)", !mPerson.pickAndExecuteAnAction());
//		
//		//New Event after Job
//		mPerson.mEvents.add(new Event(EnumEventType.GET_CAR, 0));
//		assertTrue("Person now has two events", mPerson.mEvents.size() == 2);
//		mPerson.mRoles.put(mCustomerRole, false);
//		assertTrue("Person now has two roles", mPerson.mRoles.size() == 2);
//		assertTrue("Desired items is empty", mPerson.mItemsDesired.isEmpty());
////		mGui.log.clear();
//		
//		//paea: processEvent() -> getCar()
//		assertTrue("paea: processEvent()", mPerson.pickAndExecuteAnAction());
//		
////		assertTrue("Person moved to location", mGui.log.containsString("DoGoToDestination"));
////		assertTrue("Person is invisible at location", mGui.log.containsString("setPresent: false"));
//		assertTrue("Role finished is false", mPerson.mRoleFinished == false);
//		assertTrue("Market Customer role is active", mPerson.mRoles.get(mCustomerRole) == true);
//		assertTrue("Person now has a car?", mPerson.mHasCar);
//		assertTrue("There is a car in desired items", mPerson.mItemsDesired.get(EnumItemType.CAR) == 1);
//		
//		//Conditions for continual PAEA
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		
//		
//		/*
//		mPerson.mEvents.add(new Event(EnumEventType.EAT, 0));
//		assertTrue("Person has two events: " + mPerson.mEvents.size(), mPerson.mEvents.size() == 2);
//		
//		//Paea: processEvent() -> eatFood()
//		assertTrue("paea: processEvent", mPerson.pickAndExecuteAnAction());
//		
//		assertTrue("Person still has two events", mPerson.mEvents.size() == 2);
//		//assertTrue("Person has eat event in 24", mPerson.mEvents.first().mEventType == EnumEventType.EAT);
//		assertTrue("Person has eat event in 24", mPerson.mEvents.first().mTime == 0);
//		//assertTrue("Person has eat event in 24", mPerson.mEvents.first().mLocation == null);	*/
	}

	public void testPersonLifecycle() {
//		mPerson = new PersonAgent(EnumJobType.RESTAURANT, 200, "TestPerson");
	}
	
//	public void testOne_BasicSetUp () {
//	//	setUp()
//		
//	//	Preconditions
//		assertTrue("Person has no events", mPerson.mEvents.isEmpty());
//		assertTrue("Person has no roles", mPerson.mRoles.isEmpty());
//		assertTrue("Person has no job", mPerson.mJobType == null);
//		assertTrue("Person has no cash", mPerson.getCash() == 0);
//		assertTrue("Person has no name", mPerson.getName() == null);
//		//assertTrue("Person SSN is 0. Instead: "+ mPerson.getSSN(), mPerson.getSSN() == 0);
//		assertTrue("Person has no loan", mPerson.getLoan() == 0);
//		//assertTrue("Person timeShift is 0", mPerson.getTimeShift() == 0);
//		assertTrue("Person has no housing role", mPerson.mHouseRole == null);
//		assertTrue("Role finished is true", mPerson.mRoleFinished == true);
//		assertTrue("Person is not at job", mPerson.mAtJob == false);
//		assertTrue("paea: return false", !mPerson.pickAndExecuteAnAction());
//		
//		//Set Time
//		Time.sGlobalShift = 0;
//		assertTrue("Time is currently 0", Time.GetShift() == 0);
//		Time.sGlobalDate = 0;
//		assertTrue("It is not weekend", !Time.IsWeekend());
//		
//		//Add Events
//		mPerson.mEvents.add(new Event(EnumEventType.JOB, 0));
//		assertTrue("Person has one event", mPerson.mEvents.size() == 1);
//		mPerson.mJobRole = mTellerRole;
//		assertTrue("Person has job role", mPerson.mJobRole != null);
//		mPerson.setName("Joe");
//		assertTrue("Person is named Joe", mPerson.getName().equals("Joe"));
//		
//	}
	
//	public void testTwo_RoleSwitch(){
//		//This test relies on the success of the first test and cannot be run alone. You must 
//		//run both tests in order to successfully complete this test
//		testOne_BasicSetUp();
//
//		//Start of Test 2
//		mPerson.mJobLocation = new Location(-1,-1);
//		//paea: processEvent() -> goToJob()
//		assertTrue("paea: processEvent", mPerson.pickAndExecuteAnAction());
//		
//		assertTrue("New event in 24", mPerson.mEvents.first().mTime == 24);
//		assertTrue("Person is at job", mPerson.mAtJob == true);
////		assertTrue("Person is at location", mGui.log.containsString("DoGoToDestination"));
////		assertTrue("Person is not visible", mGui.log.containsString("setPresent: false"));
//		assertTrue("Person is set in job", mTellerRole.log.containsString("setPerson: Joe"));
//		assertTrue("Person has a role", mPerson.mRoles.size() == 1);
//		assertTrue("Person has active role", mPerson.mRoles.get(mTellerRole) == true);
//		
//		//Conditions for continual PAEA
//		assertTrue("Job role is set with person", mTellerRole.getPerson() != null);
//		
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		
//		//Role Switch
//		Time.sGlobalShift =2;
//		assertTrue("Time shift is currently 2", Time.GetShift() == 2);
//		assertTrue("Time is currently 0", Time.GetTime() == 0);
//		assertTrue("Your job shift is 1", mPerson.mTimeShift == 1);
//		mPerson.msgTimeShift();
//		
//		assertTrue("Person is not at job anymore", mPerson.mAtJob == false);
//		assertTrue("Role is finished", mPerson.mRoleFinished == true);
////		assertTrue("PersonGui is visible again", mGui.log.containsString("setPresent: true"));
//		
//		assertTrue("paea: return false (break)", !mPerson.pickAndExecuteAnAction());
//		
//		//New Event after Job
//		mPerson.mEvents.add(new Event(EnumEventType.GET_CAR, 0));
//		assertTrue("Person now has two events", mPerson.mEvents.size() == 2);
//		mPerson.mRoles.put(mCustomerRole, false);
//		assertTrue("Person now has two roles", mPerson.mRoles.size() == 2);
//		assertTrue("Desired items is empty", mPerson.mItemsDesired.isEmpty());
////		mGui.log.clear();
//		
//		//paea: processEvent() -> getCar()
//		assertTrue("paea: processEvent()", mPerson.pickAndExecuteAnAction());
//		
////		assertTrue("Person moved to location", mGui.log.containsString("DoGoToDestination"));
////		assertTrue("Person is invisible at location", mGui.log.containsString("setPresent: false"));
//		assertTrue("Role finished is false", mPerson.mRoleFinished == false);
//		assertTrue("Market Customer role is active", mPerson.mRoles.get(mCustomerRole) == true);
//		assertTrue("Person now has a car?", mPerson.mHasCar);
//		assertTrue("There is a car in desired items", mPerson.mItemsDesired.get(EnumItemType.CAR) == 1);
//		
//		//Conditions for continual PAEA
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
//		
//		
//		/*
//		mPerson.mEvents.add(new Event(EnumEventType.EAT, 0));
//		assertTrue("Person has two events: " + mPerson.mEvents.size(), mPerson.mEvents.size() == 2);
//		
//		//Paea: processEvent() -> eatFood()
//		assertTrue("paea: processEvent", mPerson.pickAndExecuteAnAction());
//		
//		assertTrue("Person still has two events", mPerson.mEvents.size() == 2);
//		//assertTrue("Person has eat event in 24", mPerson.mEvents.first().mEventType == EnumEventType.EAT);
//		assertTrue("Person has eat event in 24", mPerson.mEvents.first().mTime == 0);
//		//assertTrue("Person has eat event in 24", mPerson.mEvents.first().mLocation == null);	*/
//	}
	
	
	
}
