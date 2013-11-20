package test;

import housing.House;
import housing.roles.LandlordRole;
import housing.roles.RenterRole;
import junit.framework.TestCase;
import bank.roles.BankMasterTellerRole;
import base.PersonAgent;

public class BasicTest extends TestCase{
	
	PersonAgent mPerson;
	PersonAgent mPerson2;
	LandlordRole landlord;
	RenterRole renter;
	BankMasterTellerRole master;
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
		master = new BankMasterTellerRole();
		mPerson.mMasterTeller = master;
		mPerson2.mMasterTeller = master;
		House house1 = new House(5, 5, 50);
		House house2 = new House(10, 10, 60);
		landlord.mHousesList.add(house1);
		landlord.mHousesList.add(house2);
		landlord.setPerson(mPerson);
		renter = new RenterRole();
		renter.setPerson(mPerson2);
		mPerson.addRole(landlord);
		assertEquals("mPerson contains one role (the landlord role)", mPerson.mRoles.size(), 1);
		mPerson2.addRole(renter);
		assertEquals("mPerson2 contains one role (the renter role)", mPerson2.mRoles.size(), 1);
		mPerson.addCash(100000);
		mPerson2.addCash(200000);
		landlord.msgIWouldLikeToLiveHere(renter, mPerson2.getCash(), mPerson2.getSSN());
		mPerson.pickAndExecuteAnAction();
		landlord.mTimeToCheckRent = true;
		mPerson2.pickAndExecuteAnAction();
		mPerson.pickAndExecuteAnAction();
		mPerson2.pickAndExecuteAnAction();
	}

	private void print(String message){
		System.out.println("[PersonTest] " + message);
	}
}

