package restaurant.restaurant_davidmca.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.restaurant_davidmca.Check;
import restaurant.restaurant_davidmca.interfaces.Cashier;
import restaurant.restaurant_davidmca.interfaces.Customer;
import restaurant.restaurant_davidmca.interfaces.Market;
import restaurant.restaurant_davidmca.interfaces.Waiter;
import restaurant.restaurant_davidmca.test.mock.EventLog;
import restaurant.restaurant_davidmca.test.mock.LoggedEvent;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;
import city.gui.trace.AlertTag;

/**
 * Restaurant customer restaurant_davidmca.agent.
 */
public class DavidCashierRole extends BaseRole implements Cashier {
	public RestaurantCashierRole mRole;
	
	private String name;
	public double totalCash;
	private double loans = 0;

	public class Invoice {
		Market m;
		public double total;

		public Invoice(Market mkt, double billtotal) {
			this.m = mkt;
			this.total = billtotal;
		}
	}

	public List<Check> pendingChecks = Collections
			.synchronizedList(new ArrayList<Check>());
	public List<Check> paidChecks = Collections
			.synchronizedList(new ArrayList<Check>());
	public List<Invoice> invoicesToPay = Collections
			.synchronizedList(new ArrayList<Invoice>());

	public EventLog log = new EventLog();

	// restaurant_davidmca.agent correspondents
	/**
	 * Constructor for CashierAgent class
	 * 
	 * @param name
	 *            name of the customer
	 */
	public DavidCashierRole(Person p, RestaurantCashierRole r) {
		super(p);
		mRole = r;
		this.name = "DavidCashier";
		totalCash = 100.00;
	}

	@Override
	public String getName() {
		return name;
	}

	// Messages

	@Override
	public void msgPayment(Check chk, double payment) {
		print("Received Payment");
		paidChecks.add(chk);
		chk.change = payment - chk.total;
		print("Paid " + payment + " for " + chk.total + ", change will be "
				+ chk.change);
		totalCash += chk.total;
		if (loans > 0) {
			loans -= Math.min(loans, totalCash);
			print("Payment on loan; ammount is now " + loans);
		}
		stateChanged();
	}

	@Override
	public void msgDebtPayment(double amt) {
		log.add(new LoggedEvent("msgDebtPayment occurred"));
		totalCash += amt;
		print("Recieved debt payment");
		stateChanged();
	}

	@Override
	public void msgComputeBill(Waiter w, Customer c, String choice) {
		print("Computing Bill");
		pendingChecks.add(new Check(w, c, choice));
		stateChanged();
	}

	@Override
	public void msgHereIsInvoice(Market marketAgent, double total) {
		print("Recieved invoice from " + marketAgent.getName());
		invoicesToPay.add(new Invoice(marketAgent, total));
		stateChanged();
	}

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		synchronized (pendingChecks) {
			for (Check chk : pendingChecks) {
				SendCheck(chk);
				return true;
			}
		}
		synchronized (paidChecks) {
			for (Check chk : paidChecks) {
				GiveChange(chk);
				return true;
			}
		}
		synchronized (invoicesToPay) {
			for (Invoice invoice : invoicesToPay) {
				PayInvoice(invoice);
				return true;
			}
		}
		return false;
	}

	// Actions

	private void PayInvoice(Invoice invoice) {
		totalCash -= invoice.total;
		if (totalCash < 0) {
			print("We don't have enough money to pay the bill.  The restaurant_davidmca is taking out a loan to cover the ammount");
			totalCash += 1000.00;
			loans += 1000.00;
		}
		print("Paid invoice.  Cash is now " + totalCash);
		invoice.m.msgPayInvoice(invoice.total);
		invoicesToPay.remove(invoice);
	}

	private void SendCheck(Check ch) {
		print("Returning check");
		ch.waiter.msgHereIsCheck(ch);
		pendingChecks.remove(ch);
	}

	private void GiveChange(Check ch) {
		print("Giving change");
		ch.cust.msgChange(ch.change);
		paidChecks.remove(ch);
	}

	@Override
	public List<Check> getChecks() {
		return this.pendingChecks;
	}

	public List<Check> getPaidChecks() {
		return this.paidChecks;
	}

	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(4);
	}

    public RestaurantCashierRole getIntermediateRole() {
    	return mRole;
    }

    public void Do(String msg) {
		super.Do(msg, AlertTag.R4);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R4);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R4, e);
	}
}
