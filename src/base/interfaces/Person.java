package base.interfaces;

import java.util.Map;

<<<<<<< HEAD
import city.gui.CityPerson;
import bank.interfaces.BankMasterTeller;
=======
>>>>>>> 2026c53b8f844162d0c752eac033741c20f440fb
import base.Item.EnumMarketItemType;
import city.gui.CityPerson;

public interface Person {
	void msgTimeShift();
	
	public void setCash(double credit);
	
	public double getCash();
	public int getSSN();
	
	public void addCash(double amount);
	
	public void setLoan(double loan);
	public double getLoan();
	
	public Map<EnumMarketItemType, Integer> getItemsDesired();
	public void setItemsDesired(Map<EnumMarketItemType, Integer> map);
	public Map<EnumMarketItemType, Integer> getItemInventory();
	
	public void addRole(Role role, boolean active);
	
	public void msgOverdrawnAccount(double loan);
	
	public String getName(); 
	public int getTimeShift();
	
	public void setName(String name);
	public void setSSN(int SSN);
	public Map<Role, Boolean> getRoles();
	public Role getHousingRole();
<<<<<<< HEAD
	public void setComponent(CityPerson pc);
	
=======

	void subLoan(double mTransaction);
	
	public CityPerson getPersonGui();

	void msgHereIsPayment(int senderSSN, double amount);

>>>>>>> 2026c53b8f844162d0c752eac033741c20f440fb
}
