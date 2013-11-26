package transportation;

import java.util.ArrayList;

import city.gui.CityBus;
import transportation.interfaces.TransportationRider;

public class TransportationBusInstance {
	static int sBusNumber = 0;
	int mBusNumber;
	CityBus mGui;
	int mCurrentStop;
	ArrayList<TransportationRider> mRiders = new ArrayList<TransportationRider>();
	
	enum enumState { readyToTravel, traveling, readyToUnload, unloading, readyToBoard, boarding }
	enumState state;

	public TransportationBusInstance(TransportationBusDispatch bd, int numStops) {
		mBusNumber = sBusNumber++;
		mCurrentStop = mBusNumber * numStops / 2;
		mGui = new CityBus(bd, base.ContactList.cBUS_STOPS);

		state = enumState.traveling;

		bd.addBus(this);
	}
	
	public boolean isBusy() {
		return state.equals(enumState.traveling)
				|| state.equals(enumState.unloading)
				|| state.equals(enumState.boarding);
	}

	public CityBus getCityBus() {
		return mGui;
	}
}
