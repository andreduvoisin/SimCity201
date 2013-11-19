package test;

import housing.roles.LandlordRole;
import housing.roles.RenterRole;
import junit.framework.TestCase;
import base.PersonAgent;

public class ExampleTest extends TestCase{
	
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
		mPerson.addRole(landlord);
		mPerson2.addRole(renter);
		mPerson.addCredit(100000);
		mPerson2.addCredit(200);
		
		mPerson.paea(landlord);
		mPerson2.paea(renter);
	}

	private void print(String message){
		System.out.println("[PersonTest] " + message);
	}
}

