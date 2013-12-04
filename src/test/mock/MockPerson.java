package test.mock;

import java.util.Map;

import bank.interfaces.BankMasterTeller;
import base.Event;
import base.Item.EnumItemType;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityHousing;
import city.gui.CityPerson;

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

	public Map<EnumItemType, Integer> getItemsDesired() {
		return null;
	}

	public Map<EnumItemType, Integer> getItemInventory() {
		return null;
	}

	public BankMasterTeller getMasterTeller() {
		return null;

	}

	@Override
	public void setLoan(double loan) {
		
	}

	@Override
	public double getLoan() {

		return 0;
	}

	@Override
	public void addRole(Role role, boolean active) {
		log.add(new LoggedEvent("Role: " + role)); 
	}
	
	public void msgHereIsPayment(int senderSSN, int amount) {
		log.add(new LoggedEvent("Received "+amount+" from "+senderSSN));
		
	}

	@Override
	public int getTimeShift() {
		
		return 0;
	}

	@Override
	public void setName(String name) {
		
	}

	@Override
	public void setSSN(int SSN) {
		
	}

	@Override
	public void setItemsDesired(Map<EnumItemType, Integer> map) {
		
	}
	
	public void setPerson(PersonAgent person){
		this.person = person;
	}

	@Override
	public void msgHereIsPayment(int senderSSN, double amount) {
		log.add(new LoggedEvent("SenderSSN: " + senderSSN + ". Amount received: " + amount)); 
		
	}

	@Override
	public void msgOverdrawnAccount(double loan) {
		log.add(new LoggedEvent("Loan amount: " + loan)); 
	}

	@Override
	public Map<Role, Boolean> getRoles() {
		
		return null;
	}

	@Override
	public Role getHousingRole() {
		
		return null;
	}

	@Override
	public void subLoan(double mTransaction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CityPerson getPersonGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgAddEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CityHousing getHouse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGuiPresent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CityPerson getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgRoleFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setJobFalse() {
		// TODO Auto-generated method stub
		
	}

}
