package transportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import test.mock.*;
import transportation.interfaces.TransportationRider;
import transportation.roles.CommuterRole;
import base.Agent;
import city.gui.CityBus;
import city.gui.trace.AlertTag;

/**
 * The controller who handles people waiting at bus stops, boarding buses, and
 * the buses themselves
 */
public class TransportationBus extends Agent {

	public static TransportationBus instance = null;
	public boolean testing;
	public EventLog log = new EventLog();

	public TransportationBus(boolean testBus) {
		instance = this;
		testing = testBus;

		if (!testing) mGui = new CityBus(this);

		for (int i = 0; i < base.ContactList.cBUS_STOPS.size(); i++) {
			mBusStops.add(new TransportationBusStop());
		}

		mCurrentStop = 0;
		state = enumState.ReadyToBoard;
	}

	// ==================================================================================
	// ------------------------------------- DATA ---------------------------------------
	// ==================================================================================

	public List<TransportationBusStop> mBusStops = new ArrayList<TransportationBusStop>();
	public List<TransportationRider> mRiders = Collections.synchronizedList(new ArrayList<TransportationRider>());
	CityBus mGui;
	int mCurrentStop;
	
	public enum enumState { traveling, ReadyToTravel, ReadyToUnload, ReadyToBoard }
	public enumState state;
	
	private Timer timer = new Timer(); 


	// ==================================================================================
	// ----------------------------------- MESSAGES -------------------------------------
	// ==================================================================================

	/**
	 * From GUI - bus arrived at stop
	 */
	public void msgGuiArrivedAtStop() {
		mCurrentStop = (mCurrentStop + 1) % mBusStops.size();
		state = enumState.ReadyToUnload;
		stateChanged();

		log.add(new LoggedEvent("Received msgGuiArrivedAtStop(" + mCurrentStop + ")"));
	}

	/**
	 * From Person who arrived at a bus stop
	 * @param p The Person who arrived at the stop
	 * @param riderCurrentStop The stop number the Person is at
	 */
	public void msgNeedARide(TransportationRider r, int riderCurrentStop) {
		log.add(new LoggedEvent("Received msgNeedARide(" + r.getName() + ", " + riderCurrentStop + ")"));
		synchronized(mBusStops.get(riderCurrentStop).mWaitingPeople) {
			mBusStops.get(riderCurrentStop).mWaitingPeople.add(r);
		}
	}

	/**
	 * From Person who boarded the bus
	 * @param p The Person who boarded
	 * @param riderLocation The stop number the Person is at
	 * @param riderDestination The stop number the Person is going to
	 */
	public void msgImOn(TransportationRider r) {
		log.add(new LoggedEvent("Received msgImOn(" + ((CommuterRole)r).getName() + ")"));

		synchronized(mBusStops.get(mCurrentStop).mWaitingPeople) {
			mBusStops.get(mCurrentStop).mWaitingPeople.remove(r);
		}
		synchronized(mRiders) {
			mRiders.add(r);
		}
	}

	/**
	 * From Person who got off the bus
	 * @param p The Person who got off
	 */
	public void msgImOff(TransportationRider r) {
		log.add(new LoggedEvent("Received msgImOff()"));

		// Remove rider from rider list
		synchronized(mRiders) {
			mRiders.remove(r);
		}
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
			return false;
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
		log.add(new LoggedEvent(("TellRidersToGetOff()")));

		synchronized(mRiders) {
			for (TransportationRider iRider : mRiders) {
				iRider.msgAtStop(mCurrentStop);
			}
		}

		state = enumState.ReadyToBoard;
		stateChanged();
	}

	/**
	 * Instructs all people waiting at each bus's current stop to get on that bus
	 * @param bus BusInstance of which to check current stop's waiting list
	 */
	private void TellRidersToBoard() {
		log.add(new LoggedEvent(("TellRidersToBoard()")));
		
		synchronized(mBusStops.get(mCurrentStop).mWaitingPeople) {
			for (TransportationRider r : mBusStops.get(mCurrentStop).mWaitingPeople) {
				r.msgBoardBus();
			}
		}
		mBusStops.get(mCurrentStop).mWaitingPeople.clear();

		state = enumState.ReadyToTravel;
		stateChanged();
	}

	/**
	 * Instructs a bus to move forward to the next stop
	 * @param bus BusInstance to advance
	 */
	private void AdvanceToNextStop() {
		log.add(new LoggedEvent(("AdvanceToNextStop()")));

		state = enumState.traveling;

		timer.schedule(new TimerTask(){
			public void run(){
				mGui.DoAdvanceToNextStop();
			}
		}, 2000); 
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