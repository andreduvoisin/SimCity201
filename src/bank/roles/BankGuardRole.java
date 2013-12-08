package bank.roles;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bank.gui.BankGuardGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.BankGuard;
import bank.interfaces.BankTeller;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
//import interfaces
import city.gui.trace.AlertTag;


public class BankGuardRole extends BaseRole implements BankGuard{
	
//	DATA
	private int mBankID;
	
	public Map <BankTeller, Boolean> mTellers = new HashMap<BankTeller, Boolean>();
	public Map <BankCustomer, Boolean> mCustomers = Collections.synchronizedMap(new HashMap<BankCustomer, Boolean>());
	//public List<BankCustomer> mCustomers = Collections.synchronizedList(new ArrayList<BankCustomer>());
	//public List<BankCustomer> mCriminals = Collections.synchronizedList(new ArrayList<BankCustomer>());

	//GUI
	public BankGuardGui mGUI;
	
	public BankGuardRole(Person person, int bankID) {
		super(person);
		mBankID = bankID;
		ContactList.sBankList.get(bankID).addPerson(this);
		//Add Gui to list
//		mGUI = new BankGuardGui(this);
//		ContactList.sBankList.get(bankID).addPerson(this);
//		ContactList.sBankList.get(bankID).mGuis.add(mGUI);
	}
	
	
//	MESSAGES
	
	public void msgNeedService(BankCustomer c){
		synchronized(mCustomers) {
			mCustomers.put(c, false);
		}
		print("RECEIVED MESSAGE");
		stateChanged(); 
	}
	public void msgReadyToWork(BankTeller t){
		mTellers.put(t, true);
		print("RECEIVED READY TO WORK");
		stateChanged();
	}
	public void msgRobberAlert(BankCustomer c){
		synchronized(mCustomers) {
			mCustomers.put(c, true);
		}
		print("MESSAGED ABOUT THE ROBBER");
		stateChanged();
	}
	
//	SCHEDULER
	
	public boolean pickAndExecuteAnAction(){
		synchronized(mCustomers) {
			for (BankCustomer c : mCustomers.keySet()){
				if(mCustomers.get(c)){
					killRobber(c);
					mCustomers.remove(c);
					return true;
				}
			}
		}
		synchronized(mCustomers) {
			for (BankCustomer c : mCustomers.keySet()){
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
		}
		return false;
	}
	
//	ACTIONS
	
	private void killRobber(BankCustomer c){
		c.msgStopRobber();
	}
	private void provideService(BankCustomer c, BankTeller t){
		c.msgGoToTeller(t);
		print("SENT CUSTOMER TO TELLER");
	}
	
//	UTILITIES
	
	public void msgOffWork(BankTeller t){
		mTellers.put(t, false);
	}
	public void setGui(BankGuardGui g) {
		mGUI = g;
	}
	
	@Override
	public Location getLocation() {
		switch (mBankID){
			case 1: return ContactList.cBANK1_LOCATION;
			case 2: return ContactList.cBANK2_LOCATION;
		}
		return null;
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.BANK);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.BANK);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.BANK, e);
	}
}
