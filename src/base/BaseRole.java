package base;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import base.interfaces.Person;
import base.interfaces.Role;

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
		if(mPerson.hasCar()){
			mPerson.getGui().DoDriveToDestination(location); 
		}
		else{
			mPerson.getGui().testDoGoToDestination(location);
		}
	
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
	public void Do(String msg) {
		mPerson.print(msg);
	}

	/**
	 * Print message
	 */
	public void print(String msg) {
		mPerson.print(msg);
	}

	/**
	 * Print message with exception stack trace
	 */
	public void print(String msg, Throwable e) {
		mPerson.print(msg, e);
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
