package base;

import housing.roles.HousingLandlordRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import market.roles.MarketCashierRole;
import bank.interfaces.MasterTeller;
import bank.roles.BankMasterTellerRole;
import base.Event.EnumEventType;
import base.Item.EnumMarketItemType;
import base.interfaces.Person;
import base.interfaces.Role;

public class PersonAgent extends Agent implements Person {
	//----------------------------------------------------------DATA----------------------------------------------------------
	//Static data
	static int sSSN = 0;
	static int sTimeSchedule = 0; //0,1,2
	static int sEatingTime = 0;
	static final int mealsToEat = 2;
	static int sRestaurantAssignment = 0; //list of 8 restaurants

	//Roles and Job
	static enum EnumJobPlaces {BANK, HOUSING, MARKET, RESTAURANT, TRANSPORTATION};
	private EnumJobPlaces mJobPlace;
	public Map<Role, Boolean> mRoles; // i.e. WaiterRole, BankTellerRole, etc.
	
	//Lists
	List<Person> mFriends; // best are those with same timeshift
	SortedSet<Event> mEvents; // tree set ordered by time of event
	Map<EnumMarketItemType, Integer> mItemInventory; // personal inventory
		//SHANE: Make synchronized?
	Map<EnumMarketItemType, Integer> mItemsDesired; // not ordered yet

	//Personal Variables
	private String mName; 
	int mSSN;
	int mTimeShift;
	int mEatingTime;
	int mMealsToEat;
	double mCash;
	double mLoan;
	boolean mHasHome;
	Set<Location> mHomeLocations; //multiple for landlord
	boolean mHasCar;
	Location mWorkLocation;
	
	//Role References
	public BankMasterTellerRole mMasterTeller;


	// ----------------------------------------------------------CONSTRUCTOR----------------------------------------------------------
	
	public PersonAgent() {
		initializePerson();
	}
	
	public PersonAgent(EnumJobPlaces job, double cash, String name){
		initializePerson();
		mJobPlace = job;
		mCash = cash;
		mName = name;
		
		//SHANE: 1 Make role after asking correct roles
		
		//SHANE: Add try/catch here
		switch (job){
			case BANK:
				mRoles.put(BankMasterTellerRole.getNextRole(), true); //initially active
				break;
			case HOUSING:
				mRoles.put(HousingLandlordRole.getNextRole(), true);
				break;
			case MARKET:
				//Ask market cashier for role
				mRoles.put(MarketCashierRole.getNextRole(), true);
				break;
			case RESTAURANT:
				//SHANE: MAKE A STATIC METHOD FOR RESTAURANT INTERFACE FOR ADDING PEOPLE
				Person hostPerson = (Person) ContactList.sRestaurantHosts.keySet().toArray()[sRestaurantAssignment];
				sRestaurantAssignment = (sRestaurantAssignment + 1) % ContactList.sRestaurantHosts.size(); //should be mod 8
				//SHANE: Create 
				//RestaurantHost host = (RestaurantHost) hostPerson;
				break;
			case TRANSPORTATION:
				//never will happen...
				break;
		}
//		if (mStartingRole.equals("Landlord")) {
//			LandlordRole newLandlordRole = new LandlordRole();
//			for (int i=0; i<4; i++) {
//				newLandlordRole.mHousesList.add(new House(5, 5, 60));				
//			}
//		}
	}
	
	private void initializePerson(){
		//DAVID: Check the initialization to make sure it meshes with the config file
		
		mSSN = sSSN++; // assign SSN
		mTimeShift = (sTimeSchedule++ % 3); // assign time schedule
		mEatingTime = (mTimeShift + 2 * Time.cTimeShift + (sEatingTime++ % (Time.cTimeShift / 2))) % 24; // assign first eating time

		mRoles = new HashMap<Role, Boolean>();
		mCash = 100;
		mLoan = 0;
		
		// Event Setup
		mEvents = Collections.synchronizedSortedSet(new TreeSet<Event>());
		mEvents.add(new Event(EnumEventType.BUY_HOME, 0)); //SHANE REX: 3 check initial times
		mEvents.add(new Event(EnumEventType.GET_CAR, 0));
		mEvents.add(new Event(EnumEventType.JOB, mTimeShift + 0));
		mEvents.add(new Event(EnumEventType.EAT, (mTimeShift + 8 + mSSN % 4) % 24)); // personal time
		mEvents.add(new Event(EnumEventType.EAT, (mTimeShift + 12 + mSSN % 4) % 24)); // shift 4
		mEvents.add(new Event(EnumEventType.PARTY, (mTimeShift + 16)	+ (mSSN + 3) * 24)); // night time, every SSN days
	}
	

	// ----------------------------------------------------------MESSAGES----------------------------------------------------------
	public void msgTimeShift() {
		if (Time.GetShift() == 0) {
			// resetting of variables
			mMealsToEat = 2;
		}
		stateChanged();
	}

	public void msgAddEvent(Event event) {
		if ((event.mEventType == EnumEventType.RSVP1) && (mSSN % 2 == 1)) return; // maybe don't respond (half are deadbeats)
		mEvents.add(event);
	}

	// ----------------------------------------------------------SCHEDULER----------------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {

		// Process events (calendar)
		Iterator<Event> itr = mEvents.iterator();
		while (itr.hasNext()) {
			Event event = itr.next();
			if (event.mTime > Time.GetTime())
				break; // don't do future calendar events
			processEvent(event);
			itr.remove();
		}

		// Do role actions
		for (Role iRole : mRoles.keySet()) {
			if (mRoles.get(iRole)) {
				if (iRole.pickAndExecuteAnAction())
					return true;
			}
		}

		// SHANE: 1 leave role and add role?

