package base;

import housing.roles.HousingBaseRole;
import housing.roles.HousingRenterRole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import market.roles.MarketCustomerRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.restaurant_davidmca.astar.AStarTraversal;
import transportation.roles.TransportationBusRiderRole;
import bank.roles.BankCustomerRole;
import bank.roles.BankMasterTellerRole;
import base.Event.EnumEventType;
import base.Item.EnumMarketItemType;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityPanel;
import city.gui.CityPerson;

import astar.*;


public class PersonAgent extends Agent implements Person {
	//----------------------------------------------------------DATA----------------------------------------------------------
	//Static data
	private static int sSSN = 0;
	//private static int sTimeSchedule = 0; //0,1,2
	//private static int sEatingTime = 0;
	
	//Roles and Job
	public static enum EnumJobType {BANK, HOUSING, MARKET, RESTAURANT, TRANSPORTATION, NONE};
	public EnumJobType mJobType;
	public Map<Role, Boolean> mRoles; //roles, active -  i.e. WaiterRole, BankTellerRole, etc.
	public HousingBaseRole mHouseRole;
	public Role mJobRole;
	private Location mJobLocation;
	private boolean mAtJob;
	
	//Lists
	List<Person> mFriends; // best are those with same timeshift
	public SortedSet<Event> mEvents; // tree set ordered by time of event
	Map<EnumMarketItemType, Integer> mItemInventory; // personal inventory
	Map<EnumMarketItemType, Integer> mItemsDesired; // not ordered yet
	Set<Location> mHomeLocations; //multiple for landlord
	
	//Personal Variables
	private String mName; 
	int mSSN;
	int mTimeShift;
	double mCash;
	double mLoan;
	boolean mHasCar;
	
	//Role References
	public BankMasterTellerRole mMasterTeller;
	public CityPerson mPersonGui; //SHANE JERRY: 2 instantiate this

	//PAEA Helpers
	public Semaphore semAnimationDone = new Semaphore(1);
	private boolean mRoleFinished = true;

	// ----------------------------------------------------------CONSTRUCTOR----------------------------------------------------------
	
	public PersonAgent() {
		initializePerson();
	}
	
	public PersonAgent(EnumJobType job, double cash, String name){
		mJobType = job;
		mCash = cash;
		mName = name;
		initializePerson();
		
		//REX: put in for testing
		SortingHat.InstantiateBaseRoles();
		
		//Get job role and location; set active if necessary
		mJobRole = null;
		switch (job){
			case BANK:
				mJobRole = SortingHat.getBankRole(mTimeShift);
				break;
			case MARKET:
				mJobRole = SortingHat.getMarketRole(mTimeShift);
				break;
			case RESTAURANT:
				mJobRole = SortingHat.getRestaurantRole(mTimeShift);
				break;
			case TRANSPORTATION: break;
			case HOUSING: break;
			case NONE: break;
		}
		boolean active = (mTimeShift == Time.GetShift());
		if (mJobRole != null){
			mJobLocation = ContactList.sRoleLocations.get(mJobRole);
			mRoles.put(mJobRole, active);
		}
		
		if (active){
			for (Role iRole : mRoles.keySet()){
				iRole.setPerson(this);
			}
		}
		
		//Get housing role and location; set active
		mHouseRole = (HousingBaseRole) SortingHat.getHousingRole(this); //get housing status
//		Location location = 
//		
//		ContactList.sRoleLocations.put(mHouseRole, location);
		
		
		mRoles.put(mHouseRole, true);
		
		//Add customer/rider role possibilities
		mRoles.put(new BankCustomerRole(this), false);
		mRoles.put(new HousingRenterRole(this), false);
		mRoles.put(new MarketCustomerRole(this), false);
		mRoles.put(new TransportationBusRiderRole(this), false);
		mRoles.put(new RestaurantCustomerRole(this), false);
	}
	
	private void initializePerson(){
		//Roles and Job
		mRoles = new HashMap<Role, Boolean>();
		mHouseRole = null;
		mJobLocation = null;
		mAtJob = false;
		
		//Lists
		mFriends = new ArrayList<Person>();
		mEvents = new TreeSet<Event>();
		mItemInventory = Collections.synchronizedMap(new HashMap<EnumMarketItemType, Integer>());
		mItemsDesired = Collections.synchronizedMap(new HashMap<EnumMarketItemType, Integer>());
		mHomeLocations = Collections.synchronizedSet(new HashSet<Location>());
		
		//Personal Variables
		mSSN = sSSN++; // assign SSN
		mTimeShift = (mSSN % 3); // assign time schedule
		mLoan = 0;
		mHasCar = false;
		
		//Role References
		mPersonGui = new CityPerson(200, 100, mName); //SHANE: Hardcoded
		//SHANE REX: ADD TO MOVING IN SIMCITYPANEL
		
		// Event Setup
		mEvents = new TreeSet<Event>();
		//mEvents.add(new Event(EnumEventType.GET_CAR, 0));
		//mEvents.add(new Event(EnumEventType.JOB, mTimeShift + 0));
		//mEvents.add(new Event(EnumEventType.EAT, (mTimeShift + 8 + mSSN % 4) % 24)); // personal time
		mEvents.add(new Event(EnumEventType.EAT, 0));
		mEvents.add(new Event(EnumEventType.MAINTAIN_HOUSE, 8));
		//mEvents.add(new Event(EnumEventType.EAT, (mTimeShift + 12 + mSSN % 4) % 24)); // shift 4
		//mEvents.add(new Event(EnumEventType.PARTY, (mTimeShift + 16)	+ (mSSN + 3) * 24)); // night time, every SSN+3 days
	}
	

