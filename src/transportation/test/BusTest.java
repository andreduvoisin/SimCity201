package transportation.test;

import junit.framework.TestCase;
import transportation.TransportationBus;
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
	
	public void testBusPicksPersonUpAtOneStopAndDropsThemOffAtNextStop() {
		assertTrue("TransportationBus's state should be ReadyToBoard, instead it is "
					+ bus.state,
					bus.state == TransportationBus.enumState.ReadyToBoard);

		assertTrue("TransportationBus's mRiders should be empty, instead it has size "
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
		int testStopNum = 0;
		bus.msgNeedARide(rider, testStopNum);

		assertTrue("Bus's log should contain \"Received msgNeedARide\"; instead, last logged event is "
		         + bus.log.getLastLoggedEvent(),
		         bus.log.containsString("Received msgNeedARide"));

		assertTrue("Bus's mBusStops[" + testStopNum + "].mWaitingPeople should have size 1; instead, has size "
				+ bus.mBusStops.get(testStopNum).mWaitingPeople.size(),
				bus.mBusStops.get(testStopNum).mWaitingPeople.size() == 1);

		assertTrue("First waiting person should equal the rider who just messaged the bus; it does not",
				bus.mBusStops.get(testStopNum).mWaitingPeople.get(0).equals(rider));
	}
}
