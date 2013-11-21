package restaurant_smileham.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant_smileham.Menu;
import restaurant_smileham.WaitingArea;
import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.agent.Agent;
import restaurant_smileham.agent.Check;
import restaurant_smileham.gui.CustomerGui;
import restaurant_smileham.gui.LabelGui;
import restaurant_smileham.gui.RestaurantGui;
import restaurant_smileham.interfaces.Cashier;
import restaurant_smileham.interfaces.Customer;
import restaurant_smileham.interfaces.Host;
import restaurant_smileham.interfaces.Waiter;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer{
	
	//Constants
	private static final int cHUNGER_LEVEL = 5;
	private static final int cMAX_CASH = 20;
	
	//Agents
	private Cashier mCashier;
	
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
	private HostAgent mHost;
	private WaiterAgent mWaiter;

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
	private RestaurantGui mGUI;
	
	
	//-----------------------------------------------CONSTRUCTOR-----------------------------------------------
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param mGUI  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name, HostAgent host, Cashier cashier, RestaurantGui gui){
		super();
		mName = name;
		mGUI = gui;
		print("Constructor");
		
		//set up customer
		mCustomerGui = new CustomerGui(this, mGUI);
		mGUI.getAnimationPanel().addGui(mCustomerGui);
		
		mHunger = cHUNGER_LEVEL;
		mTableNum = -1;
		mTimer = new Timer();
		mState = EnumAgentState.DoingNothing;
		mEvent = EnumAgentEvent.none;
		mHost = host;
		mCashier = cashier;
		mIsHungry = false;
		
		//Money
		int cash = mGUI.getRestaurantPanel().getAgentPanel().getHackCustomerCash(); //HACK: customer cash
		if (cash == -1){
			Random rand = new Random();
			cash = rand.nextInt(cMAX_CASH);
		}
		mCash = cash;
		
		mCheck = null; //error checking
		
		startThread();
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
		stay = (mGUI.getRestaurantPanel().getAgentPanel().getHackBusyStay()); //HACK: busy stay
		stay = true; //HACK: stay in waiting area
		if (stay) return;
		mEvent = EnumAgentEvent.restaurantFullLeave;
		stateChanged();
	}

	public void msgSitAtTable(Waiter waiter, int tableNum, Menu menu) {
		print("Message: msgSitAtTable()");
		mTableNum = tableNum;
		mMenu = menu;
		mWaiter = (WaiterAgent) waiter;
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
			leaveRestaurant();
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
		
		mHost.msgIWantFood((Customer)this);
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
		mWaiter.msgReadyToOrder((Customer)this);
	}
	
	private void orderFood(){
		print("Action: orderFood()");
		mEvent = EnumAgentEvent.none; //to prevent multiple orderings
		
		if(mGUI.getRestaurantPanel().getAgentPanel().getHackFlake() == false){ //HACK: flake
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
		}
		
		//If nothing available to buy (or can't pay), leave restaurant
		if (mMenu.numChoices() == 0){
			leaveTable();
			mWaiter.msgNotGettingFood((Customer)this);
			return;
		}
		
		Random rand = new Random();
		int choiceNum = rand.nextInt(mMenu.numChoices());
		mChoice = mMenu.getMenuOptions().get(choiceNum);
		
		if (mFoodLabelGui != null) mFoodLabelGui.remove();
		mFoodLabelGui = new LabelGui(mChoice.toString() + "?", mCustomerGui.getX(), mCustomerGui.getY(), mGUI);
		mWaiter.msgHereIsMyChoice((Customer)this, mChoice);
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
		mWaiter.msgReadyForCheck(mChoice, (Customer)this);
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
		mWaiter.msgCustomerLeaving((Customer)this);
	}
	
	private void leaveRestaurant(){
		print("Action: leaveRestaurant()");
		mHost.msgLeavingRestaurant((Customer)this);
	}

	//-----------------------------------------------ACCESSORS-----------------------------------------------
	
	public void setHost(Host host) {
		mHost = (HostAgent) host;
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

