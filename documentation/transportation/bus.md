#Transportation Design Document

##Bus("driver") Agent

###Data

    List<BusStop> mBusStops

    // If realistic, the bus needs a rider limit
    List<Rider> mRiders

    class BusStop {
        List<Person> mWaitingPeople
    }

    class Rider {
        Person mPerson
        int mDestination
    }

    int mBusCurrentStop


###Messages

    // From Person upon arriving at a bus stop
    msgNeedARide(Person p, int riderCurrentStop) {
        mBusStops.get(riderCurrentStop).waitingPeople.add(p)
    }

    // From Person upon boarding the bus
    msgGoingTo(Person p, int riderDestination) {
        mRiders.add(new Rider(p, riderDestination))
        mBusStops.get(mBusCurrentStop).remove(p)
    }

    // From Person upon getting off the bus
    msgImOff(Person p) {
		for (Rider) r in mRiders
            if r.mPerson == p
                mRiders.remove(r)
    }


###Scheduler

    if mRiders.size() > 0
        then TellRidersToGetOff()
    if mBusStops.get(mBusCurrentStop).mWaitingPeople.size() > 0
        then TellRidersToBoard()
    else
        AdvanceToNextStop()


###Actions

    TellRidersToGetOff() {
        for (Rider) r in mRiders
            if (r.mDestination == mBusCurrentStop) 
                r.msgAtYourStop()
    }

    TellRidersToBoard() {
        for (Person) p in mBusStops.get(mBusCurrentStop).mWaitingPeople
            p.msgBoardBus()
    }

    AdvanceToNextStop() {
        // Semaphore here to keep bus from telling riders board/get off while
        // moving between stops
	
        // Gui has a list of bus stop coordinates
        // gui.DoAdvance()
        mBusCurrentStop++
    }
