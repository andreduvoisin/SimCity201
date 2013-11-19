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

public class BankMasterTellerRole extends BaseRole implements MasterTeller{
	
//	DATA
	protected Map <Integer, Integer> mAccountIndex = new HashMap <Integer, Integer>();
	protected List <Account> mAccounts = Collections.synchronizedList(new ArrayList<Account>());
	private List<Transaction> mTransactions = Collections.synchronizedList(new ArrayList<Transaction>());
	
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
		int senderIndex = mAccountIndex.get(t.sender);
		int receiverIndex = mAccountIndex.get(t.receiver);
		/*
		 * Overdrawn Account Non-normative
		 * if (senderBalance < t.amount)
		 *
		 */
		mAccounts.get(senderIndex).balance -= t.amount;
		mAccounts.get(receiverIndex).balance += t.amount;
		//msgHereIsPayment(senderSSN, double amount);
	}

//	UTILITIES
	public Map<Integer, Integer> getAccountIndex() {
		return mAccountIndex;
	}
	public List<Account> getAccounts() {
		return mAccounts;
	}
}
