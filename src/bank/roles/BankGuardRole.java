package bank.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import city.gui.SimCityGui;
import bank.gui.BankGuardGui;
import bank.gui.BankPanel;
import bank.interfaces.BankCustomer;
import bank.interfaces.BankGuard;
import bank.interfaces.BankTeller;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
//import interfaces


public class BankGuardRole extends BaseRole implements BankGuard{
	
//	DATA
	private int mBankID;
	
	public Map <BankTeller, Boolean> mTellers = new HashMap<BankTeller, Boolean>();
	public List<BankCustomer> mCustomers = Collections.synchronizedList(new ArrayList<BankCustomer>());
	public List<BankCustomer> mCriminals = Collections.synchronizedList(new ArrayList<BankCustomer>());
	//REX : I would instead make an enum for both the customer state such as "CUSTOMER, CRIMINAL" instead of using
	//using two different arrays becasue you would have to remove the customer from the first one when adding it to 
	//the second
	//GUI
	public BankGuardGui mGUI;
	
	public BankGuardRole(Person person, int bankNumber) {
		super(person);
		mBankID = bankNumber;
		SimCityGui.getInstance().citypanel.masterBankList.get(mBankID).guard = this;
		//REX: 1
		SimCityGui.getInstance().citypanel.masterBankList.get(mBankID).addGui(mGUI);
	}
	
	
//	MESSAGES
	
	public void msgNeedService(BankCustomer c){
		synchronized(mCustomers) {
			mCustomers.add(c);
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
		synchronized(mCriminals) {
			mCriminals.add(c);
		}
		stateChanged();
	}
	
//	SCHEDULER
	
	public boolean pickAndExecuteAnAction(){
		synchronized(mCriminals) {
			for (BankCustomer c : mCriminals){
				killRobber(c);
				mCriminals.remove(c);
				return true;
			}
		}
		synchronized(mCustomers) {
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
