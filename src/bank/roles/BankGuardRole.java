package bank.roles;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bank.gui.BankGuardGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.BankGuard;
import bank.interfaces.BankTeller;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;
//import interfaces


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
		
		//Add Gui to list
		mGUI = new BankGuardGui(this);
		ContactList.sBankList.get(bankID).addPerson(this);
		ContactList.sBankList.get(bankID).mGuis.add(mGUI);
	}
	
	
//	MESSAGES
	
	public void msgNeedService(BankCustomer c){
		synchronized(mCustomers) {
			mCustomers.put(c, false);
		}
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
		synchronized(mCustomers) {
			mCustomers.put(c, true);
		}
		stateChanged();
	}
	
//	SCHEDULER
	
	public boolean pickAndExecuteAnAction(){
		synchronized(mCustomers) {
			for (BankCustomer c : mCustomers.keySet()){
				if(mCustomers.get(c)){
					killRobber(c);
					mCustomers.remove(c);
				}
				return true;
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
		//GUI Interactions
		// REX ANDRE: robber gui interactions, non-norm
		//DoKillRobber(m.Person) //JERRY: One way I see this actually following the robber is if you pass in the 
		//the robber's current position every time update position is called
		c.msgStopRobber();
	}
	private void provideService(BankCustomer c, BankTeller t){
		c.msgGoToTeller(t);
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
}
