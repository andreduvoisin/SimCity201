package bank.test.mock;

import bank.interfaces.Customer;
import bank.interfaces.Guard;
import bank.interfaces.Teller;
import base.PersonAgent;
import base.interfaces.Role;
import test.mock.Mock;


public class MockGuardRole extends Mock implements Guard, Role{

	public MockGuardRole(String name) {
		super(name);
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
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSSN() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void msgNeedService(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToWork(Teller t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyForNext(Teller t) {
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


}