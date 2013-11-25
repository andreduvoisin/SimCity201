package test;


import java.util.ArrayList;
import java.util.List;

import city.gui.SimCityGui;
import restaurant.restaurant_davidmca.gui.RestaurantGui;
import restaurant.restaurant_davidmca.gui.RestaurantPanel;
import junit.framework.TestCase;
import base.PersonAgent;
import base.PersonAgent.EnumJobType;
import base.Time;
import base.interfaces.Person;
import housing.roles.HousingLandlordRole;
import housing.roles.HousingRenterRole;

public class PersonAgentTest extends TestCase {

	PersonAgent mPerson;
	RestaurantPanel restaurantPanel;
	
	public void setUp() throws Exception {
		super.setUp();
		restaurantPanel = new RestaurantPanel(new RestaurantGui(new SimCityGui()));
		List<Person> mPersons = new ArrayList<Person>();
		mPersons.add(mPerson);
		Time time = new Time(mPersons);
	}
	
	public void testOne_BasicSetUp() {
	//	setUp()
		mPerson = new PersonAgent(EnumJobType.BANK, 200, "Joe");
		
	//	Preconditions
		assertTrue("Person has two events", mPerson.mEvents.size() == 2);
		assertTrue("Person has bank job", mPerson.mJobType == EnumJobType.BANK);
		assertTrue("Person has 200 cash", mPerson.getCash() == 200);
		assertTrue("Person is named Joe", mPerson.getName().equals("Joe"));
		//Because of testing and config parser, already created 17 people
		assertTrue("Person SSN is 18. Instead: "+ mPerson.getSSN(), mPerson.getSSN() == 18);
		assertTrue("Person has no loan", mPerson.getLoan() == 0);
		assertTrue("Person timeShift is 0", mPerson.getTimeShift() == 0);
		assertTrue("Person has Gui", mPerson.mPersonGui != null);
		assertTrue("Person is renter", mPerson.mHouseRole.getClass().equals(HousingRenterRole.class));
		//five base roles, housing, job
		assertTrue("Person has seven base roles. Instead: "+mPerson.mRoles.size(), mPerson.mRoles.size() == 7);
		
	//	1 : paea (processEvent(eat)) -> eatFood() called
		assertTrue("paea: processEvent", mPerson.pickAndExecuteAnAction());
		
		assertTrue("Person still has two events", mPerson.mEvents.size() == 2);
		assertTrue("Person doesn't have a house", mPerson.mHouseRole.mHouse == null);
		
		
	}
}
