package bank.interfaces;

import java.util.List;
import java.util.Map;

import bank.Account;

public interface MasterTeller {

	//	MESSAGES
	public abstract void msgSendPayment(int senderSSN, int receiverSSN,
			double amount);

	//	SCHEDULER
	public abstract boolean pickAndExecuteAnAction();
	
	//	UTILITIES
	public abstract Map<Integer, Integer> getAccountIndex();
	
	public abstract List<Account> getAccounts();
}