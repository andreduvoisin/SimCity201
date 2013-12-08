package base;

import housing.interfaces.HousingBase;
import housing.roles.HousingBaseRole;
import housing.roles.HousingLandlordRole;
import housing.roles.HousingOwnerRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import market.interfaces.MarketCustomer;
import market.roles.MarketCashierRole;
import market.roles.MarketCustomerRole;
import market.roles.MarketDeliveryTruckRole;
import market.roles.MarketWorkerRole;
import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import transportation.roles.CommuterRole;
import transportation.roles.TransportationBusRiderRole;
import bank.BankAction;
import bank.roles.BankCustomerRole;
import bank.roles.BankCustomerRole.EnumAction;
import bank.roles.BankGuardRole;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import base.Event.EnumEventType;
import base.Item.EnumItemType;
import base.interfaces.Person;
import base.interfaces.Role;
import base.reference.ContactList;
import city.gui.CityPerson;
import city.gui.SimCityGui;
import city.gui.trace.AlertTag;


public class PersonAgent extends Agent implements Person {
	//----------------------------------------------------------DATA----------------------------------------------------------
	//Static data
	public static int sSSN = 0;
	public static int sRestaurantCounter = 0;
	public static int sHouseCounter = 0;
	
	//Roles and Job
	public static enum EnumJobType {	BANK,		//master teller, teller, guard... 
										MARKET, 	//cashier, worker...
										RESTAURANT, //...
										NONE};		//party person, non-norms (can add NN1, NN2, ...)
	public EnumJobType mJobType;
	public Map<Role, Boolean> mRoles; 	//roles, active -  i.e. WaiterRole, BankTellerRole, etc.
	
	//Lists
	private List<Person> mFriends; 						// best friends are those with same timeshift
	private List<Event> mEvents; 						// tree set ordered by time of event
	private Map<EnumItemType, Integer> mItemInventory; 	// personal inventory
	private Map<EnumItemType, Integer> mItemsDesired; 	// not ordered yet
	Set<Location> mHomeLocations; 						//multiple for landlord
	
	//Personal Variables
	private String mName; 
	private int mSSN;
	private int mTimeShift;
	private double mCash;
	private double mLoan;
	private boolean mHasCar;	//ALL ANGELICA MAGGI: 3 car will be implemented later
	private boolean mAtJob;		//used in PAEA
	public CityPerson mPersonGui;

	//PAEA Helpers
	public Semaphore semAnimationDone = new Semaphore(0);
	public boolean mRoleFinished = true;

	// ----------------------------------------------------------CONSTRUCTOR----------------------------------------------------------
	
	public PersonAgent() {
		initializePerson();
	}
	
	public PersonAgent(EnumJobType jobType, double cash, String name){
		mJobType = jobType;
		mCash = cash;
		mName = name;
		initializePerson();
		
		//Get job role and location; set active if necessary
		Role jobRole = null;
		switch (jobType){
			case BANK:
				jobRole = SortingHat.getBankRole(mTimeShift);
				break;
			case MARKET:
				jobRole = SortingHat.getMarketRole(mTimeShift);
				break;
			case RESTAURANT:
				jobRole = SortingHat.getRestaurantRole(mTimeShift);
				break;
			case NONE:
				break;
		}
		
		//Link person and role
		boolean active = (mTimeShift == Time.GetShift());	//set active if job shift is now
		if (jobRole != null){
			mRoles.put(jobRole, active);					//give person a reference to the role (and if currently filling role)
		}
		if (active){
			for (Role iRole : mRoles.keySet()){
				iRole.setPerson(this);						//give role a reference to the person (only if currently filling role)
			}
		}
		
		//Add customer/rider role possibilities
		mRoles.put(SortingHat.getHousingRole(this), true);
		mRoles.put(new CommuterRole(this), false); 
		mRoles.put(new BankCustomerRole(this, mSSN%2), false);
		mRoles.put(new MarketCustomerRole(this, mSSN%2), false);
		mRoles.put(new TransportationBusRiderRole(this), false);
		mRoles.put(new RestaurantCustomerRole(this), false);
		
		/*
		 * Give houses to landlords and owners
		 */
		if (getHousingRole() instanceof HousingLandlordRole || getHousingRole() instanceof HousingOwnerRole) {
			//getHousingRole().setHouse(ContactList.sHouseList.get(sHouseCounter % ContactList.sHouseList.size()));
			sHouseCounter++;
		}
	}
	
