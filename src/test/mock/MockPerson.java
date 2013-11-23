package test.mock;

import java.util.Map;

import bank.interfaces.BankMasterTeller;
import base.Item.EnumMarketItemType;
import base.PersonAgent;
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

	public BankMasterTeller getMasterTeller() {
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

	@Override
	public void addRole(Role role, boolean active) {
		// TODO Auto-generated method stub
	}
	
	public void msgHereIsPayment(int senderSSN, int amount) {
		log.add(new LoggedEvent("Received "+amount+" from "+senderSSN));
		
	}

	@Override
	public int getTimeShift() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSSN(int SSN) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setItemsDesired(Map<EnumMarketItemType, Integer> map) {
		// TODO Auto-generated method stub
		
	}
	
	public void setPerson(PersonAgent person){
		this.person = person;
	}

	@Override
	public void msgHereIsPayment(int senderSSN, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOverdrawnAccount(double loan) {
		// TODO Auto-generated method stub
		
	}

}
