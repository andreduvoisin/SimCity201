package transportation.test;

import junit.framework.TestCase;
import transportation.TransportationBus;
import transportation.interfaces.TransportationRider;
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
 * @author Maggi Yang, Jerry Webb
 */

public class CommuterRoleTest extends TestCase {
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
		commuterRole = new CommuterRole(p);
		bus = new TransportationBus(false);
	}
	/*
	public void testRenterObtainsHousing()
	{
		/**
		 * Tests that renter can receive housing from landlord (accepted)
		 */
		
		
	//}*/

	public void testBusCommuterScenario()
	{
		//preconditions
		assertEquals("mockBus shouldn't have any events in it's event log, but it does.", 0, mockBus.log.size());
		assertEquals("commuterRole should be in the walking state, but he's not.", commuterRole.mState.walking, commuterRole.mState);
		
		//used to notify the bus
		commuterRole.mDestinationBusStop = 1;
		commuterRole.mState = commuterRole.mState.atBusStop;
		mockBus.msgNeedARide(commuterRole, 1);
		
		assertEquals("The mockBus should have one event in it's event log, but it doesn't.",1, mockBus.log.size());
		assertTrue("The commuterRole should have run, it did not.", commuterRole.pickAndExecuteAnAction());
		
		//CommuterRole will board the bus
		commuterRole.msgBoardBus();
		
		assertEquals("The commuterRole state should set to boardingBus, but it isn't.", commuterRole.mState.boardingBus, commuterRole.mState);
		assertEquals("The mockBus should still have only one event in it's event log, but it doesn't.",1, mockBus.log.size());
		assertTrue("The commuterRole should have run, it did not.", commuterRole.pickAndExecuteAnAction());
		
		//Calling BoardBus() function in commuterRole
		mockBus.msgImOn(mockCommuter);
		commuterRole.mState = commuterRole.mState.ridingBus;
		
		assertEquals("The mockBus should have two events in it's event log, but it doesn't.",2, mockBus.log.size());
		assertFalse("The commuterRole shouldn't have run, it did.", commuterRole.pickAndExecuteAnAction());
		assertEquals("The commuterRole state should set to ridingBus, but it isn't.", commuterRole.mState.ridingBus, commuterRole.mState);
		
		//mockBus has reached destination
		commuterRole.msgAtStop(1);
		
		assertEquals("The commuterRole state should set to exitingBus, but it isn't.", commuterRole.mState.exitingBus, commuterRole.mState);
		assertTrue("The commuterRole should have run, it did not.", commuterRole.pickAndExecuteAnAction());
		assertEquals("The mockBus should still have two events in it's event log, but it doesn't.",2, mockBus.log.size());
		
		//Exiting Bus
		mockBus.msgImOff(commuterRole);
		commuterRole.mState = commuterRole.mState.walking;
		
		assertEquals("The mockBus should have three events in it's event log, but it doesn't.",3, mockBus.log.size());
		assertEquals("The commuterRole state should set to walking, but it isn't.", commuterRole.mState.walking, commuterRole.mState);

	}

}
