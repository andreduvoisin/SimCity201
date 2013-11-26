package restaurant.restaurant_smileham.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.Menu;
import restaurant.restaurant_smileham.WaitingArea;
import restaurant.restaurant_smileham.agent.Check;
import restaurant.restaurant_smileham.gui.CustomerGui;
import restaurant.restaurant_smileham.gui.LabelGui;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.interfaces.SmilehamCashier;
import restaurant.restaurant_smileham.interfaces.SmilehamCustomer;
import restaurant.restaurant_smileham.interfaces.SmilehamHost;
import restaurant.restaurant_smileham.interfaces.SmilehamWaiter;
import base.BaseRole;
import base.interfaces.Person;

/**
 * Restaurant customer agent.
 */
public class SmilehamCustomerRole extends BaseRole implements SmilehamCustomer{
	
	//Constants
	private static final int cHUNGER_LEVEL = 5;
	private static final int cMAX_CASH = 20;
	
	//Agents
	private SmilehamCashier mCashier;
	
	//Regular Member Variables
	private String mName;
	private int mHunger;
	private int mTableNum;
	private Menu mMenu;
	private EnumFoodOptions mChoice;
	private boolean mIsHungry;
	private int mCash;
	private Check mCheck;
	
	
	private Timer mTimer;
	private SmilehamHost mHost;
	private SmilehamWaiter mWaiter;

	// States and Events
	public enum EnumAgentState {DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordering, WaitingForFood, Eating, AwaitingCheck, Paying, Leaving};
	private EnumAgentState mState;
	public enum EnumAgentEvent {none, gotHungry, restaurantFullLeave, followHost, seated, askedForOrder, receivedFood, doneEating, gotCheck, donePaying, doneLeaving};
	private EnumAgentEvent mEvent;
	
//	public Semaphore semAtTable = new Semaphore(0);
	public Semaphore semLeftRestaurant = new Semaphore(0);
	public Semaphore semAtPickupArea = new Semaphore(0);
	public Semaphore semPickedUp = new Semaphore(0);
	
	//GUI
	private CustomerGui mCustomerGui;
	private LabelGui mFoodLabelGui;
//	private SmilehamRestaurantGui mGUI;
	private SmilehamAnimationPanel mAnimationPanel;
	
	
	//-----------------------------------------------CONSTRUCTOR-----------------------------------------------
	/**
	 * Constructor for CustomerAgent class
	 * @param person 
	 *
	 * @param name name of the customer
	 * @param mGUI  reference to the customergui so the customer can send it messages
	 */
	public SmilehamCustomerRole(Person person, String name, SmilehamAnimationPanel animationPanel){
		super(person);

		mAnimationPanel = animationPanel;

		//SHANE: 5 grabbing at thin air...
		mName = "Customer";
		mHost = SmilehamAnimationPanel.getHost();
		mCashier = SmilehamAnimationPanel.getCashier();
		
		
//		mName = name;
//		mGUI = gui;
		print("Constructor");
		
		//set up customer
		mCustomerGui = new CustomerGui(this);
		mAnimationPanel.addGui(mCustomerGui);
		
		mHunger = cHUNGER_LEVEL;
		mTableNum = -1;
		mTimer = new Timer();
		mState = EnumAgentState.DoingNothing;
		mEvent = EnumAgentEvent.none;
//		mHost = host;
//		mCashier = cashier;
		mIsHungry = false;
		
		//Money
		int cash = 100;//RestaurantPanel.getAgentPanel().getHackCustomerCash(); //HACK: customer cash
		if (cash == -1){
			Random rand = new Random();
			cash = rand.nextInt(cMAX_CASH);
		}
		mCash = cash;
		
		mCheck = null; //error checking
		
		
		
//		startThread();
	}

	
	
	// -----------------------------------------------MESSAGES-----------------------------------------------
	
	public void msgGotHungry() {
		print("Message: gotHungry()");
		if (mState == EnumAgentState.DoingNothing){
			mEvent = EnumAgentEvent.gotHungry;
			mIsHungry = true;
		}
		stateChanged();
	}
	
	public void msgRestaurantFull(){
		Random rand = new Random();
		boolean stay = rand.nextBoolean();
//		stay = (mGUI.getRestaurantPanel().getAgentPanel().getHackBusyStay()); //HACK: busy stay
		stay = true; //HACK: stay in waiting area
		if (stay) return;
		mEvent = EnumAgentEvent.restaurantFullLeave;
		stateChanged();
	}

