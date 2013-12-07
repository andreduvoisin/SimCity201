package restaurant.restaurant_duvoisin.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.restaurant_duvoisin.MarketPrices;
import restaurant.restaurant_duvoisin.Menu;
import restaurant.restaurant_duvoisin.interfaces.Cashier;
import restaurant.restaurant_duvoisin.interfaces.Customer;
import restaurant.restaurant_duvoisin.interfaces.Market;
import restaurant.restaurant_duvoisin.interfaces.Waiter;
import restaurant.restaurant_duvoisin.test.mock.EventLog;
import restaurant.restaurant_duvoisin.test.mock.LoggedEvent;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;
import city.gui.trace.AlertTag;

/**
 * Restaurant Cashier Agent
 */
public class AndreCashierRole extends BaseRole implements Cashier {
	public double money = 15.00;
	
	public RestaurantCashierRole mRole;
	private String name;
	Boolean paused = false;
	public List<Check> openChecks = Collections.synchronizedList(new ArrayList<Check>());
	public List<MarketCheck> openMarketChecks = Collections.synchronizedList(new ArrayList<MarketCheck>());
	Menu menu = new Menu();
	public enum CheckState { Created, GivenToPeople, Paying };
	MarketPrices marketPrices = new MarketPrices();
	
	public EventLog log = new EventLog();

	public AndreCashierRole(Person mPerson, RestaurantCashierRole r) {
		super(mPerson);
		mRole = r;
		this.name = "AndreCashier";
	}
	
	public String getName() {
		return name;
	}

	// Messages
	
	public void msgComputeBill(Waiter w, Customer c, String choice) {
		print("msgComputeBill received");
		openChecks.add(new Check(w, c, choice, CheckState.Created));
		stateChanged();
	}
	
	public void msgPayment(Customer c, double amount) {
		print("msgPayment received");
		log.add(new LoggedEvent("msgPayment received"));
		synchronized(openChecks) {
			for(Check ch : openChecks)
				if(ch.customer == c) {
					ch.state = CheckState.Paying;
					ch.amountPayed = amount;
				}
		}
		stateChanged();
	}
	
	public void msgComputeMarketBill(Market m, String type, int amount) {
		print("msgComputeMarketBill received");
		log.add(new LoggedEvent("msgComputeMarketBill received"));
		openMarketChecks.add(new MarketCheck(m, type, amount));
		stateChanged();
	}
	
	public void msgPauseScheduler() {
		paused = true;
	}
	
	public void msgResumeScheduler() {
		paused = false;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(!paused) {
			synchronized(openChecks) {
				for(Check c : openChecks)
					if(c.state == CheckState.Created) {
						ComputeCheck(c);
						return true;
					}
			}
			synchronized(openChecks) {
				for(Check c : openChecks)
					if(c.state == CheckState.Paying) {
						MakeChange(c);
						return true;
					}
			}
			synchronized(openMarketChecks) {
				for(MarketCheck mc : openMarketChecks) {
					HandleMarketCheck(mc);
					return true;
				}
			}
		}
		return false;
	}

	// Actions
	void ComputeCheck(Check c) {
		print("Doing ComputeCheck");
		c.amountOwed = menu.menuItems.get(c.choice);
		c.waiter.msgHereIsCheck(c.customer, c.amountOwed);
		c.state = CheckState.GivenToPeople;
	}
	
	void MakeChange(Check c) {
		print("Doing MakeChange");
		c.change = c.amountPayed - c.amountOwed;
		c.customer.msgHereIsChange(c.change);
		openChecks.remove(c);
	}
	
	void HandleMarketCheck(MarketCheck mc) {
		print("Doing HandleMarketCheck");
		log.add(new LoggedEvent("Doing HandleMarketCheck"));
		mc.amountOwed = marketPrices.currentRate.get(mc.type) * mc.amount;
		if(mc.amountOwed <= money) {
			money -= mc.amountOwed;
			print("Paying " + mc.market);
			mc.market.msgFoodPayment(mc.type, mc.amountOwed);
			openMarketChecks.remove(mc);
		} else if(mc.amountOwed > money) {
			print("Not enough money to pay " + mc.market + ". Market has been paid remaining money = $" + money + ". Market will be paid rest of amount when enough money is gained from customer payments.");
			mc.market.msgNotEnoughMoney(mc.type, money);
			// mc.amountOwed -= money;
			money = 0;
			// yeah...
			openMarketChecks.remove(mc);
		}
	}

	// The animation DoXYZ() routines
	
	//utilities
	public class Check {
		Waiter waiter;
		public Customer customer;
		String choice;
		public double amountOwed;
		double amountPayed;
		public double change;
		public CheckState state;
		
		Check(Waiter w, Customer c, String ch, CheckState s) {
			waiter = w;
			customer = c;
			choice = ch;
			state = s;
		}
	}
	
	public class MarketCheck {
		Market market;
		String type;
		int amount;
		double amountOwed;
		
		MarketCheck(Market m, String t, int a) {
			market = m;
			type = t;
			amount = a;
		}
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(0);
	}
	

    public RestaurantCashierRole getIntermediateRole() {
    	return mRole;
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
