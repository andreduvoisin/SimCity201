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
	
	public void testRenterObtainsHousing()
	{
		/**
		 * Tests that renter can receive housing from landlord (accepted)
		 */
		
		//Preconditions 
		assertTrue("HousingLandlord has an empty log", mHousingLandlord.log.size() == 0); 
		assertEquals("HousingRenter has no bills", mHousingRenter.mBills.size(), 0); 
		assertTrue("HousingRenter should have no House", mHousingRenter.mHouse == null); 
		
		//HousingLandlord sends housing accepted message 
		mHousingRenter.msgApplicationAccepted(mHouse1);
		
		//Check
		assertEquals("HousingRenter has appropriate house", mHousingRenter.mHouse, mHouse1); 
		assertTrue("PAEA: return false", !mHousingRenter.pickAndExecuteAnAction());
		assertTrue("mHousingLandlord should not have received any messages", mHousingLandlord.log.size() == 0); 

	}
	
	public void testRenterIsDeniedHousing(){
		
		/**
		 * Tests renter being denied housing from landlord
		 */

		//Preconditions 
		assertTrue("HousingLandlord has an empty log", mHousingLandlord.log.size() == 0); 
		assertEquals("HousingRenter has no bills", mHousingRenter.mBills.size(), 0); 
		assertTrue("HousingRenter should have no House", mHousingRenter.mHouse == null); 
		
		//HousingLandlord sends housing application denied message 
		mHousingRenter.msgApplicationDenied();
		
		//Check
		assertTrue("HousingRenter should still have no house", mHousingRenter.mHouse == null); 
		assertTrue("PAEA: return false", !mHousingRenter.pickAndExecuteAnAction());
		assertTrue("mHousingLandlord should not have received any messages", mHousingLandlord.log.size() == 0); 

		
	}
	

}
