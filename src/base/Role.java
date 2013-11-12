package base;

public class Role {
	PersonAgent mPerson;
	
	public void setPerson(PersonAgent person){
		mPerson = person;
	}
	
	public PersonAgent getPersonAgent(){
		return mPerson;
	}
	
	private void stateChanged(){
		mPerson.stateChanged();
	}
	
	public boolean pickAndExecuteAnAction(){
		
		return false;
	}
}
