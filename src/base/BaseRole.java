package base;

import base.interfaces.Person;
import base.interfaces.Role;

public class BaseRole implements Role{
	
	public Person mPerson;
	
	//NEEDED METHODS
	protected void stateChanged(){
		((PersonAgent)mPerson).stateChanged();
	}
	
	public boolean pickAndExecuteAnAction(){
		
		return false;
	}
	
	//ACCESSORS
	public void setPerson(Person person){
		mPerson = person;
	}
	
	public PersonAgent getPersonAgent(){
		return ((PersonAgent)mPerson);
	}
	
	/* Utilities */
	
	protected void print(String msg) {
	}
	
	public int getSSN(){
		return mPerson.getSSN();
	}
}
