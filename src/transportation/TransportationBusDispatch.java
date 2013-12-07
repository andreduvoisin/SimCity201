package transportation;

import java.util.ArrayList;
import java.util.List;

import transportation.interfaces.TransportationRider;
import base.Agent;
import base.Location;
import city.gui.CityBus;
import city.gui.trace.AlertTag;

/**
 * The controller who handles people waiting at bus stops, boarding buses, and
 * the buses themselves
 */
public class TransportationBusDispatch extends Agent {

	public static TransportationBusDispatch instance = null;

	// ==================================================================================
	// ------------------------------------- DATA ---------------------------------------
	// ==================================================================================

	public List<TransportationBusStop> mBusStops = new ArrayList<TransportationBusStop>();
	public List<TransportationRider> mRiders = new ArrayList<TransportationRider>();
	CityBus mGui;
	int mCurrentStop;
	
	enum enumState { readyToTravel, traveling, readyToUnload, unloading, readyToBoard, boarding }
	enumState state;


	// ==================================================================================
	// ----------------------------------- MESSAGES -------------------------------------
	// ==================================================================================

	/**
	 * From GUI - bus arrived at stop
	 * @param busNum BusInstance.mBusNumber: the number of the bus
	 */
	public void msgGuiArrivedAtStop() {
		//print("msgGuiArrivedAtStop()");

		state = enumState.readyToUnload;

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

		if (mCurrentStop == r.getStop()) {
			mRiders.add(r);

			if (mBusStops.get(r.getStop()).mWaitingPeople.isEmpty()) {
				state = enumState.readyToTravel;
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
			for (TransportationRider iRider : mRiders) {
				if (iRider.equals(r)) {
					mRiders.remove(iRider);
					break;
				}
			}

			// If more riders need to get off here, do nothing (wait for them)
			for (TransportationRider iRider : mRiders) {
				if (iRider.getDestination() == mCurrentStop) {
					return;
				}
			}

			// Otherwise change state and run scheduler to board waiting people
			state = enumState.readyToBoard;

		if (NoBusesBusy()) {
			stateChanged();
		}
	}


	// ==================================================================================
	// ----------------------------------- SCHEDULER ------------------------------------
	// ==================================================================================

	public boolean pickAndExecuteAnAction() {
		if (state.equals(enumState.readyToUnload)) {
			if (! mRiders.isEmpty()) {
				TellRidersToGetOff();
			}
			else {
				state = enumState.readyToBoard;
				return true;
			}
		}

		if (state.equals(enumState.readyToBoard)) {
			if (! mBusStops.get(mCurrentStop).mWaitingPeople.isEmpty()) {
				TellRidersToBoard();
				return true;
			}
			else {
				state = enumState.readyToTravel;
				return true;
			}
		}
		
		if (state.equals(enumState.readyToTravel)) {
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

			state = enumState.unloading;
			boolean needToWait = false;
	
			for (TransportationRider iRider : mRiders) {
				iRider.msgAtStop(mCurrentStop);
			}

			state = enumState.readyToBoard;

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

			if (mBusStops.get(mCurrentStop).mWaitingPeople.isEmpty()) {
				state = enumState.readyToTravel;
			}
			else {
				state = enumState.boarding;

				for (TransportationRider r : mBusStops.get(mCurrentStop).mWaitingPeople) {
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

		state = enumState.traveling;

		// Gui has a list of bus stop coordinates
		mGui.DoAdvanceToNextStop();
	}

	private boolean NoBusesBusy() {
		//print("NoBusesBusy?");

			if (this.isBusy()) {
				//print("False!");
				return false;
			}
		//print("True!");
		return true;
	}

	public String getName() {
		return "BusDispatch";
	}

	/**
	 * @return Most recently added BusInstance's GUI
	 */
	public CityBus getBusGui() {
		return mGui;
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
		instance = this;

		mGui = new CityBus(this);

		mCurrentStop = 0;
		state = enumState.traveling;
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.TRANSPORTATION);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.TRANSPORTATION);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.TRANSPORTATION, e);
	}

	public boolean isBusy() {
		return state.equals(enumState.traveling)
				|| state.equals(enumState.unloading)
				|| state.equals(enumState.boarding);
	}
}