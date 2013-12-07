package restaurant.restaurant_cwagoner.roles;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.restaurant_cwagoner.gui.CwagonerAnimationPanel;
import restaurant.restaurant_cwagoner.gui.CwagonerCustomerGui;
import restaurant.restaurant_cwagoner.interfaces.*;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;
import city.gui.trace.AlertTag;

/**
 * Restaurant customer agent.
 */
public class CwagonerCustomerRole extends BaseRole implements CwagonerCustomer {
	
	// DATA
	
	private int hungerLevel = 5;        // determines length of meal
	private HashMap<String, Integer> menu = new HashMap<String, Integer>();
	private String food;
	private double myMoney;
	private double moneyOwed;
	private Random rand = new Random();
	
	Timer customerTimer = new Timer();
	private CwagonerCustomerGui gui;
	
	CwagonerWaiter waiter;
	CwagonerAnimationPanel animationPanel;

	public enum State { doingNothing, inRestaurant, waitingToBeSeated,
						goingToSeat, lookingAtMenu, readyToOrder, ordering, ordered,
						eating, askedForCheck, goingToCashier, waitingAtCashier, paid, leaving};
	private State state = State.doingNothing;

	public enum Event { none, gotHungry, followWaiter, seated, decidedOnFood,
						waiterAskedForOrder, gaveOrderToWaiter, foodDelivered,
						doneEating, checkReady, arrivedAtCashier, checkGiven,
						cashierAccepted, doneLeaving };
	Event event = Event.none;

	public CwagonerCustomerRole(Person person, CwagonerAnimationPanel panel) {
		super(person);
		// CHASE: handle payment: moneyOwed, myMoney?
		state = State.inRestaurant;

		animationPanel = panel;
		this.setGui(new CwagonerCustomerGui(this, panel));
	}
	
	// MESSAGES
	
	// From host when no tables are empty
	public void msgRestaurantFull() {
		print("Received msgRestaurantFull");
		
		// 50% chance decide to leave - otherwise, wait as normal
		if (Math.abs(rand.nextInt()) % 2 == 1) {
			print("Restaurant full - leave");
			state = State.doingNothing;
			event = Event.none;
			stateChanged();
		}
	}

	// From waiter this customer is assigned to
	public void msgSitAtTable(CwagonerWaiter w, int table, HashMap<String, Integer> menuOptions) {
		print("Received msgSitAtTable(" + w.getName() + ", table " + table + ")");
		
		waiter = w;
		menu = menuOptions;
		gui.setTableLocation(table);
		event = Event.followWaiter;
		stateChanged();
	}

	// From GUI - customer arrived at table
	public void msgGuiAtSeat() {
		// from animation
		event = Event.seated;
		stateChanged();
	}

	// From waiter - asking for order
	public void msgWhatDoYouWant() {
		print("Received msgWhatDoYouWant");
		
		event = Event.waiterAskedForOrder;
		stateChanged();
	}

	// From waiter - cook is out of chosen food
	public void msgPickSomethingElse(HashMap<String, Integer> newMenu) {
		print("Received msgPickSomethingElse");
		
		menu = newMenu;
		food = "";
		
		// If NO food left at all, leave
		if (menu.size() == 0) {
			print("Completely out of food - leave");
			state = State.eating;
			event = Event.doneEating;
		}
		// Some food left
		else {
			// 50% chance of being stubborn and leaving
			if (Math.abs(rand.nextInt()) % 2 == 1) {
				print("Out of my choice - leave");
				food = ""; // So cashier doesn't charge customer
				gui.clearFood();
				state = State.eating;
				event = Event.doneEating;
			}
			// Otherwise order something else
			else {
				print("Out of my choice - order again");
				food = "";
				state = State.goingToSeat;
				event = Event.seated;
			}
		}
		
		gui.clearFood();
		stateChanged();
	}

	// From waiter - delivering food
	public void msgHeresYourFood() {
		print("Received msgHeresYourFood");
		
		event = Event.foodDelivered;
		stateChanged();
	}

