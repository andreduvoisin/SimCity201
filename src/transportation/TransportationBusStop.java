package transportation;

import java.util.ArrayList;

import transportation.interfaces.TransportationRider;
import base.Location;

public class TransportationBusStop {
	// UNNECESSARY?
	public Location location = new Location(0, 0);
	ArrayList<TransportationRider> mWaitingPeople = new ArrayList<TransportationRider>();
	
	public TransportationBusStop(Location loc) {
		location.setTo(loc);
	}
}
