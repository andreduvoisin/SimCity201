package transportation;

import base.interfaces.Person;

import java.util.*;

/**
 * The controller who handles people waiting at bus stops,
 * boarding buses, and the buses themselves
 */
public class BusDispatch {

	// Reference to the GUIs
	private ArrayList<BusInstance> mBuses = new ArrayList<BusInstance>();


	//==================================================================================
	//------------------------------------- DATA ---------------------------------------
	//==================================================================================

	private ArrayList<BusStop> mBusStops = new ArrayList<BusStop>();


	//==================================================================================
	//----------------------------------- MESSAGES -------------------------------------
	//==================================================================================

	/**
	 * From GUI - bus arrived at stop
	 * @param busNum BusInstance.mBusNumber: the number of the bus
	 */
	public void msgGuiArrivedAtStop(int busNum) {
		mBuses.get(busNum).state = BusInstance.enumState.readyToUnload;
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
				return;
			}
		}
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
					if (iBus.mRiders.size() == 0) {
						iBus.state = BusInstance.enumState.readyToBoard;
					}
					return;
				}
			}
		}
	}

	//==================================================================================
	//----------------------------------- SCHEDULER ------------------------------------
	//==================================================================================

	public void pickAndExecuteAnAction() {

		// Bus ALWAYS running
		while (true) {
			for (BusInstance iBus : mBuses) {
				// Instruct riders to get off if this is their stop
				if (iBus.mRiders.size() > 0) {
					TellRidersToGetOff(iBus);
					break;
				}
			}

			for (BusInstance iBus : mBuses) {
				// Instruct waiting customers at the current stop to board 
				if (mBusStops.get(iBus.mCurrentStop).mWaitingPeople.size() > 0) {
					// Pauses this thread until all people waiting at this stop have boarded
					TellRidersToBoard(iBus);
					break;
				}
			}

			for (BusInstance iBus : mBuses) {
				// Pauses this thread until GUI arrives at next stop
				AdvanceToNextStop(iBus);
			}
		}
	}


	//==================================================================================
	//----------------------------------- ACTIONS --------------------------------------
	//==================================================================================

	/**
	 * Instructs all riders (whose destination is their bus's current stop) to get off that bus
	 * @param bus BusInstance of which to check rider list
	 */
	private void TellRidersToGetOff(BusInstance bus) {
		bus.state = BusInstance.enumState.unloading;

		for (Rider iRider : bus.mRiders) {
			if (iRider.mDestination == bus.mCurrentStop) {
				// TODO iRider.mPerson.msgAtYourStop();
			}
		}
	}

	/**
	 * Instructs all people waiting at each bus's current stop to get on that bus
	 * @param bus BusInstance of which to check current stop's waiting list
	 */
	private void TellRidersToBoard(BusInstance bus) {
		bus.state = BusInstance.enumState.boarding;

		for (Person p : mBusStops.get(bus.mCurrentStop).mWaitingPeople) {
			// TODO p.msgBoardBus(this);
		}
	}

	/**
	 * Instructs a bus to move forward to the next stop
	 * @param bus BusInstance to advance
	 */
	private void AdvanceToNextStop(BusInstance bus) {
		bus.state = BusInstance.enumState.traveling;

		// Gui has a list of bus stop coordinates
		bus.mGui.DoAdvanceToNextStop();
		bus.mCurrentStop = (bus.mCurrentStop + 1) % mBusStops.size();
	}


	//==================================================================================
	//------------------------------ ACCESSORS/UTILITIES -------------------------------
	//==================================================================================
}