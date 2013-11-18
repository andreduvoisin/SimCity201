package base;

public class Role {
	protected PersonAgent mPerson;
	
	
	//NEEDED METHODS
	protected void stateChanged(){
		mPerson.stateChanged();
	}
	
	public boolean pickAndExecuteAnAction(){
		
		return false;
	}
	
	//ACCESSORS
	public void setPerson(PersonAgent person){
		mPerson = person;
	}
	
	public PersonAgent getPersonAgent(){
		return mPerson;
	}
	
	public boolean isActive(){
		return true;
		//TODO: Fix this
	}
}
