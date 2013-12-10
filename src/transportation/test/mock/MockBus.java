package transportation.test.mock;

import java.util.ArrayList;
import java.util.List;

import restaurant.restaurant_jerryweb.test.mock.LoggedEvent;
import test.mock.Mock;
import transportation.interfaces.Bus;
import transportation.interfaces.TransportationRider;

public class MockBus extends Mock implements Bus {
	
	public List<LoggedEvent> log = new ArrayList<LoggedEvent>();
	
	public MockBus() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgGuiArrivedAtStop() {
		log.add(new LoggedEvent("Message msgGuiArrivedAtStop called"));
	}

	@Override
	public void msgNeedARide(TransportationRider r, int riderCurrentStop) {
		log.add(new LoggedEvent("Rider " + r.getName() + " has sent msgNeedARide at stop " + riderCurrentStop));
	}

	@Override
	public void msgImOn(TransportationRider r) {
		log.add(new LoggedEvent("Rider " + r.getName() + " has sent msgImOn."));
	}

	@Override
	public void msgImOff(TransportationRider r) {
		log.add(new LoggedEvent("Rider " + r.getName() + " has sent msgImOff."));
	}
}
