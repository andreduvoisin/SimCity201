package restaurant.restaurant_xurex.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.restaurant_xurex.interfaces.Cashier;
import restaurant.restaurant_xurex.interfaces.Cook;
import restaurant.restaurant_xurex.interfaces.Market;
import base.Agent;

/**
 * Restaurant Market Agent
 * 		A non-refillable source of food that the Cook Agent orders from
 * 		There is a time delay between initial order and completion of order
 * 		There will be multiple Markets that are listed in Cook
 */

public class MarketAgent extends Agent implements Market{
	public enum OrderState
	{pending, payment, ready, ignore}; 
	//ignore order when waiting(confirmed) or finished(completed)
	
	private String name;
	private float assets = 0;
	Timer orderTimer = new Timer();
	private final static int orderCompletionTime = 7000;
	
	public List<Order> orders = new ArrayList<Order>();
	private Map<String, Integer> Inventory = new HashMap<String, Integer>(); 
	
	class Order{
		Map<String, Integer> ordered;
		Map<String, Integer> provided;
		OrderState s;
		float payment;
		
		Order(Map<String, Integer> orderMap){
			this.ordered = orderMap;
			this.provided = new HashMap<String, Integer>();
			this.s = OrderState.pending;
			this.payment = 0;
		}
	}
	
	//AGENT CORRESPONDENT
	Cook cook;
	Cashier cashier;
	
	//CONSTRUCTORS
	public MarketAgent(String name) {
		super();
		this.name = name;
		Inventory.put("Steak", new Integer(10));
		Inventory.put("Chicken", new Integer(10));
		Inventory.put("Salad", new Integer(10));
		Inventory.put("Pizza", new Integer(10));
	}
	public MarketAgent(String name, Cook cook, Cashier cashier){
		super();
		this.name = name;
		this.cook = cook;
		this.cashier = cashier;
		Inventory.put("Steak", new Integer(10));
		Inventory.put("Chicken", new Integer(10));
		Inventory.put("Salad", new Integer(10));
		Inventory.put("Pizza", new Integer(10));
	}
	public MarketAgent(String name, Cook cook, Cashier cashier, int steak, int chicken, int salad, int pizza){
		super();
		this.name = name;
		this.cook = cook;
		this.cashier = cashier;
		Inventory.put("Steak", steak);
		Inventory.put("Chicken", chicken);
		Inventory.put("Salad", salad);
		Inventory.put("Pizza", pizza);
	}

	// MESSAGES
	public void HereIsOrder(Map<String, Integer> order){
		orders.add(new Order(order));
		//order.s = pending set in constructor
		stateChanged();
	}
	public void HereIsPayment(float payment){
		final float p = payment;
		orderTimer.schedule(new TimerTask(){
				public void run(){
					Do("Bill has been paid in full");
					assets += p;
					for(Order order: orders){
						if(order.s==OrderState.payment){
							order.s=OrderState.ready;
						}
					}
					stateChanged();
				}}, 5000);
	}
	void TimerDone (Order o){
		o.s = OrderState.payment;
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		for (Order order: orders){
			if(order.s==OrderState.ready){
				//RequestPayment(order);
				//SendOrder(order);
				order.s = OrderState.ignore;
				//Can also remove order from list
				return true;
			}
		}
		for (Order order: orders){
			if(order.s==OrderState.payment){
				RequestPayment(order);
				order.s = OrderState.ignore;
				return true;
			}
		}
		for (Order order: orders){
			if(order.s==OrderState.pending){
				PrepareOrder(order);
				order.s = OrderState.ignore;
				return true;
			}
		}
		return false;
	}

	// ACTIONS
	/*private void SendOrder(Order o){
		cook.OrderIsReady(this);
		Do("Order is ready sent to cook");
	}*/
	
	private void RequestPayment(Order o){
		//Compute Bill Here
		//Steak: 	8.00
		//Chicken:	5.50
		//Salad:	3.00
		//Pizza:	4.50
		if(o.provided.containsKey("Steak"))
			o.payment += o.provided.get("Steak")	*8.0;
		if(o.provided.containsKey("Chicken"))
			o.payment += o.provided.get("Chicken")	*5.5;
		if(o.provided.containsKey("Salad"))
			o.payment += o.provided.get("Salad")	*3.0;
		if(o.provided.containsKey("Pizza"))
			o.payment += o.provided.get("Pizza")	*4.5;
		cashier.HereIsBill(this, o.payment);
	}
	
	private void PrepareOrder(Order o){
		//boolean canFulfill = true;
		for(String food : o.ordered.keySet()){
			int available = Inventory.get(food);
			int ordered   = o.ordered.get(food);
			if(available>=ordered){
				o.provided.put(food, ordered);
				Inventory.put(food, available-ordered);
			}
			else{
				o.provided.put(food, available);
				Inventory.put(food, 0);
				//canFulfill = false;
			}
		}
		/*if(canFulfill){
			cook.MarketCanFulfill(this, o.provided);
		}
		else{
			cook.MarketCannotFulfill(this, o.provided);
		}*/
		
		final Order temp = o;
		orderTimer.schedule(
			new TimerTask(){ 
				public void run(){
					TimerDone(temp);
				}
			}
		,orderCompletionTime);
	}
	
	//ANIMATIONS
	//nothing here
	
	//UTILITIES
	public String getName() {
		return name;
	}
	public float getAssets() {
		return assets;
	}
	public int getQuantity(String food){
		return Inventory.get(food);
	}
}

