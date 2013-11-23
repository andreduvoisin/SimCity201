package bank;

import bank.roles.BankCustomerRole.EnumAction;

public class BankAction{
	public EnumAction action;
	public double amount = 0;
	public BankAction(EnumAction act, double amt){
		action = act;
		amount = amt;
	}
}