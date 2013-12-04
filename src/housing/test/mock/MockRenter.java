package housing.test.mock;

import housing.interfaces.HousingLandlord;
import housing.interfaces.HousingRenter;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityHousing;

/**
 * A sample MockRenter built to unit test a LandlordRole.
 *
 * @author Maggi Yang 
 *
 */
public class MockRenter extends Mock implements Role, HousingRenter {
	
	public HousingLandlord landlord; 

	public MockRenter(String name) {
		super();

	}

	public void msgApplicationAccepted(CityHousing newHouse) {
		log.add(new LoggedEvent("Received msgApplicationAccepted"));
		
	}

	public void msgApplicationDenied() {
		log.add(new LoggedEvent("Received msgApplicationDenied"));
		
	}

	public void msgRentDue(int lordssn, double total) {
		log.add(new LoggedEvent("Received msgRentDue"));
		
	}

	public void msgOverdueNotice(int lordssn, double total) {
		log.add(new LoggedEvent("Received msgOverdueNotice"));
		
	}

	public void msgEviction() {
		log.add(new LoggedEvent("Received msgEviction"));
		
	}

	public boolean pickAndExecuteAnAction() {
		log.add(new LoggedEvent("pickAndExecuteAnAction called"));
		return false;
	}
	
	public void setLandlord(HousingLandlord landlord){
		
	}

	public void setPerson(Person renterPerson) {
		
	}

	public void setPerson(PersonAgent person) {
	
		
	}

	public PersonAgent getPersonAgent() {

		return null;
	}


	public boolean isActive() {
	
		return false;
	}

	
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




}
