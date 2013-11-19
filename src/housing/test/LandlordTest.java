package housing.test;

import housing.House;
import housing.interfaces.Landlord;
import housing.interfaces.Renter;
import housing.roles.LandlordRole;
import housing.test.mock.MockRenter;
import junit.framework.TestCase;
import test.mock.MockPerson;
import base.PersonAgent;
import base.Role;
import base.interfaces.Person;

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
	Renter renter;
	House house1;
	House house2; 

	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */

	public void setUp() throws Exception {
		super.setUp();
		
		landlordPerson = new PersonAgent();
		landlordRole = new LandlordRole();
		renterPerson = new MockPerson("RenterPerson");
		renter = new MockRenter("Mockrenter");
		house1 = new House(20, 20, 100.00); 
		house2 = new House(30, 30, 250.00); 
		
	}
	
	public void testNormativeScenario()
	{
		/**
		 * Tests that the landlord can receive and fulfill housing requests from renters
		 */
		
		landlordPerson.addRole((Role) landlordRole);
		renterPerson.addRole((Role) renter); 
		
		
		
//		waiter.cashier = cashier;//You can do almost anything in a unit test.	
//		
//		cashier.msgPleaseCalculateBill(waiter, customer, "Steak");
//		assertEquals("Waiter should not have received any messages", waiter.log.size(), 0); 
//		assertTrue("Cashier should pickAndExecuteAnAction and call action", cashier.pickAndExecuteAnAction());
//		assertTrue("Waiter should have received message", waiter.log.containsString("Received msgHereIsBill of: 15.99"));
//		assertFalse("Cashier should pickAndExecuteAnAction and return false", cashier.pickAndExecuteAnAction());
//		assertEquals("Waiter should have received message", waiter.log.size(), 1); 
//		assertEquals("Cashier should have one check waiting to be paid", cashier.getChecksSize(), 1); 
//		assertEquals("MockCustomer should not have received any messages", customer.log.size(), 0); 
	}

}
