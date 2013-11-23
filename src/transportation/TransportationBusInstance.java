package transportation;

import java.util.ArrayList;

import transportation.interfaces.TransportationRider;

public class TransportationBusInstance {
	static int sBusNumber = 0;
	int mBusNumber;
	TransportationBusGui mGui;
	int mCurrentStop;
	ArrayList<TransportationRider> mRiders = new ArrayList<TransportationRider>();
	
	enum enumState { readyToTravel, traveling, readyToUnload, unloading, readyToBoard, boarding }
	enumState state;

	public TransportationBusInstance(TransportationBusDispatch bd, int numStops) {
		mBusNumber = sBusNumber++;
		mCurrentStop = mBusNumber * numStops / 2;
		mGui = new TransportationBusGui(bd, mBusNumber);

		state = enumState.traveling;
	}
	
	public boolean isBusy() {
		return state.equals(enumState.traveling)
				|| state.equals(enumState.unloading)
				|| state.equals(enumState.boarding);
	}
}