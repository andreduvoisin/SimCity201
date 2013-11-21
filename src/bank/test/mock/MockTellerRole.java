package bank.test.mock;

import bank.interfaces.Customer;
import bank.interfaces.Guard;
import bank.interfaces.MasterTeller;
import bank.interfaces.Teller;
import base.PersonAgent;
import base.interfaces.Role;
import test.mock.LoggedEvent;
import test.mock.Mock;


public class MockTellerRole extends Mock implements Teller, Role{

	public MockTellerRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setPerson(PersonAgent person) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PersonAgent getPersonAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSSN() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void msgDeposit(Customer c, int SSN, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLoan(Customer c, int SSN, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayment(Customer c, int SSN, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOpen(Customer c, int SSN, double amount, String name) {
		log.add(new LoggedEvent("msgOpen: "+SSN+" "+amount+" "+name));
	}

	@Override
	public void msgRobbery(Customer c, int SSN, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addGuard(Guard guard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaster(MasterTeller masterTeller) {
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
	
}