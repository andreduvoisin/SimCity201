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
	
	public int getSSN(){
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

	@Override
	public Person getPerson() {
		return mPerson;
	}
}
