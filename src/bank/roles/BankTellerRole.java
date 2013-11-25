package bank.roles;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import bank.BankAccount;
import bank.gui.BankTellerGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.BankGuard;
import bank.interfaces.BankMasterTeller;
import bank.interfaces.BankTeller;
import base.BaseRole;
import base.Location;
import base.PersonAgent;
import base.interfaces.Person;

public class BankTellerRole extends BaseRole implements BankTeller{

//	DATA
	
	public class MyCustomer{
		public BankCustomer customer;
		public PersonAgent mPerson;
		int mSSN;
		double desiredAmount = 0;
		EnumTransaction transaction = EnumTransaction.None;
		MyCustomer (BankCustomer c, int SSN, double a, EnumTransaction t){
			customer = c;
			mSSN = SSN;
			desiredAmount = a;
			transaction = t;
		}
	}
	public enum EnumTransaction {None, Deposit, Open, Loan, Payment, Robbery};
	
	Timer timer = new Timer();
	private static int loanTime = 10000;
	
	//GUI
	BankTellerGui mGUI;
	//GUI Coordinate
	Location mLocation;
	//Agent Correspodents
	BankGuard mGuard;
	public MyCustomer mCustomer;
	//Database
	BankMasterTeller mMasterTeller;
	public Map <Integer, Integer> mAccountIndex;
	public List <BankAccount> mAccounts;
	
	
	//CONSTRUCTOR
	public BankTellerRole(Person person) {
		mPerson = person;
	}

	public BankTellerRole() {
		
	}
	
	//	MESSAGES
	
	public void msgDeposit(BankCustomer c, int SSN, double amount){
		mCustomer = new MyCustomer(c, SSN, amount, EnumTransaction.Deposit);
		stateChanged();
	}
	public void msgLoan(BankCustomer c, int SSN, double amount){
		mCustomer = new MyCustomer(c, SSN, amount, EnumTransaction.Loan);
		stateChanged();
	}
	public void msgPayment(BankCustomer c, int SSN, double amount){
		mCustomer = new MyCustomer(c, SSN, amount, EnumTransaction.Payment);
		stateChanged();
	}
	public void msgOpen(BankCustomer c, int SSN, double amount, PersonAgent person){
		mCustomer = new MyCustomer(c, SSN, amount, EnumTransaction.Open);
		mCustomer.mPerson = person;
		stateChanged();
	}
	public void msgRobbery(BankCustomer c, int SSN, double amount){
		mCustomer = new MyCustomer(c, SSN, amount, EnumTransaction.Robbery);
		stateChanged();
	}

//	SCHEDULER
	public boolean pickAndExecuteAnAction() {
		if (!(mCustomer == null)) {
			if (mCustomer.transaction == EnumTransaction.Deposit) {
				deposit();
				mCustomer.transaction = EnumTransaction.None;
				return true;
			}
			if (mCustomer.transaction == EnumTransaction.Loan) {
				loan();
				mCustomer.transaction = EnumTransaction.None;
				return true;
			}
			if (mCustomer.transaction == EnumTransaction.Payment) {
				payment();
				mCustomer.transaction = EnumTransaction.None;
				return true;
			}
			if (mCustomer.transaction == EnumTransaction.Open) {
				open();
				mCustomer.transaction = EnumTransaction.None;
				return true;
			}
			if (mCustomer.transaction == EnumTransaction.Robbery) {
				robbery();
				mCustomer.transaction = EnumTransaction.None;
				return true;
			}
		}
		return false;
	}
	
//	ACTIONS
	
	private void deposit(){
		int accountIndex = mAccountIndex.get(mCustomer.mSSN);
		mAccounts.get(accountIndex).balance += mCustomer.desiredAmount;
		mCustomer.customer.msgHereIsBalance(mAccounts.get(accountIndex).balance);
	}
	private void loan(){
		int accountIndex = mAccountIndex.get(mCustomer.mSSN);
		double balance = mAccounts.get(accountIndex).balance;
		if (balance >= (mCustomer.desiredAmount+mAccounts.get(accountIndex).loan)*2.0){
			mAccounts.get(accountIndex).loan += mCustomer.desiredAmount;
			timer.schedule(new TimerTask(){
				public void run(){
					mCustomer.customer.msgHereIsLoan(mCustomer.desiredAmount);
				}
			}, loanTime);
		}
		else {
			//Non-normative: Loan Rejected
			timer.schedule(new TimerTask(){
				public void run(){
					mCustomer.customer.msgHereIsLoan(0);
				}
			}, loanTime);
		}
	}
	private void payment(){
		int accountIndex = mAccountIndex.get(mCustomer.mSSN);
		mAccounts.get(accountIndex).loan -= mCustomer.desiredAmount;
		mCustomer.customer.msgHereIsLoan(0);
	}
	private void open(){
		mMasterTeller.getAccounts().add(new BankAccount(0, mCustomer.desiredAmount, mCustomer.mPerson));
		int accountIndex = mMasterTeller.getAccounts().size() - 1;
		mMasterTeller.getAccountIndex().put(mCustomer.mSSN, accountIndex);
		mCustomer.customer.msgHereIsBalance(mCustomer.desiredAmount);
	}
	private void robbery(){
		int accountIndex = mAccountIndex.get(mCustomer.mSSN);
		mAccounts.get(accountIndex).balance += mCustomer.desiredAmount;
		mCustomer.customer.msgHereIsBalance(mMasterTeller.getAccounts().get(accountIndex).balance);
	}
	
//	UTILITIES
	public void addGuard(BankGuard guard){
		mGuard = guard;
	}
	public void setMaster(BankMasterTeller masterTeller) {
		mMasterTeller = masterTeller;
	}
	public void setAccountIndex(){
		mAccountIndex = mMasterTeller.getAccountIndex();
	}
	public void setAccounts(){
		mAccounts = mMasterTeller.getAccounts();
	}
	public void setGui(BankTellerGui g) {
		mGUI = g;
	}
}
