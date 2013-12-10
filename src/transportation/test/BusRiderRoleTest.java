package transportation.test;

import junit.framework.TestCase;
import transportation.TransportationBus;
import transportation.roles.CommuterRole;
import transportation.test.mock.MockBus;
import transportation.test.mock.MockCommuter;
import base.ContactList;
import base.PersonAgent;
import base.PersonAgent.EnumJobType;

/**
 * 
 * This class is a JUnit test class to unit test the bus transportation system
 * 
 * @author Maggi Yang
 */

public class BusRiderRoleTest extends TestCase {
	// these are instantiated for each test separately via the setUp() method.
	MockCommuter mockCommuter;
	MockBus mockBus;
	TransportationBus bus;
	CommuterRole commuterRole;
	PersonAgent p;
	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */

	public void setUp() throws Exception {
		super.setUp();
		ContactList.setup();
		p = new PersonAgent(EnumJobType.NONE, 200,"bob");
		mockCommuter = new MockCommuter();
		mockBus = new MockBus();
		commuterRole = new CommuterRole(null);
		bus = new TransportationBus(false);
	}
	
	public void testRenterObtainsHousing()
	{
		/**
		 * Tests that renter can receive housing from landlord (accepted)
		 */
		
		
	}

	public void testCommuterScenario()
	{
		//preconditions
		assertEquals("mockBus shouldn't have any events in it's event log, but it does.", 0, mockBus.log.size());
		assertEquals("commuterRole should be in the walking state, but he's not.", commuterRole.mState.walking, commuterRole.mState);
		
		commuterRole.mState = commuterRole.mState.atBusStop;
		
		//commuterRole.pickAndExecuteAnAction();
		//assertTrue("The CommuterRole's pickAndExecuteAction should have returned true, but it didn't", commuterRole.pickAndExecuteAnAction());
		//assertEquals("The MockCommuterRole's log should have one event, it doesn't: ", 1, commuterRole.log.size());
		
		
		//assertEquals("The MockCommuterRole's log should have two event, it doesn't: ", 2, commuterRole.log.size());
	}

}
