package restaurant_tranac.agents;

import restaurant_tranac.Check;
import restaurant_tranac.Menu;
import restaurant_tranac.agent.Agent;
import restaurant_tranac.gui.CashierGui;
import restaurant_tranac.interfaces.*;

import java.util.*;

/**
 * Restaurant Cook Agent
 */
public class CashierAgent extends Agent implements Cashier {
	
	private String name;
	private CashierGui cashierGui;
	private Menu menu = new Menu();
	public List<MyCheck> checks = Collections.synchronizedList(new ArrayList<MyCheck>());
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	public double money;
	
	public enum CheckStatus {Pending, Computed, Paying, Finished, Unfulfilled};
	public enum BillStatus {Pending, Outstanding, Fulfilled};
	
	public CashierAgent(String n) {
		super();
		name = n;
		money = 5000;
	}

	/** Messages */

	public void msgComputeCheck(Waiter w, Customer c, String item) {
		synchronized(checks) {
			checks.add(new MyCheck(w,c,item));
		}
		stateChanged();
	}

	public void msgHereIsPayment(Customer c, double p) {
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
	
	public void msgHereIsBill(Market m, String i, double c) {
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
		synchronized(bills) {
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
		return false;
	}

	/** Actions */

	private void computeCheck(MyCheck chk) {
		Check c = chk.c;
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
		Customer customer = c.c.getCustomer();
		Do("Recieved payment of " + p + " for " + a);
		
		//check if customer paid the entire bill
		if(p >= a) {
			money += p;
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
	
	/** Utilities */

	public String getName() {
		return name;
	}

	public void setGui(CashierGui c) {
		this.cashierGui = c;
	}
	
	public CashierGui getCashierGui() {
		return cashierGui;
	}
	
	public int getNumChecks() {
		return checks.size();
	}
	
	public int getNumBills() {
		return bills.size();
	}

	public void setMoney(double m) {
		money = m;
	}
	/** Classes */
	
	public class MyCheck {
		Check c;
		CheckStatus s;
		
		MyCheck(Waiter w, Customer c, String i) {
			this.c = new Check(w,c,i);
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
		Market market;
		
		Bill(Market m, String i, double c) {
			item = i;
			market = m;
			cost = c;
			s = BillStatus.Pending;
		}
		
		public BillStatus getStatus() {
			return s;
		}
	}
}

