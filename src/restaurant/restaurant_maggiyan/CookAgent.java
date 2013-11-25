package restaurant_maggiyan;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

import restaurant_maggiyan.gui.CookGui;
import restaurant_maggiyan.interfaces.Cook;
import restaurant_maggiyan.interfaces.Market;
import restaurant_maggiyan.interfaces.Waiter;


public class CookAgent extends Agent implements Cook{
	private String n; 
	private int foodCookingTime = 10; 
	private int maxFoodQty = 4; 
	private int inventoryLOW = 2; 
	private int marketCounter = 0; 
	private int totalMarkets = 3; 
	private boolean stockInventory = false; 
	private boolean allMarketsClosed = false;
	private int cookingPosition = 0; 
	private boolean orderPickedUp = false; 
	
	private Semaphore animationReady = new Semaphore(0, true);
	
	private List<Market> markets = new ArrayList<Market>(); 
	public enum state {pending, cooking, done, finished};
	
	public Timer timer = new Timer(); 
	CookGui cookGui; 
	
	private Map<String, Food> FoodMap = new HashMap<String, Food>();
	private Map<String, Integer> ShoppingMap = new HashMap<String, Integer>();
	public Map<String, Integer> restockMap = null;  
	
	List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	
	public CookAgent(String name){
		this.n = name;
		
		Food steak = new Food("Steak", inventoryLOW, foodCookingTime* 50);
		Food chicken = new Food("Chicken", maxFoodQty, foodCookingTime*75);
		Food salad = new Food("Salad", inventoryLOW, foodCookingTime*20);
		Food pizza = new Food("Pizza", maxFoodQty, foodCookingTime*30);
		
		FoodMap.put("Steak", steak);
		FoodMap.put("Chicken", chicken);
		FoodMap.put("Salad", salad);
		FoodMap.put("Pizza", pizza);
		
		startThread(); 
	}
	
	public String getName(){
		return n; 
	}
	
	public void setMarket(Market m){
		markets.add(m);
//		print("Markets size: " + markets.size());
		stockInventory = true; 
		stateChanged();
	}

	
	// Messages
	
	//From Waiter
	public void msgHereIsOrder(Waiter w, String choice, int table)
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
	
	public void msgOutOfAllInventory(Market m){
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
	protected boolean pickAndExecuteAnAction() {
		
		if(stockInventory){
			StockInventory(); 
			return true; 
		}
		
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
		if(restockMap != null){
			Restock(); 
			return true; 
		}
		
		return false; 
	}

	// Actions
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
	public Order findOrder(int pos){
		for(Order o: orders){
			if(o.cookingPos == pos){
				return o; 
			}
		}
		return null; 
	}
	
	public void setGui(CookGui c){
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
	
	private class Order{
		Order(Waiter waiter, String choice, int tableNum){
			w = waiter;
			c = choice; 
			table = tableNum; 
			s = state.pending;
			pickedUp = false; 
			if(cookingPosition < 3){
				cookingPos = cookingPosition; 
				cookingPosition++; 
			}
			
		}
		boolean pickedUp; 
		int cookingPos; 
		Waiter w; 
		String c; 
		int table; 
		state s; 
	}
}
