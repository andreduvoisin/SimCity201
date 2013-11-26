package test;

import housing.interfaces.HousingBase;
import housing.roles.HousingBaseRole;
import test.mock.MockPersonGui;
import test.mock.MockRenterRole;
import junit.framework.TestCase;
import bank.test.mock.MockTellerRole;
import base.Event;
import base.Event.EnumEventType;
import base.PersonAgent;
import base.Time;
import base.interfaces.Role;

public class PersonAgentTest extends TestCase {

	PersonAgent mPerson;
	HousingBase mRenterRole;
	MockTellerRole mTellerRole;
	MockPersonGui mGui;
	
	
	public void setUp() throws Exception {
		super.setUp();
		mPerson = new PersonAgent();
		mRenterRole = new MockRenterRole();
		mTellerRole = new MockTellerRole();
		mGui = new MockPersonGui();
		mPerson.setGui(mGui);
	}
	
	public void testOne_BasicSetUp() {
	//	setUp()
		
	//	Preconditions
		assertTrue("Person has no events", mPerson.mEvents.isEmpty());
		assertTrue("Person has no roles", mPerson.mRoles.isEmpty());
		assertTrue("Person has no job", mPerson.mJobType == null);
		assertTrue("Person has no cash", mPerson.getCash() == 0);
		assertTrue("Person has no name", mPerson.getName() == null);
		assertTrue("Person SSN is 0. Instead: "+ mPerson.getSSN(), mPerson.getSSN() == 0);
		assertTrue("Person has no loan", mPerson.getLoan() == 0);
		assertTrue("Person timeShift is 0", mPerson.getTimeShift() == 0);
		assertTrue("Person has no housing role", mPerson.mHouseRole == null);
		assertTrue("Role finished is true", mPerson.mRoleFinished == true);
		assertTrue("Person is not at job", mPerson.mAtJob == false);
		assertTrue("paea: return false", !mPerson.pickAndExecuteAnAction());
		
		//Set Time
		Time.sGlobalShift = 0;
		assertTrue("Time is currently 0", Time.GetShift() == 0);
		Time.sGlobalDate = 0;
		assertTrue("It is not weekend", !Time.IsWeekend());
		
		//Add Events
		mPerson.mEvents.add(new Event(EnumEventType.JOB, 0));
		assertTrue("Person has one event", mPerson.mEvents.size() == 1);
		mPerson.mJobRole = mTellerRole;
		assertTrue("Person has job role", mPerson.mJobRole != null);
		mPerson.setName("Joe");
		assertTrue("Person is named Joe", mPerson.getName().equals("Joe"));
		
		//Paea: processEvent() -> goToJob()
		assertTrue("paea: processEvent", mPerson.pickAndExecuteAnAction());
		
		assertTrue("New event in 24", mPerson.mEvents.first().mTime == 24);
		assertTrue("Person is at job", mPerson.mAtJob == true);
		assertTrue("Person is at location", mGui.log.containsString("DoGoToDestination"));
		assertTrue("Person is not visible", mGui.log.containsString("setPresent: false"));
		assertTrue("Person is set in job", mTellerRole.log.containsString("setPerson: Joe"));
		assertTrue("Person has a role", mPerson.mRoles.size() == 1);
		assertTrue("Person has active role", mPerson.mRoles.get(mTellerRole) == true);
		
		//Conditions for continual PAEA
		assertTrue("Job role is set with person", mTellerRole.getPerson() != null);
		
		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
		assertTrue("Should continually paea role", mPerson.pickAndExecuteAnAction());
		
		//Role Switch
		Time.sGlobalShift = 1;
		assertTrue("Time shift is currently 1", Time.GetShift() == 1);
		assertTrue("Time is currently 0", Time.GetTime() == 0);
		mPerson.msgTimeShift();
		
		assertTrue("Person is not at job anymore", mPerson.mAtJob == false);
		assertTrue("Role is finished", mPerson.mRoleFinished == true);
		assertTrue("PersonGui is visible again", mGui.log.containsString("setPresent: true"));
		
		assertTrue("paea: return false (break)", !mPerson.pickAndExecuteAnAction());
		
		//New Event after Job
		//mPerson.mEvents.add(new Event())
		
		/*mPerson.mEvents.add(new Event(EnumEventType.EAT, 0));
		assertTrue("Person has one event", mPerson.mEvents.size() == 1);
		
		//Paea: processEvent() -> eatFood()
		assertTrue("paea: processEvent", mPerson.pickAndExecuteAnAction());
		
		assertTrue("Person still has one event", mPerson.mEvents.size() == 1);
		assertTrue("Person has eat event in 24", mPerson.mEvents.first().mEventType == EnumEventType.EAT);
		assertTrue("Person has eat event in 24", mPerson.mEvents.first().mTime == 24);
		assertTrue("Person has eat event in 24", mPerson.mEvents.first().mLocation == null);*/
		
		
		
		
		
		
		
	}
}
