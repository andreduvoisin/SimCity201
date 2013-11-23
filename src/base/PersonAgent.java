package base;

import housing.roles.HousingRenterRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import market.roles.MarketCustomerRole;
import bank.interfaces.BankMasterTeller;
import bank.roles.BankCustomerRole;
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
	
	//Roles and Job
	static enum EnumJobType {BANK, HOUSING, MARKET, RESTAURANT, NONE};
	private EnumJobType mJobPlace;
	public Map<Role, Boolean> mRoles; // i.e. WaiterRole, BankTellerRole, etc.
	
	//Lists
	List<Person> mFriends; // best are those with same timeshift
	SortedSet<Event> mEvents; // tree set ordered by time of event
	Map<EnumMarketItemType, Integer> mItemInventory; // personal inventory
		//ALL: Does this need to be synchronized? -Shane
	Map<EnumMarketItemType, Integer> mItemsDesired; // not ordered yet

	//Personal Variables
	private String mName; 
	int mSSN;
	int mTimeShift;
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
	
	public PersonAgent(EnumJobType job, double cash, String name){
		initializePerson();
		mJobPlace = job;
		mCash = cash;
		mName = name;
		
		switch (job){
			case BANK:
				mRoles.put(SortingHat.getBankRole(), true); //true = initially active
				break;
			case HOUSING:
				mRoles.put(SortingHat.getHousingRole(), true);
				break;
			case MARKET:
				mRoles.put(SortingHat.getMarketRole(), true);
				break;
			case RESTAURANT:
				mRoles.put(SortingHat.getRestaurantRole(), true);
				break;
			case NONE:
				//wealthy people - no role
				break;
		}
		
		//SHANE: 1 add other roles like customer roles here
		mRoles.put(new BankCustomerRole(this), false);
		mRoles.put(new HousingRenterRole(this), false);
		mRoles.put(new MarketCustomerRole(this), false);
		//RestaurantCustomerRole
		//TransportationBusRiderRole
		
	}
	
	private void initializePerson(){
		//DAVID: Check the initialization to make sure it meshes with the config file
		
		mSSN = sSSN++; // assign SSN
		mTimeShift = (sTimeSchedule++ % 3); // assign time schedule

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
			if (!(Time.IsWeekend()) || (mJobPlace != EnumJobType.BANK)){
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
		
		// DoGoTo(work.location);
		// work.getHost().msgImHere(job);
		// job.active = T;
		// state = PersonState.Working;
	}

	private void eatFood() {
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

	private void getCar() {
		// DoGoTo(market.location);
		// market.getHose().msgImHere(roles.find(MarketCustomerRole));
		// roles.find(MarketCustomerRole).active = T;
		// state = PersonState.Shopping;
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
	

	// ----------------------------------------------------------ACCESSORS----------------------------------------------------------

	//SHANE: 4 Organize PersonAgent Accessors
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

	public BankMasterTeller getMasterTeller() {
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
	public void msgHereIsPayment(int senderSSN, double amount) {
		mCash += amount;
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

	@Override
	public void msgOverdrawnAccount(double loan) {
		mLoan += loan;
	}
}
