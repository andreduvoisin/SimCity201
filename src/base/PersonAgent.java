package base;

import housing.roles.HousingBaseRole;
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
import java.util.concurrent.Semaphore;

import market.roles.MarketCustomerRole;
import reference.simcity.gui.SimCityGui;
import restaurant.interfaces.RestaurantCustomerRole;
import transportation.roles.TransportationBusRiderRole;
import bank.roles.BankCustomerRole;
import bank.roles.BankMasterTellerRole;
import base.Event.EnumEventType;
import base.Item.EnumMarketItemType;
import base.interfaces.Person;
import base.interfaces.Role;
//import city.gui.PersonGui;
import city.gui.CityPerson;


public class PersonAgent extends Agent implements Person {
	//----------------------------------------------------------DATA----------------------------------------------------------
	//Static data
	static int sSSN = 0;
	static int sTimeSchedule = 0; //0,1,2
	static int sEatingTime = 0;
	
	//Roles and Job
	public static enum EnumJobType {BANK, HOUSING, MARKET, RESTAURANT, TRANSPORTATION, NONE};
	private EnumJobType mJobPlace;
	public Map<Role, Boolean> mRoles; // i.e. WaiterRole, BankTellerRole, etc.
	public HousingBaseRole mHouseRole;
	
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
	CityPerson personGui = null;
	
	public Semaphore semAnimationDone = new Semaphore(0);
	private boolean mRoleFinished;
	
	//Role References
	public BankMasterTellerRole mMasterTeller;
	private CityPerson mGui; //SHANE JERRY: 2 instantiate this
	private SimCityGui mRoleGui; //SHANE JERRY: 1 what type does this need to be? make sure this works


	// ----------------------------------------------------------CONSTRUCTOR----------------------------------------------------------
	
	public PersonAgent() {
		initializePerson();
	}
	
	public PersonAgent(EnumJobType job, double cash, String name){
		initializePerson();
		mJobPlace = job;
		mCash = cash;
		mName = name;
		
		boolean active = (mTimeShift == 0);
		switch (job){
			case BANK:
				mRoles.put(SortingHat.getBankRole(mTimeShift), active);
				break;
			case MARKET:
				mRoles.put(SortingHat.getMarketRole(mTimeShift), active);
				break;
			case RESTAURANT:
				mRoles.put(SortingHat.getRestaurantRole(mTimeShift), active);
				break;
			case TRANSPORTATION: break;
			case HOUSING: break;
			case NONE: break;
		}
		
		mHouseRole = (HousingBaseRole) SortingHat.getHousingRole(this); //get housing status
		mRoles.put(mHouseRole, true);
		
		//Add customer/rider role possibilities
		mRoles.put(new BankCustomerRole(this), false);
		mRoles.put(new HousingRenterRole(this), false);
		mRoles.put(new MarketCustomerRole(this), false);
		mRoles.put(new TransportationBusRiderRole(this), false);
		mRoles.put(new RestaurantCustomerRole(this), true); 
		
	}
	
	private void initializePerson(){
		mSSN = sSSN++; // assign SSN
		mTimeShift = (sTimeSchedule++ % 3); // assign time schedule

		mRoles = new HashMap<Role, Boolean>();
		mCash = 100;
		mLoan = 0;
		
		// Event Setup
		mEvents = Collections.synchronizedSortedSet(new TreeSet<Event>());
		mEvents.add(new Event(EnumEventType.GET_CAR, 0));
		mEvents.add(new Event(EnumEventType.JOB, mTimeShift + 0));
		mEvents.add(new Event(EnumEventType.EAT, (mTimeShift + 8 + mSSN % 4) % 24)); // personal time
		mEvents.add(new Event(EnumEventType.EAT, (mTimeShift + 12 + mSSN % 4) % 24)); // shift 4
		mEvents.add(new Event(EnumEventType.PARTY, (mTimeShift + 16)	+ (mSSN + 3) * 24)); // night time, every SSN+3 days
	}
	

	// ----------------------------------------------------------MESSAGES----------------------------------------------------------
	public void msgTimeShift() {
		if (Time.GetShift() == 0) {
			// resetting of variables?
		}
		stateChanged();
	}

	public void msgAddEvent(Event event) {
		if ((event.mEventType == EnumEventType.RSVP1) && (mSSN % 2 == 1)) return; // maybe don't respond (half are deadbeats)
		mEvents.add(event);
	}
	
	public void msgAnimationDone(){
		if (semAnimationDone.availablePermits() == 0) semAnimationDone.release();
	}
	
	public void msgRoleFinished(){
		mRoleFinished = true;
	}

	// ----------------------------------------------------------SCHEDULER----------------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {

		//if not during job shift
		if ((mRoleFinished) && (Time.GetShift() != mTimeShift)){
			// Process events (calendar)
			Iterator<Event> itr = mEvents.iterator();
			while (itr.hasNext()) {
				Event event = itr.next();
				if (event.mTime > Time.GetTime())
					break; // don't do future calendar events
				processEvent(event);
				itr.remove();
			}
		}
		
