package restaurant.restaurant_duvoisin.roles;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.gui.trace.AlertTag;
import restaurant.restaurant_duvoisin.Menu;
import restaurant.restaurant_duvoisin.gui.CustomerGui;
import restaurant.restaurant_duvoisin.interfaces.Cashier;
import restaurant.restaurant_duvoisin.interfaces.Customer;
import restaurant.restaurant_duvoisin.interfaces.Host;
import restaurant.restaurant_duvoisin.interfaces.Waiter;
import base.BaseRole;
import base.Event;
import base.Event.EnumEventType;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;

/**
 * Restaurant customer agent.
 */
public class AndreCustomerRole extends BaseRole implements Customer {
	// Hacks for Name:
	// ST = Will Order Steak
	// CH = Will Order Chicken
	// SA = Will Order Salad
	// PI = Will Order Pizza
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	private Timer timer = new Timer();
	private TimerTask tTask;
	private int timerTime;
	private CustomerGui customerGui;

	// agent correspondents
	private Host host;
	private Waiter waiter;
	private Cashier cashier;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Deciding, Decided, Ordered, Eating, RequestedCheck, Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;	//The start state

	public enum AgentEvent 
	{none, gotHungry, restaurantFull, followWaiter, seated, decidedOrder, waiterAskedForOrder, outOfFood, foodHere, checkHere, doneEating, gotChange, doneLeaving};
	private AgentEvent event = AgentEvent.none;
	
	private Menu menu;
	private int table;
	private String choice;
	
	double money;
	private double amountOwed;
	
	Boolean owesMoney = false;
	
	Boolean paused = false;
	
	Boolean cheapestItem = false;
	
	private Semaphore atWaitingArea = new Semaphore(0, true);
	
	// For formatting money output to console.
	DecimalFormat formatOutput = new DecimalFormat("0.00");
	
	public static int customerNumber = 0;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public AndreCustomerRole(Person person) {
		super(person);
		//this.name = person.getName();
			this.name = "AndreCustomer" + customerNumber;
			customerNumber++;
		amountOwed = 0.00;
		money = 0.00;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(Host host) {
		this.host = host;
	}

	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}
	
	public String getCustomerName() {
		return name;
	}
	
	// Messages

	public void msgGotHungry() {
		//from animation
		print("msgGotHungry received");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgFollowMeToTable(Waiter w, Menu m, int t) {
		print("msgFollowMeToTable received");
		this.waiter = w;
		this.menu = m;
		this.table = t;
		event = AgentEvent.followWaiter;
		stateChanged();
	}
	
	public void msgAtSeat() {
		print("msgAtSeat received");
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() {
		print("msgWhatWouldYouLike received");
		event = AgentEvent.waiterAskedForOrder;
		stateChanged();
	}
	
	public void msgOutOfChoice(Menu m) {
		print("msgOutOfChoice received");
		menu = m;
		event = AgentEvent.outOfFood;
		stateChanged();
	}
	
	public void msgHereIsYourFood(String myChoice) {
		print("msgHereIsYourFood received");
		customerGui.RecievedChoice(myChoice);
		event = AgentEvent.foodHere;
		stateChanged();
	}
	
	public void msgHereIsCheck(double amount, Cashier ca) {
		print("msgHereIsCheck received");
		amountOwed = amount;
		cashier = ca;
		event = AgentEvent.checkHere;
		stateChanged();
	}
	
	public void msgHereIsChange(double change) {
		print("msgHereIsChange received");
		// Add change to total money, when implemented.
		event = AgentEvent.gotChange;
		stateChanged();
	}
	
	public void msgDoneLeaving() {
		//from animation
		print("msgDoneLeaving received");
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	public void msgRestaurantFull() {
		print("msgRestaurantFull received");
		event = AgentEvent.restaurantFull;
		stateChanged();
	}
	
	public void msgAtWaitingArea() {
		atWaitingArea.release();
		stateChanged();
	}
	
	public void msgPauseScheduler() {
		paused = true;
		//if(isTimerRunning) { pauseTimer(); }
	}
	
	public void msgResumeScheduler() {
		paused = false;
		//if(isTimerRunning) { resumeTimer(); }
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if(!paused) {
			if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
				GoToRestaurant();
				return true;
			}
			if (state == AgentState.WaitingInRestaurant && event == AgentEvent.restaurantFull ){
				DecideStayOrLeave();
				return true;
			}
			if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
				SitDown();
				return true;
			}
			if (state == AgentState.BeingSeated && event == AgentEvent.seated){
				DecideOrder();
				return true;
			}
			if (state == AgentState.Deciding && event == AgentEvent.decidedOrder){
				ReadyToOrder();
				return true;
			}
			if (state == AgentState.Decided && event == AgentEvent.waiterAskedForOrder){
				OrderFood();
				return true;
			}
			if (state == AgentState.Ordered && event == AgentEvent.outOfFood){
				PickNewChoice();
				return true;
			}
			if (state == AgentState.Ordered && event == AgentEvent.foodHere){
				EatFood();
				return true;
			}
			if (state == AgentState.Eating && event == AgentEvent.doneEating){
				RequestCheck();
				return true;
			}
			if (state == AgentState.RequestedCheck && event == AgentEvent.checkHere){
				PayCheck();
				return true;
			}
			if (state == AgentState.Paying && event == AgentEvent.gotChange){
				LeaveTable();
				return true;
			}
			if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
				DoNothing();
				return true;
			}
		}
		return false;
	}

