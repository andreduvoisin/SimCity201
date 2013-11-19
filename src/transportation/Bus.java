package transportation;

import base.interfaces.Person;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Bus {

	// For moving between stops (bus is always running)
	private Semaphore semAtStop = new Semaphore(0, true);

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

	// From Person upon arriving at a bus stop
	public void msgNeedARide(Person p, int riderCurrentStop) {
		mBusStops.get(riderCurrentStop).mWaitingPeople.add(p);
	}

	// From Person upon boarding the bus
	public void msgGoingTo(Person p, int riderDestination) {
		mRiders.add(new Rider(p, riderDestination));
		mBusStops.get(mBusCurrentStop).mWaitingPeople.remove(p);
	}

	// From Person upon getting off the bus
	public void msgImOff(Person p) {
		for (Rider r : mRiders) {
			if (r.mPerson.equals(p)) {
				mRiders.remove(r);
			}
		}
	}


	// SCHEDULER

	public boolean pickAndExecuteAnAction() {

		// Instruct riders to get off if this is their stop
		if (mRiders.size() > 0) {
			TellRidersToGetOff();
			return true;
		}

		// Instruct waiting customers at the current stop to board 
		if (mBusStops.get(mBusCurrentStop).mWaitingPeople.size() > 0) {
			TellRidersToBoard();
			return true;
		}
		else {
			AdvanceToNextStop();
			return true;
		}
	}


	// ACTIONS

	private void TellRidersToGetOff() {
		for (Rider r : mRiders) {
			if (r.mDestination == mBusCurrentStop) {
				r.mPerson.msgAtYourStop();
			}
		}
	}

	private void TellRidersToBoard() {
		for (Person p : mBusStops.get(mBusCurrentStop).mWaitingPeople) {
			p.msgBoardBus();
		}
	}

	private void AdvanceToNextStop() {
		// TODO
		// Semaphore here to keep bus from telling riders board/get off while
		// moving between stops

		// Gui has a list of bus stop coordinates
		// gui.DoAdvance()
		mBusCurrentStop++;
	}
}