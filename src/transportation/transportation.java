package transportation;

import java.util.*;

public class Bus {

	// DATA

	ArrayList<BusStop> mBusStops = new ArrayList<BusStop>();
	ArrayList<Rider> mRiders = new ArrayList<Rider>();
	int mBusCurrentStop;

	class BusStop {
		List<Person> mWaitingPeople;
	}

	class Rider {
		Person mPerson;
		int mDestination;
	}


	// MESSAGES

	// From Person upon arriving at a bus stop
	public void msgNeedARide(Person p, int riderCurrentStop) {
		mBusStops.get(riderCurrentStop).waitingPeople.add(p);
	}

	// From Person upon boarding the bus
	public void msgGoingTo(Person p, int riderDestination) {
		mRiders.add(new Rider(p, riderDestination));
		mBusStops.get(mBusCurrentStop).remove(p);
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

	if (mRiders.size() > 0) {
		TellRidersToGetOff();
	}

	if (mBusStops.get(mBusCurrentStop).mWaitingPeople.size() > 0) {
		TellRidersToBoard();
	}
	else {
		AdvanceToNextStop();
	}


	// ACTIONS

	private void TellRidersToGetOff() {
		for (Rider r : mRiders) {
			if (r.mDestination == mBusCurrentStop) {
				r.msgAtYourStop();
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