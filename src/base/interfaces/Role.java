package base.interfaces;

import base.PersonAgent;

public interface Role {

	public abstract boolean pickAndExecuteAnAction();

	//ACCESSORS
	public abstract Person getPerson();
	
	public abstract void setPerson(Person person);

//	public abstract PersonAgent getPersonAgent();

	public abstract int getSSN();

}