package test;

import housing.roles.LandlordRole;
import housing.roles.RenterRole;
import junit.framework.TestCase;
import base.PersonAgent;

public class BasicTest extends TestCase{
	
	PersonAgent mPerson;
	PersonAgent mPerson2;
	LandlordRole landlord;
	RenterRole renter;
	//needed interfaces
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
			
		//clear logs
	}
	
	//TESTS
	
	public void testInstantiatePeopleAndAssignRoles() {
		mPerson = new PersonAgent();
		mPerson2 = new PersonAgent();
		landlord = new LandlordRole();
		landlord.setPerson(mPerson);
		renter = new RenterRole();
		renter.setPerson(mPerson2);
		mPerson.addRole(landlord);
		mPerson2.addRole(renter);
		mPerson.addCash(100000);
		mPerson2.addCash(200000);
		landlord.msgIWouldLikeToLiveHere(renter, mPerson2.getCash(), mPerson2.getSSN());
		mPerson.pickAndExecuteAnAction();
	}

	private void print(String message){
		System.out.println("[PersonTest] " + message);
	}
}

