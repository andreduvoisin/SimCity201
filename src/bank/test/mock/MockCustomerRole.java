package bank.test.mock;

import test.mock.LoggedEvent;
import test.mock.Mock;
import bank.interfaces.Customer;
import bank.interfaces.Guard;
import bank.interfaces.Teller;
import base.PersonAgent;
import base.interfaces.Person;
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
	
	public void msgStopRobber(){
		log.add(new LoggedEvent("msgStopRobber"));
	}

	public boolean pickAndExecuteAnAction(){
		return false;
	}
	
	public int getSSN(){
		return 0;
	}

	@Override
	public PersonAgent getPersonAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGuard(Guard guard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPerson(Person person) {
		// TODO Auto-generated method stub
		
	}

}