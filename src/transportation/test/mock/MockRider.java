package transportation.test.mock;

import test.mock.LoggedEvent;
import test.mock.Mock;
import transportation.TransportationBus;
import transportation.interfaces.TransportationRider;
import transportation.roles.CommuterRole.PersonState;

public class MockRider extends Mock implements TransportationRider {

	public TransportationBus bus;
	public int mCurrentBusStop, mDestinationBusStop; 
	public PersonState mState;
	private static int sRiderNum = 0;

	public MockRider(TransportationBus b) {
		sRiderNum++;
		bus = b;
	}

	public void msgAtBusStop(int currentStop, int destinationStop){
		log.add(new LoggedEvent("Received msgAtBusStop(current stop = " + currentStop + ", destination stop = " + destinationStop + ")"));
		mCurrentBusStop = currentStop;
		mDestinationBusStop = destinationStop;
		mState = PersonState.atBusStop;
	}
	
	public void msgBoardBus() {
		log.add(new LoggedEvent("Received msgBoardBus"));
		mState = PersonState.boardingBus;
		bus.msgImOn(this);
	}
	
	public void msgAtStop(int busStop){
		log.add(new LoggedEvent("Received msgAtStop(" + busStop + ")"));
		if(busStop == mDestinationBusStop){
			mState = PersonState.exitingBus;
			bus.msgImOff(this);
		}
	}

	public String getName() {
		return "MockRider" + sRiderNum;
	}
}