	private void initializePerson(){
		//Roles and Job
		mRoles = new HashMap<Role, Boolean>(); //role to active
		mAtJob = false;
		
		//Lists
		mFriends = new ArrayList<Person>();
		mEvents = new ArrayList<Event>();
		mItemInventory = Collections.synchronizedMap(new HashMap<EnumItemType, Integer>());
		mItemsDesired = Collections.synchronizedMap(new HashMap<EnumItemType, Integer>());
		mHomeLocations = Collections.synchronizedSet(new HashSet<Location>());
		
		//Personal Variables
		mSSN = sSSN++; // assign SSN
		mTimeShift = (mSSN % 3); // assign time schedule
		mLoan = 0;
		mHasCar = false;
		
		//Role References
		mPersonGui = new CityPerson(this, SimCityGui.getInstance(), sSSN * 5 % 600, sSSN % 10 + 250); //SHANE: 3 Hardcoded start place
		
		// Event Setup
		mEvents = new ArrayList<Event>();
	}
	
	// ----------------------------------------------------------MESSAGES----------------------------------------------------------
	public void msgTimeShift() {
		//finished role if job
		mRoleFinished = true;
		if (Time.GetShift() == mTimeShift) {
			mRoles.put(getJobRole(), true);
		}
		//Leave job
		if ((mTimeShift + 1) % 3 == Time.GetShift()){ //if job shift is over
			mAtJob = false;
			mRoles.put(getJobRole(), false); //set job role to false;
			mPersonGui.setPresent(true);
		}
		
		stateChanged();
	}
	
	public void msgStateChanged() {
		stateChanged();
	}

	public void msgAddEvent(Event event) {
		if(event.mEventType == EnumEventType.RSVP1){
			if(((EventParty)event).mHost.getName().equals("partyPersonFlake") && mSSN%2==0){
				print("I am a deadbeat");
				return;
			}
		}
		mEvents.add(event);
	}
	
	public void msgAnimationDone(){
		if (semAnimationDone.availablePermits() == 0) semAnimationDone.release();
	}
	
	public void msgCommuteAnimationDone(){
		mRoles.put(getCommuterRole(), false); 
	}
	
