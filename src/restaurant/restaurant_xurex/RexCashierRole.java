package restaurant.restaurant_xurex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.restaurant_xurex.interfaces.Cashier;
import restaurant.restaurant_xurex.interfaces.Customer;
import restaurant.restaurant_xurex.interfaces.Market;
import restaurant.restaurant_xurex.interfaces.Waiter;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;

/**
 * Restaurant Cashier Agent
 */

public class RexCashierRole extends BaseRole implements Cashier {
	public enum BillState
	{pendingWaiter, ignore, pendingCustomer}; 
	//ignore while bill is unpaid and after is paid
	
	private String name;
	private float assets = 100;
	public class Bill{
		Waiter waiter;
		public Customer customer;
		public float due;
		public float paid;
		public String order;
		public BillState state;
		Bill(Waiter waiter, Customer customer){
			this.waiter = waiter;
			this.customer = customer;
			due = paid = 0;
		}
		Bill(Waiter waiter, Customer customer, float due){
			this.waiter = waiter;
			this.customer = customer;
			this.due = due;
			paid = 0;
		}
	}
	//Public for use in test case
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	Map<String, Integer> menu = new HashMap<String, Integer>();
	Map<Market, Float> marketBills = new HashMap<Market, Float>();
	
	//CONSTRUCTORS //
	public RexCashierRole(Person person){
		super(person);
		menu.put("Steak", new Integer(16));
		menu.put("Chicken", new Integer(11));
		menu.put("Salad", new Integer(6));
		menu.put("Pizza", new Integer(9));
	}
	public RexCashierRole(){
		super(null);
		menu.put("Steak", new Integer(16));
		menu.put("Chicken", new Integer(11));
		menu.put("Salad", new Integer(6));
		menu.put("Pizza", new Integer(9));
	}

	// MESSAGES //
	public void ComputeBill(Waiter waiter, Customer customer){
		Bill bill = new Bill(waiter, customer);
		bill.order = customer.getChoice();
		bill.state = BillState.pendingWaiter;
		bills.add(bill);
		stateChanged();
	}
	public void IWantToPay(Customer customer, float cash){
		synchronized(bills){
			for (Bill iBill : bills){
				if(iBill.customer.equals(customer)){
					iBill.paid = cash;
					iBill.state = BillState.pendingCustomer;
				}
			}
		}
		stateChanged();
	}
	public void HereIsBill(Market market, float payment){
		marketBills.put(market, payment);
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (!marketBills.isEmpty()){
			for (Market market : marketBills.keySet()){
				ProcessMarketBill(market, marketBills.get(market));
				marketBills.remove(market);
				return true;
			}
		}
		synchronized(bills){
		for(Bill iBill : bills){
			if(iBill.state == BillState.pendingCustomer){
				ComputeChange(iBill);
				return true;
			}
		}}
		synchronized(bills){
		for(Bill iBill : bills){
			if(iBill.state == BillState.pendingWaiter){
				SendBill(iBill);
				return true;
			}
		}}
		return false;
	}

	// ACTIONS //
	private void ProcessMarketBill(Market market, float payment){
		if(assets<payment){
			Do("NOT ENOUGH MONEY IN CASHIER");
			market.HereIsPayment(0); return;
		}
		assets -= payment;
		market.HereIsPayment(payment);
	}
	private void SendBill(Bill bill){
		print("This is the order: "+bill.order);
		float due = menu.get(bill.order);
		bill.due  += due; //+= used for repeat customers
		bill.waiter.HereIsBill(bill.customer, bill.due);
		bill.state = BillState.ignore;
	}
	
	private void ComputeChange(Bill bill){
		Do(bill.customer.getName()+" payed "+bill.paid+" of a "+bill.due+" bill");
		if(bill.paid<=bill.due){
			bill.due -= bill.paid;
			bill.paid = 0;
			bill.customer.HereIsChange(0);
		}
		else{
			float change = bill.paid - bill.due;
			bill.due = bill.paid = 0;
			bill.customer.HereIsChange(change);
			Do(bill.customer.getName()+" paid and change is "+change);
		}
		bills.remove(bill);
	}
	
	//UTILITIES
	public String getName(){
		return this.name;
	}
	
	public float getAssets(){
		return this.assets;
	}

	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(7);
	}
	
}

