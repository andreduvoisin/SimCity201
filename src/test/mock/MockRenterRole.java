package test.mock;

import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityHousing;
import housing.interfaces.HousingBase;

/**
 * MockRenter built to test PersonAgent
 * 
 * @author Rex Xu
 * 
 */
public class MockRenterRole extends Mock implements HousingBase, Role{

	@Override
	public void msgTimeToCheckRent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTimeToMaintain() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgEatAtHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneAnimating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHouse(CityHousing h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Person getPerson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPerson(Person person) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSSN() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isRestaurantPerson() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
