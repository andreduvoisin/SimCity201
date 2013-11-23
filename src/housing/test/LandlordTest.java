package housing.test;

import housing.roles.HousingLandlordRole;
import housing.test.mock.MockRenter;
import junit.framework.TestCase;
import base.PersonAgent;
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
	MockRenter mHousingRenter2;
	MockRenter mHousingRenter3;

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
		mHousingRenter1 = new MockRenter("Mockrenter1");
		mHousingRenter2 = new MockRenter("Mockrenter2");
		mHousingRenter3 = new MockRenter("Mockrenter3");
			
	}
	
	public void testNormativeScenario1()
	{
		/**
		 * Tests that the landlord can receive and fulfill housing requests from renters and 
		 * decline applicants when there are no more available houses 
		 */
		
		//Preconditions 
		assertTrue("HousingLandlord has no potential renters", mHousingLandlord.mRenterList.size() == 0);
		assertTrue("HousingLandlord has 2 potential houses for rent", mHousingLandlord.mHousesList.size() == 2);
		assertTrue("HousingRenter1 has an empty log", mHousingRenter1.log.size() == 0); 
		
		//HousingRenter1 messages HousingLandlord to apply for housing
		mHousingLandlord.msgIWouldLikeToLiveHere(mHousingRenter1, 500.00, 1);
		
		//Check 1
		assertTrue("HousingLandlord has one renter", mHousingLandlord.mRenterList.size() == 1); 
		assertTrue("PAEA: return true and does action", mHousingLandlord.pickAndExecuteAnAction());
		assertTrue("HousingRenter1 should have received acceptance message", mHousingRenter1.log.containsString("Received msgApplicationAccepted"));
		assertTrue("PAEA: return false", !mHousingLandlord.pickAndExecuteAnAction());
		
		//HousingRenter2 messages HousingLandlord to apply for housing
		mHousingLandlord.msgIWouldLikeToLiveHere(mHousingRenter2, 200.00, 2);
		
		//Check 2
		assertTrue("HousingLandlord has two renters", mHousingLandlord.mRenterList.size() == 2); 
		assertTrue("PAEA: return true and does action", mHousingLandlord.pickAndExecuteAnAction());
		assertTrue("HousingRenter2 should have received acceptance message", mHousingRenter2.log.containsString("Received msgApplicationAccepted"));
		assertTrue("HousingRenter1 should not have received any additional messages", mHousingRenter1.log.size() == 1); 
		assertTrue("PAEA: return false", !mHousingLandlord.pickAndExecuteAnAction());
		
		//HousingRenter3 messages HousingLandlord to apply for housing
		mHousingLandlord.msgIWouldLikeToLiveHere(mHousingRenter3, 300.00, 3);		
		
		//Check 3
		assertTrue("HousingLandlord has three renters", mHousingLandlord.mRenterList.size() == 3); 
		assertTrue("PAEA: return true and does action", mHousingLandlord.pickAndExecuteAnAction());
		assertTrue("HousingRenter3 should have been declined housing", mHousingRenter3.log.containsString("Received msgApplicationDenied"));
		assertTrue("HousingRenter1 should not have received any additional messages", mHousingRenter1.log.size() == 1); 
		assertTrue("HousingRenter2 should not have received any additional messages", mHousingRenter2.log.size() == 1); 
		assertTrue("HousingLandlord should remove HousingRenter3 from RenterList", mHousingLandlord.mRenterList.size() == 2); 
		assertTrue("PAEA: return false", !mHousingLandlord.pickAndExecuteAnAction());
	
	}
	
	public void testNormativeScenario2(){
		/**
		 * Tests that the landlord prompts renters for rent and renters are able to pay in full and on time
		 */
		
		
		//Preconditions
		assertTrue("HousingRenter1 has an empty log", mHousingRenter1.log.size() == 0); 
		assertTrue("HousingRenter2 has an empty log", mHousingRenter1.log.size() == 0); 
		assertTrue("HousingLandlord has no renters", mHousingLandlord.mRenterList.size() == 0);
		
		//Add renters
		mHousingLandlord.msgIWouldLikeToLiveHere(mHousingRenter1, 500.00, 1);
		mHousingLandlord.msgIWouldLikeToLiveHere(mHousingRenter2, 700.00, 2);
		assertTrue("PAEA: return true and accept renter", mHousingLandlord.pickAndExecuteAnAction());
		assertTrue("PAEA: return true and accept renter", mHousingLandlord.pickAndExecuteAnAction());
		
		//Check 1: Renters have been successfully added
		assertTrue("HousingLandlord has 2 renters", mHousingLandlord.mRenterList.size() == 2);
		assertTrue("HousingRenter1 has received only one message", mHousingRenter1.log.size() == 1); 
		assertTrue("HousingRenter2 has received only one message", mHousingRenter2.log.size() == 1); 
		
		//Set time for rent collection
		mHousingLandlord.mTimeToCheckRent = true; 
		
		//Check 2: HousingLandlord sends appropriate rent collection messages to all renters
		assertTrue("PAEA: return true and execute action", mHousingLandlord.pickAndExecuteAnAction());
		assertTrue("HousingRenter1 should receive rent due message", mHousingRenter1.log.containsString("Received msgRentDue"));
		//assertTrue("PAEA: return true and execute action", mHousingLandlord.pickAndExecuteAnAction());
		//assertTrue("HousingRenter2 should receive rent due message", mHousingRenter2.log.containsString("Received msgRentDue"));
		
	}

}
