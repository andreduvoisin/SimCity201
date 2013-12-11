package restaurant.restaurant_davidmca.test.mock;

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

public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();
	public int total;

	public MockCustomer(String name) {
		super(name);
		total = 0;

	}

	@Override
	public void setHost(Host host) {

	}

	@Override
	public double getMoney() {
		return 0;
	}

	@Override
	public String getCustomerName() {
		return null;
	}

	@Override
	public void msgChange(Double change) {
		log.add(new LoggedEvent("Received msgChange from cashier. Change = "
				+ total));

	}

	@Override
	public void msgHereIsCheck(Check chk) {
		log.add(new LoggedEvent(
				"Received msgHereIsCheck from cashier. Total = " + total));
		if (getName() == "brokecustomer") {
			cashier.msgPayment(chk, 0.0);
			Timer timer = new Timer();
			final Check check = chk;
			timer.schedule(new TimerTask() {
				public void run() {
					cashier.msgDebtPayment(check.total);
				}
			}, 50);
		} else {
			cashier.msgPayment(chk, chk.total);
		}
	}

	@Override
	public void gotHungry() {

	}

	@Override
	public void msgAnimationFinishedGoToSeat() {

	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {

	}

	@Override
	public void msgFollowMe(Waiter w, Table t) {
		//System.out.println("Recieved msgFollowMe");
		log.add(new LoggedEvent("Recieved msgFollowMe"));

	}

	@Override
	public void msgWhatWouldYouLike(Menu m) {
		log.add(new LoggedEvent("Recieved msgWhatWouldYouLike"));
	}

	@Override
	public void msgHereIsYourOrder() {
		log.add(new LoggedEvent("Recieved msgHereIsYourOrder"));

	}

	@Override
	public int getHungerLevel() {
		return 0;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {

	}

	@Override
	public void setGui(CustomerGui g) {

	}

	@Override
	public CustomerGui getGui() {
		return null;
	}

	@Override
	public void setCashier(Cashier ca) {
		this.cashier = ca;

	}

	@Override
	public void msgAnimationFinishedGoToWaitingArea() {

	}

}
