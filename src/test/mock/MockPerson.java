package test.mock;

import housing.interfaces.Renter;
import housing.test.mock.MockRenter;

import java.util.Map;

import base.Item.EnumMarketItemType;
import base.Role;
import base.interfaces.Person;

/**
 * MockPerson built to unit test Housing
 * 
 * @author Maggi Yang
 * 
 */
public class MockPerson extends Mock implements Person {

	public Person person;

	public MockPerson(String name) {
		super(name);

	}

	public void msgTimeShift() {
		log.add(new LoggedEvent("Received msgTimeShift"));
	}

	public void setCash(double credit) {
		log.add(new LoggedEvent("Set cash to" + credit));
	}

	public double getCash() {
		return 0;
	}

	public int getSSN() {
		return 0;
	}

	public void addCash(double amount) {
		log.add(new LoggedEvent("Added " + amount + " in cash"));
	}

	public Map<EnumMarketItemType, Integer> getItemsDesired() {
		return null;
	}

	public Map<EnumMarketItemType, Integer> getItemInventory() {
		return null;
	}

	public void addRole(Role renter) {
		// TODO Auto-generated method stub
		
	}

}
