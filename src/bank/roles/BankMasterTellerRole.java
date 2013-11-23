package bank.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bank.BankAccount;
import bank.BankTransaction;
import bank.interfaces.BankMasterTeller;
import base.BaseRole;
import base.interfaces.Person;

public class BankMasterTellerRole extends BaseRole implements BankMasterTeller{
	
//	DATA
	public Map <Integer, Integer> mAccountIndex = new HashMap <Integer, Integer>();
	public List <BankAccount> mAccounts = Collections.synchronizedList(new ArrayList<BankAccount>());
	public List<BankTransaction> mTransactions = Collections.synchronizedList(new ArrayList<BankTransaction>());
		
	public BankMasterTellerRole(Person person) {
		mPerson = person;
	}
	
	public BankMasterTellerRole() {
		
	}

	//	MESSAGES
	public void msgSendPayment(int senderSSN, int receiverSSN, double amount){
		mTransactions.add(new BankTransaction(senderSSN, receiverSSN, amount));
		stateChanged();
	}
	
//	SCHEDULER
	public boolean pickAndExecuteAnAction(){
		for (BankTransaction t : mTransactions){
			processTransaction(t);
			mTransactions.remove(t);
			return true;
		}
		return false;
	}
	
//	ACTIONS
	private void processTransaction(BankTransaction t){
		BankAccount sender = mAccounts.get(mAccountIndex.get(t.sender));
		BankAccount receiver = mAccounts.get(mAccountIndex.get(t.receiver));
		if (sender.balance < t.amount){
			sender.balance = 0;
			double excess = t.amount - sender.balance;
			sender.person.msgOverdrawnAccount(excess);
		}
		else{
			sender.balance -= t.amount;
		}
		receiver.balance += t.amount;
		receiver.person.msgHereIsPayment(t.sender, t.amount);
	}

//	UTILITIES
	public Map<Integer, Integer> getAccountIndex() {
		return mAccountIndex;
	}
	public List<BankAccount> getAccounts() {
		return mAccounts;
	}
}
