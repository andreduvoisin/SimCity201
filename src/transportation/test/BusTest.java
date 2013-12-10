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
	// these are instantiated for each test separately via the setUp() method.

	TransportationBus bus;
	MockRider rider;

	/**
	 * This method is run before each test. You can use it to instantiate the
	 * class variables for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();

		bus = new TransportationBus(true);
		rider = new MockRider();
	}
	
	public void testBusPicksPersonUpAtFirstStopAndDropsThemOffAtNextStop() {
		assertTrue("Bus should be at stop 0; instead it is at " + bus.mCurrentStop,
				bus.mCurrentStop == 0);

		assertTrue("Bus's state should be ReadyToBoard, instead it is "
				+ bus.state,
				bus.state == enumState.ReadyToBoard);

		assertTrue("Bus's mRiders should be empty; instead it has size "
				+ bus.mRiders.size(),
				bus.mRiders.size() == 0);

		assertTrue("Bus's mBusStops should have size 4; instead, has size "
				+ bus.mBusStops.size(),
				bus.mBusStops.size() == 4);

		for (int i = 0; i < bus.mBusStops.size(); i++) {
			assertTrue("Bus's mBusStops[" + i + "].mWaitingPeople should be empty, instead it has size "
					+ bus.mBusStops.get(i).mWaitingPeople.size(),
					bus.mBusStops.get(i).mWaitingPeople.size() == 0);
		}


		// Add a rider
		int testStartStop = 0, testDestStop = 1;
		rider.msgAtBusStop(testStartStop, testDestStop);
		bus.msgNeedARide(rider, testStartStop);

		assertTrue("Bus's log should contain \"Received msgNeedARide\"; instead, last logged event is "
		         + bus.log.getLastLoggedEvent(),
		         bus.log.containsString("Received msgNeedARide"));

		assertTrue("Bus's mBusStops[" + testStartStop + "].mWaitingPeople should have size 1; instead, has size "
				+ bus.mBusStops.get(testStartStop).mWaitingPeople.size(),
				bus.mBusStops.get(testStartStop).mWaitingPeople.size() == 1);

		assertTrue("First waiting person should equal the rider who just messaged the bus; it does not",
				bus.mBusStops.get(testStartStop).mWaitingPeople.get(0).equals(rider));



		// Schedule stop0's waiting people to board
		assertTrue("Bus's scheduler should return true; it doesn't",
				bus.pickAndExecuteAnAction());

		assertTrue("Bus should have logged TellRidersToBoard(); instead it logged "
				+ bus.log.getLastLoggedEvent(),
				bus.log.containsString("TellRidersToBoard()"));

		assertTrue("Bus should have sent msgBoardBus(); it didn't; rider last logged "
				+ rider.log.getLastLoggedEvent(),
				rider.log.containsString("Received msgBoardBus"));

		assertTrue("Bus should have state ReadyToTravel; instead it has state "
				+ bus.state,
				bus.state == enumState.ReadyToTravel);

		// Bus already called AdvanceToNextStop()

		assertTrue("Bus should have state traveling; instead it has state "
				+ bus.state,
				bus.state == enumState.traveling);

		bus.msgGuiArrivedAtStop();

		// Schedule bus to advance to stop1
		assertTrue("Bus's scheduler should return true; it doesn't",
				bus.pickAndExecuteAnAction());


		// Schedule stop1's destined riders to unload
		assertTrue("Bus's scheduler should return true; it doesn't",
				bus.pickAndExecuteAnAction());

		assertTrue("Bus should have logged TellRidersToGetOff(); instead it logged "
				+ bus.log.getLastLoggedEvent(),
				bus.log.containsString("TellRidersToGetOff()"));

		assertTrue("Bus should have sent msgAtStop(1); it didn't; rider last logged "
				+ rider.log.getLastLoggedEvent(),
				rider.log.containsString("Received msgAtStop(1)"));


		// Schedule stop1's riders to board
		assertTrue("Bus's scheduler should return true; it doesn't",
				bus.pickAndExecuteAnAction());

		assertTrue("Bus should have logged TellRidersToBoard(); instead it logged "
				+ bus.log.getLastLoggedEvent(),
				bus.log.containsString("TellRidersToBoard()"));

		
	}
}
