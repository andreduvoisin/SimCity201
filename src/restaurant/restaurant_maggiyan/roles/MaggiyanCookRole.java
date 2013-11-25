package restaurant.restaurant_maggiyan.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import base.BaseRole;
import restaurant.restaurant_maggiyan.Order;
import restaurant.restaurant_maggiyan.Order.state;
import restaurant.restaurant_maggiyan.gui.MaggiyanCookGui;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCook;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanMarket;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;

public class MaggiyanCookRole extends BaseRole implements MaggiyanCook{
	private String n; 
	
	//Cooking Food
	private int foodCookingTime = 10; 
	private Timer timer = new Timer(); 
	
	//Market and Inventory
	private int maxFoodQty = 4; 
	private int inventoryLOW = 2; 
	private int marketCounter = 0; 
	private int totalMarkets = 3; 
	private boolean stockInventory = false; 
	private boolean allMarketsClosed = false; 
	
	//For Cook Animation
	MaggiyanCookGui cookGui; 
	private boolean orderPickedUp = false; 
	private Semaphore animationReady = new Semaphore(0, true);
	
	private List<MaggiyanMarket> markets = new ArrayList<MaggiyanMarket>(); 
	
	//Revolving Stand 
	private Timer RStandTimer = new Timer();  
	
	private Map<String, Food> FoodMap = new HashMap<String, Food>();
	private Map<String, Integer> ShoppingMap = new HashMap<String, Integer>();
	public Map<String, Integer> restockMap = null;  
	
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Order> rStandOrders = Collections.synchronizedList(new ArrayList<Order>());
	
