package base;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.SimCityGui;

public abstract class BaseRole implements Role {

	protected Person mPerson;
	public boolean mActive;
	
	public BaseRole(Person person) {
		mPerson = person;
		mActive = false;
	}

	// NEEDED METHODS
	public void stateChanged() {
		((PersonAgent) mPerson).stateChanged();
	}

	public boolean pickAndExecuteAnAction() {
		return false;
	}

	// ACCESSORS
	public void setPerson(Person person) {
		mPerson = person;
	}

	public Person getPerson() {
		if(mPerson == null)
			return null;
		return mPerson;
	}

	/* Utilities */

	public int getSSN() {
		return mPerson.getSSN();
	}

	public boolean getActive() {
		return mActive;
	}
	
	public void setActive() {
		if(mActive)
			mActive = false;
		else
			mActive = true;
	}
	
	/**
	 * The simulated action code
	 */
	protected void Do(String msg) {
		print(msg, null);
	}

	/**
	 * Print message
	 */
	protected void print(String msg) {
		if (SimCityGui.TESTING)
			print(msg, null);
	}

	/**
	 * Print message with exception stack trace
	 */
	protected void print(String msg, Throwable e) {
		StringBuffer sb = new StringBuffer();
		sb.append(mPerson.getName());
		sb.append(": ");
		sb.append(msg);
		sb.append("\n");
		if (e != null) {
			sb.append(StringUtil.stackTraceString(e));
		}
		System.out.print(sb.toString());
	}
	
	public boolean isRestaurantPerson(){ //DAVID: Put this in base rest class
		if ((this instanceof RestaurantCashierRole) ||
				(this instanceof RestaurantCookRole) ||
				(this instanceof RestaurantWaiterRole) ||
				(this instanceof RestaurantHostRole) ||
				(this instanceof RestaurantCustomerRole)){
			return true;
		}
		return false;
	}

}