	// From waiter - cashier told waiter bill is ready
	public void msgAcknowledgeLeaving() {
		print("Received msgAcknowledgeLeaving");
		
		event = Event.checkReady;
		stateChanged();
	}

	// From GUI - customer arrived at cashier
	public void msgGuiAtCashier() {
		print("Received msgGuiAtCashier");
		
		event = Event.arrivedAtCashier;
		stateChanged();
	}

	// From cashier - telling this customer their total bill
	public void msgYourTotalIs(double total) {
		print("Received YourTotalIs(" + total + ")");
		
		event = Event.checkGiven;
		moneyOwed += total;
		stateChanged();
	}
	
	// From cashier - if this customer payed with exact change
	public void msgThankYou() {
		moneyOwed = 0.0;
		event = Event.cashierAccepted;
		stateChanged();
	}
	
	// From cashier - if this customer overpayed
	public void msgHeresYourChange(double changeDue) {
		myMoney += changeDue;
		moneyOwed = 0.0;
		event = Event.cashierAccepted;
		stateChanged();
	}

	// From cashier - if this customer didn't pay enough
	public void msgYouOwe(double remainingTotal) {
		moneyOwed = remainingTotal;
		event = Event.cashierAccepted;
		stateChanged();
	}

	// From GUI - customer in "waiting position"
	public void msgGuiLeftRestaurant() {
		// from animation
		event = Event.doneLeaving;
		stateChanged();
	}


	// SCHEDULER

	public boolean pickAndExecuteAnAction() {
		
		// Tell host of presence
		if (state.equals(State.inRestaurant)) {
			AlertHost();
			return true;
		}
		
		// After being assigned a waiter, follow waiter to table
		if (state.equals(State.waitingToBeSeated)
			&& event.equals(Event.followWaiter)) {
			SitDown();
			return true;
		}
		
		// Arrive at table; sit down, look over menu
		if (state.equals(State.goingToSeat)
			&& event.equals(Event.seated)) {
			DecideWhatToEat();
			return true;
		}
		
		// Decided on order. Tell waiter
		if (state.equals(State.lookingAtMenu)
			&& event.equals(Event.decidedOnFood)) {
			AlertWaiter();
			return true;
		}
		
		// Waiter at table. Give waiter order
		if (state.equals(State.readyToOrder)
			&& event.equals(Event.waiterAskedForOrder)) {
			Order();
			return true;
		}
		
		// Food delivered to table; eat
		if (state.equals(State.ordered)
			&& event.equals(Event.foodDelivered)) {
			EatFood();
			return true;
		}

		// Finish eating, and ask for check
		if (state.equals(State.eating)
			&& event.equals(Event.doneEating)) {
			TellWaiterLeaving();
			return true;
		}
			
		// Check arrives; leave table to pay
		if (state.equals(State.askedForCheck)
			&& event.equals(Event.checkReady)) {
			GoToCashier();
			return true;
		}
		
		// Get to cashier
		if (state.equals(State.goingToCashier)
			&& event.equals(Event.arrivedAtCashier)) {
			TellCashierReady();
			stateChanged();
		}
		
		// Pay after given bill
		if (state.equals(State.waitingAtCashier)
			&& event.equals(Event.checkGiven)) {
			PayCashier();
			return true;
		}
		
		// Pay, leave restaurant
		if (state.equals(State.paid)
			&& event.equals(Event.cashierAccepted)) {
			Leave();
			return true;
		}
		
		// Finish leaving; back to doing nothing
		if (state.equals(State.leaving)
				&& event.equals(Event.doneLeaving)) {
			state = State.doingNothing;
			return true;
		}
		
		return false;
	}


	// ACTIONS
	
	private void AlertHost() {
		print("AlertHost() - sending msgIWantFood");
		state = State.waitingToBeSeated;
		animationPanel.host.msgIWantFood(this);
	}

