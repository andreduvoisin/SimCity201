package base.interfaces;

import java.util.Map;

import base.Event;
import base.Item.EnumItemType;
import city.gui.CityHousing;
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
	public void msgAddEvent(Event event);

	void subLoan(double mTransaction);
	
	public CityPerson getPersonGui();
	
	public CityHousing getHouse();

	void msgHereIsPayment(int senderSSN, double amount);

}
