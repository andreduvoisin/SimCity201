package restaurant.restaurant_tranac.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.restaurant_tranac.TranacCheck;
import restaurant.restaurant_tranac.TranacMenu;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.gui.TranacCustomerGui;
import restaurant.restaurant_tranac.interfaces.TranacCashier;
import restaurant.restaurant_tranac.interfaces.TranacCustomer;
import restaurant.restaurant_tranac.interfaces.TranacHost;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

/**
 * Restaurant customer agent.
 */
public class TranacCustomerRole extends BaseRole implements TranacCustomer {
	private TranacCustomerGui customerGui;
	private int hungerLevel = 10; // determines length of meal
	private final int baseMoney = 30;
	private String choice;
	private Timer timer = new Timer(); // used to time eating
	private Random rGenerator = new Random(); // used to select meal
	private TranacMenu menu = null;
	private TranacCheck check = null;
	private double money;
	private int num;

	private boolean flake = false;
	private boolean willWait;
	private boolean choiceFound = true;
	private List<String> pastChoices = new ArrayList<String>();

	// agent correspondents
	private TranacHost host;
	private TranacWaiter waiter;
	private TranacCashier cashier;

	public enum AgentState {
		DoingNothing, WaitingInRestaurant, LeavingRestaurantEarly, BeingSeated, Ordering, Ordered, ChoosingToLeave, Eating, DoneEating, WaitingForCheck, GoingToCashier, Paying, Leaving, GoingToRestaurant, WaitingAtHost, GoingToWaitingArea
	};

	public enum AgentEvent {
		none, gotHungry, willWait, wantToLeave, followWaiter, seated, askedForOrder, outOfChoice, noAvailableFood, decidedToLeave, givenFood, doneEating, gotCheck, atCashier, paid, doneLeaving, decidedToStay, foodTooExpensive, atHost, acknowledgeByHost, atWaitingArea
	};

	private AgentState state = AgentState.DoingNothing; // initial state
	private AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 * 
	 * @param name
	 *            name of the customer
	 * @param gui
	 *            reference to the customerGui so the customer can send it
	 *            messages
	 */
	public TranacCustomerRole() {
		super(null);
		customerGui = new TranacCustomerGui(this);
		TranacRestaurant.addPerson(this);
		TranacRestaurant.addGui(customerGui);
		num = 0;

		// randomly chooses waiting unless set later
		if (rGenerator.nextInt() % 2 == 0)
			willWait = false;
		else
			willWait = true;
	}

	public TranacCustomerRole(Person mPerson) {
		super(mPerson);
		money = baseMoney;
		num = 0;

		customerGui = new TranacCustomerGui(this);
		TranacRestaurant.addGui(customerGui);
		// randomly chooses waiting unless set later
		if (rGenerator.nextInt() % 2 == 0)
			willWait = false;
		else
			willWait = true;
	}

