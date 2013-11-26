package restaurant.restaurant_davidmca.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.restaurant_davidmca.Check;
import restaurant.restaurant_davidmca.Menu;
import restaurant.restaurant_davidmca.Table;
import restaurant.restaurant_davidmca.gui.CustomerGui;
import restaurant.restaurant_davidmca.interfaces.Cashier;
import restaurant.restaurant_davidmca.interfaces.Customer;
import restaurant.restaurant_davidmca.interfaces.Host;
import restaurant.restaurant_davidmca.interfaces.Waiter;
import base.BaseRole;
import base.PersonAgent;
import base.interfaces.Person;

/**
 * Restaurant customer restaurant_davidmca.agent.
 */
public class DavidCustomerRole extends BaseRole implements Customer {
	private String name;
	private int hungerLevel = 4000; // determines length of meal
	private double mymoney = 0;
	private double debt = 0;
	private Check mycheck = null;
	Timer timer = new Timer();
	private CustomerGui customerGui;

	// restaurant_davidmca.agent correspondents
	private Waiter waiter;
	private Host host;
	private Table table;
	private Menu menu;
	private Cashier cash;

	private String mychoice;
	private boolean availability;

	// private Semaphore isAnimating = new Semaphore(1, true);

	// public void acquireAnimationSemaphore() {
	// try {
	// isAnimating.acquire();
	// System.out.println("isAnimating acquired in DavidCustomerRole");
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// if (isAnimating.availablePermits() == 0) {
	// isAnimating.release();
	// }
	// }

	// private boolean isHungry = false; //hack for gui
	public enum AgentState {
		DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ChoosingFood, Eating, Paying, Leaving
	};

	private AgentState state = AgentState.DoingNothing;// The start state

	public enum AgentEvent {
		none, gotHungry, arrived, followWaiter, seated, pickFood, Waiting, startEating, doneEating, requestCheck, AskedForCheck, Paying, donePaying, ReadyToLeave, doneLeaving
	};

	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 * 
	 * @param name
	 *            name of the customer
	 */
	public DavidCustomerRole(Person person) {
		super(person);
		this.name = person.getName();
		this.availability = true;
	}

	@Override
	public void setHost(Host host) {
		this.host = host;
	}

	@Override
	public double getMoney() {
		return this.mymoney;
	}

	@Override
	public String getCustomerName() {
		return name;
	}

	// Messages

	public void msgAvailability(boolean availability) {
		this.availability = availability;
		stateChanged();
	}

	@Override
	public void msgChange(Double change) {
		mymoney += change;
		event = AgentEvent.ReadyToLeave;
		print("Got change, my balance is: " + mymoney);
		if (mymoney < 0) {
			debt = mymoney;
			print("Had some debt, will pay next time");
		}
		stateChanged();
	}

	@Override
	public void msgHereIsCheck(Check chk) {
		print("Received check");
		mycheck = chk;
		event = AgentEvent.Paying;
		stateChanged();
	}

