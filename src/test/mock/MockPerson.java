package test.mock;

import housing.interfaces.Renter;
import housing.test.mock.MockRenter;

import java.util.Map;

import bank.interfaces.MasterTeller;
import base.Item.EnumMarketItemType;
import base.BaseRole;
import base.interfaces.Person;
import base.interfaces.Role;

/**
 * MockPerson built to unit test Housing
 * 
 * @author Maggi Yang
 * 
 */
public class MockPerson extends Mock implements Person {
	private String name;
	
	public Person person;

	public MockPerson(String name) {
		this.name = name; 

	}
	
	public String getName(){
		return name; 
	}
	
	public String toString(){
		return this.getClass().getName() + ": " + name; 
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


	public void addRole(Role r) {

		
	}
	public MasterTeller getMasterTeller() {
		return null;

	}

	@Override
	public void setLoan(double loan) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getLoan() {
		// TODO Auto-generated method stub
		return 0;
	}

}
