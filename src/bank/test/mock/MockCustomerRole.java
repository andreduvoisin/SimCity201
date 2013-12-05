package bank.test.mock;

import test.mock.LoggedEvent;
import test.mock.Mock;
import bank.interfaces.BankCustomer;
import bank.interfaces.BankGuard;
import bank.interfaces.BankTeller;
import base.Location;
import base.PersonAgent;
import base.interfaces.Person;
import base.interfaces.Role;


public class MockCustomerRole extends Mock implements BankCustomer, Role{
	
	public PersonAgent mPerson;

	public MockCustomerRole() {
		super();
		// JERRY: Auto-generated constructor stub
	}

	public void msgGoToTeller(BankTeller t){
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
	public void setGuard(BankGuard guard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPerson(Person person) {
		mPerson = (PersonAgent)person;
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

	@Override
	public boolean hasPerson() {
		// TODO Auto-generated method stub
		return false;
	}
}