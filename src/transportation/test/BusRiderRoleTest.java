package transportation.test;

import base.PersonAgent;
import base.PersonAgent.EnumJobType;
import base.interfaces.Person;
import transportation.roles.CommuterRole;
import transportation.test.mock.MockCommuter;
import junit.framework.TestCase;

/**
 * 
 * This class is a JUnit test class to unit test the bus transportation system
 * 
 * @author Maggi Yang
 */

public class BusRiderRoleTest extends TestCase {
	// these are instantiated for each test separately via the setUp() method.
	MockCommuter mockCommuter;
	CommuterRole commuterRole;
	PersonAgent p;
	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */

	public void setUp() throws Exception {
		super.setUp();
		p = new PersonAgent(EnumJobType.NONE, 200,"bob");
		mockCommuter = new MockCommuter();
		commuterRole = new CommuterRole(null);
	}
	
	public void testRenterObtainsHousing()
	{
		/**
		 * Tests that renter can receive housing from landlord (accepted)
		 */
		
		
	}

	public void testCommuterScenario()
	{
				
		commuterRole.msgBoardBus();
		
		//assertEquals("The MockCommuterRole's log should have one event, it doesn't: ", 1, commuterRole.log.size());
		
		commuterRole.msgAtStop(0);
		//assertEquals("The MockCommuterRole's log should have two event, it doesn't: ", 2, commuterRole.log.size());
	}

}
