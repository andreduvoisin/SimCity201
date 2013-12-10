package test;

import housing.interfaces.HousingBase;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import market.test.mock.MockCustomer;
import bank.test.mock.MockTellerRole;
import base.ContactList;
import base.Event;
import base.Event.EnumEventType;
import base.PersonAgent;
import base.PersonAgent.EnumJobType;
import base.SortingHat;
import base.Time;
import city.gui.CityPerson;

public class PersonAgentTest extends TestCase {

	PersonAgent mPerson;
	PersonAgent mPerson2;
	PersonAgent mPerson3;
	HousingBase mRenterRole;
	MockTellerRole mTellerRole;
	MockCustomer mCustomerRole;
	CityPerson mGui;
	List<PersonAgent> personList = new ArrayList<PersonAgent>();
	public static Time time;
	
	public void setUp() throws Exception {
		super.setUp();
		mPerson = new PersonAgent();
		mPerson2 = new PersonAgent();
		mPerson3 = new PersonAgent();
		mCustomerRole = new MockCustomer();
		mTellerRole = new MockTellerRole();
		//time = new Time();
//		for(int i =0; i < 8; i++ ){
//			PersonAgent person;
//			personList.add(person = new PersonAgent(EnumJobType.NONE, 200, "Dobby" + i));
//		}
		
//		mPerson.semAnimationDone.release(1000);
//		SimCityGui.TESTING = true;
		//mGui = new MockPersonGui();
		//mPerson.setGui(mGui);
	}
	
