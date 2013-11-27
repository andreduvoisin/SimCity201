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

import market.gui.MarketPanel;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketWorker;
import market.roles.MarketCashierRole;
import market.roles.MarketCustomerRole;
import market.roles.MarketDeliveryTruckRole;
import market.roles.MarketWorkerRole;
import restaurant.intermediate.RestaurantCashierRole;
import restaurant.intermediate.RestaurantCookRole;
import restaurant.intermediate.RestaurantCustomerRole;
import restaurant.intermediate.RestaurantHostRole;
import restaurant.intermediate.RestaurantWaiterRole;
import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import test.mock.PersonGuiInterface;
import transportation.roles.TransportationBusRiderRole;
import bank.BankAction;
import bank.gui.BankPanel;
import bank.roles.BankCustomerRole;
import bank.roles.BankCustomerRole.EnumAction;
import bank.roles.BankGuardRole;
import bank.roles.BankMasterTellerRole;
import bank.roles.BankTellerRole;
import base.Event.EnumEventType;
import base.Item.EnumItemType;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.CityHousing;
import city.gui.CityPanel;
import city.gui.CityPerson;
import city.gui.SimCityGui;
import city.gui.CityHousing.HousingType;


public class PersonAgent extends Agent implements Person {
	//----------------------------------------------------------DATA----------------------------------------------------------
	//Static data
	public static int sSSN = 0;
	public static int sRestaurantCounter = 0;
	public static int sHouseCounter = 0;
	
	//Roles and Job
	public static enum EnumJobType {BANK, BANKCUSTOMER, HOUSING, MARKET, MARKETCUSTOMER, RESTAURANT, RESTAURANTCUSTOMER, TRANSPORTATION, PARTY, NONE};
	public static enum EnumRestaurantRole {HOST, CASHIER, COOK, WAITER, CUSTOMER};
	public EnumJobType mJobType;
	public Map<Role, Boolean> mRoles; //roles, active -  i.e. WaiterRole, BankTellerRole, etc.
	public HousingBaseRole mHouseRole;
	public Role mJobRole;
	public Location mJobLocation;
	public boolean mAtJob;
	public boolean testing = false;
	
	//Lists
	List<Person> mFriends; // best are those with same timeshift
	public SortedSet<Event> mEvents; // tree set ordered by time of event
	Map<EnumItemType, Integer> mItemInventory; // personal inventory
	public Map<EnumItemType, Integer> mItemsDesired; // not ordered yet
	Set<Location> mHomeLocations; //multiple for landlord
	
	//Personal Variables
	private String mName; 
	int mSSN;
	public int mTimeShift;
	double mCash;
	double mLoan;
	public boolean mHasCar;
	public int mRestaurantNumber;
	public EnumRestaurantRole mRestaurantRole;
	
	//Role References
	public BankMasterTellerRole mMasterTeller;
	public CityPerson mPersonGui;

	//PAEA Helpers
	public Semaphore semAnimationDone = new Semaphore(0);
	public boolean mRoleFinished = true;

	// ----------------------------------------------------------CONSTRUCTOR----------------------------------------------------------
	
	public PersonAgent() {
		initializePerson();
	}
	
