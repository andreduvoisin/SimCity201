package bank.roles;

import java.util.List;
import java.util.Map;

import bank.Account;
import base.BaseRole;
import bank.interfaces.Customer;
import bank.interfaces.Guard;
import bank.interfaces.MasterTeller;
import bank.interfaces.Teller;

public class BankTellerRole extends BaseRole implements Teller{

//	DATA
	
	class MyCustomer{
		Customer customer;
		String mName;
		int mSSN;
		double mLoan = 0;
		double desiredAmount = 0;
		EnumTransaction transaction = EnumTransaction.None;
		MyCustomer (Customer c, int SSN, double a, EnumTransaction t){
			customer = c;
			mSSN = SSN;
			desiredAmount = a;
			transaction = t;
		}
	}
	public enum EnumTransaction {None, Deposit, Open, Loan, Payment, Robbery};
	
	//GUI Coordinate
	int mLocation;
	//Agent Correspodents
	Guard mGuard;
	MyCustomer mCustomer;
	//Database
	MasterTeller mMasterTeller;
	Map <Integer, Integer> mAccountIndex;
	List <Account> mAccounts;
	
//	MESSAGES
	
	public void msgDeposit(Customer c, int SSN, double amount){
		mCustomer = new MyCustomer(c, SSN, amount, EnumTransaction.Deposit);
		stateChanged();
	}
	public void msgLoan(Customer c, int SSN, double amount, double loan){
		mCustomer = new MyCustomer(c, SSN, amount, EnumTransaction.Loan);
		mCustomer.mLoan = loan;
		stateChanged();
	}
	public void msgPayment(Customer c, int SSN, double amount){
		mCustomer = new MyCustomer(c, SSN, amount, EnumTransaction.Payment);
		stateChanged();
	}
	public void msgOpen(Customer c, int SSN, double amount, String name){
		mCustomer = new MyCustomer(c, SSN, amount, EnumTransaction.Open);
		mCustomer.mName = name;
		stateChanged();
	}
	public void msgRobbery(Customer c, int SSN, double amount){
		mCustomer = new MyCustomer(c, SSN, amount, EnumTransaction.Robbery);
		stateChanged();
	}

//	SCHEDULER
	public boolean pickAndExecuteAnAction(){
		if (mCustomer.transaction == EnumTransaction.Deposit){
			deposit();
			mCustomer.transaction = EnumTransaction.None;
			return true;
		}
		if (mCustomer.transaction == EnumTransaction.Loan){
			loan();
			mCustomer.transaction = EnumTransaction.None;
			return true;
		}
		if (mCustomer.transaction == EnumTransaction.Payment){
			payment();
			mCustomer.transaction = EnumTransaction.None;
			return true;
		}
		if (mCustomer.transaction == EnumTransaction.Open){
			open();
			mCustomer.transaction = EnumTransaction.None;
			return true;
		}
		if (mCustomer.transaction == EnumTransaction.Robbery){
			robbery();
			mCustomer.transaction = EnumTransaction.None;
			return true;
		}
		return false;
	}
	
//	ACTIONS
	
	private void deposit(){
		int accountIndex = mAccountIndex.get(mCustomer.mSSN);
		mAccounts.get(accountIndex).balance += mCustomer.desiredAmount;
	}
	private void loan(){
		int accountIndex = mAccountIndex.get(mCustomer.mSSN);
		double balance = mAccounts.get(accountIndex).balance;
		if (balance >= mCustomer.desiredAmount/2.0 && mCustomer.mLoan == 0){
			mAccounts.get(accountIndex).loan += mCustomer.desiredAmount;
			mCustomer.customer.msgHereIsLoan(mCustomer.desiredAmount);
		}
		else {
			//Non-normative: Loan Rejected
			mCustomer.customer.msgHereIsLoan(0);
		}
	}
	private void payment(){
		int accountIndex = mAccountIndex.get(mCustomer.mSSN);
		mAccounts.get(accountIndex).loan -= mCustomer.desiredAmount;
		mCustomer.customer.msgHereIsLoan(0);
	}
	private void open(){
		mMasterTeller.getAccounts().add(new Account(mCustomer.mName, 0, mCustomer.desiredAmount));
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
	public void addGuard(Guard guard){
		mGuard = guard;
	}
	public void setMaster(MasterTeller masterTeller) {
		mMasterTeller = masterTeller;
	}
	public void setAcountIndex(){
		mAccountIndex = mMasterTeller.getAccountIndex();
	}
	public void setAccounts(){
		mAccounts = mMasterTeller.getAccounts();
	}
}
