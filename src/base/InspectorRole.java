package base;

import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

// "Dummy" role!
public class InspectorRole extends BaseRole{
	
//	DATA
	
	public InspectorRole(Person person){
		super(person);
	}
	
//	MESSAGES
	
//	SCHEDULER
	
	public boolean pickAndExecuteAnAction(){
		
		return false;
	}
	
//	ACTIONS
	
	public int getSSN() {
		return mPerson.getSSN();
	}
	
//	UTILITIES
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.HOUSING);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.HOUSING);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.HOUSING, e);
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return null;
	}
}