	private void SitDown() {
		print("SitDown()");
		
		gui.DoGoToSeat(1);
		state = State.goingToSeat;
	}
	
	public void DecideWhatToEat() {
		print("DecideWhatToEat()");

		state = State.lookingAtMenu;
		
		// Arbitrary decision to take hungerLevel seconds to decide what to order
		customerTimer.schedule(new TimerTask() {
			public void run() {
				List<String> usableMenu = new ArrayList<String>();
				food = "";
				
				for (String food : menu.keySet()) {
					if (menu.get(food) <= myMoney) {
						usableMenu.add(food);
					}
				}
				
				if (usableMenu.size() != 0) {
					food = usableMenu.get(Math.abs(rand.nextInt()) % usableMenu.size());
					gui.showFood("  !");
					event = Event.decidedOnFood;
				}
				else {
					// Can't afford anything
					// 50% chance of ordering anyway (order anything on menu)
					if (Math.abs(rand.nextInt()) % 2 == 1) {
						print("Can't afford anything - order anyway");
						usableMenu.clear();
						for (String food : menu.keySet()) {
							usableMenu.add(food);
						}
						food = usableMenu.get(Math.abs(rand.nextInt()) % usableMenu.size());
						gui.showFood("  !");
						event = Event.decidedOnFood;
					}
					// Otherwise leave restaurant
					else {
						print("Can't afford anything - leave");
						state = State.eating;
						event = Event.doneEating;
					}
				}
				
				stateChanged();
			}
		}, hungerLevel * 1000);
	}
	
	public void AlertWaiter() {
		print("AlertWaiter() - sending msgReadyToOrder");

		state = State.readyToOrder;
		waiter.msgReadyToOrder(this);
	}
	
	public void Order() {
		print("Order() - sending msgHeresMyOrder");

		state = State.ordered;
		waiter.msgHeresMyOrder(this, food);
		event = Event.gaveOrderToWaiter;

		gui.showFood(food.substring(0, 2) + "?");
		stateChanged();
	}

	private void EatFood() {
		print("EatFood()");

		state = State.eating;
		
		gui.showFood(food.substring(0, 2));
		
		customerTimer.schedule(new TimerTask() {
			public void run() {
				event = Event.doneEating;
				stateChanged();
			}
		}, hungerLevel * 1000);
	}

	private void TellWaiterLeaving() {
		print("TellWaiterLeaving()");
		
		gui.showFood(" $");
		state = State.askedForCheck;
		waiter.msgLeavingTable(this);
		// stateChanged() called in msgAcknowledgeLeaving()
		// allows waiter time to give bill to cashier
	}
	
	private void GoToCashier() {
		print("GoToCashier()");

		gui.DoGoToCashier();
		state = State.goingToCashier;
	}
	
	private void TellCashierReady() {
		print("TellCashierReady()");

		state = State.waitingAtCashier;
		animationPanel.cashier.msgReadyToPay(this);
	}
	
	private void PayCashier() {
		print("PayCashier()");
		
		double amountPaid = 0.0;
		
		if (myMoney >= moneyOwed) {
			amountPaid = moneyOwed;
			myMoney -= moneyOwed;
		}
		else {
			amountPaid = myMoney;
			myMoney = 0;
		}
		
		state = State.paid;
		animationPanel.cashier.msgPayment(this, amountPaid);
		stateChanged();
	}
	
	private void Leave() {
		print("Leave()");

		state = State.leaving;
		gui.clearFood();
		gui.DoExitRestaurant();
	}


	// Accessors, etc.

	
	public String getName() {
		return "CwagonerCustomer " + mPerson.getName();
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int h) {
		hungerLevel = h;
		// could be a state change. Maybe you don't
		// need to eat until hunger lever is > 5?
	}

	public void setGui(CwagonerCustomerGui g) {
		gui = g;
	}

	public CwagonerCustomerGui getGui() {
		return gui;
	}

	public Dimension getPosition() {
		return gui.getPosition();
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(1);
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.R1);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R1);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R1, e);
	}
}

