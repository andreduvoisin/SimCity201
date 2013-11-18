package bank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.Agent;

public class BankDatabase extends Agent{
	
//	DATA
	class Account{
		String name;
		double loan = 0;
		double balance = 0;
	}
	protected Map <Integer, Account> mAccounts = new HashMap <Integer, Account>();
	
	class Transaction{
		int sender;
		int receiver;
		double amount;
		public Transaction(int s, int r, double a){
			sender = s;
			receiver = r;
			amount = a;
		}
	}
	private List<Transaction> mTransactions;
	
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
		Account senderAccount = mAccounts.get(t.sender);
		Account receiverAccount = mAccounts.get(t.receiver);
		
		double senderBalance = senderAccount.balance;
		double receiverBalance = receiverAccount.balance;
		/*
		 * Overdrawn Account Non-normative
		 * if (senderBalance < t.amount)
		 *
		 */
		senderBalance -= t.amount;
		receiverBalance += t.amount;
		
		senderAccount.balance = senderBalance;
		receiverAccount.balance = receiverBalance;
		
		mAccounts.put(t.sender, senderAccount);
		mAccounts.put(t.receiver, receiverAccount);
		//msgHereIsBankStatement(int senderSSN, double amount, double newBalance)
	}
}