	// ----------------------------------------------------------MESSAGES----------------------------------------------------------
	public void msgTimeShift() {
		if (Time.GetShift() == 0) {
			// resetting of variables?
		}
		if (Time.GetShift() == mTimeShift) {
			for(Role iRole : mRoles.keySet()){
				if (iRole == mJobRole){
					mRoles.put(iRole, true);
				}
			}
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
	
	public void msgHereIsPayment(int senderSSN, double amount){
		mCash += amount;
	}
	
	public void msgOverdrawnAccount(double loan) {
		mLoan += loan;
	}
	
	public void msgRoleFinished(){
		mRoleFinished = true;
	}

	// ----------------------------------------------------------SCHEDULER----------------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		if ((mRoleFinished) && (!mAtJob) ){
			// Process events (calendar)
				Iterator<Event> itr = mEvents.iterator();
				while (itr.hasNext()) {
					Event event = itr.next();
					//System.out.println(event.mEventType.toString() + " " + event.mTime + " " + Time.GetTime());
					if (event.mTime > Time.GetTime())
						break; // don't do future calendar events
					processEvent(event);
					return true;
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
		System.out.println(event.mEventType.toString());
		//One time events (Car)
		if (event.mEventType == EnumEventType.GET_CAR) {
			getCar(); //SHANE: 1 get car
		}
		
		//Daily Recurring Events (Job, Eat)
		else if (event.mEventType == EnumEventType.JOB) {
			//bank is closed on weekends
			if (!(Time.IsWeekend()) || (mJobType != EnumJobType.BANK)){
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
		
		mEvents.remove(event);
	}
	
	private void acquireSemaphore(Semaphore semaphore){
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void getCar(){
		Location location = ContactList.cCARDEALERSHIP_DOOR;
		mPersonGui.DoGoToDestination(location);
		//mPersonGui.guiMoveFromCurrentPostionTo(new Position(95, 255));
		acquireSemaphore(semAnimationDone);
		
		//set city person invisible
		//mPersonGui.setPresent(false);
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
		
		//SHANE: 3 When gets car, change mHasCar to true
	}
	
	private void goToJob() {
		mPersonGui.DoGoToDestination(mJobLocation);
		//mPersonGui.guiMoveFromCurrentPostionTo(new Position(mJobLocation.mX, mJobLocation.mY));
		acquireSemaphore(semAnimationDone);
		mAtJob = true; //SHANE: This will need to be set to false somewhere
		mPersonGui.setPresent(false);		
		

		// work.getHost().msgImHere(job);
	}

	public void eatFood() {
		if (isCheap() && mHouseRole.mHouse != null){
			System.out.println("Going home to eat...");
			mHouseRole.msgEatAtHome();
			mPersonGui.DoGoToDestination(ContactList.cHOUSE_LOCATIONS.get(mHouseRole.mHouse.mHouseNum));
			//mPersonGui.guiMoveFromCurrentPostionTo(new Position(ContactList.cHOUSE_LOCATIONS.get(mHouseRole.mHouse.mHouseNum).mX, ContactList.cHOUSE_LOCATIONS.get(mHouseRole.mHouse.mHouseNum).mY));
			acquireSemaphore(semAnimationDone);
		}else{
			//set random restaurant
			RestaurantCustomerRole restaurantCustomerRole = null;
			for (Role iRole : mRoles.keySet()){
				if (iRole instanceof RestaurantCustomerRole){
					restaurantCustomerRole = (RestaurantCustomerRole) iRole;
				}
			}
			//set restaurant customer role to active
			mRoles.put(restaurantCustomerRole, true);
			
			
			int restaurantChoice = 1; // SHANE: Make random
			try {
				restaurantCustomerRole.setRestaurant(restaurantChoice);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
			
			try {
				mPersonGui.DoGoToDestination(ContactList.cRESTAURANT_LOCATIONS.get(restaurantChoice));
				//mPersonGui.guiMoveFromCurrentPostionTo(new Position(ContactList.cRESTAURANT_LOCATIONS.get(restaurantChoice).mX, ContactList.cRESTAURANT_LOCATIONS.get(restaurantChoice).mY));
			}
			catch (Exception e) {
			}
			acquireSemaphore(semAnimationDone);
		}
		
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
	
	public void invokeRent() {
		mHouseRole.msgTimeToCheckRent();
	}
	
	public void invokeMaintenance() {
		
		if (mHouseRole.mHouse != null) {
			mHouseRole.msgTimeToMaintain();
		}
	}
	
	
	private List<Person> getBestFriends(){
		List<Person> bestFriends = new ArrayList<Person>();
		for (Person iPerson : mFriends){
			if (iPerson.getTimeShift() == mTimeShift) bestFriends.add(iPerson);
		}
		return bestFriends;
	}
	
	private boolean isCheap(){
//		return (mLoan == 0) && (mCash > 30); //SHANE: 4 return this to normal
		return true;
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
	
	public void subLoan(double loan) {
		mLoan -= loan;
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
	public Map<Role, Boolean> getRoles() {
		return mRoles;
	}

	@Override
	public Role getHousingRole() {
		return mHouseRole;
	}

	@Override
	public CityPerson getPersonGui() {
		return mPersonGui;
	}
}
