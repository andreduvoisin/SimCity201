package transportation.test.mock;

import test.mock.*;
import transportation.TransportationBusInstance;
import transportation.interfaces.*;

public class TransportationMockBusDispatch extends Mock implements TransportationBus {

	public void msgGuiArrivedAtStop(int busNum) {
		log.add(new LoggedEvent("Received msgGuiArrivedAtStop from bus " + busNum));
	}

	public void msgNeedARide(TransportationRider r, int riderCurrentStop) {
		// CHASE: get name
		log.add(new LoggedEvent("Received msgNeedARide"));
	}

	public void msgImOn(TransportationRider r) {
		// get name
		log.add(new LoggedEvent("Received msgImOn"));
	}

	public void msgImOff(TransportationRider r) {
		// get name
		log.add(new LoggedEvent("Received msgImOff"));
	}

	public boolean pickAndExecuteAnAction() {
		// CHASE mock scheduler
		return false;
	}

	public void TellRidersToGetOff() {
		log.add(new LoggedEvent("Called TellRidersToGetOff"));
	}

	public void TellRidersToBoard() {
		log.add(new LoggedEvent("Called TellRidersToBoard"));
	}

	public void AdvanceToNextStop() {
		log.add(new LoggedEvent("Called AdvanceToNextStop"));
	}

	public boolean NoBusesBusy() {
		// TODO Auto-generated method stub
		return false;
	}

	public void addBus(TransportationBusInstance tbi) {
		// TODO Auto-generated method stub
		
	}

	public String getName() {
		return "MockBusDispatch";
	}
}
