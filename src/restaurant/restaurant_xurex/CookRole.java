package restaurant.restaurant_xurex;

import base.BaseRole;
import base.interfaces.Person;
import restaurant.restaurant_xurex.gui.CookGui;
import restaurant.restaurant_xurex.gui.RestaurantGui;
import restaurant.restaurant_xurex.interfaces.Cook;
import restaurant.restaurant_xurex.interfaces.Market;
import restaurant.restaurant_xurex.interfaces.Waiter;

import java.util.*;
//import java.util.concurrent.Semaphore;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cook Agent
 */

public class CookRole extends BaseRole implements Cook {
	public enum OrderState
	{pending, cooking, cooked, served};
	public enum MarketOrderState
	{pending, ready, completed}; //Can use boolean instead
	
	private Semaphore atLocation = new Semaphore(100,true);
	private CookGui cookGui = null;
	RestaurantGui gui;
	
	public class CookOrder{
		Waiter w;
		String choice;
		int table;
		int kitchenNum = 0;
		public OrderState s;
		
		CookOrder(Waiter w, String choice, int table){
			this.w=w; this.choice=choice; this.table=table; s=OrderState.pending;
		}
	}
	public class MarketOrder{
		String market;
		public Map<String, Integer> provided = new HashMap<String, Integer>();
		public MarketOrderState state;
		//pending, ready, completed
		MarketOrder(Market market){
			this.market = market.getName();
			this.state  = MarketOrderState.pending;
		}
	}
	public class Food{
		String type;
		int low;
		public int quantity;
		int capacity;
		int cookingTime;
		boolean orderState = false;
		Food (String type, int low, int quantity, int capacity, int cookingTime){
			this.type=type; this.low=low; this.quantity=quantity; this.capacity=capacity;
			this.cookingTime=cookingTime;
		}
	}
	
	Timer cookTimer = new Timer();
	Timer standTimer = new Timer();
	
	private String name;
	
	public Map<String, Food> Inventory = new HashMap<String, Food>();
	private Map<String, Integer> foodToOrder = new HashMap<String, Integer>();
	
	private Map<Integer, Boolean> Kitchen = new HashMap<Integer, Boolean>();
	
	public List<CookOrder> orders = Collections.synchronizedList(new ArrayList<CookOrder>());
	public List<CookOrder> revolvingStand = Collections.synchronizedList(new ArrayList<CookOrder>());
	public List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	//AGENT CORRESPONDENTS
	public List<Market> markets = new ArrayList<Market>();

	//CONSTRUCTORS
	private void initializeInventory(){
		//Food Constructor(String food, Low Threshold, Initial Quantity, Capacity, Cook Time)//
		Inventory.put("Steak", 	 new Food("Steak",   5, 10, 15, 15));
		Inventory.put("Chicken", new Food("Chicken", 5, 10, 15, 10));
		Inventory.put("Salad", 	 new Food("Salad",   5, 2, 15, 5));
		Inventory.put("Pizza",	 new Food("Pizza",   5, 10, 15, 20));
	}
	public CookRole(){
		super();
		initializeInventory();
		for(int i=1; i<11; i++){
			Kitchen.put(new Integer(i), false);
		}
		runTimer();
	}
	public CookRole(String name, Person person) {
		super(person);
		this.name = name;
		initializeInventory();
		for(int i=1; i<11; i++){
			Kitchen.put(new Integer(i), false);
		}
		runTimer();
	}
	public CookRole(String name, String food, Person person) {
		super(person);
		this.name = name;
		initializeInventory();
		Food changedQuantity = Inventory.get(food);
		changedQuantity.quantity = 1;
		if(food.equals("Steak"))
			Inventory.put("Steak", changedQuantity);
		else if(food.equals("Chicken"))
			Inventory.put("Chicken", changedQuantity);
		else if(food.equals("Salad"))
			Inventory.put("Salad", changedQuantity);
		else if(food.equals("Pizza"))
			Inventory.put("Pizza", changedQuantity);
		else{
			//String food is not a food type
		}
		for(int i=1; i<11; i++){
			Kitchen.put(new Integer(i), false);
		}
		runTimer();
	}

	// MESSAGES
	public void HereIsOrder(Waiter w, String choice, int table){
		orders.add(new CookOrder(w,choice,table)); stateChanged();
	}
	void TimerDone (CookOrder o){
		o.s = OrderState.cooked; stateChanged();
	}
	public void MarketCanFulfill(Market market, Map<String, Integer> provided){
		Do(market.getName()+" can fulfill");
		synchronized(marketOrders){
		for(MarketOrder mo : marketOrders){
			if(mo.market.equals(market.getName())){
				mo.provided = provided;
			}
		}
		}
		foodToOrder.clear();
	}
	public void MarketCannotFulfill(Market market, Map<String, Integer> provided){
		Do(market.getName()+" cannot fulfill");
		synchronized(marketOrders){
		for(MarketOrder mo : marketOrders){
			if(mo.market.equals(market.getName())){
				mo.provided = provided;
			}
		}
		}
		for(String food : foodToOrder.keySet()){
			foodToOrder.put(food, foodToOrder.get(food)-provided.get(food));
		}
		for(int i=0; i<markets.size(); i++){
			if(markets.get(i)==market&&(i+1)<markets.size()){
				marketOrders.add(new MarketOrder(markets.get(i+1)));
				markets.get(i+1).HereIsOrder(foodToOrder); return;
			}
		}
	}
	public void OrderIsReady(Market market){
		synchronized(marketOrders){
		for(MarketOrder marketOrder : marketOrders){
			if(marketOrder.market.equals(market.getName())){
				marketOrder.state = MarketOrderState.ready;
			}
		}
		}
		stateChanged();
	}
	public void msgAtLocation() {//from animation
		atLocation.release();// = true;
		stateChanged();
	}
	public void PickedUp(int kitchen) {
		Kitchen.put(kitchen, false);
		cookGui.DoRemoveServe(kitchen);
	}
	