	public MaggiyanCookRole(String name){
		this.n = name;
		
		Food steak = new Food("Steak", inventoryLOW, foodCookingTime* 50);
		Food chicken = new Food("Chicken", maxFoodQty, foodCookingTime*75);
		Food salad = new Food("Salad", inventoryLOW, foodCookingTime*20);
		Food pizza = new Food("Pizza", maxFoodQty, foodCookingTime*30);
		
		FoodMap.put("Steak", steak);
		FoodMap.put("Chicken", chicken);
		FoodMap.put("Salad", salad);
		FoodMap.put("Pizza", pizza);
		
		//Enables cook to periodically check for orders on revolving stand
		RStandTimer.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				//stateChanged(); 
			}
		}, 0,  10000);
		
	}
	
	public String getName(){
		return n; 
	}
	
	public void setMarket(MaggiyanMarket m){
		markets.add(m);
//		print("Markets size: " + markets.size());
		stockInventory = true; 
		stateChanged();
	}

	
	// Messages
	
	//From Waiter
	public void msgHereIsOrder(MaggiyanWaiter w, String choice, int table)
	{
		Order order = new Order(w, choice, table); 
		orders.add(order);
		stateChanged(); 
		
	}
	
	public void msgPickedUpOrder(int pos){
		findOrder(pos).pickedUp = true; 
		orderPickedUp = true; 
		stateChanged(); 
	}
	
	//From Market
	public void msgFulfillingOrder(){
		print("Fulfilling complete order");
		stateChanged();
	}
	
	public void msgFulfillingPartialOrder(){
		print("Fulfilling partial order");
		stockInventory = true;
		stateChanged();
	}
	
	public void msgCannotFulfillOrder(){
		print("Out of stock, cannot fulfill order");
		//stockInventory = true;
		stateChanged();
	}
	
	public void msgDeliverOrder(Map<String, Integer> order){
		restockMap = order; 
		stateChanged();
	}
	
	public void msgOutOfAllInventory(MaggiyanMarket m){
		markets.remove(m); 
		totalMarkets--; 
		if(markets.size() == 0){
			allMarketsClosed = true; 
		}
		stateChanged(); 
	}
	
	//From Animation
	public void msgAnimationReady(){
		animationReady.release();
		stateChanged(); 
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
//		if(stockInventory){
//			StockInventory(); 
//			return true; 
//		}
		
		if(orderPickedUp){
			ClearPlatingArea(); 
			return true; 
		}
		
		if(!orders.isEmpty()){
			synchronized (orders) {
				for(Order order: orders)
				{
					if(order.s == state.done)
					{
						PlateFood(order); 
						return true;
					}	
				}
			}
			
			synchronized (orders) {
				for(Order order: orders)
				{
					if(order.s == state.pending)
					{
						order.s = state.cooking;
						CookIt(order);  
						return true; 
					}				
				}
			}
		}
		
		if(!rStandOrders.isEmpty()){
			synchronized (rStandOrders){
				for(Order order: rStandOrders){
					if(order.s == state.pending)
					{
						AddOrder(order);   
						return true; 
					}
				}
			}
		}
		
		if(restockMap != null){
			Restock(); 
			return true; 
		}
		
		return false; 
	}

	// Actions
	private void AddOrder(Order order){
		orders.add(order); 
		rStandOrders.remove(order);
	}
	
	private void StockInventory(){
		print("Stocking Inventory"); 
		stockInventory = false; 
		//Iterates through inventory
		for(Map.Entry<String, Food> entry : FoodMap.entrySet()){
			//Checks if the food qty is low
			if(entry.getValue().qty <= inventoryLOW){
				ShoppingMap.put(entry.getValue().name, maxFoodQty - entry.getValue().qty);
			}
		}
		markets.get(marketCounter).msgRequestItems(this, ShoppingMap); 
		marketCounter++;
		if(marketCounter == totalMarkets){
			marketCounter = 0; 
		}
				 
	}
	
	private void Restock(){
		for(Map.Entry<String, Integer> orderItem : restockMap.entrySet()){
			//updates inventory
			int restockQty = FoodMap.get(orderItem.getKey()).qty + orderItem.getValue(); 
			//int updatedQty = inventory.get(orderItem.getKey()) + orderItem.getValue();  
			print("Restock qty: " + restockQty); 
			//increments market inventory
			FoodMap.get(orderItem.getKey()).setQty(restockQty);
			//inventory.put(orderItem.getKey(), updatedQty);
			print("New order inventory now: " + FoodMap.get(orderItem.getKey()).qty);  
			restockMap = null; 
		}
	}
	
	private void CookIt(final Order o){
		//animation call
		if(FoodMap.get(o.c).qty == 0){
			stockInventory = true; 
			o.w.msgOutOfChoice(o.c, o.table);
			orders.remove(o);
			return; 
		}
		if(FoodMap.get(o.c).qty <= inventoryLOW){
			print("inventory low"); 
			stockInventory = true;
		}
		
		print("Order inventory: " + FoodMap.get(o.c).qty); 
		
		o.s = state.cooking; 
		print("Cooking order");
		cookGui.DoGoToGrill(o.cookingPos);
		try{
			animationReady.acquire(); 
		}catch(Exception e){
			print("DoCookFood exception thrown"); 
		}
		cookGui.DoCookFood(o.c, o.cookingPos);
		cookGui.GoToHomePosition(); 
		timer.schedule(new TimerTask() {
			public void run() {
				print("DONE!!");
				o.s = state.done;
				//decrement food qty
				FoodMap.get(o.c).setQty(FoodMap.get(o.c).qty - 1);
				print("Order inventory now: " + FoodMap.get(o.c).qty); 
				stateChanged();
			}
		},
		FoodMap.get(o.c).cookingTime*20); 
	
	}
	
	private void PlateFood(Order o){
		cookGui.DoGoToGrill(o.cookingPos);
		try{
			animationReady.acquire(); 
		}catch(Exception e){
			print("PlateFood DoGoToGrill exception thrown"); 
		}
		cookGui.DoRemoveFoodFromGrill(o.cookingPos);
		print ("Plating food");
		cookGui.DoGoToPlatingArea(o.cookingPos);
//		try{
//			animationReady.acquire(); 
//		}catch(Exception e){
//			print("PlateFood DoGoToPlatingArea exception thrown"); 
//		}
		cookGui.DoPlateFood(o.c, o.cookingPos);
		o.w.msgOrderDone(o.c, o.table, o.cookingPos); 
		o.s = state.finished; 
		cookGui.GoToHomePosition();
	}

	public void ClearPlatingArea(){
		orderPickedUp = false; 
		for(Order o: orders){
			if(o.pickedUp == true){
				cookGui.DoRemoveFood(o.cookingPos); 
			}
		}
	}
	
	//Utilities
	public void addRStandOrder(MaggiyanWaiter w, String c, int t){
		rStandOrders.add(new Order(w, c, t)); 
	}
	
	public Order findOrder(int pos){
		for(Order o: orders){
			if(o.cookingPos == pos){
				return o; 
			}
		}
		return null; 
	}
	
	public void setGui(MaggiyanCookGui c){
		cookGui = c; 
	}
	
	private class Food{
		int cookingTime;
		int qty;
		String name; 
		
		Food(String n, int q, int ct){
			name = n; 
			qty = q;
			cookingTime = ct; 
		}
		
		void setQty(int q){
			qty = q; 
		}
		
	}
}
