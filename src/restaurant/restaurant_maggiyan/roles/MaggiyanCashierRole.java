package restaurant.restaurant_maggiyan.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.Menu;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCashier;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanMarket;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;
import base.BaseRole;
import base.Location;
import base.interfaces.Person;
import base.reference.ContactList;

/**
 * Restaurant Host Agent
 */

public class MaggiyanCashierRole extends BaseRole implements MaggiyanCashier{
	private RestaurantCashierRole mRole;
	private String n; 
	private Double TOTALMONEY = 500.00; 
	private Menu menu = new Menu(); 
	//private List<Check> checks = new ArrayList<Check>();
	private List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	private List<MarketPayment> marketpayments = Collections.synchronizedList(new ArrayList<MarketPayment>());
	private List<Payment> payments = new ArrayList<Payment>();
	
	public MaggiyanCashierRole(String name, boolean isTest){
		super(null);
		this.n = name;
	}
	
	public MaggiyanCashierRole(Person p, RestaurantCashierRole r){
		super(p); 
		mRole = r;
		if(p == null){
			this.n = "null"; 
		}
		else{
			this.n = p.getName();
		}
	}
	
	public String getName(){
		return n; 
	}
	
	//For JUnit Testing
	public int getMarketPaymentSize(){
		return marketpayments.size(); 
	}
	
	public int getChecksSize(){
		return checks.size();
	}
	
	public int getPaymentsSize(){
		return payments.size(); 
	}
	
	public Double getTotalMoney(){
		return TOTALMONEY; 
	}
	
	// Messages

	//From Waiter
	public void msgPleaseCalculateBill(MaggiyanWaiter w, MaggiyanCustomer c, String choice)
	{
		print("Calculating customer bill"); 
		Check check = new Check(w, c, choice); 
		checks.add(check); 
		stateChanged(); 
		
	}
	
	//From Customer
	public void msgHereIsPayment(MaggiyanCustomer c, double cash){
		print("Received customer payment"); 
		Payment p = new Payment (c, cash);
		payments.add(p);
		stateChanged();
	}
	
	//From Market
	public void msgDeliverBill(MaggiyanMarket m, List<String> bill){
		print("Received market bill");
		MarketPayment mp = new MarketPayment(m, bill); 
		marketpayments.add(mp);
		stateChanged(); 
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
		if(!payments.isEmpty()){
			for(Payment p: payments){
				makeTransaction(p); 
				return true;
			}
		}
		
		if(!marketpayments.isEmpty()){
			synchronized(marketpayments){
				for(MarketPayment m: marketpayments){
					PayMarket(m);
					return true; 
				}
			}
		}
		
		if(!checks.isEmpty()){
			synchronized(checks){
				for(Check c: checks){
                    if(c.paid == false){
                            if(c.getCheckTotal() == 0.0){ 
                                    calculateBillTotal(c);
                                    return true;
                            }
                    }
				}
//				for(Check c: checks){
//					if(c.state.equals(CheckState.needsToBeCalculated)){
//							calculateBillTotal(c);
//							return true;
//					}
//				}
			}
		}
		

		return false; 
	}

	// Actions
	private void calculateBillTotal(Check check){ 
		for(int i = 0; i < menu.MenuOptions.size(); i++){ 
            if(menu.MenuOptions.get(i).name.equals(check.choice)){
                    check.setCheckTotal(menu.MenuOptions.get(i).price);
                    print("Check total: " + check.getCheckTotal()); 
                    check.waiter.msgHereIsBill(check);
                    return; 
            }
		}
	}
	
	private void PayMarket(MarketPayment m){
		//Calculates the bill total for amount owed for the order 
		print("Calculating market bill");
		for(int i=0; i<m.orderedItems.size(); i++){
			for(int j = 0; j < menu.MenuOptions.size(); j++){
				if(menu.MenuOptions.get(j).name.equals(m.orderedItems.get(i))){
					m.total += menu.MenuOptions.get(j).price; 
				}
			}
		}
		print("Market total to be paid: " + m.total); 
		TOTALMONEY = TOTALMONEY - m.total;  //deducts payment amount from Cashier total
		m.market.msgHereIsPayment(m.total); 
		print("" + m.market.getName());
		marketpayments.remove(m); 
	}
	
	
	private void makeTransaction(Payment p){
		print("Making customer payment transaction");
		for(Check c : checks){ 
			if(c.customer.equals(p.customer)){
				TOTALMONEY += p.paymentAmount; 
				payments.remove(p);
				checks.remove(c); 
				return;
			}
		}
	}

	//Utilities
	private class Payment{
		MaggiyanCustomer customer;
		double paymentAmount;
		
		Payment(MaggiyanCustomer c, double p){
			customer = c;
			paymentAmount = p; 
		}
	}
	
	private class MarketPayment{
		MaggiyanMarket market; 
		Double total; 
		List<String> orderedItems; 
		
		MarketPayment(MaggiyanMarket m, List<String> o){
			market = m; 
			orderedItems = o;
			total = 0.0; 
		}
	}
	
	public Double getMarketTotal(int index){
		return marketpayments.get(index).total; 
	}

	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(3);
	}

    public RestaurantCashierRole getIntermediateRole() {
    	return mRole;
    }

}
