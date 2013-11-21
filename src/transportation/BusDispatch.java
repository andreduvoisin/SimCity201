package transportation;

import base.interfaces.Person;

import java.util.concurrent.Semaphore;
import java.util.*;

/**
 * The controller who handles people waiting at bus stops, boarding buses, and
 * the buses themselves
 */
public class BusDispatch {

	// Reference to the GUIs
	private ArrayList<BusInstance> mBuses = new ArrayList<BusInstance>();


	// ==================================================================================
	// ------------------------------------- DATA ---------------------------------------
	// ==================================================================================

	private ArrayList<BusStop> mBusStops = new ArrayList<BusStop>();
	private Semaphore semAtLeastOneBusy = new Semaphore(0, true);


	// ==================================================================================
	// ----------------------------------- MESSAGES -------------------------------------
	// ==================================================================================

	/**
	 * From GUI - bus arrived at stop
	 * @param busNum BusInstance.mBusNumber: the number of the bus
	 */
	public void msgGuiArrivedAtStop(int busNum) {
		mBuses.get(busNum).state = BusInstance.enumState.readyToUnload;

		// If no buses are busy, run scheduler
		if (NoBusesBusy()) semAtLeastOneBusy.release();
	}

	/**
	 * From Person who arrived at a bus stop
	 * @param p The Person who arrived at the stop
	 * @param riderCurrentStop The stop number the Person is at
	 */
	public void msgNeedARide(Person p, int riderCurrentStop) {
		mBusStops.get(riderCurrentStop).mWaitingPeople.add(p);
	}

	/**
	 * From Person who boarded the bus
	 * @param p The Person who boarded
	 * @param riderLocation The stop number the Person is at
	 * @param riderDestination The stop number the Person is going to
	 */
	public void msgGoingTo(Person p, int riderLocation, int riderDestination) {
		for (BusInstance iBus : mBuses) {
			if (iBus.mCurrentStop == riderLocation) {
				iBus.mRiders.add(new Rider(p, riderDestination));
				mBusStops.get(riderLocation).mWaitingPeople.remove(p);

				if (mBusStops.get(riderLocation).mWaitingPeople.size() == 0) {
					iBus.state = BusInstance.enumState.readyToTravel;
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
	public void msgImOff(Person p) {
		// Remove rider from correct bus's rider list
		for (BusInstance iBus : mBuses) {
			for (Rider iRider : iBus.mRiders) {
				if (iRider.mPerson.equals(p)) {
					iBus.mRiders.remove(iRider);
					break;
				}
			}

			// If more riders need to get off here, do nothing (wait for them)
			for (Rider iRider : iBus.mRiders) {
				if (iRider.mDestination == iBus.mCurrentStop) {
					return;
				}
			}

			// Otherwise change state and run scheduler to board waiting people
			iBus.state = BusInstance.enumState.readyToBoard;
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

			for (BusInstance iBus : mBuses) {
				if (iBus.state.equals(BusInstance.enumState.readyToUnload)) {
					if (iBus.mRiders.size() > 0) {
						TellRidersToGetOff();
						break;
					}
				}
			}

			for (BusInstance iBus : mBuses) {
				if (iBus.state.equals(BusInstance.enumState.readyToBoard)) {
					if (mBusStops.get(iBus.mCurrentStop).mWaitingPeople.size() > 0) {
						TellRidersToBoard();
						break;
					}
				}
			}

			for (BusInstance iBus : mBuses) {
				if (iBus.state.equals(BusInstance.enumState.readyToTravel)) {
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
		for (BusInstance iBus : mBuses) {
			iBus.state = BusInstance.enumState.unloading;
			boolean needToWait = false;
	
			for (Rider iRider : iBus.mRiders) {
				if (iRider.mDestination == iBus.mCurrentStop) {
					needToWait = true;
					// CHASE: Implement Person.msgAtYourStop() : iRider.mPerson.msgAtYourStop();
				}
			}

			if (! needToWait) iBus.state = BusInstance.enumState.readyToBoard;
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
		for (BusInstance iBus : mBuses) {
			if (mBusStops.get(iBus.mCurrentStop).mWaitingPeople.size() > 0) {
				iBus.state = BusInstance.enumState.boarding;
		
				for (Person p : mBusStops.get(iBus.mCurrentStop).mWaitingPeople) {
					// CHASE: Implement Person.msgBoardBus(BusDispatch) : p.msgBoardBus(this);
				}
			}
			else {
				iBus.state = BusInstance.enumState.readyToTravel;
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
		for (BusInstance iBus : mBuses) {
			iBus.state = BusInstance.enumState.traveling;

			// Gui has a list of bus stop coordinates
			iBus.mGui.DoAdvanceToNextStop();
			iBus.mCurrentStop = (iBus.mCurrentStop + 1) % mBusStops.size();
		}
		
		try { semAtLeastOneBusy.acquire(); } catch(Exception e) {}
	}



	private boolean NoBusesBusy() {
		for (BusInstance iBus : mBuses) {
			if (iBus.isBusy()) return false;
		}

		return true;
	}
}