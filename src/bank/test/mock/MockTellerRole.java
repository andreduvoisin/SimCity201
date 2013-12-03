package bank.test.mock;

import bank.interfaces.BankCustomer;
import bank.interfaces.BankGuard;
import bank.interfaces.BankMasterTeller;
import bank.interfaces.BankTeller;
import base.Location;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;
import test.mock.LoggedEvent;
import test.mock.Mock;


public class MockTellerRole extends Mock implements BankTeller, Role{

	public MockTellerRole() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public int getSSN() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void msgDeposit(BankCustomer c, int SSN, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLoan(BankCustomer c, int SSN, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayment(BankCustomer c, int SSN, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRobbery(BankCustomer c, int SSN, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		return true;
	}

	@Override
	public void addGuard(BankGuard guard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaster(BankMasterTeller masterTeller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAccountIndex() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAccounts() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPerson(Person person) {
		log.add(new LoggedEvent("setPerson: "+person.getName()));
	}

	@Override
	public void msgOpen(BankCustomer c, int SSN, double amount, PersonAgent person) {
		log.add(new LoggedEvent("msgOpen: "+SSN+" "+amount+" "+person.getName()));
	}
	@Override
	public Person getPerson() {
		Person temp = new PersonAgent();
		return temp;
	}
	@Override
	public boolean isRestaurantPerson() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void msgLeaving() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setActive() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getWindowNumber() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return null;
	}
}