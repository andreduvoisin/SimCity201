package bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bank.gui.BankCustomerGui;
import bank.gui.BankGuardGui;
import bank.gui.BankTellerGui;
import bank.roles.BankCustomerRole;
import bank.roles.BankGuardRole;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import base.ContactList;
import base.Gui;
import base.interfaces.Person;
import base.interfaces.Role;

public class Bank {
	public int mBankID;
	//people
	public BankGuardRole mGuard;
	public List<BankCustomerRole> mCustomers = Collections.synchronizedList(new ArrayList<BankCustomerRole>());
	public BankMasterTellerRole mMasterTeller;
	public List<BankTellerRole> mTellers = Collections.synchronizedList(new ArrayList<BankTellerRole>());
	public List<Person> masterPersonList = Collections.synchronizedList(new ArrayList<Person>());
	public BankTellerRole teller; 
	//guis
//	public List<BankCustomerGui> mCustomerGuis = new ArrayList<BankCustomerGui>();
//	public List<BankGuardGui> mGuardGuis = new ArrayList<BankGuardGui>();
//	public List<BankTellerGui> mTellerGuis = new ArrayList<BankTellerGui>();
	//private BankPanel BankPanel;
	public List<Gui> mGuis = new ArrayList<Gui>();
	
	public Bank(int n){
		mBankID = n;
	}
	
	public void addPerson(Role role) {
		 if(role instanceof BankGuardRole){
			////System.out.println("Bank guard added");
			mGuard = ((BankGuardRole)role);
			((BankGuardRole)role).mGUI = new BankGuardGui((BankGuardRole)role);
			//((BankGuardRole)role).mGUI.setPresent(true);
			mGuis.add(((BankGuardRole)role).mGUI);
		}
		 
		if(role instanceof BankCustomerRole) {;
			mCustomers.add((BankCustomerRole)role);
			((BankCustomerRole)role).setGuard(mGuard);
			((BankCustomerRole)role).setTeller(teller);
			((BankCustomerRole)role).mGUI = new BankCustomerGui((BankCustomerRole)role);
			//((BankCustomerRole)role).mGUI.setPresent(true);
			mGuis.add(((BankCustomerRole)role).mGUI);
		}
		
		if(role instanceof BankTellerRole) {
			if(mTellers.size() < 3){
				////System.out.println("Bank teller added");
				mTellers.add((BankTellerRole)role);
				((BankTellerRole)role).addGuard(mGuard);
				((BankTellerRole)role).setMaster(ContactList.masterTeller);
				((BankTellerRole)role).mGUI = new BankTellerGui((BankTellerRole)role);//, BankPanel);
				//((BankTellerRole)role).mGUI.setPresent(true);
				mGuis.add(((BankTellerRole)role).mGUI);
				teller = ((BankTellerRole)role);
			}
		}
		 if(role instanceof BankMasterTellerRole){
			 mMasterTeller = ((BankMasterTellerRole)role);
		 }
	}
	
	public void updateCustomerLine() {
		BankCustomerGui.LINE_POSITION--;
		for(Gui gui : mGuis) {
            if (gui.isPresent()) {
            	if(gui instanceof BankCustomerGui)
            		((BankCustomerGui)gui).moveForwardInLine();
            }
        }
	}
}
