package restaurant.restaurant_jerryweb;

import restaurant.restaurant_jerryweb.agent.Agent;
import restaurant.restaurant_jerryweb.JerrywebCookRole.OrderState;
import restaurant.restaurant_jerryweb.JerrywebCustomerRole.AgentEvent;
import restaurant.restaurant_jerryweb.JerrywebWaiterRole.CustomerState;
import restaurant.restaurant_jerryweb.gui.HostGui;
import restaurant.restaurant_jerryweb.gui.Menu;
import restaurant.restaurant_jerryweb.interfaces.Market;
import restaurant.restaurant_jerryweb.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import base.BaseRole;

/**
 * Restaurant Cook Agent
 */
public class JerrywebCookRole extends BaseRole {
	static final int semaphoreCerts = 0;
	public List<Order> Orders= Collections.synchronizedList(new ArrayList<Order>());
	public List<Order>  RevolvingStandOrders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Food> foodItems= new ArrayList<Food>();
	Timer cookingTimer = new Timer();
	private Timer checkRevolvingStand = new Timer();
	public List<JerrywebWaiterRole> Waiters = new ArrayList<JerrywebWaiterRole>();
	public Menu m = new Menu();
	public Map<String,Food> foodMap = new HashMap<String,Food>(4);	
	public List<Market> markets = new ArrayList<Market>();
	int selection = 0; 
	private boolean orderSent = false;
	
	
	public class Order{
		Waiter w;
		
		String choice;
		int table;
		OrderState s;
		public Order(Waiter waiter, String custOrder, int t, OrderState orderS){
			w = waiter;
			choice = custOrder;
			table = t;
			s = orderS;
		}
		
	}

	public enum OrderState
	{pending, cooking, done, waitingForWaiter, giveToWaiter, served, needToRestock, restocking}
	
	public class Food {
		String type;
		int cookingTimes;
		int amount; 
		int low;
		int capacity;
		FoodState s;

		public Food(String foodType, FoodState state, int timeToCook, int cap, int quantity, int l){
			type = foodType;
			cookingTimes = timeToCook;
			amount = quantity; 
			low = l;
			capacity = cap;
			s = state;
		}
	}
	
	public enum FoodState
	{ordered, delivered}
	
	private String name;
	private Semaphore atTable = new Semaphore(semaphoreCerts,true);

	//public HostGui hostGui = null;

