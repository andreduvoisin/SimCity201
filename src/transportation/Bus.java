package transportation;

import base.interfaces.Person;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Bus {

	// For moving between stops (bus is always running)
	private Semaphore semAtStop = new Semaphore(0, true);

	// Reference to the GUI
	private BusGui mGui;

	// DATA

	private ArrayList<BusStop> mBusStops = new ArrayList<BusStop>();
	private ArrayList<Rider> mRiders = new ArrayList<Rider>();
	private int mBusCurrentStop;

	private class BusStop {
		List<Person> mWaitingPeople;
	}

	private class Rider {
		Person mPerson;
		int mDestination;
		
		public Rider(Person p, int dest) {
			mPerson = p;
			mDestination = dest;
		}
	}


	// MESSAGES

	// From GUI - bus arrived at stop
	public void msgGuiArrivedAtStop() {
		semAtStop.release();
	}

	// From Person who arrived at a bus stop
	public void msgNeedARide(Person p, int riderCurrentStop) {
		mBusStops.get(riderCurrentStop).mWaitingPeople.add(p);
	}

	// From Person who boarded the bus
	public void msgGoingTo(Person p, int riderDestination) {
		mRiders.add(new Rider(p, riderDestination));
		mBusStops.get(mBusCurrentStop).mWaitingPeople.remove(p);
	}

	// From Person who got off the bus
	public void msgImOff(Person p) {
		for (Rider r : mRiders) {
			if (r.mPerson.equals(p)) {
				mRiders.remove(r);
			}
		}

		int ridersStoppingHere = 0;
		for (Rider r : mRiders) {
			if (r.mDestination == mBusCurrentStop) {
				ridersStoppingHere++;
			}
		}
		
	}


	// SCHEDULER

	public void pickAndExecuteAnAction() {

		while (true) {
			// Instruct riders to get off if this is their stop
			if (mRiders.size() > 0) {
				// Pauses this thread until all riders (whose destination is here) are off
				TellRidersToGetOff();
			}
	
			// Instruct waiting customers at the current stop to board 
			if (mBusStops.get(mBusCurrentStop).mWaitingPeople.size() > 0) {
				TellRidersToBoard();
			}
			else {
				// Pauses this thread until GUI arrives at next stop
				AdvanceToNextStop();
			}
		}
	}


	// ACTIONS

	private void TellRidersToGetOff() {
		for (Rider r : mRiders) {
			if (r.mDestination == mBusCurrentStop) {
				// TODO r.mPerson.msgAtYourStop();
			}
		}
	}

	private void TellRidersToBoard() {
		for (Person p : mBusStops.get(mBusCurrentStop).mWaitingPeople) {
			// TODO p.msgBoardBus();
		}
	}

	private void AdvanceToNextStop() {
		// Gui has a list of bus stop coordinates
		mGui.DoAdvanceToNextStop();

		// Wait while GUI advances
		try { semAtStop.acquire(); } catch (Exception e) {}
		// semAtStop released when GUI calls msgGuiArrivedAtStop()

		mBusCurrentStop++;
	}


	// ACCESSORS/UTILITIES

	// TODO - setGui can (should?) be placed in base Agent
	public void setGui(BusGui bg) {
		mGui = bg;
	}
}