package base.interfaces;

import java.util.Map;

import bank.interfaces.MasterTeller;
import base.BaseRole;
import base.Item.EnumMarketItemType;

public interface Person {
	//TODO: add stuff here
	
	void msgTimeShift();
	
	public void setCash(double credit);
	
	public double getCash();
	public int getSSN();
	public MasterTeller getMasterTeller();
	
	public void addCash(double amount);
	
	public Map<EnumMarketItemType, Integer> getItemsDesired();
	public Map<EnumMarketItemType, Integer> getItemInventory();
	
	public void addRole(Role r); 
	
}
