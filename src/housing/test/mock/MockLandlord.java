package housing.test.mock;

import housing.House;
import housing.interfaces.HousingLandlord;
import housing.interfaces.HousingRenter;

import java.util.ArrayList;
import java.util.List;

import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;

/**
 * A sample MockRenter built to unit test a LandlordRole.
 *
 * @author Maggi Yang 
 *
 */
public class MockLandlord extends Mock implements Role, HousingLandlord {

	public List<House> mHousesList = new ArrayList<House>(); 
	
	public MockLandlord(String name) {
		super();
		

	}

	@Override
	public void msgIWouldLikeToLiveHere(HousingRenter r, double cash, int SSN) {
		log.add(new LoggedEvent("Received msgIWouldLikeToLiveHere"));
		
	}

	@Override
	public void msgHereIsPayment(int SSN, double paymentAmt) {
		log.add(new LoggedEvent("Received msgHereIsPayment: " + paymentAmt));
		
	}

	@Override
	public int getRenterListSize() {
		
		return 0;
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		
		return false;
	}


	@Override
	public void setPerson(Person person) {
		
		
	}

	@Override
	public int getSSN() {
		
		return 0;
	}

	@Override
	public Person getPerson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRestaurantPerson() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setActive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasPerson() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void GoToDestination(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return null;
	}



}
