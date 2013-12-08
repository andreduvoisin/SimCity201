package transportation.test.mock;

import java.util.ArrayList;
import java.util.List;

import restaurant.restaurant_jerryweb.test.mock.LoggedEvent;
import test.mock.Mock;
import transportation.interfaces.TransportationRider;
import transportation.roles.CommuterRole;

public class MockCommuter extends Mock implements TransportationRider {
	
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public MockCommuter() {
		
	}

	@Override
	public void msgBoardBus() {
		log.add(new LoggedEvent("Commuter is boarding the bus"));
		
	}

	@Override
	public void msgAtStop(int stopBusIsAt) {
		log.add(new LoggedEvent("The bus has reached the communter's desired stop: " + stopBusIsAt));
		
	}

	/*@Override
	public void NotifyBus() {
		log.add(new LoggedEvent("The notifyBus function has been called"));
		
	}*/

	@Override
	public void msgAtBusStop(int currentStop, int destinationStop) {
		log.add(new LoggedEvent("The msgAtBusStop has been called for the currentStop: " + currentStop + " and destinationStop: " 
				+ destinationStop));
	}
	
	

}
