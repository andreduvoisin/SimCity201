package base.interfaces;


public interface Role {

	public abstract boolean pickAndExecuteAnAction();

	//ACCESSORS
	public abstract Person getPerson();
	
	public abstract void setPerson(Person person);

//	public abstract PersonAgent getPersonAgent();

	public abstract int getSSN();

	public abstract boolean isRestaurantPerson();

}