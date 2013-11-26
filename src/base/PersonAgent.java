package base;

import housing.interfaces.HousingBase;
import housing.roles.HousingBaseRole;
import housing.roles.HousingRenterRole;

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

import market.interfaces.MarketCustomer;
import market.roles.MarketCustomerRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import test.mock.MockPersonGui;
import test.mock.PersonGuiInterface;
import transportation.roles.TransportationBusRiderRole;
import bank.BankAction;
import bank.roles.BankCustomerRole;
import bank.roles.BankCustomerRole.EnumAction;
import bank.roles.BankMasterTellerRole;
import base.Event.EnumEventType;
import base.Item.EnumItemType;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityPerson;
import city.gui.SimCityGui;


public class PersonAgent extends Agent implements Person {
	//----------------------------------------------------------DATA----------------------------------------------------------
	//Static data
	public static int sSSN = 0;
	
	//Roles and Job
	public static enum EnumJobType {BANK, HOUSING, MARKET, RESTAURANT, TRANSPORTATION, NONE};
	public EnumJobType mJobType;
	public Map<Role, Boolean> mRoles; //roles, active -  i.e. WaiterRole, BankTellerRole, etc.
	public HousingBaseRole mHouseRole;
	public Role mJobRole;
	private Location mJobLocation;
	public boolean mAtJob;
	
	//Lists
	List<Person> mFriends; // best are those with same timeshift
	public SortedSet<Event> mEvents; // tree set ordered by time of event
	Map<EnumItemType, Integer> mItemInventory; // personal inventory
	public Map<EnumItemType, Integer> mItemsDesired; // not ordered yet
	Set<Location> mHomeLocations; //multiple for landlord
	
	//Personal Variables
	private String mName; 
	int mSSN;
	int mTimeShift;
	double mCash;
	double mLoan;
	public boolean mHasCar;
	
	//Role References
	public BankMasterTellerRole mMasterTeller;
	public PersonGuiInterface mPersonGui;

	//PAEA Helpers
	public Semaphore semAnimationDone = new Semaphore(0);
	public boolean mRoleFinished = true;

	// ----------------------------------------------------------CONSTRUCTOR----------------------------------------------------------
	
	public PersonAgent() {
		initializePerson();
	}
	
	public PersonAgent(EnumJobType job, double cash, String name){
		mJobType = job;
		mCash = cash;
		mName = name;
		initializePerson();
		
		if (mTimeShift == 1){
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
					//System.out.println(mJobRole.toString());
					((RestaurantBaseInterface) mJobRole).setPerson(this);
					((RestaurantBaseInterface) mJobRole).setRestaurant(SimCityGui.TESTNUM); //HACK ANDRE ALL
					break;
				case TRANSPORTATION: break;
				case HOUSING: break;
				case NONE:
					mJobRole = new RestaurantCustomerRole(this);
					((RestaurantBaseInterface) mJobRole).setPerson(this);
					((RestaurantBaseInterface) mJobRole).setRestaurant(SimCityGui.TESTNUM);
					break;
			}
		}else{
			mJobRole = new RestaurantCustomerRole(this);
			((RestaurantBaseInterface) mJobRole).setPerson(this);
			((RestaurantBaseInterface) mJobRole).setRestaurant(SimCityGui.TESTNUM);
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
		//mHouseRole = (HousingBaseRole) SortingHat.getHousingRole(this); //get housing status
		//mRoles.put(mHouseRole, true);
		
		//Add customer/rider role possibilities
		mRoles.put(new BankCustomerRole(this), false);
		mRoles.put(new HousingRenterRole(this), false);
		mRoles.put(new MarketCustomerRole(this), false);
		mRoles.put(new TransportationBusRiderRole(this), false);
		mRoles.put(new RestaurantCustomerRole(this), false);
		
		//Add events
		mEvents.add(new Event(EnumEventType.JOB, 0));
		
//		if (mJobType != EnumJobType.NONE){
//		if ((mTimeShift == 0) && (mJobType != EnumJobType.NONE)){
//			mEvents.add(new Event(EnumEventType.JOB, 0));
//		}
//		mEvents.add(new Event(EnumEventType.EAT, 1)); // ANDRE ALL HACK

//		mEvents.add(new Event(EnumEventType.GET_CAR, 0));
//		mEvents.add(new Event(EnumEventType.JOB, mTimeShift + 0));
		
//		mEvents.add(new Event(EnumEventType.DEPOSIT_CHECK, mTimeShift + 8));
//		mEvents.add(new Event(EnumEventType.JOB, mTimeShift*2));
//		mEvents.add(new Event(EnumEventType.EAT, (mTimeShift + 8 + mSSN % 4) % 24)); // personal time
//		mEvents.add(new Event(EnumEventType.EAT, 1)); //THIS IS A PROBLEM
//		mEvents.add(new Event(EnumEventType.MAINTAIN_HOUSE, 8));
//		mEvents.add(new Event(EnumEventType.EAT, (mTimeShift + 12 + mSSN % 4) % 24)); // shift 4
//		mEvents.add(new Event(EnumEventType.PARTY, (mTimeShift + 16)	+ (mSSN + 3) * 24)); // night time, every SSN+3 days
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
		mItemInventory = Collections.synchronizedMap(new HashMap<EnumItemType, Integer>());
		mItemsDesired = Collections.synchronizedMap(new HashMap<EnumItemType, Integer>());
		mHomeLocations = Collections.synchronizedSet(new HashSet<Location>());
		
		//Personal Variables
		mSSN = sSSN++; // assign SSN
		mTimeShift = (mSSN % 3); // assign time schedule
		mLoan = 0;
		mHasCar = false;
		
		//Role References
		mPersonGui = new CityPerson(this, SimCityGui.getInstance(), 0, sSSN*10); //SHANE: Hardcoded start place
		
		// Event Setup
		mEvents = new TreeSet<Event>(); //SHANE: 2 CHANGE THIS TO LIST - sorted set
	}
	

