package bank.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bank.Account;
import bank.Transaction;
import bank.interfaces.MasterTeller;
import base.BaseRole;
import base.interfaces.Role;

public class BankMasterTellerRole extends BaseRole implements MasterTeller{
	
//	DATA
	public Map <Integer, Integer> mAccountIndex = new HashMap <Integer, Integer>();
	public List <Account> mAccounts = Collections.synchronizedList(new ArrayList<Account>());
	public List<Transaction> mTransactions = Collections.synchronizedList(new ArrayList<Transaction>());
	
	//List <PersonAgent> totalPopulation;
	
//	MESSAGES
	public void msgSendPayment(int senderSSN, int receiverSSN, double amount){
		mTransactions.add(new Transaction(senderSSN, receiverSSN, amount));
		stateChanged();
	}
	
//	SCHEDULER
	public boolean pickAndExecuteAnAction(){
		for (Transaction t : mTransactions){
			processTransaction(t);
			mTransactions.remove(t);
			return true;
		}
		return false;
	}
	
//	ACTIONS
	private void processTransaction(Transaction t){
		Account sender = mAccounts.get(mAccountIndex.get(t.sender));
		Account receiver = mAccounts.get(mAccountIndex.get(t.receiver));
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
	public List<Account> getAccounts() {
		return mAccounts;
	}

	public static Role getNextRole() {
		// SHANE: ADD GETROLE METHODS
		
		Role role = new BankTellerRole();
		return role;
	}
}
