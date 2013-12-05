package transportation;

import java.util.ArrayList;
import java.util.List;

import transportation.interfaces.TransportationRider;
import base.Location;
import city.gui.CityBus;

public class TransportationBusInstance {
	static int sBusNumber = 0;
	int mBusNumber;
	CityBus mGui;
	int mCurrentStop;
	ArrayList<TransportationRider> mRiders = new ArrayList<TransportationRider>();
	List<Location> mBusStops;
	
	enum enumState { readyToTravel, traveling, readyToUnload, unloading, readyToBoard, boarding }
	enumState state;

	public TransportationBusInstance(TransportationBusDispatch bd, List<Location> busStops) {
		mBusNumber = sBusNumber++;
		mCurrentStop = 0;
		mGui = new CityBus(bd, base.reference.ContactList.cBUS_STOPS);
		mBusStops = busStops;
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