		return false;
	}

	// ----------------------------------------------------------ACTIONS----------------------------------------------------------

	private synchronized void processEvent(Event event) {
		//One time events (Home, Car)
		if (event.mEventType == EnumEventType.BUY_HOME) {
			buyHome();
		}
		else if (event.mEventType == EnumEventType.GET_CAR) {
			getCar();
		}
		
		//Daily Recurring Events (Job, Eat)
		if (event.mEventType == EnumEventType.JOB) {
			//bank is closed on weekends
			if (!(Time.IsWeekend()) || (mJobPlace != EnumJobPlaces.BANK)){
				goToJob();
			}
			mEvents.add(new Event(event, 24));
		}
		if (event.mEventType == EnumEventType.EAT) {
			eatFood();
			mEvents.add(new Event(event, 24));
		}

		//Intermittent Events (Deposit Check)
		if (event.mEventType == EnumEventType.DEPOSIT_CHECK) {
			depositCheck();
		}
		
		//Party Events
		if (event.mEventType == EnumEventType.INVITE1) {
			inviteToParty();
		}
		if (event.mEventType == EnumEventType.INVITE2) {
			reinviteDeadbeats();
		}
		if (event.mEventType == EnumEventType.RSVP1) {
			respondToRSVP();
		}
		if (event.mEventType == EnumEventType.RSVP2) {
			respondToRSVP();
		}
		if (event.mEventType == EnumEventType.PARTY) {
			throwParty();
			int inviteNextDelay = 24*mSSN;
			EventParty party = (EventParty) event;
			mEvents.add(new EventParty(party, inviteNextDelay + 2));
			mEvents.add(new EventParty(party, EnumEventType.INVITE1, inviteNextDelay, getBestFriends()));
			mEvents.add(new EventParty(party, EnumEventType.INVITE2, inviteNextDelay + 1, getBestFriends()));
			//SHANE: check event classes
		}
	}

	private void buyHome() {
		
	}

	private void goToJob() {
//		gui.DoGoTo(Location Job);
//		semAnimation.acquire();
		//add job role
	}

	private void eatFood() {

	}

	private void getCar() {

	}

	private void depositCheck() {

	}

	private void throwParty() {

	}

	private void inviteToParty() {

	}

	private void reinviteDeadbeats() {

	}
	
	private void respondToRSVP(){
		
	}

	
	
	private List<Person> getBestFriends(){
		List<Person> bestFriends = new ArrayList<Person>();
		for (Person iPerson : mFriends){
			if (iPerson.getTimeShift() == mTimeShift) bestFriends.add(iPerson);
		}
		return bestFriends;
	}
	
	
	// ----------------------------------------------------------OLD ACTIONS----------------------------------------------------------

	private void GoToWork() {
		// DoGoTo(work.location);
		// work.getHost().msgImHere(job);
		// job.active = T;
		// state = PersonState.Working;
	}

	private void EatFood() {
		// // What will be our algorithm to figure out which to do?
		// switch(random(2)) {
		// case 0:
		// // Eat at home.
		// DoGoTo(home.location);
		// roles.find(HouseRenterRole).active = T;
		// DoGoMakeFoodAtHome();
		// state = PersonState.Eating;
		// break;
		// case 1:
		// // Eat at restaurant.
		// // What will be our algorithm to figure out which restaurant to go
		// to?
		// restaurantChoice = restaurants.chooseRestaurant();
		// DoGoTo(restaurantChoice.location);
		// restaurantChoice.getHost().msgImHungry(roles.find(CustomerRole));
		// roles.find(CustomerRole).active = T;
		// state = PersonState.Eating;
		// break;
		// }
	}

	private void BuyHouse() {
		// DoGoTo(market.location);
		// market.getHost().msgImHere(roles.find(MarketCustomerRole));
		// roles.find(MarketCustomerRole).active = T;
		// state = PersonState.Shopping;
	}

	private void BuyCar() {
		// DoGoTo(market.location);
		// market.getHose().msgImHere(roles.find(MarketCustomerRole));
		// roles.find(MarketCustomerRole).active = T;
		// state = PersonState.Shopping;
	}

	// ----------------------------------------------------------ACCESSORS----------------------------------------------------------

	//SHANE: Organize PersonAgent Accessors
	public void addRole(Role role, boolean active) {
		mRoles.put(role, active);
		role.setPerson(this);
	}

	public void removeRole(Role r) {
		mRoles.remove(r);
	}

	public double getCash() {
		return mCash;
	}

	public void setCash(double cash) {
		mCash = cash;
	}

	public void addCash(double amount) {
		mCash += amount;

	}
	
	public void setLoan(double loan) {
		mLoan = loan;
	}
	
	public double getLoan() {
		return mLoan;
	}

	public Map<EnumMarketItemType, Integer> getItemsDesired() {
		return mItemsDesired;
	}

	public int getSSN() {
		return mSSN;
	}

	public MasterTeller getMasterTeller() {
		return mMasterTeller;
	}

	public Map<EnumMarketItemType, Integer> getItemInventory() {
		return mItemInventory;
	}
	

	protected void print(String msg) {
		System.out.println("" + mName + ": "  + msg);
	}
	
	public String getName(){
		return mName;
	}
	
	public int getTimeShift(){
		return mTimeShift;
	}
	public void msgHereIsPayment(int senderSSN, int amount) {
		mCash += amount;
		//REX: What is this? -Shane
		//SHANE: notification of successful transaction from MasterTeller
	}
	public void setName(String name) {
		mName = name;
	}
	public void setSSN(int SSN) {
		mSSN = SSN;
	}

	@Override
	public void setItemsDesired(Map<EnumMarketItemType, Integer> map) {
		mItemsDesired = map;
	}
}
