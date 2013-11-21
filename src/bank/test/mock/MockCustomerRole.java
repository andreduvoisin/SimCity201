package bank.test.mock;

import test.mock.LoggedEvent;
import test.mock.Mock;
import bank.interfaces.Customer;
import bank.interfaces.Teller;
import base.PersonAgent;
import base.interfaces.Role;


public class MockCustomerRole extends Mock implements Customer, Role{

	public MockCustomerRole() {
		super();
		// JERRY: Auto-generated constructor stub
	}

	public void msgGoToTeller(Teller t){
		log.add(new LoggedEvent("msgGoToTeller"));
	}

	public void msgAtLocation(){
		log.add(new LoggedEvent("msgAtLocation"));
	}

	public void msgHereIsBalance(double balance){
		log.add(new LoggedEvent("msgHereIsBalance: "+balance));
	}

	public void msgHereIsLoan(double loan){
		log.add(new LoggedEvent("msgHereIsLoan: "+loan));
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

}