	public void msgRoleFinished(){ //SHANE ALL: 3 Call at end of role
		mRoleFinished = true;
		mPersonGui.setPresent(true);
		for (Role iRole : mRoles.keySet()){
			if(!(iRole instanceof HousingBase)){
				mRoles.put(iRole, false);
				iRole.setActive();
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
			Collections.sort(mEvents);
			if(mEvents.isEmpty())
				return false;
			Event event = mEvents.get(0); //next event
			if (event.mTime <= Time.GetTime()){ //only do events that have started
				mRoleFinished = false; //doing a role
				processEvent(event);
				return true;
			}
		}

		// Do role actions
		for (Role iRole : mRoles.keySet()) {
			if (mRoles.get(iRole) && iRole!= null) {
				if (!iRole.hasPerson()) {
					print(iRole.toString());
					print("getPerson in iRole was null");
				}
				else if (iRole instanceof CommuterRole){
					if(mRoles.get(iRole))
						if(iRole.pickAndExecuteAnAction())
							return true; 
				}
				else if (iRole.pickAndExecuteAnAction()){
					return true;
				}
			}
		}
		
		//SHANE: 5 last choice - go home
		return false;
	}

	// ----------------------------------------------------------ACTIONS----------------------------------------------------------
//ANGELICA MAGGI: change all test functions back later
	private synchronized void processEvent(Event event) {
		mAtJob = false;
		//One time events (Car)
		if (event.mEventType == EnumEventType.GET_CAR) {
			getCar();
//			testGetCar();
		}
		
		//Daily Recurring Events (Job, Eat)
		else if (event.mEventType == EnumEventType.JOB) {
			//bank is closed on weekends
			if (!(Time.IsWeekend()) || (mJobType != EnumJobType.BANK)){
				mAtJob = true;
				goToJob();
			}
			mEvents.add(new Event(event, 24));
		}
		else if (event.mEventType == EnumEventType.EAT) {
			eatFood();
			mEvents.add(new Event(event, 24));
		}

		//Intermittent Events (Deposit Check)
		else if (event.mEventType == EnumEventType.DEPOSIT_CHECK) {
			print("DepositCheck");
			depositCheck();
		}
		
		else if (event.mEventType == EnumEventType.ASK_FOR_RENT) {
			invokeRent();
		}
		
		else if (event.mEventType == EnumEventType.MAINTAIN_HOUSE) {
			invokeMaintenance();
		}
		
		//Party Events
		else if (event.mEventType == EnumEventType.INVITE1) {
			inviteToParty();
		}
		else if (event.mEventType == EnumEventType.INVITE2) {
			reinviteDeadbeats();
		}
		else if (event.mEventType == EnumEventType.RSVP1) {
			respondToRSVP();
		}
		else if (event.mEventType == EnumEventType.RSVP2) {
			respondToRSVP();
		}
		else if (event.mEventType == EnumEventType.PARTY) {
			if (event instanceof EventParty){
				if(((EventParty)event).mAttendees.isEmpty()){
					print("OMG THIS PARTY SUCKS and is cancelled");
					return;
				}
				goParty((EventParty)event);
			}
		}
		else if (event.mEventType == EnumEventType.PLANPARTY) {
			planParty(Time.GetTime());
		}

		//Transportation
		else if (event.mEventType == EnumEventType.BOARD_BUS) {
			boardBus();
		}
		else if (event.mEventType == EnumEventType.EXIT_BUS) {
			exitBus();
		}
		
		mEvents.remove(event);
	}
	
/**ANGELICA: MAGGI: Testing transportationnnnnnnnnnnnnnnnnnnnnnnnnnnnnn */
	
	private void testGetCar() {
		//activate marketcustomer role
		for (Role iRole : mRoles.keySet()){
			if (iRole instanceof MarketCustomer){
				if(!SimCityGui.TESTING) {
					//ANGELICA: change to choosing which market to get a car
					Location location = ContactList.getDoorLocation(ContactList.cMARKET1_LOCATION);
					iRole.GoToDestination(location);
					acquireSemaphore(semAnimationDone);
				}
				mPersonGui.setPresent(false);
				mRoles.put(iRole, true); //set active
				iRole.setPerson(this);
				break;
			}
		}
		
		//add desired item
		mItemsDesired.put(EnumItemType.CAR, 1); // want 1 car
		// PAEA for role will message market cashier to start transaction
		mHasCar = true;
	}

	private void testGoToJob() {
		print("goToJob");
		Role jobRole = getJobRole();
		if (jobRole == null) {
			print("didn't go to job");
			return;
		}
		mAtJob = true; // set to false in msgTimeShift
		mPersonGui.setPresent(false);
		print("my job is " + jobRole.toString());
		if (jobRole != null) {
			jobRole.setPerson(this); // take over job role
			mRoles.put(getCommuterRole(), true);
			CommuterRole cRole = (CommuterRole) getCommuterRole();
			cRole.setLocation(getJobLocation());
			mRoles.put(jobRole, true); // set role to active
			jobRole.setActive();
		}
	}

	private void testEatFood() {
		if (isCheap() && getHousingRole().getHouse() != null) {
			print("Going to eat at home");
			getHousingRole().msgEatAtHome();
			BaseRole r = (BaseRole) getHousingRole();
			r.GoToDestination(ContactList.cHOUSE_LOCATIONS.get(getHousingRole()
					.getHouse().mHouseNum));
			acquireSemaphore(semAnimationDone);
			mPersonGui.setPresent(false);
		} else {
			print("Going to restaurant");
			// set random restaurant
			Role restCustRole = null;
			for (Role iRole : mRoles.keySet()) {
				if (iRole instanceof RestaurantCustomerRole) {
					restCustRole = iRole;
				}
			}
			mRoles.put(restCustRole, true);

			// SHANE DAVID ALL: 3 make this random
			int restaurantChoice = 0;

			if (SimCityGui.TESTING) {
				restaurantChoice = SimCityGui.TESTNUM; // override if testing
			}

			restCustRole.GoToDestination(ContactList.cRESTAURANT_LOCATIONS
					.get(restaurantChoice));
			acquireSemaphore(semAnimationDone);
			mPersonGui.setPresent(false);

			((RestaurantCustomerRole) restCustRole).setPerson(this);
			((RestaurantCustomerRole) restCustRole)
					.setRestaurant(restaurantChoice);
		}
	}

	private void testDepositCheck() {
		mPersonGui.setPresent(true);

		// mPersonGui.DoGoToDestination(mSSN%2==0?
		// ContactList.cBANK1_LOCATION:ContactList.cBANK2_LOCATION);

		acquireSemaphore(semAnimationDone);
		mPersonGui.setPresent(false);

		int deposit = 50; // REX: add mDeposit, and do after leaving job

		BankCustomerRole bankCustomerRole = null;
		for (Role iRole : mRoles.keySet()) {
			if (iRole instanceof BankCustomerRole) {
				bankCustomerRole
						.GoToDestination(mSSN % 2 == 0 ? ContactList.cBANK1_LOCATION
								: ContactList.cBANK2_LOCATION);
				acquireSemaphore(semAnimationDone);
				mPersonGui.setPresent(false);
				bankCustomerRole = (BankCustomerRole) iRole;
				mRoles.put(iRole, true);
				break;
			}
		}

		// deposit check
		bankCustomerRole.mActions.add(new BankAction(EnumAction.Deposit,
				deposit));

		// pay back loan if needed
		if (mLoan > 0) {
			double payment = Math.max(mCash, mLoan);
			mCash -= payment;
			bankCustomerRole.mActions.add(new BankAction(EnumAction.Payment,
					payment));
		}
		bankCustomerRole.setPerson(this);
		bankCustomerRole.setActive();
		ContactList.sBankList.get(mSSN % 2).addPerson(bankCustomerRole);
	}
	
/*************************************************************************/
	public void getCar(){
		Location location = ContactList.getDoorLocation(ContactList.cMARKET1_LOCATION);
		if(!SimCityGui.TESTING){
			mPersonGui.DoGoToDestination(location);
			acquireSemaphore(semAnimationDone);
		}
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
		//print("goToJob");
		Role jobRole = getJobRole();
		if(jobRole == null){
			//print("didn't go to job"); 
			return;
		}
		mPersonGui.DoGoToDestination(getJobLocation()); 
		acquireSemaphore(semAnimationDone);
		mAtJob = true; //set to false in msgTimeShift
		mPersonGui.setPresent(false);
		print("my job is " +jobRole.toString());
		if(jobRole != null) {
			jobRole.setPerson(this); //take over job role
			mRoles.put(jobRole, true); //set role to active
			jobRole.setActive();
		}
	}

	public void eatFood() {
		if (isCheap() && getHousingRole().getHouse() != null){
			print("Going to eat at home");
			getHousingRole().msgEatAtHome();
			mPersonGui.DoGoToDestination(ContactList.cHOUSE_LOCATIONS.get(getHousingRole().getHouse().mHouseNum));
			acquireSemaphore(semAnimationDone);
		}else{
			print("Going to restaurant");
			//set random restaurant
			Role restCustRole = null;
			for (Role iRole : mRoles.keySet()){
				if (iRole instanceof RestaurantCustomerRole){
					restCustRole = iRole;
				}
			}
			mRoles.put(restCustRole, true);
			
			//SHANE DAVID ALL: 3 make this random
			int restaurantChoice = 0;
			
			if (SimCityGui.TESTING){
				restaurantChoice = SimCityGui.TESTNUM; //override if testing
			}

			mPersonGui.DoGoToDestination(ContactList.cRESTAURANT_LOCATIONS.get(restaurantChoice));
			acquireSemaphore(semAnimationDone);
			mPersonGui.setPresent(false);
			
			((RestaurantCustomerRole) restCustRole).setPerson(this);
			((RestaurantCustomerRole) restCustRole).setRestaurant(restaurantChoice);
			
		}
		
	}
	
	private void depositCheck() {
		mPersonGui.setPresent(true);
		
		mPersonGui.DoGoToDestination(mSSN%2==0? ContactList.cBANK1_LOCATION:ContactList.cBANK2_LOCATION);
		//acquireSemaphore(semAnimationDone);
		mPersonGui.setPresent(false);
		
		int deposit = 50; //REX: add mDeposit, and do after leaving job
		
		BankCustomerRole bankCustomerRole = null;
		for (Role iRole : mRoles.keySet()){
			if (iRole instanceof BankCustomerRole){
				bankCustomerRole = (BankCustomerRole) iRole;
				mRoles.put(iRole, true);
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
		if(mName.equals("robber")){
			//REX: hard coded to try to steal 100
			print("Robbery action added to bank options");
			bankCustomerRole.mActions.add(new BankAction(EnumAction.Robbery, 100));
		}
		bankCustomerRole.setPerson(this);
		bankCustomerRole.setActive();
		ContactList.sBankList.get(mSSN%2).addPerson(bankCustomerRole);
	}
	
	private void planParty(int time){
		print("Planning a party");
		mEvents.add(new Event(EnumEventType.INVITE1, time));
		if(!mName.equals("partyPerson"))
			mEvents.add(new Event(EnumEventType.INVITE2, time+2));
		Location partyLocation = new Location(100, 0); //REX: remove hardcoded party pad after dehobo the host
		mEvents.add(new EventParty(EnumEventType.PARTY, time+4, partyLocation, this, mFriends));
		//mEvents.add(new EventParty(EnumEventType.PARTY, time+4, ((HousingBaseRole)getHousingRole()).getLocation(), this, mFriends));
	}

	private void goParty(EventParty event) {
		print("Going to party");
		mPersonGui.DoGoToDestination(event.mLocation);
		//acquireSemaphore(semAnimationDone);
		mPersonGui.setPresent(false);
		((HousingBaseRole) getHousingRole()).gui.setPresent(true);
		SimCityGui.getInstance().cityview.mCityHousingList.get(event.mHost
				.getHousingRole().getHouse().mHouseNum).mPanel
				.addGui((Gui) ((HousingBaseRole) getHousingRole()).gui);
		((HousingBaseRole) getHousingRole()).gui.DoParty();
	}

	private void inviteToParty() {
		if(mFriends.isEmpty()){
			int numPeople = ContactList.sPersonList.size();
			print("Num People in city: " + numPeople); //SHANE: Print remove
			for (int i = 0; i < numPeople; i = i + 2){
				mFriends.add(ContactList.sPersonList.get(i));
			}
			print("Created friends for party host");
		}
		print("First RSVP is sent out");
		//party is in 3 days
		//send RSVP1 and event invite
//		Location test = ContactList.sRoleLocations.get(); //SHANE REX: 2 This is null...
		Location partyLocation = new Location(100, 0);
		Event party = new EventParty(EnumEventType.PARTY, Time.GetTime()+4, partyLocation, this, mFriends);
		
		//Event party = new EventParty(EnumEventType.PARTY, Time.GetTime()+4, ((HousingBaseRole)getHousingRole()).getLocation(), this, mFriends);
		Event rsvp  = new EventParty(EnumEventType.RSVP1, -1, this); //respond immediately
		if(mName.equals("partyPersonFlake")){
			for (Person iFriend : mFriends){
				iFriend.msgAddEvent(rsvp);
				iFriend.msgAddEvent(party);
			}
		}
		else{ //partyPerson or partyPersonNO
			for (Person iFriend : mFriends){
				if(iFriend.getTimeShift()==mTimeShift){
					iFriend.msgAddEvent(rsvp);
					iFriend.msgAddEvent(party);
				}
			}
		}
	}

	private void reinviteDeadbeats() {
		print("Second RSVP is sent out");
		EventParty party = null;
		for (Event iEvent : mEvents){
			if (iEvent instanceof EventParty){
				if (((EventParty) iEvent).mHost == this){
					party = (EventParty) iEvent;
				}
			}
		}
		for (Person iPerson : party.mAttendees.keySet()){
			if (party.mAttendees.get(iPerson) == false){ //haven't responded yet
				Event rsvp = new EventParty(EnumEventType.RSVP2, -1, this);
				iPerson.msgAddEvent(rsvp);
			}
		}
	}
	
	private void respondToRSVP(){
		for (Event iEvent : mEvents){
			if (iEvent instanceof EventParty){
				if(((EventParty) iEvent).mHost.getName().equals("partyPersonNO")){
					((EventParty) iEvent).mAttendees.remove(this);
					print("Responding to RSVP: NO");
				}
				else if (((EventParty) iEvent).mHost.getTimeShift() == mTimeShift){
					((EventParty) iEvent).mAttendees.put(this, true);
					print("Responding to RSVP: YES");
				}else{
					((EventParty) iEvent).mAttendees.remove(this);
					print("Responding to RSVP: NO");
				}
			}
		}
	}
	
	public void invokeRent() {
		getHousingRole().msgTimeToCheckRent(); //this role is always active
	}
	
	public void invokeMaintenance() {
		//ALL: this is a hack needs to be fixed
//		mJobRole = (HousingBaseRole) SortingHat.getHousingRole(this); //get housing status
//		Role jobRole = new HousingOwnerRole(this);
//		jobRole.setPerson(this);
//		((HousingBaseRole) jobRole).setHouse(ContactList.sHouseList.get(sHouseCounter));
//		mPersonGui.setPresent(true);
//		mPersonGui.DoGoToDestination(ContactList.cHOUSE_LOCATIONS.get(sHouseCounter));
//		sHouseCounter++;
//		acquireSemaphore(semAnimationDone);
//		mPersonGui.setPresent(false);
//		((HousingBaseRole) jobRole).msgTimeToMaintain();
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


	private void boardBus() {
		int boardAtStop = ((TransportationBusRiderRole) getJobRole()).mBusDispatch.getBusStopClosestTo(new Location(mPersonGui.xDestination, mPersonGui.yDestination));
		int exitAtStop  = ((TransportationBusRiderRole) getJobRole()).mBusDispatch.getBusStopClosestTo(mPersonGui.mNextDestination);
		Role jobRole = getJobRole();

		mPersonGui.DoGoToDestination(base.reference.ContactList.cBUS_STOPS.get(boardAtStop));
		acquireSemaphore(semAnimationDone);

		((TransportationBusRiderRole) jobRole).msgReset();
		
	}

	private void exitBus() {
		mRoleFinished = true;
		mPersonGui.NewDestination(new Location(mPersonGui.mNextDestination.mX, mPersonGui.mNextDestination.mY));
	}

	private void acquireSemaphore(Semaphore semaphore){
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Role getCommuterRole(){
		for(Role iRole : mRoles.keySet()){
			if(iRole instanceof CommuterRole)
				return iRole; 
		}
		return null; 
	}
	
	private Role getJobRole(){
		for (Role iRole : mRoles.keySet()){
			//Bank roles
			if (	//Bank jobs
					iRole instanceof BankGuardRole ||
					iRole instanceof BankMasterTellerRole ||
					iRole instanceof BankTellerRole ||
					//Market jobs
					iRole instanceof MarketCashierRole ||
					iRole instanceof MarketDeliveryTruckRole ||
					iRole instanceof MarketWorkerRole ||
					//Restaurant job
					iRole instanceof RestaurantCashierRole ||
					iRole instanceof RestaurantCookRole ||
					iRole instanceof RestaurantHostRole ||
					iRole instanceof RestaurantWaiterRole){
				return iRole;
			}
		}
		//print("job role null!");
		return null;
	}
	
	private Location getJobLocation(){
		if (getJobRole() == null)
			return null;
		return getJobRole().getLocation();
	}
	
	// ----------------------------------------------------------ACCESSORS----------------------------------------------------------
	
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
	public HousingBase getHousingRole() {
		for (Role iRole : mRoles.keySet()){
			if(iRole instanceof HousingBase){
				return (HousingBase) iRole;
			}
		}
		return null;
	}

	@Override
	public CityPerson getPersonGui() {
		return (CityPerson)mPersonGui;
	}

	@Override
	public void setGuiPresent() {
		mPersonGui.setPresent(true);
//		mPersonGui.setX(250);
//		mPersonGui.setY(300);
	}
	
	public CityPerson getGui(){
		return mPersonGui;
	}

	@Override
	public void setJobFalse() {
		mAtJob = false;
	}
	
	public boolean hasCar() {
		return mHasCar;
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.PERSON);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.PERSON);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.PERSON, e);
	}
}
