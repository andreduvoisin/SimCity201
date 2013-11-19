package bank.roles;

import base.BaseRole;
import bank.interfaces.Customer;
import bank.interfaces.Guard;
import bank.interfaces.MasterTeller;
import bank.interfaces.Teller;

public class BankTellerRole extends BaseRole implements Teller{
	
//	DATA
	
	class MyCustomer{
		Customer customer = null;
		double amount = 0;
		EnumTransaction transaction = EnumTransaction.None;
		MyCustomer (Customer c, double a, EnumTransaction t){
			customer = c;
			amount = a;
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
	
//	MESSAGES
	
	public void msgDeposit(Customer c, double amount){
		mCustomer = new MyCustomer(c, amount, EnumTransaction.Deposit);
		stateChanged();
	}
	public void msgLoan(Customer c, double amount){
		mCustomer = new MyCustomer(c, amount, EnumTransaction.Loan);
		stateChanged();
	}
	public void msgPayment(Customer c, double amount){
		mCustomer = new MyCustomer(c, amount, EnumTransaction.Payment);
		stateChanged();
	}
	public void msgOpen(Customer c, double amount){
		mCustomer = new MyCustomer(c, amount, EnumTransaction.Open);
		stateChanged();
	}
	public void msgRobbery(Customer c, double amount){
		mCustomer = new MyCustomer(c, amount, EnumTransaction.Robbery);
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
		int accountIndex = mMasterTeller.getAccountIndex().get(mCustomer.customer.getSSN());
		mMasterTeller.getAccounts().get(accountIndex).balance += mCustomer.amount;
	}
	private void loan(){
		int accountIndex = mMasterTeller.getAccountIndex().get(mCustomer.customer.getSSN());
		mMasterTeller.getAccounts().get(accountIndex).loan += mCustomer.amount;
		mCustomer.customer.msgHereIsLoan(mCustomer.amount);
	}
	private void payment(){
		int accountIndex = mMasterTeller.getAccountIndex().get(mCustomer.customer.getSSN());
		mMasterTeller.getAccounts().get(accountIndex).loan -= mCustomer.amount;
		mCustomer.customer.msgHereIsLoan(0);
	}
	private void open(){
		//mMasterTeller.getAccounts().add(new Account(mCustomer.customer.))
	}
	private void robbery(){
		int accountIndex = mMasterTeller.getAccountIndex().get(mCustomer.customer.getSSN());
		mMasterTeller.getAccounts().get(accountIndex).balance += mCustomer.amount;
		mCustomer.customer.msgHereIsBalance(mMasterTeller.getAccounts().get(accountIndex).balance);
	}
	
//	UTILITIES
	public void addGuard(Guard guard){
		mGuard = guard;
	}
	public void setMaster(MasterTeller masterTeller) {
		mMasterTeller = masterTeller;
	}
}
