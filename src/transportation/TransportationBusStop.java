package transportation;

import java.util.ArrayList;

import transportation.interfaces.TransportationRider;

public class TransportationBusStop {
	static int sStopNumber = 0;
	final int mStopNumber;
	ArrayList<TransportationRider> mWaitingPeople = new ArrayList<TransportationRider>();
	
	public TransportationBusStop() {
		mStopNumber = sStopNumber++;
	}

	public int getStopNumber() {
		return mStopNumber;
	}
}