	public PersonAgent(EnumJobType job, double cash, String name, int restaurantNum, EnumRestaurantRole restRole){
		mJobType = job;
		mCash = cash;
		mName = name;
		mRestaurantNumber = restaurantNum;
		mRestaurantRole = restRole;
		initializePerson();
		
		if (mTimeShift == 1){
			//Get job role and location; set active if necessary
			mJobRole = null;
			switch (job){
				case BANK:
					mJobRole = SortingHat.getBankRole(mTimeShift);
					if(mJobRole == null) {
						mJobRole = new BankCustomerRole(this);
						mJobRole.setPerson(this);
						BankPanel.getInstance().addPerson(mJobRole);
						break;
					}
					
					if(mJobRole instanceof BankGuardRole) {
						mJobRole = BankPanel.getInstance().guard;
						BankPanel.getInstance().addGui(((BankGuardRole)mJobRole).mGUI);
					} else if(mJobRole instanceof BankMasterTellerRole) {
						mJobRole = BankPanel.getInstance().masterTeller;
					} else if(mJobRole instanceof BankTellerRole) {
						mJobRole = BankPanel.getInstance().teller;
						((BankTellerRole)mJobRole).addGuard(BankPanel.getInstance().guard);
						((BankTellerRole)mJobRole).setMaster(BankPanel.getInstance().masterTeller);
						BankPanel.getInstance().addGui(((BankTellerRole)mJobRole).mGUI);
						BankPanel.getInstance().guard.msgReadyToWork((BankTellerRole)mJobRole);
					}
					mJobRole.setPerson(this);
					break;
				case BANKCUSTOMER:
					mJobRole = new BankCustomerRole(this);
					mJobRole.setPerson(this);
					BankPanel.getInstance().addPerson(mJobRole);
					break;
				case MARKET:
					mJobRole = SortingHat.getMarketRole(mTimeShift);
					if(mJobRole instanceof MarketCashierRole) {
						mJobRole = MarketPanel.getInstance().mCashier;
					} else if(mJobRole instanceof MarketDeliveryTruckRole) {
						mJobRole = MarketPanel.getInstance().mDeliveryTruck;
					} else if(mJobRole instanceof MarketWorkerRole){
						MarketPanel.getInstance().mCashier.addWorker((MarketWorker)mJobRole);
					}
					mJobRole.setPerson(this);
					break;
				case MARKETCUSTOMER:
					mJobRole = new MarketCustomerRole(this);
					mJobRole.setPerson(this);
					break;	
				case RESTAURANT:
					switch(mRestaurantRole) {
						case HOST:
							mJobRole = new RestaurantHostRole(null);
							break;
						case COOK:
							mJobRole = new RestaurantCookRole(null);
							break;
						case CASHIER:
							mJobRole = new RestaurantCashierRole(null);
							break;
						case WAITER:
							mJobRole = new RestaurantWaiterRole(null);
							break;
						case CUSTOMER:
							mJobRole = new RestaurantCustomerRole(null);
							break;
					}
					mJobLocation = ContactList.cRESTAURANT_DOORS.get(mRestaurantNumber);
					((RestaurantBaseInterface) mJobRole).setPerson(this);
					((RestaurantBaseInterface) mJobRole).setRestaurant(mRestaurantNumber); //HACK ANDRE ALL
					break;
				case TRANSPORTATION:
					mJobRole = SortingHat.getTransportationRole(this);
					break;
				case HOUSING: 
					mJobRole = (HousingBaseRole) SortingHat.getHousingRole(this); //get housing status
					mJobRole.setPerson(this);
					((HousingBaseRole) mJobRole).setHouse(SimCityGui.getInstance().citypanel.masterHouseList.get(sHouseCounter));
					mEvents.add(new Event(EnumEventType.MAINTAIN_HOUSE, 0));
					sHouseCounter++;
					break;
				case PARTY:
					mJobRole = new HousingRenterRole((Person)this);
					for(CityHousing iHouse : CityPanel.getInstance().masterHouseList){
						if(iHouse.mOccupant == null){
							iHouse.type = HousingType.House;
							iHouse.mOccupant = (HousingRenterRole)mJobRole;
							((HousingBaseRole) mJobRole).setHouse(iHouse);
							break;
						}
					}
					mHouseRole = (HousingBaseRole) mJobRole;
					if(mTimeShift == (Time.GetShift())+2){
						planParty(0);
					}
					else{
						planParty(-1);
					}
				case NONE:
					break;
			}
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
		mPersonGui = new CityPerson(this, SimCityGui.getInstance(), sSSN * 5 % 600, sSSN % 10 + 250); //SHANE: Hardcoded start place
		
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
			//mJobRole.setActive();
			mPersonGui.setPresent(true);
		}
		
		stateChanged();
	}

	public void msgAddEvent(Event event) {
		if ((event.mEventType == EnumEventType.RSVP1) && (mSSN % 2 == 1)) return; // maybe don't respond (half are deadbeats) - everyone always responds
		mEvents.add(event);
	}
	
	public void msgAnimationDone(){
		if (semAnimationDone.availablePermits() == 0) semAnimationDone.release();
	}
	
