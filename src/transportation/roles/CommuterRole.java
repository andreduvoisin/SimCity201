package transportation.roles;


import transportation.interfaces.TransportationRider;

import transportation.TransportationBus;

import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

public class CommuterRole extends BaseRole implements TransportationRider {
	
	//DATA
	private Location mDestination;
	private Location mHousingLocation; 
	private Location mCurrentLocation; 
	
	//Bus Data 
	private enum PersonBusState {WaitingForBus, BoardBus, RidingBus}; 
	private int mCurrentBusStop;
	private int mDestinationBusStop; 
	private PersonBusState mState;
	//MAGGI: put where MasterTeller is being created 
	private TransportationBus mBus = new TransportationBus(); 
	
	//CONSTRUCTOR
	public CommuterRole(Person person) {
		super(person);
		mPerson = person;
		
		mHousingLocation = mPerson.getHousingRole().getLocation(); 
	}	
	
	//MESSAGES
	/** From GUI
	 * When arrived at a corner
	 */
	public void msgAtBusStop(int currentStop, int destinationStop){
		mCurrentBusStop = currentStop;
		mDestinationBusStop = destinationStop; 
		mState = PersonBusState.WaitingForBus; 
		stateChanged(); 
	}
	
	//From Animation
	public void msgAnimationDone(){
		
	}
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction() {
		if(mState == PersonBusState.WaitingForBus){
			NotifyBusDispatch(); 
		}
		if(mPerson.hasCar()){
			DriveToDestination(); 
			//TryDrivingToDestination(); 
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
	
	private void DriveToDestination(){
		mPerson.getGui().DoDriveToDestination(); 
	}
	
	private void TryDrivingToDestination(){
		if(mCurrentLocation == mHousingLocation){
			mPerson.getGui().DoDriveToDestination(); 
		}
		else{
			if(mDestination == mHousingLocation){
				mPerson.getGui().DoDriveToDestination(); 
				mPerson.getGui().testDoGoToDestination(mDestination);
				mCurrentLocation = mDestination; 
			}
			else{
				
			}
		}
	
	}
	
	private void GoToDestination(){
		if(mDestination != null)
			mPerson.getGui().testDoGoToDestination(mDestination);
			mCurrentLocation = mDestination; 
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

	@Override
	public void msgBoardBus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtStop(int stopBusIsAt) {
		if (stopBusIsAt == mDestinationBusStop) {
			mState = PersonBusState.BoardBus;
		}
		stateChanged();
	}
	
	//UTILITES
	
	public void setCurrentLocation(Location location){
		mCurrentLocation = location; 
	}
}