	public JerrywebCookRole(String name) {
		super();
		this.name = name;

		//This populates the food map using the string names of the food items as keys and holds the
		foodMap.put("steak",new Food("steak", FoodState.delivered, 17000, 5, 5, 2));
		foodMap.put("chicken",new Food("chicken", FoodState.delivered, 12000, 7, 7, 3));
		foodMap.put("salad",new Food("salad", FoodState.delivered, 2000, 10, 10, 4));
		foodMap.put("pizza",new Food("pizza", FoodState.delivered, 14000, 8, 8, 3));
		
		/*checkRevolvingStand.schedule(new TimerTask() {
			public void run() {
				
				moveRevlovingStandOrders();
//				stateChanged(); //JERRY: 1 Add this back in with your restaurant
			}
		},
		25000);*/
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void addWaiter(JerrywebWaiterRole w){
		Waiters.add(w);
	}

	


	// Messages
	public void msgOutOfStock(Market market, String choice){
		synchronized(Orders){
		for(int i=0; i<Orders.size(); i++){
			if(Orders.get(i).choice.equals(choice) && Orders.get(i).s == OrderState.needToRestock){
				
				if(selection == 2){
					//print("Iterated through all the markets...");
					selection = 0;
				}
				selection++;
				//Orders.remove(i);
				
			}
		}
		}
		stateChanged();
	}
	
	public void msgHereIsOrder(String choice, Map<String, Food> fm){
		synchronized(Orders){
		for(int i=0; i<Orders.size(); i++){
			if(Orders.get(i).choice.equals(choice) && Orders.get(i).s == OrderState.restocking){
				foodMap = fm;
				//print("Steak amount is now: " + foodMap.get("steak").amount);
				Orders.remove(i);
				stateChanged();
				orderSent = false;
				break;
			}
		}
		}
	}
	
	public void msgCookThis(JerrywebWaiterRole w, String choice, int table){
		//Orders.choice = m.menuItems.get(choice);
		if(choice.equals("nothing")){
			//print("No");
		}
		else{ 
			Orders.add(new Order(w, choice, table, OrderState.pending));
			
		stateChanged();}
		
	}


	public void msgfoodDone(Order order){
		
		order.s = OrderState.done;
		stateChanged();
	}
	
	public void msgGiveMeOrder(int t){
		synchronized(Orders){
		for(int i=0; i<Orders.size(); i++){
			if(Orders.get(i).table == t){
				Orders.get(i).s = OrderState.giveToWaiter;
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
		for(int i=0; i<Orders.size(); i++){
			if(Orders.get(i).s == OrderState.needToRestock){
				OrderFood(i);
				return true;
			}
		
		}
		

		
		for(int i=0; i<Orders.size(); i++){
			if(Orders.get(i).s == OrderState.pending){
				TryToCookIt(Orders.get(i), i);
				return true;
			}
		
		}
		
		for(int i=0; i<Orders.size(); i++){
			if(Orders.get(i).s == OrderState.done){
				plateIt(Orders.get(i));
				return true;
			}
		}
		
		for(int i=0; i<Orders.size(); i++){
			if(Orders.get(i).s == OrderState.giveToWaiter){
				giveFoodToWaiter(Orders.get(i), i);//cookIt(Orders.get(i));
				return true;
			}
		
		}
		
		checkInventory();//comment this out to prevent the cook from checking his inventory
		return false;
		}
		catch (ConcurrentModificationException e) {
			
			return false;
		}
	}

	// Actions
	
	public void moveRevlovingStandOrders(){
		for(int i = 0; i < RevolvingStandOrders.size(); i++){
			Orders.add(RevolvingStandOrders.get(i));
			
		}
	}
	
	public void checkInventory(){//This checks the inventory while the waiter is idle
		//print("my inventory " + foodMap.foodMap.get("steak").amount)
		for(int i = 0; i <4; i++){
			if(foodMap.get(m.menuItems.get(i)).amount <= foodMap.get(m.menuItems.get(i)).low){
				markets.get(selection).msgGiveMeOrder(m.menuItems.get(i), foodMap);
				break;
			}

		}
		if(selection == 2){
			selection = 0;
		}
		selection++;
	}
	
	public void OrderFood(int x){
		if(!orderSent){
			orderSent = true;
			markets.get(selection).msgGiveMeOrder(Orders.get(x).choice, foodMap);
			Orders.get(x).s = OrderState.restocking;
		//Orders.remove(x);
			//print("Ordering food");
		}
	}
	
	private void plateIt(Order order) {
		//print("" + order.choice + " is ready");
		order.w.msgOrderReady(order.choice, order.table);	
		order.s = OrderState.waitingForWaiter;
	}

	public void giveFoodToWaiter(Order order, int x){
		//print("Here is the " + order.choice + " for table " + order.table);
		order.s = OrderState.served;
		order.w.msgTakeFood(order.choice, order.table);
	}
	
	public void TryToCookIt(Order order, int orderLocation){
		if(foodMap.get(order.choice).amount == 0){
			//print("Removing order " + Orders.get(orderLocation).choice);
			Orders.get(orderLocation).w.msgOutOfFood(Orders.get(orderLocation).choice, Orders.get(orderLocation).table);
			Orders.get(orderLocation).s = OrderState.needToRestock;
			//Orders.remove(orderLocation);
		}
		
		else{
			//print("Alright " + Orders.get(orderLocation).choice + " coming up!");
			int cookTime = 0;
			final int  orderLocationFinal = orderLocation;
		
			//cookTime = foodMap.get(Orders.get(orderLocationFinal).choice).cookingTimes;
			cookTime = foodMap.get(order.choice).cookingTimes;

			//print("cook time is: " + cookTime);
			//foodMap.get(Orders.get(orderLocationFinal).choice).amount = foodMap.get(Orders.get(orderLocationFinal).choice).amount - 1;
			foodMap.get(order.choice).amount = foodMap.get(order.choice).amount - 1;

			cookingTimer.schedule(new TimerTask() {
				public void run() {
					//DoCooking(order); // animation for cooking
					cookingTimes(orderLocationFinal);
					}
				},
				cookTime);
			Orders.get(orderLocationFinal).s = OrderState.cooking;
			//foodMap.get(Orders.get(orderLocation)
		}
	}
	
	public void cookingTimes(final int orderLoc){//calls the food done message to change the state of the corresponding order
		//print("the ordering list is of size: " + Orders.size());
		this.msgfoodDone(Orders.get(orderLoc));
		
	}
	// The animation DoXYZ() routines
	

	//utilities
	
	public void addToRevolvingStand(Waiter w,String custOrder, int t, OrderState orderS){
		RevolvingStandOrders.add(new Order(w, custOrder,  t, orderS));
	}
	
	public void addMarket(Market market){
		//print("Adding " + market.getName());
		markets.add(market);
	}

}