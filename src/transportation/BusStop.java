package transportation;

import java.util.ArrayList;

import transportation.interfaces.Rider;

public class BusStop {
	static int sStopNumber = 0;
	final int mStopNumber;
	ArrayList<Rider> mWaitingPeople = new ArrayList<Rider>();
	
	public BusStop() {
		mStopNumber = sStopNumber++;
	}

	public int getStopNumber() {
		return mStopNumber;
	}
}
