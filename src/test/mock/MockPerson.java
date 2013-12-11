package test.mock;

import housing.interfaces.HousingBase;

import java.util.Map;

import bank.interfaces.BankMasterTeller;
import base.Event;
import base.Item.EnumItemType;
import base.PersonAgent;
import base.PersonAgent.EnumJobType;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityHousing;
import city.gui.CityPerson;
import city.gui.trace.AlertTag;

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
		log.add(new LoggedEvent("GetRoles was called and will return null"));
		return null;
	}

	@Override
	public HousingBase getHousingRole() {
		
		return null;
	}

	@Override
	public void subLoan(double mTransaction) {
		log.add(new LoggedEvent("SubLoan called for the transaction value for " + mTransaction));
	}

	@Override
	public CityPerson getPersonGui() {
		log.add(new LoggedEvent("Called GetPersonGui Person function"));
		return null;
	}

	@Override
	public void msgAddEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	public CityHousing getHouse() {
		log.add(new LoggedEvent("Called GetHouse Person function"));
		return null;
	}

	@Override
	public void setGuiPresent() {
		log.add(new LoggedEvent("Called setGuiPresent function"));
		
	}

	@Override
	public CityPerson getGui() {
		log.add(new LoggedEvent("Called GetGui function"));
		return null;
	}

	@Override
	public void msgRoleFinished() {
		log.add(new LoggedEvent("Called msgRoleFinished for Mock Person"));
	}

	@Override
	public void setJobFalse() {
		log.add(new LoggedEvent("The setJobFalse method was function was called"));	}

	@Override
	public boolean hasCar() {
		log.add(new LoggedEvent("Called hasCar and will return false"));
		return false;
	}

	@Override
	public void print(String msg, AlertTag tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void print(String msg, AlertTag tag, Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgStateChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EnumJobType getJobType() {
		log.add(new LoggedEvent("Called getJobType for Mock Person. This should return null"));
		return null;
	}

	@Override
	public void setHasCar(boolean c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assignNextEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setJobType(EnumJobType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRole(Role r) {
		// TODO Auto-generated method stub
		
	}

}
