package transportation.roles;

import city.gui.trace.AlertTag;
import transportation.TransportationBusDispatch;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;

public class CommuterRole extends BaseRole{
	
	//DATA
	private Location mDestination;
	
	//Bus Data 
	private enum PersonBusState {WaitingForBus}; 
	private int mCurrentBusStop;
	private int mDestinationBusStop; 
	private PersonBusState mState; 
	//MAGGI: put where MasterTeller is being created 
	private TransportationBusDispatch mBusDispatch = new TransportationBusDispatch(); 
	
	//CONSTRUCTOR
	public CommuterRole(Person person) {
		super(person);
		mPerson = person;
	}	
	
	//MESSAGES
	public void msgAtBusStop(int currentStop, int destinationStop){
		mCurrentBusStop = currentStop;
		mDestinationBusStop = destinationStop; 
		mState = PersonBusState.WaitingForBus; 
		stateChanged(); 
	}
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction() {
		if(mState == PersonBusState.WaitingForBus){
			NotifyBusDispatch(); 
		}
		if(mPerson.hasCar()){
			//mPerson.getGui().DoDriveToDestination(); 
		}
		else{
			GoToDestination(); 
			return true; 
		}
		return false; 
	}

	//ACTIONS
	//Manages Bus Transportation
	private void NotifyBusDispatch(){
		//mBusDispatch.msgNeedARide(this, mCurrentBusStop);
	}
	
	
	private void GoToDestination(){
		if(mDestination != null)
			mPerson.getGui().testDoGoToDestination(mDestination);
	}
	
	public void setLocation(Location location){
		mDestination = location; 
	}
	
	@Override
	public Location getLocation() {
		return null;
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.TRANSPORTATION);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.TRANSPORTATION);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.TRANSPORTATION, e);
	}
}
