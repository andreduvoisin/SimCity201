package bank.test.mock;

import bank.interfaces.Customer;
import bank.interfaces.Teller;
import base.PersonAgent;
import base.interfaces.Role;
import test.mock.Mock;


public class MockCustomerRole extends Mock implements Customer, Role{

	public MockCustomerRole(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void msgGoToTeller(Teller t){
		
	}

	public void msgAtLocation(){
		
	}

	public void msgHereIsBalance(double balance){
		
	}

	public void msgHereIsLoan(double loan){
		
	}

	public boolean pickAndExecuteAnAction(){
		return false;
	}
	
	public int getSSN(){
		return 0;
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

}