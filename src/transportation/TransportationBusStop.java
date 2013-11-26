package transportation;

import java.util.ArrayList;

import base.Location;
import transportation.interfaces.TransportationRider;

public class TransportationBusStop {
	static int sStopNumber = 0;
	final int mStopNumber;
	Location location;
	ArrayList<TransportationRider> mWaitingPeople = new ArrayList<TransportationRider>();
	
	public TransportationBusStop(Location loc) {
		location = loc;
		mStopNumber = sStopNumber++;
	}

	public int getStopNumber() {
		return mStopNumber;
	}
}
