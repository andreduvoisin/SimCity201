package bank.test.mock;

import test.mock.LoggedEvent;
import test.mock.Mock;
import bank.interfaces.BankCustomer;
import bank.interfaces.BankGuard;
import bank.interfaces.BankTeller;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;


public class MockGuardRole extends Mock implements BankGuard, Role{

	public MockGuardRole() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public int getSSN() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void msgNeedService(BankCustomer c) {
		log.add(new LoggedEvent("msgNeedService"));
	}
	public void msgReadyToWork(BankTeller t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRobberAlert(BankCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgOffWork(BankTeller t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPerson(Person person) {
		// TODO Auto-generated method stub
		
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
	public Location getLocation() {
		// TODO Auto-generated method stub
		return null;
	}
}