	@Override
	public void gotHungry() {// from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// from animation
		event = AgentEvent.seated;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedGoToWaitingArea() {
		event = AgentEvent.arrived;
		stateChanged();
	}

	@Override
	public void msgFollowMe(Waiter w, Table t) {
		waiter = w;
		table = t;
		event = AgentEvent.followWaiter;
		stateChanged();
	}

	@Override
	public void msgWhatWouldYouLike(Menu m) {
		menu = m;
		event = AgentEvent.pickFood;
		stateChanged();
	}

	@Override
	public void msgHereIsYourOrder() {
		event = AgentEvent.startEating;
		stateChanged();
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		// CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry) {
			state = AgentState.WaitingInRestaurant;
			CheckAvailability();
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant
				&& event == AgentEvent.followWaiter) {
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated) {
			state = AgentState.Seated;
			callWaiter();
			return true;
		}
		if (state == AgentState.Seated && event == AgentEvent.pickFood) {
			state = AgentState.Seated;
			chooseFood();
			return true;
		}

		if (state == AgentState.Seated && event == AgentEvent.startEating) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}

		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			state = AgentState.Paying;
			RequestCheck();
			return true;
		}

		if (state == AgentState.Paying && event == AgentEvent.Paying) {
			DoneAndPaying();
			return true;
		}

		if (state == AgentState.Paying && event == AgentEvent.ReadyToLeave) {
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}

		if (state == AgentState.Seated && event == AgentEvent.ReadyToLeave) {
			// customer leaving because they couldn't afford anything
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}

		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			// no action
			return true;
		}
		return false;
	}

	// Actions

	private void CheckAvailability() {
		host.msgCheckAvailability(this);
	}

	private void goToRestaurant() {
		customerGui.DoGoToWaitingArea();
		if (this.getName().startsWith("hack")) {
			String moneyhack = this.getName().substring(4,
					this.getName().length());
			this.mymoney += Double.parseDouble(moneyhack);
			print("Added $" + moneyhack + " to wallet");
		} else {
			this.mymoney += 40;
			print("Added $40 to wallet");
		}
		if (debt < 0) {
			print("Paying debt");
			cash.msgDebtPayment(debt);
			mymoney += debt;
		}
		if (availability) {
			host.msgIWantFood(this);
			Do("Going to restaurant_davidmca");
		} else if (!availability) {
			Random rand = new Random();
			int randvalue = rand.nextInt(1000);
			int stay = randvalue % 2;
			switch (stay) {
			case 0:
				Do("Restaurant full, decided to go anyway");
				host.msgIWantFood(this);
				Do("Going to restaurant_davidmca");
				break;
			case 1:
				Do("Restaurant full, decided to leave");
				event = AgentEvent.ReadyToLeave;
				customerGui.DoExitRestaurant();
				((PersonAgent) mPerson).msgRoleFinished();
				((PersonAgent) mPerson).msgRoleInactive();
				break;
			}
		}
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(table.getX(), table.getY());
	}

	private void callWaiter() {
		print("Ready to order");
		waiter.msgReadyToOrder(this);
	}

	private void chooseFood() {
		List<String> canAfford = Collections
				.synchronizedList(new ArrayList<String>());
		List<String> entrees = Collections
				.synchronizedList(new ArrayList<String>());
		entrees = menu.getItemList();
		synchronized (entrees) {
			for (String entree : entrees) {
				if (menu.getPrice(entree) <= mymoney) {
					canAfford.add(entree);
				}
			}
		}
		if (canAfford.size() > 0) {
			Random generator = new Random();
			int choice = generator.nextInt(canAfford.size());
			mychoice = canAfford.get(choice);
			customerGui.setLabelText(mychoice + "?");
			waiter.msgHereIsMyOrder(this, mychoice);
			event = AgentEvent.Waiting;
		} else {
			print("Cannot afford any items");
			int decide;
			if (menu.isReOrder() == true) {
				decide = 0;
			} else {
				Random rand2 = new Random();
				int randvalue = rand2.nextInt(1000);
				decide = randvalue % 2;
			}
			switch (decide) {
			case 0:
				print("Decided to leave");
				event = AgentEvent.ReadyToLeave;
				break;
			case 1:
				print("Order Anyway");
				mychoice = menu.pickItem();
				customerGui.setLabelText(mychoice + "?");
				waiter.msgHereIsMyOrder(this, mychoice);
				event = AgentEvent.Waiting;
				break;
			}
		}
	}

	private void EatFood() {
		Do("Eating Food: " + mychoice);
		customerGui.setLabelText(mychoice);
		timer.schedule(new TimerTask() {
			Object cookie = 1;

			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				// isHungry = false;
				stateChanged();
			}
		}, getHungerLevel());// how long to wait before running task
	}

	private void RequestCheck() {
		print("Requested Check");
		waiter.msgReadyForCheck(this, mychoice);
		event = AgentEvent.AskedForCheck;
		stateChanged();
	}

	private void DoneAndPaying() {
		print("Done and Paying");
		cash.msgPayment(mycheck, mymoney);
		mymoney = 0;
		waiter.msgDoneAndPaying(this);
		event = AgentEvent.donePaying;
	}

	private void leaveTable() {
		customerGui.setLabelText("");
		Do("Leaving.");
		waiter.msgDoneEating(this);
		customerGui.DoExitRestaurant();
		stateChanged();
	}

	// Accessors, etc.

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getHungerLevel() {
		return hungerLevel;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	// @Override
	// public String toString() {
	// return "customer " + getName();
	// }

	@Override
	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	@Override
	public CustomerGui getGui() {
		return customerGui;
	}

	@Override
	public void setCashier(Cashier ca) {
		this.cash = ca;
	}

}
