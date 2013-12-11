package restaurant.restaurant_jerryweb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.intermediate.RestaurantCookRole;
import restaurant.restaurant_jerryweb.gui.Menu;
import restaurant.restaurant_jerryweb.interfaces.Market;
import restaurant.restaurant_jerryweb.interfaces.Waiter;
import base.BaseRole;
import base.ContactList;
import base.Item;
import base.Item.EnumItemType;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

/**
 * Restaurant Cook Agent
 */
public class JerrywebCookRole extends BaseRole {
	public RestaurantCookRole mRole;
	static final int semaphoreCerts = 0;
	public List<Order> Orders= Collections.synchronizedList(new ArrayList<Order>());
	public List<Order>  RevolvingStandOrders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Food> foodItems= new ArrayList<Food>();
	//public Map<EnumItemType, Integer> mItemsDesired = new HashMap<EnumItemType, Integer>();
	Timer cookingTimer = new Timer();
	private Timer checkRevolvingStand = new Timer();
	public List<Waiter> Waiters = new ArrayList<Waiter>();
	public Menu m = new Menu();
	public Map<String,Food> foodMap = new HashMap<String,Food>(4);
	public Map<EnumItemType,Integer> mCookTimes = new HashMap<EnumItemType, Integer>();
	public List<Market> markets = new ArrayList<Market>();
	int selection = 0; 
	private boolean orderSent = false;
	int baseNeed = 10;
	
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

	public JerrywebCookRole(Person p, RestaurantCookRole r){ 
		super(p);
		mRole = r;
		this.name = "JerrywebCook";

        mCookTimes.put(EnumItemType.STEAK,17000);
        mCookTimes.put(EnumItemType.CHICKEN,12000);
        mCookTimes.put(EnumItemType.SALAD,2000);
        mCookTimes.put(EnumItemType.PIZZA,14000);
        
        checkRevolvingStand.scheduleAtFixedRate(checkRS, 15000, 10000);
	}
	
	TimerTask checkRS = new TimerTask(){
		public void run(){
			if(mPerson != null){
				moveRevlovingStandOrders();
			}
		}
	};

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void addWaiter(Waiter w){
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

				Orders.remove(i);
				stateChanged();
				orderSent = false;
				break;
			}
		}
		}
	}
	
	public void msgCookThis(Waiter w, String choice, int table){
		//Orders.choice = m.menuItems.get(choice);
		if(choice.equals("nothing")){
			
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
			RevolvingStandOrders.remove(i);
		}
	}
	
	public void checkInventory(){//This checks the inventory while the waiter is idle
		//print("my inventory " + foodMap.foodMap.get("steak").amount)
		/*for(int i = 0; i <4; i++){
			if(mItemInventory.get(i) <= mItemInventory.get(i){
				markets.get(selection).msgGiveMeOrder(m.menuItems.get(i), foodMap);
				break;
			}

		}
		if(selection == 2){
			selection = 0;
		}
		selection++;*/
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
		EnumItemType food = Item.stringToEnum(order.choice);
		print("" + food);
		if(mRole.mItemInventory.get(food) == 0){
			//Do("Out of choice " + order.choice);
			Orders.get(orderLocation).w.msgOutOfFood(Orders.get(orderLocation).choice, Orders.get(orderLocation).table);
			//Orders.get(orderLocation).s = OrderState.needToRestock;
			Orders.remove(order);
			mRole.mItemsDesired.put(food,baseNeed);
			 return;
		}
		
		
		else{
			int cookTime = 0;
			final int  orderLocationFinal = orderLocation;
			cookTime = mCookTimes.get(food);
			mRole.decreaseInventory(food);

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
	/*
	public void addMarketOutOfItem(Market m) {
        outOfItem.add(m);
	}*/

	// The animation DoXYZ() routines
	

	//utilities
	
	public void addToRevolvingStand(Waiter w,String custOrder, int t, OrderState orderS){
		RevolvingStandOrders.add(new Order(w, custOrder,  t, orderS));
	}
	
	public void addMarket(Market market){
		//print("Adding " + market.getName());
		markets.add(market);
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(2);
	}
	
	public void Do(String msg) {
		super.Do(msg, AlertTag.R2);
	}
	
	public void print(String msg) {
		super.print(msg, AlertTag.R2);
	}
	
	public void print(String msg, Throwable e) {
		super.print(msg, AlertTag.R2, e);
	}
}