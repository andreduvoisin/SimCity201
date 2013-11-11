package restaurant_davidmca.test.mock;

import java.util.Timer;
import java.util.TimerTask;

import restaurant_davidmca.Check;
import restaurant_davidmca.HostAgent;
import restaurant_davidmca.Menu;
import restaurant_davidmca.Table;
import restaurant_davidmca.CustomerAgent.AgentEvent;
import restaurant_davidmca.gui.CustomerGui;
import restaurant_davidmca.interfaces.Cashier;
import restaurant_davidmca.interfaces.Customer;
import restaurant_davidmca.interfaces.Waiter;

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
	public void setHost(HostAgent host) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getMoney() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
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
					cashier.msgDebtPayment(check.getTotal());
				}
			}, 50);
		} else {
			cashier.msgPayment(chk, chk.getTotal());
		}
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgFollowMe(Waiter w, Table t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgWhatWouldYouLike(Menu m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgHereIsYourOrder() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub

	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCashier(Cashier ca) {
		this.cashier = ca;

	}

	@Override
	public void msgAnimationFinishedGoToWaitingArea() {
		// TODO Auto-generated method stub

	}

}
