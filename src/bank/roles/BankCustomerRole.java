package bank.roles;

import java.util.ArrayList;
import java.util.List;

import base.BaseRole;
import base.PersonAgent;
import bank.Action;
import bank.interfaces.Customer;
import bank.interfaces.Guard;
import bank.interfaces.Teller;

public class BankCustomerRole extends BaseRole implements Customer{
	
//	DATA
	
	/*
	 * Variables in PersonAgent in Role
	 * double mCredit;
	 * boolean hasLoan;
	 */
	
	public double mTransaction;

	public List<Action> mActions = new ArrayList<Action>();
	/*Example: (Deposit, 34.23), (Loan, 3000.23)*/
	
	public enum EnumAction {Deposit, Loan, Payment, Open, Robbery};

	public EnumState mState = EnumState.None;
	public enum EnumState {None, Waiting, Moving, Teller};
	public EnumEvent mEvent = EnumEvent.None;
	public enum EnumEvent {None, Assigned, Arrived, Received};

	//Agent Correspondents//
	public Guard mGuard;
	public Teller mTeller;
	
//	MESSAGES
	
	public void msgGoToTeller(Teller t){
		mTeller = t;
		mEvent = EnumEvent.Assigned;
		stateChanged();
	}
	public void msgAtLocation(){
		//from GUI
		mEvent = EnumEvent.Arrived;
		stateChanged();
	}
	public void msgHereIsBalance(double balance){
		mTransaction = balance;
		mEvent = EnumEvent.Received;
		stateChanged();
	}
	public void msgHereIsLoan(double loan){
		mTransaction = loan;
		mEvent = EnumEvent.Received;
		stateChanged();
	}
	public void msgStopRobber() {
		// REX: robber gui interactions	
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
		mGuard.msgNeedService(this);
	}
	private void goToTeller(){
		//Using Teller mTeller.location
		//GUI Interaction
	}
	private void pickAction(){
		if (mActions.isEmpty()){
			leave();
		}
		else{
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
	}
	private void leave(){
		//GUI Interaction
		mTransaction = -1;
	}
	private void processTransaction(){
		EnumAction action = mActions.get(0).action;
		mActions.remove(0);
		if (action == EnumAction.Deposit){
			mPerson.addCash(mTransaction);
		}
		else if (action == EnumAction.Loan){
			if (mTransaction == 0){
				//Rejected loan
				//Non-normative
			}
			else{
				mPerson.addCash(mTransaction);
				mPerson.setLoan(mTransaction);
			}
		}
		else if (action == EnumAction.Payment){
			//REX: Does the base agent keep track of loan?
				//yes, personagent has an int loan
		}
		else if (action == EnumAction.Open){
			mPerson.setCash(mTransaction);
		}
		else if (action == EnumAction.Robbery){
			mPerson.addCash(mTransaction);
		}
	}
	public int getSSN() {
		return mPerson.getSSN();
	}
	public void setGuard(Guard guard){
		mGuard = guard;
	}
}
