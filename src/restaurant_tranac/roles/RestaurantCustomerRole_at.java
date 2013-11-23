package restaurant_tranac.roles;

import restaurant_tranac.Check;
import restaurant_tranac.Menu;
import base.BaseRole;
import restaurant_tranac.gui.CustomerGui_at;
import restaurant_tranac.interfaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class RestaurantCustomerRole_at extends BaseRole implements Customer{
	private CustomerGui_at customerGui;
	private int hungerLevel = 10;        		//determines length of meal
	private final int baseMoney = 30;
	private String choice;
	private Timer timer = new Timer();			//used to time eating
	private Random rGenerator = new Random();	//used to select meal
	private Menu menu = null;
	private Check check = null;
	private double money;
	private int num;
	
	private boolean flake = false;
	private boolean willWait;
	private boolean choiceFound = true;
	private List<String> pastChoices = new ArrayList<String>();
	
	//agent correspondents
	private Host host;
	private Waiter waiter;
	private Cashier cashier;

	public enum AgentState
	{DoingNothing, WaitingInRestaurant, LeavingRestaurantEarly, BeingSeated, 
		Ordering, Ordered, ChoosingToLeave, Eating, DoneEating, WaitingForCheck, 
		GoingToCashier, Paying, Leaving, GoingToRestaurant, WaitingAtHost, GoingToWaitingArea};
	public enum AgentEvent 
	{none, gotHungry, willWait, wantToLeave, followWaiter, seated, askedForOrder,
		outOfChoice, noAvailableFood, decidedToLeave, givenFood, doneEating, 
		gotCheck, atCashier, paid, doneLeaving, decidedToStay, foodTooExpensive, atHost, acknowledgeByHost, atWaitingArea};
	
	private AgentState state = AgentState.DoingNothing;	//initial state
	private AgentEvent event = AgentEvent.none;
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customerGui so the customer can send it messages
	 */
	public RestaurantCustomerRole_at(){
		super();
		money = baseMoney;	//ANGELICA: no longer necessary; will get from person
		num = 0;
		
		//randomly chooses waiting unless set later
		if(rGenerator.nextInt() % 2 == 0)
			willWait = false;
		else
			willWait = true;
	}
	
	/** Messages */
	public void msgGotHungry() {			//from animation
		print("I'm hungry.");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgPleaseWaitHere(int n) {
		num = n;
		event = AgentEvent.acknowledgeByHost;
		stateChanged();
	}
	
	public void msgRestaurantFull() {
		if(!willWait) {
			event = AgentEvent.wantToLeave;
		}
		else {
			event = AgentEvent.willWait;
		}
		stateChanged();
	}
	
	public void msgFollowMe(Menu m, Waiter w) {
		menu = m;
		waiter = w;
		event = AgentEvent.followWaiter;
		stateChanged();
	}
	
	public void msgWhatDoYouWant() {
		event = AgentEvent.askedForOrder;
		stateChanged();
	}

	public void msgOutOfChoice() {
		pastChoices.add(choice);
		event = AgentEvent.outOfChoice;
		stateChanged();
	}
	
	public void msgHereIsFood() {
		event = AgentEvent.givenFood;
		stateChanged();
	}
	
	public void msgDoneEating() {
		event = AgentEvent.doneEating;
		stateChanged();
	}
	
	public void msgHereIsCheck(Check c) {
		check = c;
		event = AgentEvent.gotCheck;
		stateChanged();
	}
	
	public void msgHereIsChange(Check c) {
		event = AgentEvent.paid;
		stateChanged();
	}
	
	public void msgPayNextTime() {
		event = AgentEvent.paid;
		stateChanged();
	}
	
	/** Animation Messages */
	public void msgAnimationFinishedGoToSeat() {
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedGoToCashier() {
		event = AgentEvent.atCashier;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	public void msgAnimationAtHost() {
		event = AgentEvent.atHost;
		stateChanged();
	}
	public void msgAnimationAtWaitingArea() {
		event = AgentEvent.atWaitingArea;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry){
			state = AgentState.GoingToRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.GoingToRestaurant && event == AgentEvent.atHost){
			state = AgentState.WaitingAtHost;
			tellHostAtRestaurant();
			return true;
		}
		if (state == AgentState.WaitingAtHost && event == AgentEvent.acknowledgeByHost) {
			state = AgentState.GoingToWaitingArea;
			goToWaitingArea();
			return true;
		}
		if (state == AgentState.WaitingAtHost && event == AgentEvent.willWait) {
			tellHostWillWait();
			return true;
		}
		if (state == AgentState.WaitingAtHost && event == AgentEvent.wantToLeave) {
			state = AgentState.Leaving;
			leaveRestaurantEarly();
			return true;
		}
		if(state == AgentState.GoingToWaitingArea && event == AgentEvent.atWaitingArea) {
			state = AgentState.WaitingInRestaurant;
			//no action; wait for host
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter){
			state = AgentState.BeingSeated;
			sitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Ordering;
			chooseOrder();
			return true;
		}
		if (state == AgentState.Ordering && event == AgentEvent.askedForOrder) {
			state = AgentState.Ordered;
			orderFood();
			return true;
		}
		if (state == AgentState.Ordering && event == AgentEvent.noAvailableFood) {
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.Ordering && event == AgentEvent.foodTooExpensive) {
			state = AgentState.ChoosingToLeave;
			chooseToStay();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.outOfChoice) {
			state = AgentState.Ordering;
			chooseOrder();
			return true;
		}
		if (state == AgentState.ChoosingToLeave && event == AgentEvent.decidedToLeave) {
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.ChoosingToLeave && event == AgentEvent.decidedToStay) {
			state = AgentState.Ordered;
			orderFood();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.givenFood) {
			state = AgentState.Eating;
			eatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.WaitingForCheck;
			askForCheck();
			return true;
		}
		if (state == AgentState.WaitingForCheck && event == AgentEvent.gotCheck) {
			state = AgentState.GoingToCashier;
			DoGoToCashier();		//animation call
		}
		if (state == AgentState.GoingToCashier && event == AgentEvent.atCashier) {
			state = AgentState.Paying;
			payCheck();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.paid) {
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			//no action; return to idle state so customer can become hungry again
			return true;
		}
		return false;
	}

	/** Actions */

	private void goToRestaurant() {
		Do("Going to restaurant.");
		DoGoToHost();
	}

	private void tellHostAtRestaurant() {
		Do("Helloooo.");
		host.msgIWantFood(this);
	}
	
	private void goToWaitingArea() {
		Do("Going to waiting area " + num);
		DoGoToWaitingArea();
		host.msgAtWaitingArea(this);
	}
	
	private void tellHostWillWait() {
		Do("Telling host I will wait at restaurant.");
		event = AgentEvent.none;
		host.msgWillWait(this);
	}
	
	private void leaveRestaurantEarly() {
		Do("Telling host I'm leaving.");
		host.msgLeavingEarly(this);
		DoExitRestaurant();
	}
	
	private void sitDown() {
		Do("Being seated. Going to table.");
		DoGoToTable();
	}
	
	private void chooseOrder() {
		Do("Choosing order.");
		choiceFound = false;
		boolean poor = false;
		
		//check if poor
		if(money <= menu.getCheapestItem())
			poor = true;
		
		while(!choiceFound) {
			
			if(pastChoices.size() == menu.getSize()) {
				Do("No more choices available...");
				event = AgentEvent.noAvailableFood;
				return;
			}
			
			int c = Math.abs(rGenerator.nextInt() % 4);
			
			choice = menu.getChoice(c);
			Do("Want " + choice);
		
			double p = menu.getCost(choice);
		
			if(p <= money) {
				for(String pc : pastChoices) {
					if(choice.equals(pc)) {
						if(poor && p == menu.getCheapestItem()) {
							event = AgentEvent.foodTooExpensive;
							return;
						}
						choiceFound = false;
						break;
					}
				}
				choiceFound = true;				
			}
			else {
				pastChoices.add(choice);					//add item to list of unavailable
				if(pastChoices.size() == menu.getSize()) {	//if it was the last available item,
					event = AgentEvent.foodTooExpensive;	//decide to pay or to leave
					return;
				}
			}
		}
		
		customerGui.setAlerting();
		waiter.msgReadyToOrder(this);
		
	}

	private void chooseToStay() {
		Do("Deciding to stay (and be a flake).");
		if(flake)
			event = AgentEvent.decidedToStay;
		else
			event = AgentEvent.decidedToLeave;
	}
	
	private void orderFood() {
		Do("Ordering " + choice + ".");
		customerGui.setFood(choice);			//create food icon
		customerGui.setOrdering();
		waiter.msgReceivedOrder(this, choice);
	}

	private void eatFood() {
		Do("Eating food.");
		customerGui.setEating();				//removes the question mark from food icon
		timer.schedule(new TimerTask() {
			public void run() {
				Do("Done eating.");
				event = AgentEvent.doneEating;
				stateChanged();
			}
		},
		getHungerLevel() * 1000);			//how long to wait before running task
	}
	
	private void askForCheck() {
		Do("Asking for check.");
		customerGui.setAlerting();
		waiter.msgAskingForCheck(this);
	}
	
	private void payCheck() {
		Do("Paying check of " + check.getAmount());
		//check if customer can afford payment. if he can't, he must be a flake
		if(check.getAmount() <= money) {
			money -= check.getAmount();
			cashier.msgHereIsPayment(this, check.getAmount());
		}
		else {
			Do("Flaking like a boss.");
			cashier.msgHereIsPayment(this, 0);
		}
	}
	
	private void leaveRestaurant() {
		Do("Leaving.");
		
		//check change from check
		if(check != null) {
			Do("Adding change " + check.getChange() + " to money.");
			money += check.getChange();
		}
		
		//clear past choices for future visits
		pastChoices.clear();
		check = null;
		waiter.msgDoneEating(this);
		customerGui.setClear();
		DoExitRestaurant();
	}

	/** Animation Actions */	
	private void DoGoToHost() {
		customerGui.DoGoToHost();
	}
	
	private void DoGoToWaitingArea() {
		customerGui.DoGoToWaitingArea(num);
	}
	
	private void DoGoToTable() {
		//send second flag to customerGui
	}
	
	private void DoGoToCashier() {
		customerGui.setPaying();			//remove food icon
		customerGui.DoGoToCashier();
	}
	
	private void DoExitRestaurant() {
		customerGui.DoExitRestaurant();
	}
	
	/** Utilities */

	public String getName() {
		return mPerson.getName();
	}
	
	public void setHost(Host h) {
		host = h;
	}

	public void setWaiter(Waiter w) {
		waiter = w;
	}
	
	public void setCashier(Cashier c) {
		cashier = c;
	}

	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	public void setMoney(double m) {
		money = m;
	}
	
	public double getMoney() {
		return money;
	}
	
	public void setWillWait(boolean b) {
		willWait = b;
	}
	
	public void setFlake(boolean b) {
		flake = b;
	}
	
	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui_at g) {
		customerGui = g;
	}

	public CustomerGui_at getGui() {
		return customerGui;
	}
}

