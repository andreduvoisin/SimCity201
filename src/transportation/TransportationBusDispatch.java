package transportation;

import java.util.*;

import city.gui.CityBus;
import base.Location;
import base.Agent;
import transportation.interfaces.TransportationRider;

/**
 * The controller who handles people waiting at bus stops, boarding buses, and
 * the buses themselves
 */
public class TransportationBusDispatch extends Agent {

	public static TransportationBusDispatch instance = null;

	// ==================================================================================
	// ------------------------------------- DATA ---------------------------------------
	// ==================================================================================

	TransportationBusInstance mBus;
	public List<TransportationBusStop> mBusStops = new ArrayList<TransportationBusStop>();


	// ==================================================================================
	// ----------------------------------- MESSAGES -------------------------------------
	// ==================================================================================

	/**
	 * From GUI - bus arrived at stop
	 * @param busNum BusInstance.mBusNumber: the number of the bus
	 */
	public void msgGuiArrivedAtStop() {
		//print("msgGuiArrivedAtStop()");

		mBus.state = TransportationBusInstance.enumState.readyToUnload;

		// If no buses are busy, run scheduler
		if (NoBusesBusy()) {
			stateChanged();
		}
	}

	/**
	 * From Person who arrived at a bus stop
	 * @param p The Person who arrived at the stop
	 * @param riderCurrentStop The stop number the Person is at
	 */
	public void msgNeedARide(TransportationRider r, int riderCurrentStop) {
		print("msgNeedARide(current stop: " + riderCurrentStop + ")");
		mBusStops.get(riderCurrentStop).mWaitingPeople.add(r);
		stateChanged();
	}

	/**
	 * From Person who boarded the bus
	 * @param p The Person who boarded
	 * @param riderLocation The stop number the Person is at
	 * @param riderDestination The stop number the Person is going to
	 */
	public void msgImOn(TransportationRider r) {
		print("msgImOn()");

		mBusStops.get(r.getStop()).mWaitingPeople.remove(r);

		if (mBus.mCurrentStop == r.getStop()) {
			mBus.mRiders.add(r);

			if (mBusStops.get(r.getStop()).mWaitingPeople.isEmpty()) {
				mBus.state = TransportationBusInstance.enumState.readyToTravel;
			}
		}

		// If all riders everywhere are boarded, run scheduler
		if (NoBusesBusy()) {
			stateChanged();
		}
	}

	/**
	 * From Person who got off the bus
	 * @param p The Person who got off
	 */
	public void msgImOff(TransportationRider r) {
		print("msgImOff()");

		// Remove rider from correct bus's rider list
			for (TransportationRider iRider : mBus.mRiders) {
				if (iRider.equals(r)) {
					mBus.mRiders.remove(iRider);
					break;
				}
			}

			// If more riders need to get off here, do nothing (wait for them)
			for (TransportationRider iRider : mBus.mRiders) {
				if (iRider.getDestination() == mBus.mCurrentStop) {
					return;
				}
			}

			// Otherwise change state and run scheduler to board waiting people
			mBus.state = TransportationBusInstance.enumState.readyToBoard;

		if (NoBusesBusy()) {
			stateChanged();
		}
	}


	// ==================================================================================
	// ----------------------------------- SCHEDULER ------------------------------------
	// ==================================================================================

	public boolean pickAndExecuteAnAction() {
		if (mBus.state.equals(TransportationBusInstance.enumState.readyToUnload)) {
			if (! mBus.mRiders.isEmpty()) {
				TellRidersToGetOff();
			}
			else {
				mBus.state = TransportationBusInstance.enumState.readyToBoard;
				return true;
			}
		}

		if (mBus.state.equals(TransportationBusInstance.enumState.readyToBoard)) {
			if (! mBusStops.get(mBus.mCurrentStop).mWaitingPeople.isEmpty()) {
				TellRidersToBoard();
				return true;
			}
			else {
				mBus.state = TransportationBusInstance.enumState.readyToTravel;
				return true;
			}
		}
		
		if (mBus.state.equals(TransportationBusInstance.enumState.readyToTravel)) {
			AdvanceToNextStop();
			return true;
		}

		return false;
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
		//print("TellRidersToGetOff()");

			mBus.state = TransportationBusInstance.enumState.unloading;
			boolean needToWait = false;
	
			for (TransportationRider iRider : mBus.mRiders) {
				if (iRider.getDestination() == mBus.mCurrentStop) {
					needToWait = true;
					iRider.msgAtYourStop();
				}
			}

			if (! needToWait) mBus.state = TransportationBusInstance.enumState.readyToBoard;

		// If no buses have riders to unload move on in scheduler
		if (NoBusesBusy()) {
			stateChanged();
		}
	}

	/**
	 * Instructs all people waiting at each bus's current stop to get on that bus
	 * @param bus BusInstance of which to check current stop's waiting list
	 */
	private void TellRidersToBoard() {
		print("TellRidersToBoard()");

			if (mBusStops.get(mBus.mCurrentStop).mWaitingPeople.isEmpty()) {
				mBus.state = TransportationBusInstance.enumState.readyToTravel;
			}
			else {
				mBus.state = TransportationBusInstance.enumState.boarding;

				for (TransportationRider r : mBusStops.get(mBus.mCurrentStop).mWaitingPeople) {
					r.msgBoardBus();
				}
			}

		// If no buses have riders to board move on in scheduler
		if (NoBusesBusy()) {
			stateChanged();
		}
	}

	/**
	 * Instructs a bus to move forward to the next stop
	 * @param bus BusInstance to advance
	 */
	private void AdvanceToNextStop() {
		//print("AdvanceToNextStop()");

		mBus.state = TransportationBusInstance.enumState.traveling;

		// Gui has a list of bus stop coordinates
		mBus.mGui.DoAdvanceToNextStop();
	}

	private boolean NoBusesBusy() {
		//print("NoBusesBusy?");

			if (mBus.isBusy()) {
				//print("False!");
				return false;
			}
		//print("True!");
		return true;
	}

	public void addBus(TransportationBusInstance tbi) {
		//print("addBus()");
//CHASE
		//mBus.add(tbi);
	}

	public String getName() {
		return "BusDispatch";
	}

	/**
	 * @return Most recently added BusInstance's GUI
	 */
	public CityBus getBusGui() {
		return mBus.mGui;
	}

	public int getBusStopClosestTo(Location loc) {
		double distance = 360000;
		int shortest = 0;
		for (int i = 0; i < mBusStops.size(); i++) {
			double d = Math.sqrt(Math.pow((mBusStops.get(i).location.mX - loc.mX), 2)
						+ Math.pow((mBusStops.get(i).location.mY - loc.mY), 2));
			if (d < distance) {
				distance = d;
				shortest = i;
			}
		}
		return shortest;
	}

	public static TransportationBusDispatch getInstance() {
		if (instance == null) return new TransportationBusDispatch();
		return instance;
	}

	public TransportationBusDispatch() {
		List<Location> busStops = base.ContactList.cBUS_STOPS;
		for (Location iL : busStops) {
			mBusStops.add(new TransportationBusStop(iL));
		}
		
		instance = this;

		mBus = new TransportationBusInstance(this, busStops);
	}
}