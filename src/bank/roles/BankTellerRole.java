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
import base.ContactList;
import base.Location;
import base.PersonAgent;
import base.PersonAgent.EnumJobType;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

public class BankTellerRole extends BaseRole implements BankTeller{

	//------------------------------------------------------DATA------------------------------------------------------
	public int mBankID;
	
	public class MyCustomer{
		public BankCustomer customer;
		public PersonAgent mPerson;
		int mSSN;
		double amount = 0;
		EnumTransaction transaction = EnumTransaction.None;
		MyCustomer (BankCustomer c, int SSN, double a, EnumTransaction t){
			customer = c;
			mSSN = SSN;
			amount = a;
			transaction = t;
		}
	}
	public enum EnumTransaction {None, Deposit, Open, Loan, Payment, Robbery};
	
	Timer timer = new Timer();
	private static int loanTime = 10000;
	
	//GUI
	public BankTellerGui mGUI;
	//GUI Coordinate
	int mWindow;
	//Agent Correspodents
	BankGuard mGuard;
	public MyCustomer mCustomer;
	//Database
	BankMasterTeller mMasterTeller;
	public Map <Integer, Integer> mAccountIndex;
	public List <BankAccount> mAccounts;
	
	
	//------------------------------------------------------CONSTRUCTOR------------------------------------------------------
	public BankTellerRole(Person person, int bankID) {
		super(person);
		mBankID = bankID;
		ContactList.sBankList.get(bankID).addPerson(this);
		//Add Gui to list
//		mGUI = new BankTellerGui(this);
//		ContactList.sBankList.get(bankID).addPerson(this);
//		ContactList.sBankList.get(bankID).mGuis.add(mGUI);
	}
	
	//------------------------------------------------------MESSAGES------------------------------------------------------
	
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
	public void msgLeaving(){
		mGuard.msgReadyToWork(this);
	}

	//------------------------------------------------------SCHEDULER------------------------------------------------------
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
	
	//------------------------------------------------------ACTIONS------------------------------------------------------
	
	private void deposit(){
		int accountIndex = mAccountIndex.get(mCustomer.mSSN);
		mAccounts.get(accountIndex).balance += mCustomer.amount;
		mCustomer.customer.msgHereIsBalance(mAccounts.get(accountIndex).balance);
	}
	private void loan(){
		int accountIndex = mAccountIndex.get(mCustomer.mSSN);
		double balance = mAccounts.get(accountIndex).balance;
		if (balance >= (mCustomer.amount+mAccounts.get(accountIndex).loan)*2.0){
			mAccounts.get(accountIndex).loan += mCustomer.amount;
			timer.schedule(new TimerTask(){
				public void run(){
					mCustomer.customer.msgHereIsLoan(mCustomer.amount);
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
		mAccounts.get(accountIndex).loan -= mCustomer.amount;
		mCustomer.customer.msgHereIsLoan(0);
	}
	private void open(){
		mMasterTeller.getAccounts().add(new BankAccount(0, mCustomer.amount, mCustomer.mPerson));
		int accountIndex = mMasterTeller.getAccounts().size() - 1;
		mMasterTeller.getAccountIndex().put(mCustomer.mSSN, accountIndex);
		mCustomer.customer.msgHereIsBalance(mCustomer.amount);
	}
	private void robbery(){
//		int accountIndex = mAccountIndex.get(mCustomer.mSSN);
//		mAccounts.get(accountIndex).balance += mCustomer.amount; 
		mCustomer.customer.msgHereIsBalance(200); //(mMasterTeller.getAccounts().get(accountIndex).balance);
		mGuard.msgRobberAlert(mCustomer.customer);
		print("MESSAGED GUARD ABOUT ROBBERY");
	}
	
	//------------------------------------------------------UTILITIES------------------------------------------------------
	public void addGuard(BankGuard guard){
		mGuard = guard;
	}
	public void setLocation(int location){
		mWindow = location;
	}
	public void setMaster(BankMasterTeller masterTeller) {
		mMasterTeller = masterTeller;
		mAccounts = mMasterTeller.getAccounts();
		mAccountIndex = mMasterTeller.getAccountIndex();
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

	@Override
	public Location getLocation() {
		switch (mBankID){
			case 0: 
				return ContactList.cBANK1_LOCATION;
			case 1: 
				return ContactList.cBANK2_LOCATION;
		}
		return null;
	}
	
	public void setActive(){
		mGuard.msgReadyToWork(this);
	}

	@Override
	public int getWindowNumber() {
		return mWindow;
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
	
	public void fired(){
		mGUI.setFired(true);
		
		mPerson.msgRoleFinished();
		mPerson.assignNextEvent();
		
		mPerson.removeRole(this);
		
		mPerson.setJobType(EnumJobType.NONE);
		
		print("Help I've been fired!");
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				hireNewPerson();
			}
		}, 2000);
	}
	
	public void hireNewPerson(){
		String name = mPerson.getName();
		mPerson.setName("New Person");
		mGUI.setFired(false);
		print("Yay I've been hired :)");
		mPerson.setName(name);
	}
}
