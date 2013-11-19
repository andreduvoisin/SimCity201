package bank.roles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bank.interfaces.Customer;
import bank.interfaces.Guard;
import bank.interfaces.Teller;
import base.Role;
//import interfaces


public class BankGuardRole extends Role implements Guard{
	
//	DATA
	
	Map <Teller, Boolean> mTellers = new HashMap<Teller, Boolean>();
	List<Customer> mCustomers;
	List<Customer> mCriminals;
	
//	MESSAGES
	
	public void msgNeedService(Customer c){
		mCustomers.add(c);
		stateChanged();
	}
	public void msgReadyToWork(Teller t){
		mTellers.put(t, true);
		stateChanged();
	}
	public void msgReadyForNext(Teller t){
		mTellers.put(t, true);
		stateChanged();
	}
	public void msgRobberAlert(Customer c){
		mCriminals.add(c);
		stateChanged();
	}
	
//	SCHEDULER
	
	public boolean pickAndExecuteAnAction(){
		for (Customer c : mCriminals){
			killRobber(c);
			mCriminals.remove(c);
			return true;
		}
		for (Customer c : mCustomers){
			for (Teller t : mTellers.keySet()){
				//if Teller t is available
				if (mTellers.get(t)){
					provideService(c, t);
					mCustomers.remove(c);
					mTellers.put(t, false);
					return true;
				}
			}
		}
		return false;
	}
	
//	ACTIONS
	
	private void killRobber(Customer c){
		//GUI Interactions
	}
	private void provideService(Customer c, Teller t){
		c.msgGoToTeller(t);
	}
	
//	UTILITIES
	
	public void msgOffWork(Teller t){
		mTellers.put(t, false);
	}
}