	public void testOne_BasicSetUp () throws Exception {
		//setUp();
		
	//	Preconditions
		assertTrue("Person has no events", mPerson.getEvents().isEmpty());
		assertTrue("Person has no roles", mPerson.mRoles.isEmpty());
		assertTrue("Person has no job", mPerson.mJobType == null);
		assertTrue("Person has no cash", mPerson.getCash() == 0);
		assertTrue("Person has no name", mPerson.getName() == null);
		//assertTrue("Person SSN is 0. Instead: "+ mPerson.getSSN(), mPerson.getSSN() == 3);
		assertTrue("Person has no loan", mPerson.getLoan() == 0);
		assertTrue("Person timeShift is 0", mPerson.getTimeShift() == 0);
		//assertTrue("Person has no housing role", mPerson.mHouseRole == null);
		assertTrue("Role finished is true", mPerson.mRoleFinished == true);
		assertTrue("Person is not at job", mPerson.getAtJob() == false);
		assertTrue("paea: return false", !mPerson.pickAndExecuteAnAction());
//		
//		//Set Time
		Time.sGlobalTimeInt = 0;
		assertTrue("Time is currently 0", Time.GetShift() == 0);
		//Time.sGlobalDate = 0;
		//assertTrue("It is not weekend", !Time.IsWeekend());
//		
//		//Add Events
		mPerson.getEvents().add(new Event(EnumEventType.JOB, 0));
		assertTrue("Person has one event", mPerson.getEvents().size() == 1);
		//mPerson.mJobRole = mTellerRole;
		
		//assertTrue("Person has job role", mPerson.mJobRole != null);
		mPerson.setName("Joe");
		assertTrue("Person is named Joe", mPerson.getName().equals("Joe"));
		
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
	
	public void testTellerScenario() throws Exception{
		setUp();
		ContactList.setup();
		SortingHat.InstantiateBaseRoles();
		
		int cash = 200;
		String name = "Dobby";
		mPerson = new PersonAgent(EnumJobType.BANK, cash, name);
		mPerson.msgAddEvent(new Event(EnumEventType.JOB, 0));
		mPerson.msgTimeShift();
		mPerson.msgAnimationDone();
		
		
		assertEquals("mPerson should have one Event in his list of events. It does not.", 1, mPerson.getEvents().size());
		
		mPerson.pickAndExecuteAnAction();
		assertTrue("mPerson should be at his job. He is not.", mPerson.getAtJob());
		
	}
	
	public void testTimeShift() throws Exception {
		setUp();
		ContactList.setup();
		SortingHat.InstantiateBaseRoles();
		
		mPerson = new PersonAgent(EnumJobType.BANK, 100, "Dobby");
		//Preconditions
		assertEquals("mPerson should have 0 Events in his list of events. He does not.", 0, mPerson.getEvents().size());
		assertFalse("mPerson shouldn't be at his job. He is.", mPerson.getAtJob());
		assertTrue("mRoleFinished for mPerson should be true. It is not.", mPerson.mRoleFinished);
		
		//adding the job to the list of events
		mPerson.msgAddEvent(new Event(EnumEventType.JOB, 0));
		assertEquals("mPerson should have 1 Event in his list of events. He does not.", 1, mPerson.getEvents().size());
		
		//Perform the actual time shift
		mPerson.msgTimeShift();
		mPerson.msgAnimationDone();
		mPerson.pickAndExecuteAnAction();
		assertTrue("mPerson should be at his job. He is.", mPerson.getAtJob());
		
	}
	
	@SuppressWarnings("static-access")
	public void testBasicParty() throws Exception {
		setUp();
		ContactList.setup();
		SortingHat.InstantiateBaseRoles();
		
		mPerson = new PersonAgent(EnumJobType.NONE, 100, "partyPerson");
		
		mPerson2 = new PersonAgent(EnumJobType.NONE, 100, "Harry");
		//mPerson3 = new PersonAgent(EnumJobType.BANK, 100, "RON");
		mPerson.getFriendList().add(mPerson2);
		//mPerson.getFriendList().add(mPerson3);
		time = new Time();
		//Preconditions
		assertEquals("partyPerson should have 0 Events in his list of events. It does not.", 0, mPerson.getEvents().size());
		assertEquals("Time should be 0. It is not. ",0, time.GetTime());
		assertEquals("mPerson2 should have 0 Events in his list of events. It does not.", 0, mPerson2.getEvents().size());
		assertEquals("partyPerson should have 1 friend on it's mFriends list. It does not.", 1, mPerson.getFriendList().size());
		
		//Start of Party test
		mPerson.msgAddEvent(new Event(EnumEventType.PLANPARTY, 0));
		assertEquals("partyPerson should have one Event in his list of events. It does not.", 1, mPerson.getEvents().size());
		assertEquals("Time should be 0. It is not. ",1, time.GetTime());
		assertTrue("partyPerson scheduler should have been called. It was not. ", mPerson.pickAndExecuteAnAction());

		assertFalse("mPerson2 scheduler should not have been called. It was. ", mPerson2.pickAndExecuteAnAction());
		time.notifyPeople();
		//distribution of invites
		
		//mPerson.msgAddEvent(new Event(EnumEventType.INVITE1,0));
		
		assertFalse("partyPerson scheduler should not have been called. It was not. ", mPerson.pickAndExecuteAnAction());
		//assertEquals("partyPerson should have three Events in his list of events. It does not.", 3, mPerson.getEvents().size());

	}

	@SuppressWarnings("static-access")
	public void testFlakeParty() throws Exception {
		setUp();
		ContactList.setup();
		SortingHat.InstantiateBaseRoles();
		
		mPerson = new PersonAgent(EnumJobType.NONE, 100, "Harry");
		mPerson2 = new PersonAgent(EnumJobType.NONE, 100, "partyPersonFlake");
		mPerson3 = new PersonAgent(EnumJobType.NONE, 100, "Ron");
		mPerson.getFriendList().add(mPerson2);
		mPerson.getFriendList().add(mPerson3);
		time = new Time();
		//Preconditions
		assertEquals("Global time should be at 0. It is not.", 0, time.GetTime());
		assertEquals("partyPerson should have 0 Event in his list of events. It does not.", 0, mPerson.getEvents().size());
		assertEquals("mPerson2 should have 0 Event in his list of events. It does not.", 0, mPerson2.getEvents().size());
		assertEquals("partyPerson should have 2 friends on it's mFriends list. It does not.", 2, mPerson.getFriendList().size());
		time.notifyPeople();
		//Start of Party test
		mPerson.msgAddEvent(new Event(EnumEventType.PLANPARTY, time.GetTime()));
		assertEquals("Global time should be at 1. It is not.", 1, time.GetTime());
		assertEquals("Harry should have one Event in his list of events. It does not.", 1, mPerson.getEvents().size());
		assertTrue("Harry scheduler should have been called. It was not. ", mPerson.pickAndExecuteAnAction());
		assertFalse("mPerson2 scheduler should not have been called. It was. ", mPerson2.pickAndExecuteAnAction());
		time.notifyPeople();
		//time.equals(1);
		
		//distribution of invites
		//mPerson.msgAddEvent(new Event(EnumEventType.INVITE1,1));
		assertEquals("Global time should be at 2. It is not.", 2, time.GetTime());
		assertFalse("Harry scheduler should not have been called. It was not. ", mPerson.pickAndExecuteAnAction());
		assertEquals("Harry should have four Events in his list of events. It does not.", 4, mPerson.getEvents().size());
		
	}	
	
	public void testTwo_RoleSwitch() throws Exception {
		//This test relies on the success of the first test and cannot be run alone. You must 
		//run both tests in order to successfully complete this test
		//testOne_BasicSetUp();

		//Start of Test 2
		//mPerson.mJobLocation = new Location(-1,-1);
		//paea: processEvent() -> goToJob()
		//assertTrue("paea: processEvent", mPerson.pickAndExecuteAnAction());
		
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