	//Called from looped timer in Cook
	private void msgCheckStand(){
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(orders){
		for (CookOrder order: orders){
			if(order.s==OrderState.cooked){
				ServeOrder(order);
				return true;
			}
			if(order.s==OrderState.pending){
				TryToCookOrder(order);
				return true;
			}
		}
		}
		synchronized(marketOrders){
		for (MarketOrder marketOrder : marketOrders){
			if(marketOrder.state == MarketOrderState.ready){
				RefillInventory(marketOrder);
				return true;
			}
		}
		}
		
		synchronized(revolvingStand){
			if(!revolvingStand.isEmpty()){
			for (CookOrder order : revolvingStand){
				orders.add(order);
				revolvingStand.remove(order);
				return true;
			}
			}
		}
		
		if(!cookGui.atHome()){
			cookGui.DoGoHome();
		}
		
		return false;
	}

	// ACTIONS
	private void ServeOrder(CookOrder o){
		DoServe(o);
		o.w.OrderIsReady(o.choice, o.table, o.kitchenNum);
		o.s=OrderState.served;
	}
	
	private void TryToCookOrder(CookOrder o){ 
		Food f = Inventory.get(o.choice);
		if(f.quantity == 0){
			Do("Out of Food message sent"); 
			o.w.OutOfFood(o.table, o.choice);
			orders.remove(o); return;
		}
		f.quantity--;
		gui.updateInventory();
		CheckInventory();
		o.s = OrderState.cooking;
		DoCooking(o);
		final CookOrder temp = o; //Need final variable to use in TimerTask.run()
		cookTimer.schedule(new TimerTask(){
			public void run(){
				TimerDone(temp);
			}
		}, Inventory.get(o.choice).cookingTime*500);
	}
	
	private void RefillInventory(MarketOrder mo){
		mo.state = MarketOrderState.completed;
		for(String food : mo.provided.keySet()){
			Food newValue = Inventory.get(food);
			newValue.quantity += mo.provided.get(food);
			newValue.orderState= false;
			Inventory.put(food, newValue);
		}
		gui.updateInventory();
	}
	
	//ANIMATIONS
	private void DoServe(CookOrder o){
		DoGoToTable(o.kitchenNum);
		cookGui.DoRemoveOrder(o.kitchenNum);
		Kitchen.put(o.kitchenNum, false);
		for(int i=6; i<11; i++){
			if(!Kitchen.get(i)){
				o.kitchenNum = i;
				DoGoToTable(i);
				cookGui.DoDisplayServe(o.choice, i);
				Kitchen.put(i, true);
				break;
			}
		}
		
	}
	
	private void DoCooking(CookOrder o){
		DoGoRef();
		for(int i=1; i<6; i++){
			if(!Kitchen.get(i)){
				o.kitchenNum = i;
				DoGoToTable(i);
				Kitchen.put(i, true);
				cookGui.DoDisplayOrder(o.choice, i);
				break;
			}
		}
	}
	
	//ANIMATION CALLS
	private void DoGoToTable(int table){
		cookGui.DoGoToTable(table);
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void DoGoRef(){
		cookGui.DoGoRef();
		try {
			atLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//UTILITIES
	private void CheckInventory(){
		boolean foodNeeded = false;
		for(Food f : Inventory.values()){
			if(f.quantity<=f.low&&f.orderState==false){
				foodToOrder.put(f.type, (f.capacity-f.quantity)); //Orders as much as possible
				f.orderState=true; foodNeeded = true;
			}
		}
		if(foodNeeded){
			for(String food : foodToOrder.keySet()){
				Do(foodToOrder.get(food)+" "+food+"s ordered");
			}
			marketOrders.add(new MarketOrder(markets.get(0)));
			markets.get(0).HereIsOrder(foodToOrder);
		}
	}
	
	public void addMarket(Market market){
		markets.add(market);
	}

	public int getQuantity(String food){
		return Inventory.get(food).quantity;
	}
	public String getName() {
		return name;
	}
	public void setGui(RestaurantGui gui){
		this.gui = gui;
	}
	
	public void setGui(CookGui cookGui){
		this.cookGui = cookGui;
	}
	private void runTimer(){
		standTimer.schedule(new TimerTask(){
			public void run(){
				msgCheckStand();
				runTimer();
			}
		}, 25000);
	}
	@Override
	public void addToStand(Waiter w, String choice, int table) {
		revolvingStand.add(new CookOrder(w, choice, table));
	}

}

