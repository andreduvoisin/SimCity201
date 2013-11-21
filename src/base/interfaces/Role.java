package base.interfaces;

import base.PersonAgent;

public interface Role {

	public abstract boolean pickAndExecuteAnAction();

	//ACCESSORS
	public abstract void setPerson(PersonAgent person);

	public abstract PersonAgent getPersonAgent();

	public abstract int getSSN();

}