package base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import base.Event.EnumEventType;
import base.Item.EnumMarketItemType;
import base.interfaces.Person;

public class PersonAgent extends Agent implements Person{

	//Data
	List<Role> mRoles; //i.e. WaiterRole, BankTellerRole, etc.
	List<Person> mFriends; //best are those with same timeshift
	SortedSet<Event> mEvents; //tree set ordered by time of event
	Map<EnumMarketItemType, Integer> mItemInventory; //personal inventory
	Map<EnumMarketItemType, Integer> mItemsDesired; //not ordered yet
	
	// Assigned in Constructor when PersonAgent is initialized.
	static int sSSN = 0;
	int mSSN;
	static int sTimeSchedule = 0;
	int mTimeSchedule;
	static int sEatingTime = 0;
	int mEatingTime;
	static final int mealsToEat = 2;
	int mMealsToEat;

	double mCash;
	int mAge;

	boolean mHasHome;
	boolean mHasLoan;
	boolean mHasCar;

//	List<Restaurant> mRestaurants;
//	Restaurant mRestaurantChoice;
//
//	Home mHome;
//	Work mWork;
//	Market mMarket;
	Role mJob;

	//----------------------------------------------------------CONSTRUCTOR----------------------------------------------------------
	public PersonAgent(){
		mSSN = sSSN++; //assign SSN
		mTimeSchedule = (sTimeSchedule++ % Time.cTimeShift); //assign time schedule 
		mEatingTime = (mTimeSchedule + 2*Time.cTimeShift + (sEatingTime++ % (Time.cTimeShift/2))) % 24; //assign first eating time
		
		mRoles = new ArrayList<Role>();
		mCash = 0; //TODO: 3 update this val
		

		//Event Setup
		mEvents.add(new Event(EnumEventType.BUY_HOME, 0)); //TODO Shane: 3 check initial times TODO Rex: 3 check initial times
		mEvents.add(new Event(EnumEventType.GET_CAR, 0));
		mEvents.add(new Event(EnumEventType.JOB, mTimeSchedule + 0));
		mEvents.add(new Event(EnumEventType.EAT, (mTimeSchedule + 8 + mSSN%4)%24 )); //personal time
		mEvents.add(new Event(EnumEventType.EAT, (mTimeSchedule + 12 + mSSN%4)%24)); //shift 4
		mEvents.add(new Event(EnumEventType.PARTY, (mTimeSchedule + 16) + (mSSN+3)*24)); //night time, every SSN days
		
	}
	
	
	
	//----------------------------------------------------------MESSAGES----------------------------------------------------------
	public void msgTimeShift(){
		if (Time.GetShift() == 0){
			//resetting of variables
			mAge++;
			mMealsToEat = 2;
		}
		stateChanged();
	}
	
	public void msgAddEvent(Event event){
		if ((event.mEvent == EnumEventType.RSVP1) && (mSSN%2 == 1)) return; //maybe don't respond
		mEvents.add(event);
	}
	
	
	
	//----------------------------------------------------------SCHEDULER----------------------------------------------------------
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		//Process events (calendar)
		for (Event event : mEvents){
			if (event.mTime > Time.GetTime()) break; //don't do future calendar events
			processEvent(event);
		}
		
		//Do role actions
		for (Role iRole : mRoles){
			if (iRole.isActive()){
				if (iRole.pickAndExecuteAnAction()) return true;
			}
		}
		
		//TODO: 1 leave role and add role?
		
		return false;
	}
	
	
	
	//----------------------------------------------------------ACTIONS----------------------------------------------------------
	
	private void processEvent(Event event){
		//TODO 1 add event
		if (event.mEvent == EnumEventType.BUY_HOME){
			buyHome();
		}
		if (event.mEvent == EnumEventType.JOB){
			goToJob();
			mEvents.add(new Event(event, 24));
		}
		if (event.mEvent == EnumEventType.EAT){
			eatFood();
			mEvents.add(new Event(event, 24));
		}
		if (event.mEvent == EnumEventType.GET_CAR){
			getCar();
		}
		if (event.mEvent == EnumEventType.DEPOSIT_CHECK){
			depositCheck();
		}
		if (event.mEvent == EnumEventType.INVITE1){
			
		}
		if (event.mEvent == EnumEventType.INVITE2){
			
		}
		if (event.mEvent == EnumEventType.PARTY){
			throwParty();
			//recurring?
		}
		if (event.mEvent == EnumEventType.RSVP1){
			
		}
		if (event.mEvent == EnumEventType.RSVP2){
			
		}
		//and remove the event
		mEvents.remove(event);
	}
	
	
	
	
	private void buyHome(){
		
	}
	
	private void goToJob(){
		
	}
	
	private void eatFood(){
		
	}
	
	private void getCar(){
		
	}
	
	private void depositCheck(){
		
	}
	
	private void throwParty(){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//----------------------------------------------------------OLD ACTIONS----------------------------------------------------------
	
	private void GoToWork() {
//		DoGoTo(work.location);
//		work.getHost().msgImHere(job);
//		job.active = T;
//		state = PersonState.Working;
	}

	private void EatFood() {
//		// What will be our algorithm to figure out which to do?
//		switch(random(2)) {
//			case 0:
//				// Eat at home.
//				DoGoTo(home.location);
//				roles.find(HouseRenterRole).active = T;
//				DoGoMakeFoodAtHome();
//				state = PersonState.Eating;
//				break;
//			case 1:
//				// Eat at restaurant.
//				// What will be our algorithm to figure out which restaurant to go to?
//				restaurantChoice = restaurants.chooseRestaurant();
//				DoGoTo(restaurantChoice.location);
//				restaurantChoice.getHost().msgImHungry(roles.find(CustomerRole));
//				roles.find(CustomerRole).active = T;
//				state = PersonState.Eating;
//				break;
//		}
	}

	private void BuyHouse() {
//		DoGoTo(market.location);
//		market.getHost().msgImHere(roles.find(MarketCustomerRole));
//		roles.find(MarketCustomerRole).active = T;
//		state = PersonState.Shopping;
	}

	private void BuyCar() {
//		DoGoTo(market.location);
//		market.getHose().msgImHere(roles.find(MarketCustomerRole));
//		roles.find(MarketCustomerRole).active = T;
//		state = PersonState.Shopping;
	}
	
	
	
	
	
	
	
	
	//----------------------------------------------------------ACCESSORS----------------------------------------------------------
	
	public void addRole(Role r) {
		mRoles.add(r);
		r.setPerson(this);
	}
	public void removeRole(Role r) {
		mRoles.remove(r);
	}

	public double getCash () {
		return mCash;
	}

	public void setCash (double cash) {
		mCash = cash;
	}

	public void addCash(double amount) {
		mCash += amount;
		
	}
	
	public Map<EnumMarketItemType, Integer> getItemsDesired(){
		return mItemsDesired;
	}

	public int getSSN(){
		return mSSN;
	}
	
	public Map<EnumMarketItemType, Integer> getItemInventory(){
		return mItemInventory;
	}
}
