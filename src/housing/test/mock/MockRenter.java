package housing.test.mock;

import base.interfaces.Person;
import test.mock.MockPerson;
import housing.House;
import housing.interfaces.Landlord;
import housing.interfaces.Renter;

/**
 * A sample MockRenter built to unit test a LandlordRole.
 *
 * @author Maggi Yang 
 *
 */
public class MockRenter extends Mock implements Renter {
	
	public Landlord landlord; 

	public MockRenter(String name) {
		super(name);

	}

	@Override
	public void msgApplicationAccepted(House newHouse) {
		log.add(new LoggedEvent("Received msgApplicationAccepted"));
		
	}

	@Override
	public void msgApplicationDenied() {
		log.add(new LoggedEvent("Received msgApplicationDenied"));
		
	}

	@Override
	public void msgRentDue(int lordssn, double total) {
		log.add(new LoggedEvent("Received msgRentDue"));
		
	}

	@Override
	public void msgOverdueNotice(int lordssn, double total) {
		log.add(new LoggedEvent("Received msgOverdueNotice"));
		
	}

	@Override
	public void msgEviction() {
		log.add(new LoggedEvent("Received msgEviction"));
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		log.add(new LoggedEvent("pickAndExecuteAnAction called"));
		return false;
	}

	public void setPerson(Person renterPerson) {
		
	}



}
