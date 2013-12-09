package transportation.test.mock;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import transportation.interfaces.TransportationRider;
import transportation.roles.CommuterRole.PersonBusState;

public class MockRider implements TransportationRider {

	public EventLog log = new EventLog(); 
	public int mCurrentBusStop, mDestinationBusStop; 
	public PersonBusState mState;
	private static int sRiderNum = 0;

	public MockRider() {
		sRiderNum++;
	}

	public void msgAtBusStop(int currentStop, int destinationStop){
		log.add(new LoggedEvent("Received msgAtBusStop(current stop = " + currentStop + ", destination stop = " + destinationStop + ")"));
		mCurrentBusStop = currentStop;
		mDestinationBusStop = destinationStop; 
		mState = PersonBusState.atBusStop;
	}
	
	public void msgBoardBus() {
		log.add(new LoggedEvent("Received msgBoardBus"));
		mState = PersonBusState.boardingBus;
	}
	
	public void msgAtStop(int busStop){
		log.add(new LoggedEvent("Received msgAtStop(bus's stop = " + busStop + ")"));
		if(busStop == mDestinationBusStop){
			mState = PersonBusState.noBus; 
		}
	}

	public String getName() {
		return "MockRider" + sRiderNum;
	}
}
