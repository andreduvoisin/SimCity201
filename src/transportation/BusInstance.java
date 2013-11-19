package transportation;

import java.util.ArrayList;

public class BusInstance {
	static int sBusNumber = 0;
	int mBusNumber;
	BusGui mGui;
	int mCurrentStop;
	ArrayList<Rider> mRiders = new ArrayList<Rider>();
	
	enum enumState { readyToTravel, traveling, readyToUnload, unloading, readyToBoard, boarding }
	enumState state = enumState.traveling;
	
	public BusInstance(BusDispatch bd, int numStops) {
		mBusNumber = sBusNumber++;
		mCurrentStop = mBusNumber * numStops / 2;
		mGui = new BusGui(bd);
	}
}
