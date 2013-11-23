package base.interfaces;

import java.util.Map;

import bank.interfaces.BankMasterTeller;
import base.Item.EnumMarketItemType;

public interface Person {
	void msgTimeShift();
	
	public void setCash(double credit);
	
	public double getCash();
	public int getSSN();
	public BankMasterTeller getMasterTeller();
	
	public void addCash(double amount);
	
	public void setLoan(double loan);
	public double getLoan();
	
	public Map<EnumMarketItemType, Integer> getItemsDesired();
	public void setItemsDesired(Map<EnumMarketItemType, Integer> map);
	public Map<EnumMarketItemType, Integer> getItemInventory();
	
	public void addRole(Role role, boolean active);
	
	public void msgHereIsPayment(int senderSSN, double amount);
	public void msgOverdrawnAccount(double loan);
	
	public String getName(); 
	public int getTimeShift();
	
	public void setName(String name);
	public void setSSN(int SSN);
	public Map<Role, Boolean> getRoles();
	public Role getHousingRole();

}
