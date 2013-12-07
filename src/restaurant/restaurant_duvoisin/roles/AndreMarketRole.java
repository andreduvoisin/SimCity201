package restaurant.restaurant_duvoisin.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.restaurant_duvoisin.MarketPrices;
import restaurant.restaurant_duvoisin.interfaces.Cashier;
import restaurant.restaurant_duvoisin.interfaces.Cook;
import restaurant.restaurant_duvoisin.interfaces.Market;
import base.BaseRole;
import base.Location;
import base.reference.ContactList;
import city.gui.trace.AlertTag;

/**
 * Restaurant Market Agent
 */
public class AndreMarketRole extends BaseRole implements Market {
	private String name;
	Boolean paused = false;
	Cook cook;
	List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	enum MarketOrderState { Pending, Fulfilling, Fulfilled };
	Map<String, Integer> inventory = new HashMap<String, Integer>();
	static final int timePerItem = 40000;
	Cashier cashier;
	MarketPrices marketPrices = new MarketPrices();
	List<MyCheck> myChecks = Collections.synchronizedList(new ArrayList<MyCheck>());

	public AndreMarketRole(String name, Cook c) {
		super(null);
		
		this.name = name;
		cook = c;
		
		inventory.put("steak", 2);
		inventory.put("chicken", 2);
		inventory.put("salad",  2);
		inventory.put("pizza", 2);
	}
	
	public String getName() {
		return name;
	}

	// Messages
	
	public void msgOrderFood(Map<String, Integer> orders) {
		print("msgOrderFood received");
		//currentOrders.putAll(orders);
		String[] keys = orders.keySet().toArray(new String[0]);
		Integer[] values = orders.values().toArray(new Integer[0]);
		for(int i = 0; i < orders.size(); i++)
			marketOrders.add(new MarketOrder(keys[i], values[i], MarketOrderState.Pending));
		stateChanged();
	}
	
	public void msgFoodPayment(String type, double payment) {
		print("msgFoodPayment received");
		synchronized(myChecks) {
			for(MyCheck mc : myChecks)
				if (mc.type.equals(type) && mc.amount == payment) {
					myChecks.remove(mc);
					break;
				}
		}
	}
	
	public void msgNotEnoughMoney(String type, double payment) {
		print("msgNotEnoughMoney received");
		synchronized(myChecks) {
			for(MyCheck mc : myChecks)
				if(mc.type.equals(type) && mc.amount > payment) {
					mc.amount -= payment;
					break;
				}
		}
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
			synchronized(marketOrders) {
				for(MarketOrder mo : marketOrders)
					if(mo.state == MarketOrderState.Pending) {
						ProcessOrder(mo);
						return true;
					}
			}
			synchronized(marketOrders) {
				for(MarketOrder mo : marketOrders)
					if(mo.state == MarketOrderState.Fulfilled) {
						FinishOrder(mo);
						return true;
					}
			}
		}
		return false;
	}

	// Actions
	void ProcessOrder(MarketOrder mo) {
		print("Doing ProcessOrder");
		if(inventory.get(mo.type) <= 0) {
			//cook.msgFailedToFulfillRequest(this, mo.type, mo.amount);
			marketOrders.remove(mo);
		} else if(mo.amount <= inventory.get(mo.type)) {
			mo.state = MarketOrderState.Fulfilling;
			inventory.put(mo.type, inventory.get(mo.type) - mo.amount);
			mo.fulfillThisOrder();
		} else if(mo.amount > inventory.get(mo.type)) {
			mo.state = MarketOrderState.Fulfilling;
			//cook.msgFailedToFulfillRequest(this, mo.type, mo.amount - inventory.get(mo.type));
			mo.amount = inventory.get(mo.type);
			inventory.put(mo.type, 0);
			mo.fulfillThisOrder();
		}
	}
	
	void FinishOrder(MarketOrder mo) {
		print("Doing FinishOrder");
		//cook.msgReplenishFood(mo.type, mo.amount);
		myChecks.add(new MyCheck(mo.type, marketPrices.currentRate.get(mo.type) * mo.amount));
		cashier.msgComputeMarketBill(this, mo.type, mo.amount);
		marketOrders.remove(mo);
	}
	
	// The animation DoXYZ() routines
	
	//utilities
	public void setCashier(Cashier c) { cashier = c; }
	
	class MarketOrder {
		String type;
		int amount;
		MarketOrderState state;
		Timer timer;
		
		MarketOrder(String t, int a, MarketOrderState s) {
			type = t;
			amount = a;
			state = s;
			timer = new Timer();
		}
		
		public void fulfillThisOrder() {
			timer.schedule(new TimerTask() {
				public void run() {
					state = MarketOrderState.Fulfilled;
					stateChanged();
				}
			}, timePerItem * amount);
		}
	}
	
	class MyCheck {
		String type;
		double amount;
		
		MyCheck(String t, double a) {
			type = t;
			amount = a;
		}
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(0);
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