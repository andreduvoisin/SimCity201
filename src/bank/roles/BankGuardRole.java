package bank.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bank.interfaces.BankCustomer;
import bank.interfaces.BankGuard;
import bank.interfaces.BankTeller;
import base.BaseRole;
//import interfaces


public class BankGuardRole extends BaseRole implements BankGuard{
	
//	DATA
	
	public Map <BankTeller, Boolean> mTellers = new HashMap<BankTeller, Boolean>();
	public List<BankCustomer> mCustomers = new ArrayList<BankCustomer>();
	public List<BankCustomer> mCriminals = new ArrayList<BankCustomer>();
	
//	MESSAGES
	
	public void msgNeedService(BankCustomer c){
		mCustomers.add(c);
		stateChanged();
	}
	public void msgReadyToWork(BankTeller t){
		mTellers.put(t, true);
		stateChanged();
	}
	/*public void msgReadyForNext(Teller t){
		mTellers.put(t, true);
		stateChanged();
	}*/
	public void msgRobberAlert(BankCustomer c){
		mCriminals.add(c);
		stateChanged();
	}
	
//	SCHEDULER
	
	public boolean pickAndExecuteAnAction(){
		for (BankCustomer c : mCriminals){
			killRobber(c);
			mCriminals.remove(c);
			return true;
		}
		for (BankCustomer c : mCustomers){
			for (BankTeller t : mTellers.keySet()){
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
	
	private void killRobber(BankCustomer c){
		//GUI Interactions
		c.msgStopRobber();
	}
	private void provideService(BankCustomer c, BankTeller t){
		c.msgGoToTeller(t);
	}
	
//	UTILITIES
	
	public void msgOffWork(BankTeller t){
		mTellers.put(t, false);
	}
}
