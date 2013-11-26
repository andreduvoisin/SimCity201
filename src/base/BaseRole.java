package base;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import base.interfaces.Person;
import base.interfaces.Role;

public class BaseRole implements Role {

	protected Person mPerson;
	private Location mLocation;
	
	public BaseRole(Person person) {
		mPerson = person;
	}

	public BaseRole() {
		//SHANE ANGELICA: 2 Remove this soon
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
		return mPerson;
	}

	/* Utilities */

	public int getSSN() {
		return mPerson.getSSN();
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
