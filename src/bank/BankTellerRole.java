package bank;

import base.Role;
import bank.interfaces.Customer;
import bank.interfaces.Guard;
import bank.interfaces.Teller;

public class BankTellerRole extends Role implements Teller{
	
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
	BankDatabase mDatabase;
	
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
		
	}
	private void loan(){
		
	}
	private void payment(){
		
	}
	private void open(){
		
	}
	private void robbery(){
		
	}
	
//	UTILITIES
	public void addGuard(Guard guard){
		mGuard = guard;
	}
	public void addDatabase(BankDatabase database){
		mDatabase = database;
	}
}