	/** Messages */
	public void msgGotHungry() {
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgPleaseWaitHere(int n) {
		num = n;
		event = AgentEvent.acknowledgeByHost;
		stateChanged();
	}

	public void msgRestaurantFull() {
		if (!willWait) {
			event = AgentEvent.wantToLeave;
		} else {
			event = AgentEvent.willWait;
		}
		stateChanged();
	}

	public void msgFollowMe(TranacMenu m, TranacWaiter w) {
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

	public void msgHereIsCheck(TranacCheck c) {
		check = c;
		event = AgentEvent.gotCheck;
		stateChanged();
	}

	public void msgHereIsChange(TranacCheck c) {
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
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		// CustomerAgent is a finite state machine
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry) {
			state = AgentState.GoingToRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.GoingToRestaurant && event == AgentEvent.atHost) {
			state = AgentState.WaitingAtHost;
			tellHostAtRestaurant();
			return true;
		}
		if (state == AgentState.WaitingAtHost
				&& event == AgentEvent.acknowledgeByHost) {
			state = AgentState.GoingToWaitingArea;
			goToWaitingArea();
			return true;
		}
		if (state == AgentState.WaitingAtHost && event == AgentEvent.willWait) {
			tellHostWillWait();
			return true;
		}
		if (state == AgentState.WaitingAtHost
				&& event == AgentEvent.wantToLeave) {
			state = AgentState.Leaving;
			leaveRestaurantEarly();
			return true;
		}
		if (state == AgentState.GoingToWaitingArea
				&& event == AgentEvent.atWaitingArea) {
			state = AgentState.WaitingInRestaurant;
			// no action; wait for host
			return true;
		}
		if (state == AgentState.WaitingInRestaurant
				&& event == AgentEvent.followWaiter) {
			state = AgentState.BeingSeated;
			sitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated) {
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
		if (state == AgentState.Ordering
				&& event == AgentEvent.foodTooExpensive) {
			state = AgentState.ChoosingToLeave;
			chooseToStay();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.outOfChoice) {
			state = AgentState.Ordering;
			chooseOrder();
			return true;
		}
		if (state == AgentState.ChoosingToLeave
				&& event == AgentEvent.decidedToLeave) {
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.ChoosingToLeave
				&& event == AgentEvent.decidedToStay) {
			state = AgentState.Ordered;
			orderFood();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.givenFood) {
			state = AgentState.Eating;
			eatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			state = AgentState.WaitingForCheck;
			askForCheck();
			return true;
		}
		if (state == AgentState.WaitingForCheck && event == AgentEvent.gotCheck) {
			state = AgentState.GoingToCashier;
			DoGoToCashier(); // animation call
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
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			// no action; return to idle state so customer can become hungry
			// again
			return true;
		}
		return false;
	}

	/** Actions */

	private void goToRestaurant() {
	//	print("Going to restaurant.");
		DoGoToHost();
	}

	private void tellHostAtRestaurant() {
	//	print("Helloooo.");
		host.msgIWantFood(this);
	}

	private void goToWaitingArea() {
		print("Going to waiting area " + num);
		DoGoToWaitingArea();
		host.msgAtWaitingArea(this);
	}

	private void tellHostWillWait() {
		print("Telling host I will wait at restaurant.");
		event = AgentEvent.none;
		host.msgWillWait(this);
	}

	private void leaveRestaurantEarly() {
		print("Telling host I'm leaving.");
		host.msgLeavingEarly(this);
		DoExitRestaurant();
	}

	private void sitDown() {
		print("Being seated. Going to table.");
		DoGoToTable();
	}

	private void chooseOrder() {
		print("Choosing order.");
		choiceFound = false;
		boolean poor = false;

		// check if poor
		if (money <= menu.getCheapestItem())
			poor = true;

		while (!choiceFound) {

			if (pastChoices.size() == menu.getSize()) {
				print("No more choices available...");
				event = AgentEvent.noAvailableFood;
				return;
			}

			int c = Math.abs(rGenerator.nextInt() % 4);

			choice = menu.getChoice(c);
			print("Want " + choice);

			double p = menu.getCost(choice);

			if (p <= money) {
				for (String pc : pastChoices) {
					if (choice.equals(pc)) {
						if (poor && p == menu.getCheapestItem()) {
							event = AgentEvent.foodTooExpensive;
							return;
						}
						choiceFound = false;
						break;
					}
				}
				choiceFound = true;
			} else {
				pastChoices.add(choice); // add item to list of unavailable
				if (pastChoices.size() == menu.getSize()) { // if it was the
															// last available
															// item,
					event = AgentEvent.foodTooExpensive; // decide to pay or to
															// leave
					return;
				}
			}
		}

		customerGui.setAlerting();
		waiter.msgReadyToOrder(this);

	}

	private void chooseToStay() {
		print("Deciding to stay (and be a flake).");
		if (flake)
			event = AgentEvent.decidedToStay;
		else
			event = AgentEvent.decidedToLeave;
	}

	private void orderFood() {
		print("Ordering " + choice + ".");
		customerGui.setFood(choice); // create food icon
		customerGui.setOrdering();
		waiter.msgReceivedOrder(this, choice);
	}

	private void eatFood() {
		print("Eating food.");
		customerGui.setEating(); // removes the question mark from food icon
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating.");
				event = AgentEvent.doneEating;
				stateChanged();
			}
		}, getHungerLevel() * 1000); // how long to wait before running task
	}

	private void askForCheck() {
		print("Asking for check.");
		customerGui.setAlerting();
		waiter.msgAskingForCheck(this);
	}

	private void payCheck() {
		print("Paying check of " + check.getAmount());
		// check if customer can afford payment. if he can't, he must be a flake
		if (check.getAmount() <= money) {
			money -= check.getAmount();
			ContactList
					.SendPayment(getSSN(), check.getSsn(), check.getAmount());
			cashier.msgHereIsPayment(this, check.getAmount());
		} else {
			print("Flaking like a boss.");
			cashier.msgHereIsPayment(this, 0);
		}
	}

	private void leaveRestaurant() {
		print("Leaving.");

		// check change from check
		if (check != null) {
			print("Adding change " + check.getChange() + " to money.");
			money += check.getChange();
		}

		// clear past choices for future visits
		pastChoices.clear();
		check = null;
		waiter.msgDoneEating(this);
		customerGui.setClear();
		DoExitRestaurant();
		
		mPerson.msgRoleFinished();
		mPerson.assignNextEvent();
	}

	/** Animation Actions */
	private void DoGoToHost() {
		customerGui.DoGoToHost();
	}

	private void DoGoToWaitingArea() {
		customerGui.DoGoToWaitingArea(num);
	}

	private void DoGoToTable() {
		// send second flag to customerGui
	}

	private void DoGoToCashier() {
		customerGui.setPaying(); // remove food icon
		customerGui.DoGoToCashier();
	}

	private void DoExitRestaurant() {
		customerGui.DoExitRestaurant();
	}

	/** Utilities */

	public String getName() {
		return mPerson.getName();
	}

	public void setHost(TranacHost h) {
		host = h;
	}

	public void setWaiter(TranacWaiter w) {
		waiter = w;
	}

	public void setCashier(TranacCashier c) {
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

	public void setGui(TranacCustomerGui g) {
		customerGui = g;
	}

	public TranacCustomerGui getGui() {
		return customerGui;
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(6);
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.R6);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R6);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R6, e);
	}
}
