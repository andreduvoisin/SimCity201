package bank.test.mock;

import java.util.List;
import java.util.Map;

import bank.BankAccount;
import bank.interfaces.BankMasterTeller;
import base.interfaces.Person;
import base.interfaces.Role;
import test.mock.Mock;


public class MockMasterTellerRole extends Mock implements BankMasterTeller, Role{

	public MockMasterTellerRole(String name) {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public int getSSN() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void msgSendPayment(int senderSSN, int receiverSSN, double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<Integer, Integer> getAccountIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BankAccount> getAccounts() {
		// TODO Auto-generated method stub
		return null;
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
}