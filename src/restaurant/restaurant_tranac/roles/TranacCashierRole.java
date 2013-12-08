package restaurant.restaurant_tranac.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.restaurant_tranac.TranacCheck;
import restaurant.restaurant_tranac.TranacMenu;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.gui.TranacCashierGui;
import restaurant.restaurant_tranac.interfaces.TranacCashier;
import restaurant.restaurant_tranac.interfaces.TranacCustomer;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

/**
 * Restaurant Cashier Agent
 */
public class TranacCashierRole extends BaseRole implements TranacCashier {
	public RestaurantCashierRole mRole;
	private TranacCashierGui cashierGui;
	
	private TranacMenu menu = new TranacMenu();
	public List<MyCheck> checks = Collections.synchronizedList(new ArrayList<MyCheck>());
	
	public enum CheckStatus {Pending, Computed, Paying, Finished, Unfulfilled};
	
	public TranacCashierRole(Person person, RestaurantCashierRole r) {
		super(person);
		cashierGui = new TranacCashierGui(this);
		TranacRestaurant.addPerson(this);
		TranacRestaurant.addGui(cashierGui);
		mRole = r;
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
		return false;
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

    public RestaurantCashierRole getIntermediateRole() {
    	return mRole;
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

