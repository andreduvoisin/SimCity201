package base;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.trace.AlertTag;

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
	
	//TRANSPORTATION
	public void GoToDestination(Location location){
		mPerson.getGui().DoGoToDestination(location);
	
	}
	//SHANE: 4 The commuterRole uses this call to mPerson.getGui() but I can't seem to find that function so I added an empty shell... -Jerry
	public void DriveToDestination(Location location){
		
	}
	
	// ACCESSORS
	public void setPerson(Person person) {
		mPerson = person;
	}

	public Person getPerson() {
		return mPerson;
	}
	
	public boolean hasPerson() {
		if (mPerson == null)
			return false;
		return true;
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
	public void Do(String msg, AlertTag tag) {
		mPerson.print(msg, tag);
	}

	/**
	 * Print message
	 */
	public void print(String msg, AlertTag tag) {
		mPerson.print(msg, tag);
	}

	/**
	 * Print message with exception stack trace
	 */
	public void print(String msg, AlertTag tag, Throwable e) {
		mPerson.print(msg, tag, e);
	}
	
	public boolean isRestaurantPerson(){ 
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
