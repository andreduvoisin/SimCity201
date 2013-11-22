package bank.test.mock;

import bank.interfaces.Customer;
import bank.interfaces.Guard;
import bank.interfaces.Teller;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;
import test.mock.LoggedEvent;
import test.mock.Mock;


public class MockGuardRole extends Mock implements Guard, Role{

	public MockGuardRole() {
		super();
		// TODO Auto-generated constructor stub
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

	public void msgNeedService(Customer c) {
		log.add(new LoggedEvent("msgNeedService"));
	}
	public void msgReadyToWork(Teller t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRobberAlert(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgOffWork(Teller t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPerson(Person person) {
		// TODO Auto-generated method stub
		
	}


}