		// Do role actions
		for (Role iRole : mRoles.keySet()) {
			if (mRoles.get(iRole)) {
				if (iRole.pickAndExecuteAnAction())
					return true;
			}
		}
		
		return false;
	}

	// ----------------------------------------------------------ACTIONS----------------------------------------------------------

	private synchronized void processEvent(Event event) {
		//One time events (Car)
		if (event.mEventType == EnumEventType.GET_CAR) {
			getCar(); //SHANE: 1 get car
		}
		
		//Daily Recurring Events (Job, Eat)
		else if (event.mEventType == EnumEventType.JOB) {
			//bank is closed on weekends
			if (!(Time.IsWeekend()) || (mJobPlace != EnumJobType.BANK)){
				goToJob(); //SHANE: 1 go to job
			}
			mEvents.add(new Event(event, 24));
		}
		else if (event.mEventType == EnumEventType.EAT) {
			eatFood(); //SHANE: 1 eat food
			mEvents.add(new Event(event, 24));
		}

		//Intermittent Events (Deposit Check)
		else if (event.mEventType == EnumEventType.DEPOSIT_CHECK) {
			depositCheck(); //SHANE: 1 deposit check
		}
		
		else if (event.mEventType == EnumEventType.ASK_FOR_RENT) {
			invokeRent(); //SHANE: 1 invoke rent
		}
		
		else if (event.mEventType == EnumEventType.MAINTAIN_HOUSE) {
			invokeMaintenance(); //SHANE: 1 invoke maintenance
		}
		
		//Party Events
		else if (event.mEventType == EnumEventType.INVITE1) {
			inviteToParty(); //SHANE: 1 invite to party
		}
		else if (event.mEventType == EnumEventType.INVITE2) {
			reinviteDeadbeats(); //SHANE: 1 reinvite deadbeats
		}
		else if (event.mEventType == EnumEventType.RSVP1) {
			respondToRSVP(); //SHANE: 1 respond to rsvp
		}
		else if (event.mEventType == EnumEventType.RSVP2) {
			respondToRSVP(); //SHANE: 1 respond to rsvp (same)
		}
		else if (event.mEventType == EnumEventType.PARTY) {
			throwParty(); //SHANE: 1 throw party
			int inviteNextDelay = 24*mSSN;
			EventParty party = (EventParty) event;
			mEvents.add(new EventParty(party, inviteNextDelay + 2));
			mEvents.add(new EventParty(party, EnumEventType.INVITE1, inviteNextDelay, getBestFriends()));
			mEvents.add(new EventParty(party, EnumEventType.INVITE2, inviteNextDelay + 1, getBestFriends()));
			//SHANE: 3 check event classes
		}
	}
	
	private void acquireSemaphore(Semaphore semaphore){
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void getCar(){
		Location location = ContactList.cMARKET_LOCATION;
		mGui.DoGoToDestination(location);
		acquireSemaphore(semAnimationDone);
		
		//remove current gui (isPresent = false)
		mGui.setInvisible();
		//create new market gui
		
		
		//lock person until role is finished
		mRoleFinished = false;
		//activate marketcustomer role
		for (Role iRole : mRoles.keySet()){
			if (iRole instanceof MarketCustomerRole){
				mRoles.put(iRole, true); //set active
			}
		}
		
		//add desired item
		mItemsDesired.put(EnumMarketItemType.CAR, 1); //want 1 car
		//PAEA for role will message market cashier to start transaction
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

	public void eatFood() {
		//decide if eating at home or not
		//SHANE REX: 3 get to this 
		
		//set random restaurant
		RestaurantCustomerRole restaurantCustomerRole = null;
		for (Role iRole : mRoles.keySet()){
			if (iRole instanceof RestaurantCustomerRole){
				restaurantCustomerRole = (RestaurantCustomerRole) iRole;
			}
		}
		
		int randomRestaurant = 1; //SHANE: Make random
		restaurantCustomerRole.setRestaurant(randomRestaurant); //DAVID: 1 This is where it's set
		
		try {
			mGui.DoGoToDestination(ContactList.cRESTAURANT_LOCATIONS.get(randomRestaurant));
		}
		catch (Exception e) {
		}
		acquireSemaphore(semAnimationDone);
		
	}
	
	public void SetGui(CityPerson pGui){
		personGui = pGui;
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
	
	private void invokeRent() {
		mHouseRole.msgTimeToCheckRent();
	}
	
	private void invokeMaintenance() {
		mHouseRole.msgTimeToMaintain();
	}
	
	
	//JERRY 0 FOR TESTING
	public void move(){
		mGui.DoGoToDestination(ContactList.cBANK_LOCATION);
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

	@Override
	public Map<Role, Boolean> getRoles() {
		return mRoles;
	}

	@Override
	public Role getHousingRole() {
		return mHouseRole;
	}
}
