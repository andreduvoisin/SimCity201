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
		if(inAHouse()){
			if(!goingToFarHouse(mDestination)){
				mPerson.getGui().DoGoToDestination(mDestination);
			}
			else{
				mPerson.getGui().DoDriveToDestination(mDestination); 
			}
		}
		else{
			if(destinationInSameBlock(mDestination) || !goingToFarHouse(mDestination)){
				mPerson.getGui().DoGoToDestination(mDestination);
			}
			else{
				mPerson.getGui().DoDriveToDestination(mDestination); 
			}
		}
	}
	
	private void GoToDestination(){
		if(mDestination != null)
			mPerson.getGui().DoGoToDestination(mDestination);
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
	//True if destination is in same block as current location
	public boolean destinationInSameBlock(Location destination){
		//LEFT BLOCKS
		if(mCurrentLocation.mX < 300 && destination.mX < 300){
			//TOP LEFT
			if(mCurrentLocation.mY < 300 && destination.mX < 300){
				return true; 
			}
			//BOTTOM LEFT
			else if(mCurrentLocation.mY > 300 && destination.mX < 300){
				return true;
			}
		}
		//RIGHT BLOCKS
		else if(mCurrentLocation.mX > 300 && destination.mX > 300){
			//TOP RIGHT
			if(mCurrentLocation.mY < 300 && destination.mX < 300){
				return true; 
			}
			//TOP LEFT
			else if(mCurrentLocation.mY > 300 && destination.mX < 300){
				return true;
			}
		}
		return false; 
	}
	
	//True if person is going to house across city from their house location
	public boolean goingToFarHouse(Location destination){
		if(Math.abs(mHousingLocation.mX - destination.mX) == 580){
			return true; 
		}
		else if(Math.abs(mHousingLocation.mY - destination.mY) == 580){
			return true; 
		}
		return false; 
	}
	
	//True if person is currently at any House, not necessarily their own
	public boolean inAHouse(){
		if(mCurrentLocation == mHousingLocation){
			return true; 
		}
		else if(mCurrentLocation.mX < 100 
				|| mCurrentLocation.mX > 500 
				|| mCurrentLocation.mY < 100 
				|| mCurrentLocation.mY > 500){
			
			return true; 
		}
		return false;
	}
	
	public void setCurrentLocation(Location location){
		mCurrentLocation = location; 
	}
}
