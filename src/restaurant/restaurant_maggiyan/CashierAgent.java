package restaurant.restaurant_maggiyan;

import agent.Agent;

import java.util.*;

import restaurant.restaurant_maggiyan.Check;
import restaurant.restaurant_maggiyan.interfaces.Cashier;
import restaurant.restaurant_maggiyan.interfaces.Customer;
import restaurant.restaurant_maggiyan.interfaces.Market;
import restaurant.restaurant_maggiyan.interfaces.Waiter;

/**
 * Restaurant Host Agent
 */

public class CashierAgent extends Agent implements Cashier{
	private String n; 
	private Double TOTALMONEY = 500.00; 
	private Menu menu = new Menu(); 
	//private List<Check> checks = new ArrayList<Check>();
	private List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	private List<MarketPayment> marketpayments = Collections.synchronizedList(new ArrayList<MarketPayment>());
	private List<Payment> payments = new ArrayList<Payment>();
	
	public CashierAgent(String name, boolean isTest){
		this.n = name;
		if(!isTest){
			startThread(); 
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
	public void msgPleaseCalculateBill(Waiter w, Customer c, String choice)
	{
		print("Calculating customer bill"); 
		Check check = new Check(w, c, choice); 
		checks.add(check); 
		stateChanged(); 
		
	}
	
	//From Customer
	public void msgHereIsPayment(Customer c, double cash){
		print("Received customer payment"); 
		Payment p = new Payment (c, cash);
		payments.add(p);
		stateChanged();
	}
	
	//From Market
	public void msgDeliverBill(Market m, List<String> bill){
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
		Customer customer;
		double paymentAmount;
		
		Payment(Customer c, double p){
			customer = c;
			paymentAmount = p; 
		}
	}
	
	private class MarketPayment{
		Market market; 
		Double total; 
		List<String> orderedItems; 
		
		MarketPayment(Market m, List<String> o){
			market = m; 
			orderedItems = o;
			total = 0.0; 
		}
	}
	
	public Double getMarketTotal(int index){
		return marketpayments.get(index).total; 
	}


}
