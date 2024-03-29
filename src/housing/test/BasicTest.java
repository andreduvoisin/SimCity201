package housing.test;

import housing.House;
import housing.roles.HousingLandlordRole;
import housing.roles.HousingRenterRole;
import junit.framework.TestCase;
import bank.roles.BankMasterTellerRole;
import base.PersonAgent;

/*
 * Basic test to ensure that PersonAgent and Roles are interacting correctly and
 * firing the appropriate actions, messages, etc.
 * @author David Carr
 */

public class BasicTest extends TestCase {

	PersonAgent mPerson;
	PersonAgent mPerson2;
	HousingLandlordRole landlord;
	HousingRenterRole renter;
	BankMasterTellerRole master;
	House mHouse1;
	House mHouse2;

	public void setUp() throws Exception {
		super.setUp();
		mPerson = new PersonAgent();
		mPerson2 = new PersonAgent();
		landlord = new HousingLandlordRole(mPerson);
		mHouse1 = new House(1, 300.00);
		mHouse2 = new House(2, 200.00);
		landlord.mHousesList.add(mHouse1);
		landlord.mHousesList.add(mHouse2);
	}

	// TESTS
	
	public void testHousingGui() {
		mPerson = new PersonAgent();
		mPerson2 = new PersonAgent();
//		HousingRenterRole renter1 = new HousingRenterRole(mPerson);
//		HousingRenterRole renter2 = new HousingRenterRole(mPerson2);
//		HousingPersonGui gui1 = new HousingPersonGui();
//		HousingPersonGui gui2 = new HousingPersonGui();
		
		
//		renter1.setGui(gui1);
//		renter2.setGui(gui2);
		/*HousingHouseGuiPanel housepanel = HousingHouseGuiPanel.getInstance();
		housepanel.addGui(gui1);
		housepanel.addGui(gui2);*/
	}

	public void testInstantiatePeopleAndAssignRoles() {
		
		assertEquals("Landlord housing size correct",
				landlord.mHousesList.size(), 2);
		renter = new HousingRenterRole(null);
		mPerson.addRole(landlord, true);
		assertEquals("mPerson contains one role (the landlord role)",
				mPerson.mRoles.size(), 1);
		mPerson2.addRole(renter, true);
		assertEquals("mPerson2 contains one role (the renter role)",
				mPerson2.mRoles.size(), 1);
		mPerson.addCash(100000);
		mPerson2.addCash(200000);
		landlord.msgIWouldLikeToLiveHere(renter, mPerson2.getCash(),
				mPerson2.getSSN());
		mPerson.pickAndExecuteAnAction();
		landlord.mTimeToCheckRent = true;
		mPerson2.pickAndExecuteAnAction();
		mPerson.pickAndExecuteAnAction();
		mPerson2.pickAndExecuteAnAction();
	}

//	private void print(String message) {
//		//System.out.println("[PersonTest] " + message);
//	}

}
