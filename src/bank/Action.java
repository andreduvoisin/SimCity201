package bank;

import bank.roles.BankCustomerRole.EnumAction;

public class Action{
	public EnumAction action;
	public double amount = 0;
	public Action(EnumAction act, double amt){
		action = act;
		amount = amt;
	}
}