	// Actions
	private void GoToRestaurant() {
		state = AgentState.WaitingInRestaurant;
		print("Doing GoToRestaurant");
		switch(name) {
			case "0.00":
				money = 0.00;
				break;
			case "5.00":
				money = 5.00;
				break;
			case "10.00":
				money = 10.00;
				break;
			case "15.00":
				money = 15.00;
				break;
			case "20.00":
				money = 20.00;
				break;
			default:
				// Random Money amount.
				// $0.00 - $20.00 (approx)
				Random rng = new Random();
				money = rng.nextDouble() + rng.nextInt(20);
				break;
		}
		if(owesMoney) {
			money = 40.00;
			owesMoney = false;
		}
		print("$" + formatOutput.format(money));
		choice = null;
		customerGui.DoGoToWaitingArea();
		try {
			atWaitingArea.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		host.msgIWantToEat(this, customerGui.WAIT_POS);	//send our instance, so he can respond to us
	}
	
	private void DecideStayOrLeave() {
		print("Doing DecideStayOrLeave");
		Random rng = new Random();
		switch(rng.nextInt(2)) {
			case 0:
				host.msgLeavingBecauseRestaurantFull(this);
				customerGui.DoExitFullRestaurant();
				DoNothing();
				break;
			case 1:
				event = AgentEvent.gotHungry;
				break;
		}
		stateChanged();
	}
	
	private void SitDown() {
		state = AgentState.BeingSeated;
		print("Doing SitDown");
		customerGui.DoGoToSeat(table);
	}
	
	private void DecideOrder() {
		state = AgentState.Deciding;
		print("Doing DecideOrder");
		tTask = new TimerTask() {
			public void run() {
				Random rng = new Random();
				String[] myMenuOptions = menu.menuItems.keySet().toArray(new String[0]);
				Double[] myMenuPrices = menu.menuItems.values().toArray(new Double[0]);
				switch(name) {
					case "ST":
						choice = "steak";
						break;
					case "CH":
						choice = "chicken";
						break;
					case "SA":
						choice = "salad";
						break;
					case "PI":
						choice = "pizza";
						break;
					default:
						if(money < myMenuPrices[0]) {
							switch(rng.nextInt(2)) {
								case 0:
									LeaveTable();
									return;
							}
						}
						if(myMenuPrices.length > 1) {
							if(money >= myMenuPrices[0] && money < myMenuPrices[1]) {
								choice = myMenuOptions[0];
								cheapestItem = true;
							} else {
								choice = myMenuOptions[rng.nextInt(menu.menuItems.size())];
								if(money > myMenuPrices[0])
									while(menu.menuItems.get(choice) > money) { choice = myMenuOptions[rng.nextInt(menu.menuItems.size())]; }
							}
						} else {
							choice = myMenuOptions[0];
						}
						break;
				}
				event = AgentEvent.decidedOrder;
				stateChanged();
			}
		};
		timerTime = (hungerLevel * 1000) / 4;
		timer.schedule(tTask, timerTime);
	}
	
	private void PickNewChoice() {
		event = AgentEvent.waiterAskedForOrder;	//Should probably rename this...
		print("Doing PickNewChoice");
		Random rng = new Random();
		String[] myMenuOptions = menu.menuItems.keySet().toArray(new String[0]);
		//Double[] myMenuPrices = menu.menuItems.values().toArray(new Double[0]);
		if(cheapestItem) {
			LeaveTable();
			return;
		}
		choice = myMenuOptions[rng.nextInt(menu.menuItems.size())];
		waiter.msgHereIsMyChoice(this, choice);
		customerGui.OrderedChoice(choice);
		stateChanged();	//Don't need this?
	}
	
	private void ReadyToOrder() {
		state = AgentState.Decided;
		print("Doing ReadyToOrder");
		waiter.msgImReadyToOrder(this);
	}
	
	private void OrderFood() {
		state = AgentState.Ordered;
		print("Doing OrderFood");
		waiter.msgHereIsMyChoice(this, choice);
		customerGui.OrderedChoice(choice);
	}

	private void EatFood() {
		state = AgentState.Eating;
		print("Doing EatFood");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		tTask = new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done Eating! cookie = " + cookie);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
				//isTimerRunning = false;
			}
		};
		timerTime = hungerLevel * 1000;	//how long to wait before running task
		timer.schedule(tTask, timerTime);
		//isTimerRunning = true;
	}
	
	private void RequestCheck() {
		state = AgentState.RequestedCheck;
		print("Doing RequestCheck");
		waiter.msgRequestCheck(this);
	}
	
	private void PayCheck() {
		state = AgentState.Paying;
		print("Doing PayCheck");
		if(!owesMoney)
			cashier.msgPayment(this, amountOwed);
		else
			cashier.msgPayment(this, money);
		
		if(amountOwed > money)
			owesMoney = true;
	}

	private void LeaveTable() {
		state = AgentState.Leaving;
		print("Doing LeaveTable");
		waiter.msgDoneEatingAndLeaving(this);
		customerGui.DoExitRestaurant();
	}
	
	private void DoNothing() {
		state = AgentState.DoingNothing;
		print("Doing DoNothing");
		mPerson.msgAddEvent(new Event(EnumEventType.DEPOSIT_CHECK, 0));
		mPerson.setJobFalse();
		mPerson.msgRoleFinished();
		// ANDRE: Should probably do something like this...
//		customerGui.animationPanel.removeCustomer(this);
//		customerGui.animationPanel.removeGui(customerGui);
	}

	// Accessors, etc.
	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
	
	// Utilities
	/*
	public void pauseTimer() {
		tempTime = timerTime - (int)(new Date().getTime() - tTask.scheduledExecutionTime());
		tTask.cancel();
		timer.cancel();
	}
	
	public void resumeTimer() {
		tTask = new TimerTask() {
			public void run() {
				// Fix this. Doesn't use menu class at all. D:
				Random rng = new Random(1000);
				switch(rng.nextInt(menu.menuItems.size())) {
					case 0:
						choice = "steak";
						break;
					case 1:
						choice = "chicken";
						break;
					case 2:
						choice = "salad";
						break;
					case 3:
						choice = "pizza";
						break;
					default:
						choice = "steak";
						break;
				}
				event = AgentEvent.decidedOrder;
				stateChanged();
				isTimerRunning = false;
			}
		};
		timer = new Timer();
		timer.schedule(tTask, tempTime);
	}
	*/
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(0);
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.R0);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R0);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R0, e);
	}
}