	// ----------------------------------------------------------MESSAGES----------------------------------------------------------
	public void msgTimeShift() {
		mRoleFinished = true;
		if (Time.GetShift() == mTimeShift) {
			for(Role iRole : mRoles.keySet()){
				if (iRole == mJobRole){
					mRoles.put(iRole, true);
				}
			}
		}
		//Leave job
		if ((mTimeShift + 1) % 3 == Time.GetShift()){ //if job shift is over
			mAtJob = false;
			mRoles.put(mJobRole, false); //set job role to false;
			mPersonGui.setPresent(true);
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
	
	public void msgRoleFinished(){ //SHANE: 3 Call at end of role
		mRoleFinished = true;
		mPersonGui.setPresent(true);
	}
	public void msgRoleInactive(){
		for (Role iRole : mRoles.keySet()){
			if(!(iRole instanceof HousingBase)){
				mRoles.put(iRole, false);
			}
		}
	}
	public void msgHereIsPayment(int senderSSN, double amount){
		mCash += amount;
	}
	
	public void msgOverdrawnAccount(double loan) {
		mLoan += loan;
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
					mRoleFinished = false;
					processEvent(event);
					return true;
				}
		}

		// Do role actions
		for (Role iRole : mRoles.keySet()) {
			if (mRoles.get(iRole)) {
				//print(iRole.toString());
				if (iRole.getPerson() == null) {
					print(iRole.toString());
					print("getPerson in iRole was null");
				}
				else if (iRole.pickAndExecuteAnAction()) {
					//System.out.println(iRole.toString() + "pAEA fired");
					return true;
				}
			}
		}
		
		//SHANE: 4 last choice - go home
		
		return false;
	}

	// ----------------------------------------------------------ACTIONS----------------------------------------------------------

