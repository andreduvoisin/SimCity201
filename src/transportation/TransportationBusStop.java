package transportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import transportation.interfaces.TransportationRider;

public class TransportationBusStop {
	List<TransportationRider> mWaitingPeople = Collections.synchronizedList(new ArrayList<TransportationRider>());

	public TransportationBusStop() {
		
	}
}
