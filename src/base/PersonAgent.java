package base;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import base.interfaces.Person;

public class PersonAgent extends Agent implements Person{

	//final Vars

	//Data
	List<Role> mRoles; //i.e. WaiterRole, BankTellerRole, etc.
	List<Person> mFriends; //best are those with same timeshift
	SortedSet<Event> mEventsScheduled; //tree set ordered by time of event
	List<Event> mEventsQueued;
	List<Item> mItem;
	
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


	public PersonAgent(){
		mSSN = sSSN++; //assign SSN
		mTimeSchedule = (sTimeSchedule++ % Time.cTimeShift); //assign time schedule 
		mEatingTime = (mTimeSchedule + 2*Time.cTimeShift + (sEatingTime++ % (Time.cTimeShift/2))) % 24; //assign first eating time
		
		mRoles = new ArrayList<Role>();
		mCash = 0; //TODO: 3 update this val
		
		//TODO 1 constructor - add events
		
		
	}
	
	
	
	//Messages
	public void msgTimeShift(){
		if (Time.GetShift() == 0){
			//resetting of variables
			mAge++;
			mMealsToEat = 2;
		}
		stateChanged();
	}
	
	public void msgAddEvent(Event a){
		
	}
	
	public void msgAddEvent(Event a, Event b){
		
	}
	
	//Scheduler
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		//INITIAL SETUP
		if (!mHasHome && (mAge < 2)){
//			arriveAtCity();
		}
		if (!mHasHome){
			BuyHouse();
		}
		if (!mHasCar && mAge > 2){
			BuyCar();
		}
		//has loan?
		
		
		// WORK
		if ((Time.GetShift() + mTimeSchedule) % 3 == 0){	
			GoToWork();
		}
		
		// ROLE
		for (Role iRole : mRoles){
			if (iRole.isActive()){
				if (iRole.pickAndExecuteAnAction()) return true;
			}
		}

		// PERSONAL
		
		//Eating
		if (	(mMealsToEat == 2) && ((Time.GetHour() >= mEatingTime) && (Time.GetHour() < mEatingTime + Time.cTimeShift))	||
				(mMealsToEat == 1) && ((Time.GetHour() >= (mEatingTime + Time.cTimeShift/2) % 24) && (Time.GetHour() < mEatingTime + Time.cTimeShift) ) ){
			EatFood();
		}
		
		return false;
	}
	
	public void paea(Role role){
		while(role.pickAndExecuteAnAction());
	}
	
	
	//Actions
	
	private void processEvent(Event event){
		//TODO 1 add event
	}
	
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
	
	
	
	public void addRole(Role r) {
//		roles.add(r);
//		r.setPerson(this);
	}
	public void removeRole(Role r) {
//		roles.remove(r);
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
	
	
}
