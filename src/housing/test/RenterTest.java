package housing.test;

import housing.House;
import housing.roles.HousingLandlordRole;
import housing.roles.HousingRenterRole;
import housing.test.mock.MockLandlord;
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

public class RenterTest extends TestCase {
	// these are instantiated for each test separately via the setUp() method.
	
	Person mPerson;
	HousingRenterRole mHousingRenter;
	MockLandlord mHousingLandlord;
	House mHouse1; 


	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */

	public void setUp() throws Exception {
		super.setUp();
		
		//Renter 
		mPerson = new PersonAgent();
		mHousingRenter = new HousingRenterRole(); 
		mPerson.addRole((Role) mHousingRenter, true); 
	
		//Mock Interfaces 
		mHousingLandlord = new MockLandlord("Mocklandlord"); 
		mHousingRenter.setLandlord(mHousingLandlord);
		
		//Houses
		mHouse1 = new House(10, 10, 300.00); 
	}
	
	public void testNormativeScenario1()
	{
		/**
		 * Tests that the renter apply and successfully obtains housing from landlord (accepted)
		 */
		
		//Preconditions 
		assertTrue("HousingLandlord has an empty log", mHousingLandlord.log.size() == 0); 
		assertEquals("HousingRenter has no bills", mHousingRenter.mBills.size(), 0); 
		assertTrue("HousingRenter should have no House", mHousingRenter.mHouse == null); 
		
		//HousingLandlord accepts housing application from HousingRenter 
		mHousingRenter.msgApplicationAccepted(mHouse1);
		
		//Check 1
		assertEquals("HousingRenter has appropriate house", mHousingRenter.mHouse, mHouse1); 
		
//		
//		//HousingRenter1 messages HousingLandlord to apply for housing
//		mHousingLandlord.msgIWouldLikeToLiveHere(mHousingRenter1, 500.00, 1);
//		
//		//Check 1
//		assertTrue("HousingLandlord has one renter", mHousingLandlord.mRenterList.size() == 1); 
//		assertTrue("PAEA: return true and does action", mHousingLandlord.pickAndExecuteAnAction());
//		assertTrue("HousingRenter1 should have received acceptance message", mHousingRenter1.log.containsString("Received msgApplicationAccepted"));
//		assertTrue("PAEA: return false", !mHousingLandlord.pickAndExecuteAnAction());
//		
//		//HousingRenter2 messages HousingLandlord to apply for housing
//		mHousingLandlord.msgIWouldLikeToLiveHere(mHousingRenter2, 200.00, 2);
//		
//		//Check 2
//		assertTrue("HousingLandlord has two renters", mHousingLandlord.mRenterList.size() == 2); 
//		assertTrue("PAEA: return true and does action", mHousingLandlord.pickAndExecuteAnAction());
//		assertTrue("HousingRenter2 should have received acceptance message", mHousingRenter2.log.containsString("Received msgApplicationAccepted"));
//		assertTrue("HousingRenter1 should not have received any additional messages", mHousingRenter1.log.size() == 1); 
//		assertTrue("PAEA: return false", !mHousingLandlord.pickAndExecuteAnAction());
//		
//		//HousingRenter3 messages HousingLandlord to apply for housing
//		mHousingLandlord.msgIWouldLikeToLiveHere(mHousingRenter3, 300.00, 3);		
//		
//		//Check 3
//		assertTrue("HousingLandlord has three renters", mHousingLandlord.mRenterList.size() == 3); 
//		assertTrue("PAEA: return true and does action", mHousingLandlord.pickAndExecuteAnAction());
//		assertTrue("HousingRenter3 should have been declined housing", mHousingRenter3.log.containsString("Received msgApplicationDenied"));
//		assertTrue("HousingRenter1 should not have received any additional messages", mHousingRenter1.log.size() == 1); 
//		assertTrue("HousingRenter2 should not have received any additional messages", mHousingRenter2.log.size() == 1); 
//		assertTrue("HousingLandlord should remove HousingRenter3 from RenterList", mHousingLandlord.mRenterList.size() == 2); 
//		assertTrue("PAEA: return false", !mHousingLandlord.pickAndExecuteAnAction());
//	
	}
	

}
