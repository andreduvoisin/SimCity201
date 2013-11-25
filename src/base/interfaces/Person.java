package base.interfaces;

import java.util.Map;

<<<<<<< HEAD
<<<<<<< HEAD
import city.gui.CityPerson;
import bank.interfaces.BankMasterTeller;
=======
>>>>>>> 2026c53b8f844162d0c752eac033741c20f440fb
import base.Item.EnumMarketItemType;
=======
import base.Item.EnumItemType;
>>>>>>> 52f192f2b9f249fd590d755ac054c75f2f56e2a4
import city.gui.CityPerson;

public interface Person {
	void msgTimeShift();

	public void setCash(double credit);
	
	//public void stateChanged();
	
	public double getCash();
	public int getSSN();
	
	public void addCash(double amount);
	
	public void setLoan(double loan);
	public double getLoan();
	
	public Map<EnumItemType, Integer> getItemsDesired();
	public void setItemsDesired(Map<EnumItemType, Integer> map);
	public Map<EnumItemType, Integer> getItemInventory();
	
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
