package transportation.test.mock;

import test.mock.LoggedEvent;
import test.mock.Mock;
import transportation.TransportationBus;
import transportation.interfaces.TransportationRider;

public class MockRider extends Mock implements TransportationRider {

	public static int sRiderNum = 0, mRiderNum;
	public TransportationBus bus;

	public MockRider(TransportationBus b) {
		mRiderNum = sRiderNum++;
		bus = b;
	}

	public void msgAtBusStop(int currentStop, int destinationStop){
		log.add(new LoggedEvent("Received msgAtBusStop(" + currentStop + ", " + destinationStop + ")"));
	}
	
	public void msgBoardBus() {
		log.add(new LoggedEvent("Received msgBoardBus"));
	}
	
	public void msgAtStop(int busStop){
		log.add(new LoggedEvent("Received msgAtStop(" + busStop + ")"));
	}

	public String getName() {
		return "MockRider " + mRiderNum;
	}
}
