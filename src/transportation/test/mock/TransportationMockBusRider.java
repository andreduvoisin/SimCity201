package transportation.test.mock;

import test.mock.*;
import transportation.interfaces.*;

public class TransportationMockBusRider extends Mock implements TransportationRider {

	public TransportationMockBusRider() {
		super();
	}

	public void msgBoardBus() {
		log.add(new LoggedEvent("Received msgBoardBus()"));
	}

	public void msgAtYourStop() {
		log.add(new LoggedEvent("Received msgAtYourStop()"));
	}

	public int getLocation() {
		return 0;
	}

	public int getDestination() {
		return 0;
	}

	public String getName() {
		return "MockBusRider";
	}

}