	public void msgSitAtTable(SmilehamWaiter waiter, int tableNum, Menu menu) {
		print("Message: msgSitAtTable()");
		mTableNum = tableNum;
		mMenu = menu;
		mWaiter = (SmilehamWaiterRole) waiter;
		mEvent = EnumAgentEvent.followHost;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToSeat() {
		print("Message: msgAnimationFinishedGoToSeat()");
		mEvent = EnumAgentEvent.seated;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike(Menu menu){
		print("Message: msgWhatWouldYouLike()");
		mMenu = menu;
		mEvent = EnumAgentEvent.askedForOrder;
		stateChanged();
	}
	
	public void msgHereIsYourFood(EnumFoodOptions choice){
		print("Message: msgHereIsYourFood()" + choice);
		
		if (choice != mChoice){
			print("Error: Did not receive correct order");
		}
		mFoodLabelGui.setLabel(mChoice.toString());
		
		mEvent = EnumAgentEvent.receivedFood;
		stateChanged();
	}
	
	public void msgCheckDelivered(Check check){
		mCheck = check;
		mEvent = EnumAgentEvent.gotCheck;
		stateChanged();
	}
	
	public void msgGoodToGo(int change){
		mCash += change;
		mEvent = EnumAgentEvent.donePaying;
		stateChanged();
	}
	
	
	
	public void msgAnimationFinishedLeaveRestaurant() {
		print("Message: msgAnimationFinishedLeaveRestaurant()");
		mEvent = EnumAgentEvent.doneLeaving;
		semLeftRestaurant.release();
		mTableNum = -1;
		stateChanged();
	}
	
	public void msgAnimationPickedUp(){
		if (semPickedUp.availablePermits() == 0) semPickedUp.release();
	}
	
	public void msgAnimationAtPickupArea(){
		if (semAtPickupArea.availablePermits() == 0) semAtPickupArea.release();
	}
	

	//-----------------------------------------------SCHEDULER-----------------------------------------------
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		//DoingNothing + gotHungry = WaitingInRestaurant
		if (mState == EnumAgentState.DoingNothing && mEvent == EnumAgentEvent.gotHungry ){
			mState = EnumAgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		
		//WaitingInRestaurant + followHost = BeingSeated
		if (mState == EnumAgentState.WaitingInRestaurant && mEvent == EnumAgentEvent.followHost ){
			mState = EnumAgentState.BeingSeated;
			sitDown();
			return true;
		}
		
		//WaitingInRestaurant + followHost = BeingSeated
		if (mState == EnumAgentState.WaitingInRestaurant && mEvent == EnumAgentEvent.restaurantFullLeave ){
			mState = EnumAgentState.Leaving;
//			leaveRestaurant(); //SHANE: 2 put this back after testing
//			leaveTable(); or something like this
			return true;
		}
		
		//BeingSeated + seated = Seated
		if (mState == EnumAgentState.BeingSeated && mEvent == EnumAgentEvent.seated ){
			mState = EnumAgentState.Seated;
			getWaitersAttention();
			return true;
		}
		
		//Seated +  askedForOder = Ordering
		if ((mState == EnumAgentState.Seated || mState == EnumAgentState.WaitingForFood) && mEvent == EnumAgentEvent.askedForOrder){
			mState = EnumAgentState.WaitingForFood;
			orderFood();
			return true;
		}
				
		//WaitingForFood + receivedFood = Eating
		if (mState == EnumAgentState.WaitingForFood && mEvent == EnumAgentEvent.receivedFood){
			mState = EnumAgentState.Eating;
			eatFood(); //on timer
			return true;
		}

		//Eating + doneEating = Paying
		if (mState == EnumAgentState.Eating && mEvent == EnumAgentEvent.doneEating){
			mState = EnumAgentState.AwaitingCheck;
			askForCheck();
			return true;
		}
		
		//AwaitingCheck + gotCheck = Paying
		if (mState == EnumAgentState.AwaitingCheck && mEvent == EnumAgentEvent.gotCheck){
			mState = EnumAgentState.Paying;
			payCheck();
			return true;
		}
		
		//Paying + donePaying = Leaving
		if (mState == EnumAgentState.Paying && mEvent == EnumAgentEvent.donePaying){
//			mState = EnumAgentState.Leaving; //redundant
			leaveTable(); //on timer
			return true;
		}
		
		//Leaving + doneLeaving = DoingNothing
		if (mState == EnumAgentState.Leaving && mEvent == EnumAgentEvent.doneLeaving){
			mState = EnumAgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// -----------------------------------------------ACTIONS-----------------------------------------------

	private void acquireSemaphore(Semaphore semaphore){
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void goToRestaurant() {
		print("Action: goToRestaurant()");
		
		//if waiting area is full, don't go to restaurant
		if (WaitingArea.waitingAreaFull()){
			mEvent = EnumAgentEvent.restaurantFullLeave;
			stateChanged();
			return;
		}
		
		mCustomerGui.DoGoToWaitingArea();
		WaitingArea.addWaitingCustomer();
		
		mHost.msgIWantFood((SmilehamCustomer)this);
	}

	private void sitDown() {
		print("Action: sitDown()");
		WaitingArea.removeWaitingCustomer();
		mCustomerGui.DoGoToPickupArea();
		acquireSemaphore(semAtPickupArea);
		acquireSemaphore(semPickedUp);
		
		mCustomerGui.DoGoToTable(mTableNum);
	}
	
	private void getWaitersAttention(){
		print("Action: getWaitersAttention()");
		mWaiter.msgReadyToOrder((SmilehamCustomer)this);
	}
	
	private void orderFood(){
		print("Action: orderFood()");
		mEvent = EnumAgentEvent.none; //to prevent multiple orderings
		
		//Check what customer can pay for
		List<EnumFoodOptions> foodList = new ArrayList<EnumFoodOptions>();
		for (EnumFoodOptions iFood : mMenu.getMenuOptions()){
			if (mMenu.getMenu().get(iFood) > mCash){
				foodList.add(iFood);
			}
		}
		for (EnumFoodOptions iFood : foodList){
			mMenu.removeChoice(iFood);
		}
		
		//If nothing available to buy (or can't pay), leave restaurant
		if (mMenu.numChoices() == 0){
			leaveTable();
			mWaiter.msgNotGettingFood((SmilehamCustomer)this);
			return;
		}
		
		Random rand = new Random();
		int choiceNum = rand.nextInt(mMenu.numChoices());
		mChoice = mMenu.getMenuOptions().get(choiceNum);
		
		if (mFoodLabelGui != null) mFoodLabelGui.remove();
		mFoodLabelGui = new LabelGui(mChoice.toString() + "?", mCustomerGui.getX(), mCustomerGui.getY(), mAnimationPanel);
		mWaiter.msgHereIsMyChoice((SmilehamCustomer)this, mChoice);
	}

	private void eatFood() {
		print("Action: eatFood()");
			//Description:
			//This next complicated line creates and starts a timer thread.
			//We schedule a deadline of getHungerLevel()*1000 milliseconds.
			//When that time elapses, it will call back to the run routine
			//located in the anonymous class created right there inline:
			//TimerTask is an interface that we implement right there inline.
			//Since Java does not all us to pass functions, only objects.
			//So, we use Java syntactic mechanism to create an
			//anonymous inner class that has the public method run() in it.
		mTimer.schedule(new TimerTask() {
			public void run() {
				print("Done eating");
				mEvent = EnumAgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		getHungerLevel() * 200);
		mIsHungry = false;
	}
	
	private void askForCheck(){
		print("Action: askForCheck()");
		mWaiter.msgReadyForCheck(mChoice, (SmilehamCustomer)this);
	}
	
	private void payCheck(){
		print("Action: payCheck()");
		if (mCash < mCheck.mCash){
			//SHANE: 5 Dine and dash - other actions?
			//This won't happen because of preconditions...
		}
		int cash = mCash;
		mCash -= cash;
		mCashier.msgPayingCheck(mCheck);
//		mWaiter.msgDoneAndPaying(cash, (Customer)this);
	}

	private void leaveTable() {
		print("Action: leaveTable()");
		mState = EnumAgentState.Leaving;
		if (mFoodLabelGui != null) mFoodLabelGui.remove();
		mCustomerGui.DoExitRestaurant();
		acquireSemaphore(semLeftRestaurant);
		mWaiter.msgCustomerLeaving((SmilehamCustomer)this);
	}
	
	private void leaveRestaurant(){
		print("Action: leaveRestaurant()");
		mHost.msgLeavingRestaurant((SmilehamCustomer)this);
	}

	//-----------------------------------------------ACCESSORS-----------------------------------------------
	
	public void setHost(SmilehamHost host) {
		mHost = (SmilehamHostRole) host;
	}

	public String getCustomerName() {
		return mName;
	}
	
	public String getName() {
		return mName;
	}
	
	public int getHungerLevel() {
		return mHunger;
	}
	
	public int getCash(){
		return mCash;
	}

	public void setHungerLevel(int hungerLevel) {
		this.mHunger = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public void setGui(CustomerGui g) {
		mCustomerGui = g;
	}

	public CustomerGui getGui() {
		return mCustomerGui;
	}
	
	public boolean isHungry(){
		return mIsHungry;
	}
	
	public String toString() {
		return "[Customer " + getName() + "]";
	}
}

