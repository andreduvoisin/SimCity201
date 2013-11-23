package restaurant_tranac.roles;

import base.BaseRole;
import restaurant_tranac.interfaces.*;

import java.util.*;

/**
 * Restaurant Cook Agent
 */
public class MarketAgent extends BaseRole implements Market {
	
	private Cashier cashier = null;

	public enum OrderState {Pending, Fulfilling, Done,
		NeedPayment, Paying, Outstanding, Paid, Finished};
	private List<Order> orders = new ArrayList<Order>();

	private List<Food> inventory = new ArrayList<Food>();
	private Map<String, Double> prices = new HashMap<String, Double>();
	private Timer timer = new Timer();
	
	private static final int baseTime = 5000;
	private static final int baseInventory = 3;
	private static final int basePrice = 3;
	
	public MarketAgent() {
		super();
		
		//create inventory
		inventory.add(new Food("Steak",baseInventory));
		inventory.add(new Food("Chicken",baseInventory));
		inventory.add(new Food("Salad", baseInventory));
		inventory.add(new Food("Pizza",baseInventory));
		
		//create prices
		prices.put("Steak", (double)(basePrice*2));
		prices.put("Chicken", (double)(basePrice*1.75));
		prices.put("Salad", (double)(basePrice));
		prices.put("Pizza", (double)(basePrice*1.5));
	}

	/** Messages */

	public void msgOrderFood(Cook c, String f, int n) {
		try {
		orders.add(new Order(c,f,n));
		stateChanged();
		} catch (ConcurrentModificationException e) {
			  e.printStackTrace();
		}
	}
	
	public void msgOrderDone(Order o) {
		o.s = OrderState.Done;
		stateChanged();
	}
	
	public void msgHereIsPayment(String i, double p) {
		try {
		for(Order o : orders) {
			if(o.food.equals(i) && o.cost == p) {
				o.s = OrderState.Paid;
			}
		}
		stateChanged();
		} catch (ConcurrentModificationException e) {
		  e.printStackTrace();
		}
	}
	
	public void msgWillPaySoon(String i, double p) {
		try {
		for(Order o : orders) {
			if(o.food.equals(i) && o.cost == p) {
				o.s = OrderState.Outstanding;
			}
		}
		stateChanged();
		} catch (ConcurrentModificationException e) {
		  e.printStackTrace();
		}
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	  try {
		for(Order o : orders) {
			if(o.s == OrderState.Pending) {
				tryToFulfillOrder(o);
				return true;
			}
		}
		for(Order o : orders) {
			if(o.s == OrderState.Done) {
				sendOrderToCook(o);
				return true;
			}
		}
		for(Order o : orders) {
			if(o.s == OrderState.NeedPayment) {
				sendBillToCashier(o);
				return true;
			}
		}
		for(Order o : orders) {
			if(o.s == OrderState.Paid) {
				completeOrder(o);
				return true;
			}
		}
		return false;
	  } catch (ConcurrentModificationException e) {
		  e.printStackTrace();
		  return true;
	  }
	}

	/** Actions */

	private void tryToFulfillOrder(final Order o) {
	  try {
		Food food = null;
		for(Food f : inventory) {
			if(f.name == o.food)
				food = f;
		}
		
		if(food.stock == 0) {				//handles if out of food item
			Do("Out of " + food.name);
			o.cook.msgOutOfInventory(this,o.food);
			orders.remove(o);
		}
		else if(food.stock >= o.num) {
			Do("Fulfilling all of order.");
			o.s = OrderState.Fulfilling;
			food.stock-= o.num;
			o.cook.msgCanFulfillInventory(o.food,o.num);
			timer.schedule(new TimerTask() {
				public void run() {
					msgOrderDone(o);
				}
			},baseTime);
		}
		else if(food.stock < o.num) {
			Do("Fulfilling part of order.");
			o.s = OrderState.Fulfilling;
			o.num = food.stock;
			food.stock = 0;
			o.cook.msgCanFulfillInventory(o.food,o.num);
			timer.schedule(new TimerTask() {
				public void run() {
					msgOrderDone(o);
				}
			},baseTime);
		}
	  } catch (ConcurrentModificationException e) {
			  e.printStackTrace();
	  }
	}
	
	private void sendOrderToCook(Order o) {
		Do("Sending inventory of " + o.num + " " + o.food);
		o.s = OrderState.NeedPayment;
		o.cook.msgHereIsInventory(o.food,o.num);
	}

	private void sendBillToCashier(Order o) {
		o.cost = prices.get(o.food) * o.num;
		Do("Sending bill to cashier of " + o.cost + " for " + o.num + " " + o.food);
		o.s = OrderState.Paying;
		cashier.msgHereIsBill(this, o.food, o.cost);
	}
	
	private void completeOrder(Order o) {
		Do("Order of " + o.num + " " + o.food + " completed.");
		o.s = OrderState.Finished;
	}
	/** Utilities */

	public String getName() {
		return mPerson.getName();
	}
	
	public void setInventory(int n) {
		for(Food f : inventory) {
			f.stock = n;
		}
	}
	
	public void setCashier(Cashier c) {
		cashier = c;
	}

	/** Classes */
	
	private class Order {		//holds all relevant information for the order
		Cook cook;
		String food;
		int num;
		double cost;
		OrderState s;
		
		Order(Cook c, String f, int n) {
			cook = c;
			food = f;
			num = n;
			cost = 0;
			s = OrderState.Pending;
		}
	}
	
	private class Food {
		String name;
		int stock;
		
		Food(String n, int s) {
			name = n;
			stock = s;
		}
	}
}

