package transportation;

import java.util.concurrent.Semaphore;
import java.util.*;

import transportation.interfaces.TransportationRider;

/**
 * The controller who handles people waiting at bus stops, boarding buses, and
 * the buses themselves
 */
public class TransportationBusDispatch {

	// Reference to the GUIs
	private ArrayList<TransportationBusInstance> mBuses = new ArrayList<TransportationBusInstance>();


	// ==================================================================================
	// ------------------------------------- DATA ---------------------------------------
	// ==================================================================================

	private ArrayList<TransportationBusStop> mBusStops = new ArrayList<TransportationBusStop>();
	private Semaphore semAtLeastOneBusy = new Semaphore(0, true);


	// ==================================================================================
	// ----------------------------------- MESSAGES -------------------------------------
	// ==================================================================================

	/**
	 * From GUI - bus arrived at stop
	 * @param busNum BusInstance.mBusNumber: the number of the bus
	 */
	public void msgGuiArrivedAtStop(int busNum) {
		mBuses.get(busNum).state = TransportationBusInstance.enumState.readyToUnload;

		// If no buses are busy, run scheduler
		if (NoBusesBusy()) semAtLeastOneBusy.release();
	}

	/**
	 * From Person who arrived at a bus stop
	 * @param p The Person who arrived at the stop
	 * @param riderCurrentStop The stop number the Person is at
	 */
	public void msgNeedARide(TransportationRider r, int riderCurrentStop) {
		mBusStops.get(riderCurrentStop).mWaitingPeople.add(r);
	}

	/**
	 * From Person who boarded the bus
	 * @param p The Person who boarded
	 * @param riderLocation The stop number the Person is at
	 * @param riderDestination The stop number the Person is going to
	 */
	public void msgImOn(TransportationRider r) {
		mBusStops.get(r.getLocation()).mWaitingPeople.remove(r);

		for (TransportationBusInstance iBus : mBuses) {
			if (iBus.mCurrentStop == r.getLocation()) {
				iBus.mRiders.add(r);

				if (mBusStops.get(r.getLocation()).mWaitingPeople.isEmpty()) {
					iBus.state = TransportationBusInstance.enumState.readyToTravel;
				}
			}
		}

		// If all riders everywhere are boarded, run scheduler
		if (NoBusesBusy()) semAtLeastOneBusy.release();
	}

	/**
	 * From Person who got off the bus
	 * @param p The Person who got off
	 */
	public void msgImOff(TransportationRider r) {
		// Remove rider from correct bus's rider list
		for (TransportationBusInstance iBus : mBuses) {
			for (TransportationRider iRider : iBus.mRiders) {
				if (iRider.equals(r)) {
					iBus.mRiders.remove(iRider);
					break;
				}
			}

			// If more riders need to get off here, do nothing (wait for them)
			for (TransportationRider iRider : iBus.mRiders) {
				if (iRider.getDestination() == iBus.mCurrentStop) {
					return;
				}
			}

			// Otherwise change state and run scheduler to board waiting people
			iBus.state = TransportationBusInstance.enumState.readyToBoard;
			break;
		}

		if (NoBusesBusy()) semAtLeastOneBusy.release();
	}


	// ==================================================================================
	// ----------------------------------- SCHEDULER ------------------------------------
	// ==================================================================================

	public void pickAndExecuteAnAction() {

		// Bus always running
		while (true) {

			for (TransportationBusInstance iBus : mBuses) {
				if (iBus.state.equals(TransportationBusInstance.enumState.readyToUnload)) {
					if (! iBus.mRiders.isEmpty()) {
						TellRidersToGetOff();
						break;
					}
				}
			}

			for (TransportationBusInstance iBus : mBuses) {
				if (iBus.state.equals(TransportationBusInstance.enumState.readyToBoard)) {
					if (! mBusStops.get(iBus.mCurrentStop).mWaitingPeople.isEmpty()) {
						TellRidersToBoard();
						break;
					}
				}
			}

			for (TransportationBusInstance iBus : mBuses) {
				if (iBus.state.equals(TransportationBusInstance.enumState.readyToTravel)) {
					AdvanceToNextStop();
					break;
				}
			}
		}
	}


	// ==================================================================================
	// ----------------------------------- ACTIONS --------------------------------------
	// ==================================================================================

	/**
	 * Instructs all riders (whose destination is their bus's current stop) to
	 * get off that bus
	 * @param bus BusInstance of which to check rider list
	 */
	private void TellRidersToGetOff() {
		for (TransportationBusInstance iBus : mBuses) {
			iBus.state = TransportationBusInstance.enumState.unloading;
			boolean needToWait = false;
	
			for (TransportationRider iRider : iBus.mRiders) {
				if (iRider.getDestination() == iBus.mCurrentStop) {
					needToWait = true;
					iRider.msgAtYourStop();
				}
			}

			if (! needToWait) iBus.state = TransportationBusInstance.enumState.readyToBoard;
		}

		// If at least one buses has riders unloading wait for messages
		if (! NoBusesBusy()) {
			try { semAtLeastOneBusy.acquire(); } catch(Exception e) {}
		}
	}

	/**
	 * Instructs all people waiting at each bus's current stop to get on that bus
	 * @param bus BusInstance of which to check current stop's waiting list
	 */
	private void TellRidersToBoard() {
		for (TransportationBusInstance iBus : mBuses) {
			if (mBusStops.get(iBus.mCurrentStop).mWaitingPeople.isEmpty()) {
				iBus.state = TransportationBusInstance.enumState.readyToTravel;
			}
			else {
				iBus.state = TransportationBusInstance.enumState.boarding;

				for (TransportationRider r : mBusStops.get(iBus.mCurrentStop).mWaitingPeople) {
					r.msgBoardBus();
				}
			}
		}

		// If at least one buses has riders boarding wait for messages
		if (! NoBusesBusy()) {
			try { semAtLeastOneBusy.acquire(); } catch(Exception e) {}
		}
	}

	/**
	 * Instructs a bus to move forward to the next stop
	 * @param bus BusInstance to advance
	 */
	private void AdvanceToNextStop() {
		for (TransportationBusInstance iBus : mBuses) {
			iBus.state = TransportationBusInstance.enumState.traveling;

			// Gui has a list of bus stop coordinates
			iBus.mGui.DoAdvanceToNextStop();
			iBus.mCurrentStop = (iBus.mCurrentStop + 1) % mBusStops.size();
		}
		
		try { semAtLeastOneBusy.acquire(); } catch(Exception e) {}
	}



	private boolean NoBusesBusy() {
		for (TransportationBusInstance iBus : mBuses) {
			if (iBus.isBusy()) return false;
		}

		return true;
	}
}