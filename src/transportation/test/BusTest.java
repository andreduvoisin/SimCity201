package transportation.test;

import junit.framework.TestCase;
import transportation.TransportationBus;
import transportation.TransportationBus.enumState;
import transportation.test.mock.MockRider;

/**
 * This class is a JUnit test class to unit test
 * the transportation system's bus (TransportationBus)
 */
public class BusTest extends TestCase {

	TransportationBus bus;
	MockRider rider;

	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();

		bus = new TransportationBus(true);
		rider = new MockRider(bus);
	}

	public void testBusRunsAroundCityOnceWithNoRidersAtAnyStop() {
		for (int i = 0; i < 4; i++) {
			// Preconditions
			assertEquals("Bus should be at stop "+i+"; it is at " + bus.mCurrentStop, bus.mCurrentStop, i);
			assertEquals("Bus should be ReadyToBoard; it is " + bus.state, bus.state, enumState.ReadyToBoard);
			assertEquals("mRiders should be empty; it has size " + bus.mRiders.size(), bus.mRiders.size(), 0);
			// All bus stop waiting lists empty
			for (int j = 0; j < bus.mBusStops.size(); j++) {
				assertEquals("mBusStops["+j+"].mWaitingPeople should be empty; it has size " + bus.mBusStops.get(j).mWaitingPeople.size(),
						bus.mBusStops.get(j).mWaitingPeople.size(), 0);
			}
	
			// Schedule bus to board
			assertEquals("Bus should be ReadyToBoard; it is " + bus.state, bus.state, enumState.ReadyToBoard);
			assertTrue("Bus's scheduler should return true", bus.pickAndExecuteAnAction());
			assertTrue("Bus should have logged TellRidersToBoard", bus.log.containsString("TellRidersToBoard"));
	
			// Schedule bus to advance
			assertEquals("Bus should be ReadyToTravel", bus.state, enumState.ReadyToTravel);
			assertTrue("Bus's scheduler should return true", bus.pickAndExecuteAnAction());
			assertTrue("Bus should have logged AdvanceToNextStop", bus.log.containsString("AdvanceToNextStop"));
	
			// Bus arrives at stop1
			assertEquals("Bus should be traveling; it is " + bus.state, bus.state, enumState.traveling);
			bus.msgGuiArrivedAtStop();
			assertEquals("Bus should be at stop "+((i+1)%bus.mBusStops.size())+"; it's at stop " + bus.mCurrentStop, bus.mCurrentStop, ((i+1)%bus.mBusStops.size()));
	
			// Schedule bus to unload
			assertEquals("Bus should be ReadyToUnload; it is " + bus.state, bus.state, enumState.ReadyToUnload);
			assertTrue("Bus's scheduler should return true", bus.pickAndExecuteAnAction());
			assertTrue("Bus should have logged TellRidersToGetOff", bus.log.containsString("TellRidersToGetOff"));
		}

		// Postconditions
		assertEquals("Bus should be at stop 0; it is at " + bus.mCurrentStop, bus.mCurrentStop, 0);
		assertEquals("Bus should be ReadyToBoard; it is " + bus.state, bus.state, enumState.ReadyToBoard);
		assertEquals("mRiders should be empty; it has size " + bus.mRiders.size(), bus.mRiders.size(), 0);
		// All bus stop waiting lists empty
		for (int i = 0; i < bus.mBusStops.size(); i++) {
			assertEquals("mBusStops["+i+"].mWaitingPeople should be empty; it has size " + bus.mBusStops.get(i).mWaitingPeople.size(),
					bus.mBusStops.get(i).mWaitingPeople.size(), 0);
		}
	}

	public void testBusStartsAtStopZeroThenPicksPersonUpAtStopOneThenDropsThemOffAtStopTwo() {

		// Preconditions
		assertEquals("Bus should be at stop 0; it is at " + bus.mCurrentStop, bus.mCurrentStop, 0);
		assertEquals("Bus should be ReadyToBoard; it is " + bus.state, bus.state, enumState.ReadyToBoard);
		assertEquals("mRiders should be empty; it has size " + bus.mRiders.size(), bus.mRiders.size(), 0);
		// All bus stop waiting lists empty
		for (int i = 0; i < bus.mBusStops.size(); i++) {
			assertEquals("mBusStops["+i+"].mWaitingPeople should be empty; it has size " + bus.mBusStops.get(i).mWaitingPeople.size(),
					bus.mBusStops.get(i).mWaitingPeople.size(), 0);
		}


		// Add a rider
		int testStartStop = 1, testDestStop = 2;
		rider.msgAtBusStop(testStartStop, testDestStop);
		bus.msgNeedARide(rider, testStartStop);

		assertTrue("Bus should have logged Received msgNeedARide from " + rider.getName() + "; last logged event is "
		         + bus.log.getLastLoggedEvent(), bus.log.containsString("Received msgNeedARide from " + rider.getName()));
		assertEquals("mBusStops["+testStartStop+"].mWaitingPeople should have size 1; it has size "
				+ bus.mBusStops.get(testStartStop).mWaitingPeople.size(),
				bus.mBusStops.get(testStartStop).mWaitingPeople.size(), 1);
		assertTrue("First waiting person should be the rider who just messaged the bus",
				bus.mBusStops.get(testStartStop).mWaitingPeople.get(0).equals(rider));


		// Schedule stop0's waiting people (EMPTY) to board
		assertTrue("Bus's scheduler should return true", bus.pickAndExecuteAnAction());
		assertTrue("Bus should have logged TellRidersToBoard; last logged " + bus.log.getLastLoggedEvent(), bus.log.containsString("TellRidersToBoard"));

		// Schedule bus to advance
		assertEquals("Bus should be ReadyToTravel", bus.state, enumState.ReadyToTravel);
		assertTrue("Bus's scheduler should return true", bus.pickAndExecuteAnAction());
		assertTrue("Bus should have logged AdvanceToNextStop", bus.log.containsString("AdvanceToNextStop"));

		// Bus arrives at stop1
		assertEquals("Bus should be traveling; it is " + bus.state, bus.state, enumState.traveling);
		bus.msgGuiArrivedAtStop();
		assertEquals("Bus should be at stop 1; it's at stop " + bus.mCurrentStop, bus.mCurrentStop, 1);

		// Schedule bus to unload
		assertEquals("Bus should be ReadyToUnload; it is " + bus.state, bus.state, enumState.ReadyToUnload);
		assertTrue("Bus's scheduler should return true", bus.pickAndExecuteAnAction());
		assertTrue("Bus should have logged TellRidersToGetOff", bus.log.containsString("TellRidersToGetOff"));

		// Schedule bus to board
		assertEquals("Bus should be ReadyToBoard; it is " + bus.state, bus.state, enumState.ReadyToBoard);
		assertTrue("Bus's scheduler should return true", bus.pickAndExecuteAnAction());
		assertTrue("Bus should have logged TellRidersToBoard", bus.log.containsString("TellRidersToBoard"));
		assertTrue("Bus should have sent msgBoardBus; rider last logged " + rider.log.getLastLoggedEvent(), rider.log.containsString("Received msgBoardBus"));

		// Rider gets on
		bus.msgImOn(rider);
		assertEquals("mRiders should have size 1; it has size " + bus.mRiders.size(), bus.mRiders.size(), 1);
		assertEquals("mBusStops["+bus.mCurrentStop+"].mWaitingPeople should be empty; it is size " + bus.mBusStops.get(bus.mCurrentStop).mWaitingPeople.size(), bus.mBusStops.get(bus.mCurrentStop).mWaitingPeople.size(), 0);

		// Schedule bus to advance
		assertEquals("Bus should be ReadyToTravel; it is " + bus.state, bus.state, enumState.ReadyToTravel);
		assertTrue("Bus's scheduler should return true", bus.pickAndExecuteAnAction());
		assertTrue("Bus should have logged AdvanceToNextStop", bus.log.containsString("AdvanceToNextStop"));

		// Arrives at stop 2
		assertEquals("Bus should be traveling; it is " + bus.state, bus.state, enumState.traveling);
		bus.msgGuiArrivedAtStop();
		assertEquals("Bus should be at stop 1; it's at stop " + bus.mCurrentStop, bus.mCurrentStop, 2);

		// Schedule bus to unload
		assertEquals("Bus should be ReadyToUnload; it is " + bus.state, bus.state, enumState.ReadyToUnload);
		assertTrue("Bus's scheduler should return true", bus.pickAndExecuteAnAction());
		assertTrue("Bus should have logged TellRidersToGetOff", bus.log.containsString("TellRidersToGetOff"));

		// Tell rider to get off
		assertTrue("Bus should have sent msgAtStop("+bus.mCurrentStop+")", rider.log.containsString("Received msgAtStop("+bus.mCurrentStop+")"));
		assertEquals("Rider hasn't gotten off yet, should have 1 rider", bus.mRiders.size(), 1);

		// Rider off
		bus.msgImOff(rider);
		assertEquals("mRiders should be empty again", bus.mRiders.size(), 0);
	}
}
