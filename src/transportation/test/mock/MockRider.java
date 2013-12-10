package transportation.test.mock;

import test.mock.LoggedEvent;
import test.mock.Mock;
import transportation.interfaces.TransportationRider;
import transportation.roles.CommuterRole.PersonState;

public class MockRider extends Mock implements TransportationRider {

	public int mCurrentBusStop, mDestinationBusStop; 
	public PersonState mState;
	private static int sRiderNum = 0;

	public MockRider() {
		sRiderNum++;
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
	}
	
	public void msgAtStop(int busStop){
		log.add(new LoggedEvent("Received msgAtStop(bus's stop = " + busStop + ")"));
		if(busStop == mDestinationBusStop){
			mState = PersonState.noNewDestination; 
		}
	}

	public String getName() {
		return "MockRider" + sRiderNum;
	}
}
