package housing.test;

import test.mock.MockPerson;
import housing.House;
import housing.roles.HousingLandlordRole;
import housing.roles.HousingRenterRole;
import housing.test.mock.MockLandlord;
import housing.test.mock.MockRenter;
import junit.framework.TestCase;
import bank.test.mock.MockMasterTellerRole;
import base.ContactList;
import base.Location;
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
	MockPerson mMockPerson; 
	MockMasterTellerRole mMockMasterTeller; 


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
		mHousingRenter.setLandlord(mHousingLandlord);
	
		//Mock Interfaces 
		mHousingLandlord = new MockLandlord("Mocklandlord"); 
		mMockPerson = new MockPerson("MockPerson"); 
		mMockMasterTeller = new MockMasterTellerRole("MockMasterTeller"); 
		mMockPerson.addRole((Role) mMockMasterTeller, true); 
		
		ContactList.sRoleLocations.put(mMockMasterTeller, new Location(10,10)); 
		
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
	
	public void testRenterPaysRent(){
		
		/**
		 * Tests renter paying rent after receiving notice that rent is due 
		 */
		
		//Set house 
		mHousingRenter.mHouse = mHouse1; 
		
		//Preconditions 
		assertTrue("HousingLandlord has an empty log", mHousingLandlord.log.size() == 0); 
		assertEquals("HousingRenter has no bills", mHousingRenter.mBills.size(), 0); 
		assertEquals("HousingRenter should have House", mHousingRenter.mHouse, mHouse1); 
		
		//HousingLandlord sends housing application denied message 
		mHousingRenter.msgRentDue(3, 300.00);
		
		//Check
		assertEquals("HousingRenter should have one bill", mHousingRenter.mBills.size(), 1); 
		assertTrue("PAEA: return true and does action", mHousingRenter.pickAndExecuteAnAction()); 
		//assertEquals("HousingRenter should pay and then remove bill", mHousingRenter.mBills.size(), 0); 
		
	}
	
	

}
