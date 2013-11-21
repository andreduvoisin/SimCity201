package test;

import java.io.FileNotFoundException;

import city.gui.CityPanel;
import housing.House;
import housing.roles.LandlordRole;
import housing.roles.RenterRole;
import junit.framework.TestCase;
import bank.roles.BankMasterTellerRole;
import base.ConfigParser;
import base.PersonAgent;

/*
 * Basic test to ensure that PersonAgent and Roles are interacting correctly and
 * firing the appropriate actions, messages, etc.
 * @author David Carr
 */

public class BasicTest extends TestCase{
	
	PersonAgent mPerson;
	PersonAgent mPerson2;
	LandlordRole landlord;
	RenterRole renter;
	BankMasterTellerRole master;
	
	
	public void setUp() throws Exception{
		super.setUp();
	}
	
	//TESTS
	
	public void testImportFromConfigFile() throws FileNotFoundException {
		ConfigParser config = ConfigParser.getInstanceOf();
		config.readFileCreatePersons();
		CityPanel citypanel = CityPanel.getInstanceOf();
		assertEquals("8 people added", citypanel.masterPersonList.size(), 8);
	}
	
	public void testInstantiatePeopleAndAssignRoles() {
		mPerson = new PersonAgent("Person1");
		mPerson2 = new PersonAgent("Person2");
		landlord = new LandlordRole();
		master = new BankMasterTellerRole();
		mPerson.mMasterTeller = master;
		mPerson2.mMasterTeller = master;
		House house1 = new House(5, 5, 50);
		assertEquals("House1 rent set correctly", 50.00, house1.mRent);
		House house2 = new House(10, 10, 60);
		assertEquals("House2 rent set correctly", 60.00, house2.mRent);
		landlord.mHousesList.add(house1);
		landlord.mHousesList.add(house2);
		assertEquals("Landlord housing size correct", landlord.mHousesList.size(), 2);
		landlord.setPerson(mPerson);
		renter = new RenterRole();
		renter.setPerson(mPerson2);
		mPerson.addRole(landlord, true);
		assertEquals("mPerson contains one role (the landlord role)", mPerson.mRoles.size(), 1);
		mPerson2.addRole(renter, true);
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

