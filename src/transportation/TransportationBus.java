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
public class TransportationBus extends Agent {

	public static TransportationBus instance = null;

	public TransportationBus() {
		instance = this;

		mGui = new CityBus(this, mCurrentStop);

		for (Location iLoc: base.ContactList.cBUS_STOPS) {
			mBusStops.add(new TransportationBusStop(iLoc));
		}

		mCurrentStop = 0;
		state = enumState.ReadyToBoard;
	}

	// ==================================================================================
	// ------------------------------------- DATA ---------------------------------------
	// ==================================================================================

	public List<TransportationBusStop> mBusStops = new ArrayList<TransportationBusStop>();
	public List<TransportationRider> mRiders = new ArrayList<TransportationRider>();
	CityBus mGui;
	int mCurrentStop;
	
	enum enumState { ReadyToTravel, ReadyToUnload, ReadyToBoard }
	enumState state;


	// ==================================================================================
	// ----------------------------------- MESSAGES -------------------------------------
	// ==================================================================================

	/**
	 * From GUI - bus arrived at stop
	 */
	public void msgGuiArrivedAtStop() {
		print("msgGuiArrivedAtStop(" + mCurrentStop + ")");

		state = enumState.ReadyToUnload;
		stateChanged();
	}

	/**
	 * From Person who arrived at a bus stop
	 * @param p The Person who arrived at the stop
	 * @param riderCurrentStop The stop number the Person is at
	 */
	public void msgNeedARide(TransportationRider r, int riderCurrentStop) {
		print("msgNeedARide(current stop: " + riderCurrentStop + ")");
		mBusStops.get(riderCurrentStop).mWaitingPeople.add(r);
	}

	/**
	 * From Person who boarded the bus
	 * @param p The Person who boarded
	 * @param riderLocation The stop number the Person is at
	 * @param riderDestination The stop number the Person is going to
	 */
	public void msgImOn(TransportationRider r) {
		print("msgImOn()");

		mBusStops.get(mCurrentStop).mWaitingPeople.remove(r);
		mRiders.add(r);
	}

	/**
	 * From Person who got off the bus
	 * @param p The Person who got off
	 */
	public void msgImOff(TransportationRider r) {
		print("msgImOff()");

		// Remove rider from rider list
		mRiders.remove(r);
	}


	// ==================================================================================
	// ----------------------------------- SCHEDULER ------------------------------------
	// ==================================================================================

	public boolean pickAndExecuteAnAction() {
		if (state == enumState.ReadyToUnload) {
			TellRidersToGetOff();
			return true;
		}

		if (state == enumState.ReadyToBoard) {
			TellRidersToBoard();
			return true;
		}
		
		if (state == enumState.ReadyToTravel) {
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

		for (TransportationRider iRider : mRiders) {
			iRider.msgAtStop(mCurrentStop);
		}

		state = enumState.ReadyToBoard;
		stateChanged();

	}

	/**
	 * Instructs all people waiting at each bus's current stop to get on that bus
	 * @param bus BusInstance of which to check current stop's waiting list
	 */
	private void TellRidersToBoard() {
		print("TellRidersToBoard()");

		for (TransportationRider r : mBusStops.get(mCurrentStop).mWaitingPeople) {
			r.msgBoardBus();
		}

		state = enumState.ReadyToTravel;
		stateChanged();
	}

	/**
	 * Instructs a bus to move forward to the next stop
	 * @param bus BusInstance to advance
	 */
	private void AdvanceToNextStop() {
		//print("AdvanceToNextStop()");

		mCurrentStop++;
		state = enumState.ReadyToTravel;
		mGui.DoAdvanceToNextStop();
	}

	public String getName() {
		return "Bus";
	}

	/**
	 * @return Bus's GUI (CityBus object)
	 */
	public CityBus getBusGui() {
		return mGui;
	}

	public static TransportationBus getInstance() {
		if (instance == null) return new TransportationBus();
		return instance;
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
}