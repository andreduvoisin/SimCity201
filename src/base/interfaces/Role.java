package base.interfaces;

import base.Location;


public interface Role {

	public abstract boolean pickAndExecuteAnAction();

	//ACCESSORS
	public abstract Person  getPerson();
	public abstract boolean hasPerson();
	
	public abstract void GoToDestination(Location location);
	
	public abstract void setPerson(Person person);

	public abstract int getSSN();

	public abstract boolean isRestaurantPerson();

	public abstract void setActive();
	
	public abstract Location getLocation();

}