	private synchronized void processEvent(Event event) {
		//System.out.println(event.mEventType.toString());
		mAtJob = false;
		//One time events (Car)
		if (event.mEventType == EnumEventType.GET_CAR) {
			getCar();
		}
		
		//Daily Recurring Events (Job, Eat)
		else if (event.mEventType == EnumEventType.JOB) {
			//bank is closed on weekends
			if (!(Time.IsWeekend()) || (mJobType != EnumJobType.BANK)){
				mAtJob = true;
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
		acquireSemaphore(semAnimationDone);
		mPersonGui.setPresent(false); //set city person invisible
		
		//activate marketcustomer role
		for (Role iRole : mRoles.keySet()){
			if (iRole instanceof MarketCustomer){
				mRoles.put(iRole, true); //set active
				iRole.setPerson(this);
			}
		}
		
		//add desired item
		mItemsDesired.put(EnumItemType.CAR, 1); //want 1 car
		//PAEA for role will message market cashier to start transaction
		mHasCar = true;
	}
	
	public void goToJob() {
		System.out.println("Going to Job");
		if (mJobLocation != null){
			mPersonGui.DoGoToDestination(mJobLocation);
		}else{
			mPersonGui.DoGoToDestination(ContactList.cRESTAURANT_DOORS.get(SimCityGui.TESTNUM));
		}
		acquireSemaphore(semAnimationDone);
		mAtJob = true; //SHANE: This will need to be set to false somewhere
		mPersonGui.setPresent(false);
		if(mJobRole != null) {
			mJobRole.setPerson(this); //take over job role
			mRoles.put(mJobRole, true); //set role to active
		}
	}

	public void eatFood() {
		if (isCheap() && mHouseRole.mHouse != null){
			System.out.println(this + ": Going to eat at home");
			mHouseRole.msgEatAtHome();
			mPersonGui.DoGoToDestination(ContactList.cHOUSE_DOORS.get(mHouseRole.mHouse.mHouseNum));
			acquireSemaphore(semAnimationDone);
			
			//SHANE REX: 1 what else needs to be done here?
		}else{
			System.out.println("Going to restaurant");
			//set random restaurant
			Role restCustRole = null;
			for (Role iRole : mRoles.keySet()){
				if (iRole instanceof RestaurantCustomerRole){
					restCustRole = iRole;
				}
			}
			mRoles.put(restCustRole, true);
			
			int restaurantChoice = SimCityGui.TESTNUM;

			mPersonGui.DoGoToDestination(ContactList.cRESTAURANT_DOORS.get(restaurantChoice));
			acquireSemaphore(semAnimationDone);
			mPersonGui.setPresent(false);
			
			((RestaurantBaseInterface) restCustRole).setPerson(this);
			((RestaurantBaseInterface) restCustRole).setRestaurant(restaurantChoice);
			
		}
		
	}
	
	private void depositCheck() {
		mPersonGui.DoGoToDestination(ContactList.cBANK_DOOR);
		acquireSemaphore(semAnimationDone);
		mPersonGui.setPresent(false);
		
		int deposit = 50; //REX: deposit based on job type or constant amount
		BankCustomerRole bankCustomerRole = null;
		for (Role iRole : mRoles.keySet()){
			if (iRole instanceof BankCustomerRole){
				bankCustomerRole = (BankCustomerRole) iRole;
			}
		}
		
		//deposit check
		bankCustomerRole.mActions.add(new BankAction(EnumAction.Deposit, deposit));
		
		//pay back loan if needed
		if(mLoan > 0){
			double payment = Math.max(mCash, mLoan);
			mCash -= payment;
			bankCustomerRole.mActions.add(new BankAction(EnumAction.Payment, payment));
		}
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
		mHouseRole.msgTimeToCheckRent(); //this role is always active
	}
	
	public void invokeMaintenance() {
		if (mHouseRole.mHouse != null) {
			mHouseRole.msgTimeToMaintain(); //this role is always active
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
		return false;
	}
	

	// ----------------------------------------------------------ACCESSORS----------------------------------------------------------

	//SHANE: 4 Organize PersonAgent Accessors
	public void addRole(Role role, boolean active) {
		mRoles.put(role, active);
		print(this.getName());
		if (role.getPerson() == null) {
			print("person is null in addrole");
		}
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

	public Map<EnumItemType, Integer> getItemsDesired() {
		return mItemsDesired;
	}

	public int getSSN() {
		return mSSN;
	}

	public Map<EnumItemType, Integer> getItemInventory() {
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
	public void setItemsDesired(Map<EnumItemType, Integer> map) {
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
		return (CityPerson)mPersonGui;
	}

	public void setGui(MockPersonGui gui) {
		mPersonGui = gui;
	}
}
