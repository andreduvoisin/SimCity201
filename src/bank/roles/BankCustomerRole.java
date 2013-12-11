package bank.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import bank.BankAction;
import bank.gui.BankCustomerGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.BankGuard;
import bank.interfaces.BankTeller;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.PersonAgent;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

public class BankCustomerRole extends BaseRole implements BankCustomer{
	
//	DATA
	private int mBankID;
	
	public double mTransactionAmount;

	public List<BankAction> mActions = new ArrayList<BankAction>(); //Example: (Deposit, 34.23), (Loan, 3000.23)
	
	public enum EnumAction {Deposit, Loan, Payment, Open, Robbery};
	public EnumState mState = EnumState.None;
	public enum EnumState {None, Waiting, Moving, Teller};
	public EnumEvent mEvent = EnumEvent.None;
	public enum EnumEvent {None, Assigned, Arrived, Received};

	//Agent Correspondents//
	public BankGuard mGuard;
	public BankTeller mTeller;
	
	//GUI
	public BankCustomerGui mGUI;
	int mTellerLocation = 0;
	Semaphore atLocation = new Semaphore(0, true);
	
	Timer stay = new Timer();
	Boolean canLeave;
	
	public BankCustomerRole(Person person, int bankID){
		super(person);
		mBankID = bankID;
		canLeave = true;
		//Add Gui to list
//		mGUI = new BankCustomerGui(this);
//		mGUI.setPresent(true);
//		ContactList.sBankList.get(bankID).addPerson(this);
//		ContactList.sBankList.get(bankID).mGuis.add(mGUI);
//		mGUI.DoGoWaitInLine();
//		print("Created bank customer for "+person.getName());
	}
	
//	MESSAGES
	
	public void msgGoToTeller(BankTeller t){
		mTeller = t;
		mEvent = EnumEvent.Assigned;
		stateChanged();
	}
	public void msgAtLocation(){
		//from GUI
		atLocation.release();
		mEvent = EnumEvent.Arrived;
		stateChanged();
	}
	public void msgHereIsBalance(double balance){
		mTransactionAmount = balance;
		mEvent = EnumEvent.Received;
		stateChanged();
	}
	public void msgHereIsLoan(double loan){
		mTransactionAmount = loan;
		mEvent = EnumEvent.Received;
		stateChanged();
	}
	public void msgStopRobber() {
		print("I'M BEING APPREHENDED");
		mGUI.die();
	}
	
//	SCHEDULER
	
	public boolean pickAndExecuteAnAction(){
		if (mState == EnumState.None && mEvent == EnumEvent.None){
			waitInLine();
			mState = EnumState.Waiting;
			return true;
		}
		if (mState == EnumState.Waiting && mEvent == EnumEvent.Assigned){
			goToTeller();
			mState = EnumState.Moving;
			return true;
		}
		if (mState == EnumState.Moving && mEvent == EnumEvent.Arrived){
			mState = EnumState.Teller;
			return true;
		}
		if (mState == EnumState.Teller && mEvent == EnumEvent.Arrived){
			if (mActions.isEmpty()){
				if(canLeave)
					leave();
				return false;
			}
			pickAction();
			mEvent = EnumEvent.None;
			return true;
		}
		if (mState == EnumState.Teller && mEvent == EnumEvent.Received){
			processTransaction();
			mEvent = EnumEvent.Arrived;
			return true;
		}
		return false;
	}
	
//	ACTIONS
	
	private void waitInLine(){
		mGUI.DoGoWaitInLine();
		print("MESSAGED GUARD ABOUT WAITING");
		mGuard.msgNeedService(this);
	}
	private void goToTeller(){
		mGUI.DoGoToTeller(mTeller.getWindowNumber());
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void pickAction(){
		EnumAction action = mActions.get(0).action;
		double amount = mActions.get(0).amount;
		if (action == EnumAction.Deposit){
			mTeller.msgDeposit(this, mPerson.getSSN(), amount);
		}
		else if (action == EnumAction.Loan){
			mTeller.msgLoan(this, mPerson.getSSN(), amount); 
		}
		else if (action == EnumAction.Payment){
			mTeller.msgPayment(this, mPerson.getSSN(), amount);
		}
		else if (action == EnumAction.Open){
			mTeller.msgOpen(this, mPerson.getSSN(), amount, (PersonAgent)mPerson);
		}
		else if (action == EnumAction.Robbery){
			mTeller.msgRobbery(this, mPerson.getSSN(), amount);
		}
	}
	private void leave(){
		//GUI Interaction
		mGUI.DoLeaveBank();
		mTeller.msgLeaving();
		mTransactionAmount = -1;
		canLeave = false;
		
		mPerson.msgRoleFinished();
		mPerson.assignNextEvent();
	}
	private void processTransaction(){
		EnumAction action = mActions.get(0).action;
		mActions.remove(0);
		if (action == EnumAction.Deposit){
			mPerson.addCash(mTransactionAmount);
		}
		else if (action == EnumAction.Loan){
			if (mTransactionAmount == 0){
				//Rejected loan
				//Non-normative
			}
			else{
				mPerson.addCash(mTransactionAmount);
				mPerson.setLoan(mTransactionAmount);
			}
		}
		else if (action == EnumAction.Payment){
			mPerson.subLoan(mTransactionAmount);
		}
		else if (action == EnumAction.Open){
			mPerson.setCash(mTransactionAmount);
		}
		else if (action == EnumAction.Robbery){
			mPerson.addCash(mTransactionAmount);
		}
	}
	public int getSSN() {
		return mPerson.getSSN();
	}
	public void setGuard(BankGuard guard){
		mGuard = guard;
	}
	public void setTeller(BankTeller teller){
		mTeller = teller;
	}
	
//	UTILITIES
	
	public void setGui(BankCustomerGui g) {
		mGUI = g;
	}

	@Override
	public Location getLocation() {
		if (mBankID == 0) {
			return ContactList.cBANK1_LOCATION;
		}
		else if (mBankID == 1) {
			return ContactList.cBANK2_LOCATION;
		}
		return null;
	}
	
	public int getBankID() {
		return mBankID;
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
