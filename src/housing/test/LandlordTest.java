package housing.test;

import java.util.ArrayList;
import java.util.List;

import housing.House;
import housing.interfaces.Landlord;
import housing.interfaces.Renter;
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
	Person landlordPerson;
	Person renterPerson;
	Landlord landlordRole;
	Renter renterRole;
	House house1;
	House house2; 

	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */

	public void setUp() throws Exception {
		super.setUp();
		
		landlordPerson = new PersonAgent("LandlordPerson");
		renterPerson = new MockPerson("RenterPerson");
		landlordRole = new HousingLandlordRole();
	
		renterRole = new MockRenter("Mockrenter");
	
	}
	
	public void testNormativeScenario()
	{
		/**
		 * Tests that the landlord can receive and fulfill housing requests from renters
		 */
		
		landlordPerson.addRole((Role) landlordRole, true);
		renterPerson.addRole((Role) renterRole, true); 
		
		renterRole.setLandlord(landlordRole); 
		
		landlordRole.msgIWouldLikeToLiveHere(renterRole, 500.00, 1); 
		//landlordRole.msgIWouldLikeToLiveHere(renterRole, 250.00, 2); 
		
		assertEquals("Landlord should have one Renter in MyRenter list", landlordRole.getRenterListSize(), 1); 
		assertTrue("Landlord should pickAndExecuteAnAction and call action", landlordRole.pickAndExecuteAnAction());
		
		
		
		
		

		
	}

}
