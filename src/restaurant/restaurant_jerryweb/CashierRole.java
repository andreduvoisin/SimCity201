package restaurant.restaurant_jerryweb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import restaurant.restaurant_jerryweb.agent.Agent;
import restaurant.restaurant_jerryweb.gui.HostGui;
import restaurant.restaurant_jerryweb.gui.Menu;
import restaurant.restaurant_jerryweb.interfaces.Cashier;
import restaurant.restaurant_jerryweb.interfaces.Customer;
import restaurant.restaurant_jerryweb.interfaces.Market;
import restaurant.restaurant_jerryweb.interfaces.Waiter;

/**
 * Restaurant Host Agent
 */
public class CashierRole extends Agent implements Cashier {
	static final int semaphoreCerts = 0;
	public List<Order>Bills = Collections.synchronizedList(new ArrayList<Order>());
	public List<MarketBill>MarketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	public Menu m = new Menu();
	public Map<String,Food> foodMap = new HashMap<String,Food>(4);
	public double money = 200;
	public double change = 0;
	
	public class MarketBill{
		public Market market;
		public double amount;
		int billNumber;
		public mBillState s;
		public MarketBill(Market m, double charge, mBillState state, int BN){
			market = m;
			amount = charge;
			s = state;
			billNumber = BN;
		}
	}
	
	public enum mBillState
	{OutStanding, SentPayment, Paid}
	
	public class Order{
		Waiter w;
		public Customer c;
		String choice;
		OrderState s;
		public double payment;
		int billNumber;
		public Order(Waiter waiter, Customer customer, String custOrder, OrderState orderS, double pay){
			w = waiter;
			c = customer;
			choice = custOrder;
			s = orderS;
			payment = pay;
			
		}
	}
	
	public enum OrderState
	{needToCompute, waitingForPayment, Paid, needChange}
	
	public class Food {
		String type;
		int cookingTimes;
		int amount; 
		double  cost;
		int capacity;
		FoodState s;
		
		public Food(String foodType, FoodState state, int timeToCook, int cap, int quantity, double c){
			type = foodType;
			cookingTimes = timeToCook;
			amount = quantity; 
			cost = c;
			capacity = cap;
			s = state;
		}
	}
	
	public enum FoodState
	{ordered, delivered}
	


	public enum CustomerState
	{waiting, seated, doneEating}
	
	private String name;
	private Semaphore atTable = new Semaphore(semaphoreCerts,true);

	public HostGui hostGui = null;

	public CashierRole(String name) {
		super();
		
		this.name = name;
		foodMap.put("steak",new Food("steak", FoodState.delivered, 17000, 5, 0, 15.99));
		foodMap.put("chicken",new Food("chicken", FoodState.delivered, 12000, 7, 0, 10.99));
		foodMap.put("salad",new Food("salad", FoodState.delivered, 2000, 10, 1, 5.99));
		foodMap.put("pizza",new Food("pizza", FoodState.delivered, 14000, 8, 0, 8.99));
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	// Messages
	
	public void msgComputeBill(Waiter waiter, Customer customer, String choice){
		//print("Alright " + customer.getName() + " is going to PAY BABY!!!");
		Bills.add(new Order(waiter, customer, choice, OrderState.needToCompute, 0));
		stateChanged();		
	}
	
	public void msgPayment(Customer customer, double cash){
		int x = find(customer);
		Bills.get(x).s = OrderState.needChange;
		Bills.get(x).payment = cash;
		stateChanged();
	}
	
	public void msgPayMarket(Market market, double total, int billNumber){
		//print("" + market.getName() + " wants me to pay " + total);
		MarketBills.add(new MarketBill(market, total, mBillState.OutStanding, billNumber));
		stateChanged();
	}
	
	public void msgMarketPaid(Market market){
		synchronized(MarketBills){
		for(int i =0; i < MarketBills.size(); i++){
			if(MarketBills.get(i).market == market){
				MarketBills.get(i).s = mBillState.Paid;
				//print("Bill " + MarketBills.get(i).billNumber + " from market " + market.getName() + " has been paid.");
				stateChanged();
			}
		}
		}
	}
		/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {

		try{
		for(int i =0; i < Bills.size(); i++){
			//print("" + Customers.get(i).s );
			if(Bills.get(i).s == OrderState.needToCompute){
				Compute(Bills.get(i));
				return true;
			}
		}
		
		
		for(int i =0; i < Bills.size(); i++){
			
			if(Bills.get(i).s == OrderState.needChange){
				makeChange(Bills.get(i), i);
				return true;
			}
		}
		
		
		for(int i =0; i < MarketBills.size(); i++){
			if(MarketBills.get(i).s == mBillState.OutStanding){
				payMarket(MarketBills.get(i));
				return true;
			}
		}
		return false;}
		
		catch (ConcurrentModificationException e) {
		
			return false;
		}
				
	}

	// Actions
	
	
	public void Compute(Order order){
		order.w.msgHereIsBill(order.c, foodMap.get(order.choice).cost);
		order.s = OrderState.waitingForPayment;
		//print("bill : " + order.c.getName() + " computed");
		//stateChanged();
	}
	
	public void makeChange(Order order, int x){
		//print("order.payment = " + order.payment);
		change = order.payment - foodMap.get(order.choice).cost;
		if(order.payment < foodMap.get(order.choice).cost){
			money = money + order.payment;
		}
		else{money = money + (order.payment - change);}
		//money = money + (order.payment - change);
		//print("" + order.c.getName() + " your change is: $" + change);
		order.c.msgHereIsChange(change);
		order.s = OrderState.Paid;

		//Bills.remove(x);
	}
	
	public void payMarket(MarketBill mb){
		mb.market.msgPayment(money - mb.amount, mb.billNumber);
		money = money - mb.amount;
		//print("Our balance is now: $" + money);
		mb.s = mBillState.SentPayment;
	}
	
	//utilities
	public int find(Customer c){
		synchronized(Bills){
		for(int i = 0; i <Bills.size(); i++){
			if(Bills.get(i).c.equals(c)){
				return i;
			}
		}
		}
		//print("Can't find check in cashier find function!");
		return 0;
	}


}

