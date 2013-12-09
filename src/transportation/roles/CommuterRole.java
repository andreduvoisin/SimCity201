package transportation.roles;


import transportation.TransportationBus;
import transportation.interfaces.TransportationRider;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

public class CommuterRole extends BaseRole implements TransportationRider {
	
	//DATA
	private Location mDestination;
	private Location mHousingLocation; 
	private Location mCurrentLocation; 

	
	//Bus Data
	public enum PersonBusState {noBus, atBusStop, waitingForBus, boardingBus, 
		ridingBus, exitingBus, noNewDestination};
	private int mCurrentBusStop;
	private int mDestinationBusStop; 
	private PersonBusState mState = PersonBusState.noBus;
	private TransportationBus mBus; 
	
	//CONSTRUCTOR
	public CommuterRole(Person person) {
		super(person);
		mPerson = person;
		mBus = ContactList.cBus; 
		//mHousingLocation = mPerson.getHousingRole().getLocation(); 
	}	
	
	//MESSAGES
	/** From CityPerson (GUI)
	 * When arrived at a corner
	 */
	public void msgAtBusStop(int currentStop, int destinationStop){
		mCurrentBusStop = currentStop;
		mDestinationBusStop = destinationStop; 
		mState = PersonBusState.atBusStop; 
		stateChanged(); 
	}
	
	//FROM BUS
	/**
	 * From BusDispatch
	 * Sent when a BusInstance is at this Rider's current stop
	 */
	public void msgBoardBus() {
		//print("Received msgBoardBus");
		mState = PersonBusState.boardingBus;
		stateChanged();
	}
	
	public void msgAtStop(int busStop){
		if(busStop == mDestinationBusStop){
			mState = PersonBusState.exitingBus; 
		}
		stateChanged(); 
	}
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction() {
			if(mState == PersonBusState.atBusStop){
				NotifyBus(); 
				return true;
			}
			
			else if(mState == PersonBusState.boardingBus){
				BoardBus(); 
				return true; 
			}
			
			else if(mState == PersonBusState.exitingBus){
				ExitBus();
				return true; 
			}
			//MAGGI: Check if you really need this later 
			else if(mState == PersonBusState.noBus){
				GoToDestination(); 
				mState = PersonBusState.noNewDestination; 
				return true; 
			}
		return false; 
	}

	//ACTIONS
	//Manages Bus Transportation
	private void NotifyBus(){
		mBus.msgNeedARide(this, mCurrentBusStop);
		mState = PersonBusState.waitingForBus; 
	}

	private void BoardBus(){
		mPerson.getGui().DoBoardBus();
		mBus.msgImOn(this);
		mState = PersonBusState.ridingBus;
	}
	
	private void ExitBus(){
		mPerson.getGui().DoExitBus(mDestinationBusStop); 
		mBus.msgImOff(this);
		mState = PersonBusState.noBus; 
		stateChanged(); 
	}
	
	private void GoToDestination(){
		if(mDestination != null){
			mPerson.getGui().DoGoToDestination(mDestination);
			mCurrentLocation = mDestination; 
		}
	}
	
	public void setLocation(Location location){
		mDestination = location; 
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

	public String getName() {
		return mPerson.getName();
	}

	@Override
	public Location getLocation() {
		return mCurrentLocation; 
	}
}
