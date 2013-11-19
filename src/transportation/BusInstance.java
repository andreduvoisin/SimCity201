package transportation;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class BusInstance {
	static int sBusNumber = 0;
	int mBusNumber;
	BusGui mGui;
	int mCurrentStop;
	ArrayList<Rider> mRiders = new ArrayList<Rider>();
	Semaphore semBusy = new Semaphore(0, true);
	
	enum enumState { readyToTravel, traveling, readyToUnload, unloading, readyToBoard, boarding }
	enumState state = enumState.traveling;
	
	public BusInstance(BusDispatch bd, int numStops) {
		mBusNumber = sBusNumber++;
		mCurrentStop = mBusNumber * numStops / 2;
		mGui = new BusGui(bd, mBusNumber);
	}
}
