package restaurant.restaurant_tranac.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_tranac.TranacCheck;
import restaurant.restaurant_tranac.TranacMenu;
import restaurant.restaurant_tranac.gui.TranacCashierGui;
import restaurant.restaurant_tranac.interfaces.TranacCashier;
import restaurant.restaurant_tranac.interfaces.TranacCustomer;
import restaurant.restaurant_tranac.interfaces.TranacMarket;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;
import base.BaseRole;
import base.ContactList;
import base.Location;

/**
 * Restaurant Cook Agent
 */
public class TranacRestaurantCashierRole extends BaseRole implements TranacCashier {
	private TranacCashierGui cashierGui;
	private TranacMenu menu = new TranacMenu();
	public List<MyCheck> checks = Collections.synchronizedList(new ArrayList<MyCheck>());
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	//public double money;	//ANGELICA: switch to bank ssn?
	
	public enum CheckStatus {Pending, Computed, Paying, Finished, Unfulfilled};
	public enum BillStatus {Pending, Outstanding, Fulfilled};
	
	public TranacRestaurantCashierRole() {
		super(null);
	//	money = 5000;	//ANGELICA: no longer initialize
	}

	/** Messages */

	public void msgComputeCheck(TranacWaiter w, TranacCustomer c, String item) {
		synchronized(checks) {
			checks.add(new MyCheck(w,c,item, getSSN()));
		}
		stateChanged();
	}

	public void msgHereIsPayment(TranacCustomer c, double p) {
		synchronized(checks) {
			for(MyCheck check : checks) {
				if(check.c.getCustomer() == c) {
					check.s = CheckStatus.Paying;
					check.c.setPayment(p);
					stateChanged();
				}
			}
		}
	}
	
	public void msgHereIsBill(TranacMarket m, String i, double c) {
		synchronized(bills) {
			bills.add(new Bill(m,i,c));
		}
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
//	protected boolean pickAndExecuteAnAction() {
	public boolean pickAndExecuteAnAction() {
		synchronized(checks) {
			for(MyCheck c : checks) {
				if(c.s == CheckStatus.Pending) {
					computeCheck(c);
					return true;
				}
			}
		}
		synchronized(checks) {
			for(MyCheck c : checks) {
				if(c.s == CheckStatus.Paying) {
					payCheck(c);
					return true;
				}
			}
		}
/*		synchronized(bills) {
			for(Bill b : bills) {
				if(b.s == BillStatus.Outstanding) {
					tryToFulfillBill(b);
				}
			}
		}
		synchronized(bills) {
			for(Bill b : bills) {
				if(b.s == BillStatus.Pending) {
					tryToPayBill(b);
					return true;
				}
			}
		}
*/		return false;
	}

	/** Actions */

	private void computeCheck(MyCheck chk) {
		TranacCheck c = chk.c;
		Do("Computing check.");
		chk.s = CheckStatus.Computed;
		c.setAmount(menu.getCost(c.getItem()));
		
		//check if customer has outstanding balance; add to current bill
		for(MyCheck check : checks) {
			if(check.c.getCustomer() == c.getCustomer() && check.s == CheckStatus.Unfulfilled) {
				c.addOutstandingBill(check.c.getAmount());
				check.s = CheckStatus.Finished;
				break;
			}
		}
		
		Do("Total check amount: " + c.getAmount());
		
		c.getWaiter().msgHereIsCheck(c);
	}
	
	private void payCheck(MyCheck c) {
		double p = c.c.getPayment();
		double a = c.c.getAmount();
		TranacCustomer customer = c.c.getCustomer();
		Do("Recieved payment of " + p + " for " + a);
		
		//check if customer paid the entire bill
		if(p >= a) {
	//		money += p;
			c.c.setChange(p-a);
			c.s = CheckStatus.Finished;
			Do("Thank you and come again!");
			customer.msgHereIsChange(c.c);
		}
		//if he didn't, make the check unfulfilled and let the customer go
		else {
			c.s = CheckStatus.Unfulfilled;
			Do("Lazy bum! You'll pay next time.");
			customer.msgPayNextTime();
		}
	}
	/*
	private void tryToFulfillBill(Bill b) {
		Do("Trying to fulfill outstanding bill.");
		if(money >= b.cost) {
			payBill(b);
		}
		else {
			Do("It will have to wait...");
		}
	}
	
	private void tryToPayBill(Bill b) {
		Do("Trying to pay market bill of " + b.cost + " for " + b.item);
		if(money < b.cost) {
			Do("Our restaruant can't afford it...");
			b.s = BillStatus.Outstanding;
			b.market.msgWillPaySoon(b.item, b.cost);
		}
		else
			payBill(b);
	}

	private void payBill(Bill b) {
		Do("Paying bill of " + b.cost + " for " + b.item);
		money -= b.cost;
		b.s = BillStatus.Fulfilled;
		b.market.msgHereIsPayment(b.item, b.cost);
	}
	*/
	/** Utilities */

	public String getName() {
		return mPerson.getName();
	}
	
	public void setGui(TranacCashierGui c) {
		this.cashierGui = c;
	}
	
	public TranacCashierGui getCashierGui() {
		return cashierGui;
	}
	
	public int getNumChecks() {
		return checks.size();
	}
	
	public int getNumBills() {
		return bills.size();
	}

	/** Classes */
	
	public class MyCheck {
		TranacCheck c;
		CheckStatus s;
		
		MyCheck(TranacWaiter w, TranacCustomer c, String i, int n) {
			this.c = new TranacCheck(w,c,i,n);
			s = CheckStatus.Pending;
		}
		
		public CheckStatus getStatus() {
			return s;
		}
	}
	
	public class Bill {
		String item;
		double cost;
		BillStatus s;
		TranacMarket market;
		
		Bill(TranacMarket m, String i, double c) {
			item = i;
			market = m;
			cost = c;
			s = BillStatus.Pending;
		}
		
		public BillStatus getStatus() {
			return s;
		}
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(6);
	}
}