	public void msgRoleFinished(){ //SHANE: 3 Call at end of role
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
		if(mTimeShift == 1) {
			if ((mRoleFinished) && (!mAtJob) ){
				//System.out.println("Processing events");
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
		} else {
			mPersonGui.disable();
		}
		return false;
	}

	// ----------------------------------------------------------ACTIONS----------------------------------------------------------

	private synchronized void processEvent(Event event) {
		//System.out.println("processEvent "+event.mEventType.toString());
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
			print("DepositCheck");
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
			throwParty((EventParty)event);
			if(((EventParty)event).mHost == this){
				planParty(Time.GetTime()+24);
			}
			/*
			int inviteNextDelay = 24*mSSN;
			EventParty party = (EventParty) event;
			mEvents.add(new EventParty(party, inviteNextDelay + 2));
			mEvents.add(new EventParty(party, EnumEventType.INVITE1, inviteNextDelay, getBestFriends()));
			mEvents.add(new EventParty(party, EnumEventType.INVITE2, inviteNextDelay + 1, getBestFriends()));
			*/
			//SHANE: 3 check event classes
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
	
	private void acquireSemaphore(Semaphore semaphore){
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void getCar(){
		Location location = ContactList.cMARKET_DOOR;
		if(!testing){
		mPersonGui.DoGoToDestination(location);
		}
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
//		System.out.println("Going to Job");
		if (!testing){
			if (mJobLocation != null){
				mPersonGui.DoGoToDestination(mJobLocation);
			}else{
				mPersonGui.DoGoToDestination(ContactList.cRESTAURANT_DOORS.get(mRestaurantNumber));
			}
		}
		acquireSemaphore(semAnimationDone);
		mAtJob = true; //SHANE: This will need to be set to false somewhere
		mPersonGui.setPresent(false);
		if(mJobRole != null) {
			mJobRole.setPerson(this); //take over job role
			mRoles.put(mJobRole, true); //set role to active
			mJobRole.setActive();
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
			
			int restaurantChoice = mRestaurantNumber;

			mPersonGui.DoGoToDestination(ContactList.cRESTAURANT_DOORS.get(restaurantChoice));
			acquireSemaphore(semAnimationDone);
			mPersonGui.setPresent(false);
			
			((RestaurantBaseInterface) restCustRole).setPerson(this);
			((RestaurantBaseInterface) restCustRole).setRestaurant(restaurantChoice);
			
		}
		
	}
	
	private void depositCheck() {
		mPersonGui.setPresent(true);
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
		bankCustomerRole.setPerson(this);
		BankPanel.getInstance().addPerson(bankCustomerRole);
	}
	
	private void planParty(int time){
		mEvents.add(new Event(EnumEventType.INVITE1, time));
		mEvents.add(new Event(EnumEventType.INVITE2, time+2));
		mEvents.add(new Event(EnumEventType.PARTY,   time+4));
	}

	private void throwParty(EventParty event) {
		mPersonGui.DoGoToDestination(event.mLocation);
		acquireSemaphore(semAnimationDone);
		mPersonGui.setPresent(false);
		
		mHouseRole.gui.setPresent(true);
		event.mHost.getHouse().mPanel.addGui((Gui)mHouseRole.gui);
		mHouseRole.gui.DoParty();
	}

	private void inviteToParty() {
		print("First RSVP is sent out");
		if(mFriends.isEmpty()){
			int numPeople = CityPanel.getInstance().masterPersonList.size();
			for (int i = 0; i < numPeople; i = i + 2){
				mFriends.add(CityPanel.getInstance().masterPersonList.get(i));
			}
			print("Created friends for party host");
		}
		//party is in 3 days
		//send RSVP1 and event invite
		for (Person iFriend : mFriends){
			Event party = new EventParty(EnumEventType.PARTY, Time.GetTime()+4, ContactList.sRoleLocations.get(this), this, getBestFriends());
			Event rsvp = new Event(EnumEventType.RSVP1, -1); //respond immediately
			iFriend.msgAddEvent(rsvp);
			iFriend.msgAddEvent(party);
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
				Event rsvp = new Event(EnumEventType.RSVP2, -1);
				iPerson.msgAddEvent(rsvp);
			}
		}
	}
	
	private void respondToRSVP(){
		print("Responding to RSVP");
		for (Event iEvent : mEvents){
			if (iEvent instanceof EventParty){
				if (((EventParty) iEvent).mHost.getTimeShift() == mTimeShift){
					((EventParty) iEvent).mAttendees.put(this, true);
				}else{
					((EventParty) iEvent).mAttendees.remove(this);
				}
			}
		}
	}
	
	public void invokeRent() {
		mHouseRole.msgTimeToCheckRent(); //this role is always active
	}
	
	public void invokeMaintenance() {
//		if (mHouseRole.mHouse != null) {
			((HousingBaseRole) mJobRole).msgTimeToMaintain();
//			mHouseRole.msgTimeToMaintain(); //this role is always active
//		}
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


	private void boardBus() {
		int boardAtStop = ((TransportationBusRiderRole) mJobRole).mBusDispatch.getBusStopClosestTo(new Location(mPersonGui.xDestination, mPersonGui.yDestination));
		int exitAtStop = ((TransportationBusRiderRole) mJobRole).mBusDispatch.getBusStopClosestTo(mPersonGui.mFinalDestination);

		mPersonGui.DoGoToDestination(base.ContactList.cBUS_STOPS.get(boardAtStop));
		acquireSemaphore(semAnimationDone);

		((TransportationBusRiderRole) mJobRole).msgReset();
		
	}

	private void exitBus() {
		mRoleFinished = true;
		mPersonGui.NewDestination(new Location(mPersonGui.mFinalDestination.mX, mPersonGui.mFinalDestination.mY));
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

	/*public void setGui(MockPersonGui gui) {
		mPersonGui = gui;
	}*/

	@Override
	public CityHousing getHouse() {
		return mHouseRole.mHouse;
	}

	@Override
	public void setGuiPresent() {
		mPersonGui.setPresent(true);
		mPersonGui.setX(250);
		mPersonGui.setY(300);
	}
	
	public CityPerson getGui(){
		return mPersonGui;
	}

	@Override
	public void setJobFalse() {
		mAtJob = false;
	}
}
