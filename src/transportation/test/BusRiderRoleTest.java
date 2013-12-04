package transportation.test;

import city.gui.CityHousing;
import city.gui.SimCityGui;
import test.mock.MockPerson;
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
 * This class is a JUnit test class to unit test the bus transportation system
 * 
 * @author Maggi Yang
 */

public class BusRiderRoleTest extends TestCase {
	// these are instantiated for each test separately via the setUp() method.


	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */

	public void setUp() throws Exception {
		super.setUp();
		

	}
	
	public void testRenterObtainsHousing()
	{
		/**
		 * Tests that renter can receive housing from landlord (accepted)
		 */
		

	}

	

}
