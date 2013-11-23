package housing.test;

import java.util.ArrayList;

import java.util.List;

import housing.House;
import housing.interfaces.HousingLandlord;
import housing.interfaces.HousingRenter;
import housing.roles.HousingLandlordRole;
import housing.test.mock.MockRenter;
import junit.framework.TestCase;
import test.mock.MockPerson;
import base.PersonAgent;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

/**
 * 
 * This class is a JUnit test class to unit test the Landlord's interaction with
 * renters.
 * 
 * @author Maggi Yang
 */

public class LandlordTest extends TestCase {
	// these are instantiated for each test separately via the setUp() method.
	
	Person mPerson;
	HousingLandlordRole mHousingLandlord;
	MockRenter mHousingRenter1;
	House house1;
	House house2; 

	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */

	public void setUp() throws Exception {
		super.setUp();
		
		//Landlord 
		mPerson = new PersonAgent();
		mHousingLandlord = new HousingLandlordRole();
		mPerson.addRole((Role) mHousingLandlord, true); 
	
		//Mock Interfaces 
		mHousingRenter1 = new MockRenter("Mockrenter");
			
	}
	
	public void testNormativeScenario()
	{
		/**
		 * Tests that the landlord can receive and fulfill housing requests from renters and decline when there are no available houses 
		 */
		
		//Preconditions 
		assertTrue("HousingLandlord has no potential renters", mHousingLandlord.mRenterList.size() == 0);
		assertTrue("HousingLandlord has 2 available houses for rent", mHousingLandlord.mHousesList.size() == 2);
		assertTrue("HousingRenter1 has an empty log", mHousingRenter1.log.size() == 0); 
		
		//HousingRenter1 msgs HousingLandlord to apply for housing
		mHousingLandlord.msgIWouldLikeToLiveHere(mHousingRenter1, 500.00, 1);
		
		//Check
		assertTrue("HousingLandlord has one renter", mHousingLandlord.mRenterList.size() == 1); 
		assertTrue("PAEA: return true and does action", mHousingLandlord.pickAndExecuteAnAction());
		assertTrue("HousingRenter1 should have received acceptance message", mHousingRenter1.log.containsString("Received msgApplicationAccepted"));
	
		
		
		
		
		
//		assertTrue("Guard has no tellers.", mGuard.mTellers.isEmpty());
//		assertTrue("Customer1 has an empty log.", mCustomer1.log.size() == 0);
//		
//		landlordPerson.addRole((Role) landlordRole, true);
//		renterPerson.addRole((Role) renterRole, true); 
//		
//		renterRole.setLandlord(landlordRole); 
//		
//		landlordRole.msgIWouldLikeToLiveHere(renterRole, 500.00, 1); 
//		//landlordRole.msgIWouldLikeToLiveHere(renterRole, 250.00, 2); 
//		
//		assertEquals("Landlord should have one Renter in MyRenter list", landlordRole.getRenterListSize(), 1); 
//		assertTrue("Landlord should pickAndExecuteAnAction and call action", landlordRole.pickAndExecuteAnAction());
//		
//		
//		
//		
		

		
	}

}
