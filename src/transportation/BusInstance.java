package transportation;

import java.util.ArrayList;

public class BusInstance {
	static int sBusNumber = 0;
	int mBusNumber;
	BusGui mGui;
	int mCurrentStop;
	ArrayList<Rider> mRiders = new ArrayList<Rider>();
	
	enum enumState { readyToTravel, traveling, readyToUnload, unloading, readyToBoard, boarding }
	enumState state;
	
	public BusInstance(BusDispatch bd, int numStops) {
		mBusNumber = sBusNumber++;
		mCurrentStop = mBusNumber * numStops / 2;
		mGui = new BusGui(bd, mBusNumber);

		state = enumState.traveling;
	}
	
	public boolean isBusy() {
		return state.equals(enumState.traveling)
				|| state.equals(enumState.unloading)
				|| state.equals(enumState.boarding);
	}
}
