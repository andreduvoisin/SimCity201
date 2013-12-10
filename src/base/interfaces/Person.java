package base.interfaces;

import housing.interfaces.HousingBase;

import java.util.Map;

import base.Event;
import base.Item.EnumItemType;
import base.PersonAgent.EnumJobType;
import city.gui.CityPerson;
import city.gui.trace.AlertTag;

public interface Person {
	void msgTimeShift();

	public void setCash(double credit);
	
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
	public HousingBase getHousingRole();
	public void msgAddEvent(Event event);

	void subLoan(double mTransaction);
	
	public CityPerson getPersonGui();
	
	void msgHereIsPayment(int senderSSN, double amount);
	public void setGuiPresent();
	public CityPerson getGui();
	
	public void msgRoleFinished();
	public void setJobFalse();
	public boolean hasCar();
	public void setHasCar(boolean c);
	
	abstract void print(String msg, AlertTag tag);
	abstract void print(String msg, AlertTag tag, Throwable e);

	void msgStateChanged();
	
	public abstract EnumJobType getJobType();
}
