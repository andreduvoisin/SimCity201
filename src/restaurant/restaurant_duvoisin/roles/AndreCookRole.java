package restaurant.restaurant_duvoisin.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.intermediate.RestaurantCookRole;
import restaurant.restaurant_duvoisin.gui.CookGui;
import restaurant.restaurant_duvoisin.interfaces.Cook;
import restaurant.restaurant_duvoisin.interfaces.Waiter;
import restaurant.restaurant_duvoisin.test.mock.EventLog;
import restaurant.restaurant_duvoisin.test.mock.LoggedEvent;
import base.ContactList;
import base.Item;
import base.Item.EnumItemType;
import base.Location;

/**
 * Restaurant Cook Agent
 */
public class AndreCookRole extends RestaurantCookRole implements Cook {
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Order> revolvingStand = Collections.synchronizedList(new ArrayList<Order>());
	public enum OrderState { Pending, Cooking, Done };
//	Map<String, Food> foods = new HashMap<String, Food>();
	private String name;
	Boolean paused = false;
//	Boolean orderFood;
//	enum FoodState { None, Ordered, Rejected };
//	Vector<Market> markets = new Vector<Market>();
//	int currentMarket = 0;
	CookGui cookGui;
	Boolean cookHere[] = new Boolean[26];
	Boolean plateHere[] = new Boolean[26];
	
	Boolean checkRevolvingStand;
	
	Timer timer = new Timer();
	
	public Semaphore atFridge = new Semaphore(0, true);
	public Semaphore atGrill = new Semaphore(0, true);
	public Semaphore atPlating = new Semaphore(0, true);
	public Semaphore atStand = new Semaphore(0, true);
	
	static final int FOOD_LOW = 2;
	static final int FOOD_ORDER = 5;
	Map<String, Boolean> hasOrdered = new HashMap<String, Boolean>();
	Map<String, Integer> cookingTimes = new HashMap<String, Integer>();
	
	public EventLog log = new EventLog();

	public AndreCookRole(String name) {
		super(null, 0);
		
		this.name = name;
		
//		foods.put("steak", new Food("steak", 1000, 2, 1, 3, FoodState.None));
//		foods.put("chicken", new Food("chicken", 1000, 2, 1, 3, FoodState.None));
//		foods.put("salad", new Food("salad", 1000, 2, 1, 4, FoodState.None));
//		foods.put("pizza", new Food("pizza", 1000, 2, 1, 3, FoodState.None));
		
//		orderFood = true;
		
		//stateChanged();
		
		for(int i = 0; i < cookHere.length; i++)
			cookHere[i] = false;
		for(int i = 0; i < plateHere.length; i++)
			plateHere[i] = false;
		
		checkRevolvingStand = false;
		
		mItemInventory.put(EnumItemType.STEAK,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.CHICKEN,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.SALAD,DEFAULT_FOOD_QTY);
        mItemInventory.put(EnumItemType.PIZZA,DEFAULT_FOOD_QTY);
        
        cookingTimes.put("steak", 10000);
        cookingTimes.put("chicken", 7500);
        cookingTimes.put("salad", 5000);
        cookingTimes.put("pizza", 7500);
        
        hasOrdered.put("steak", false);
        hasOrdered.put("chicken", false);
        hasOrdered.put("salad", false);
        hasOrdered.put("pizza", false);
        // ANDRE: hasOrdered never gets set back to false... ask Angel
	}
	
	public void runStandTimer() {
		timer.schedule(new TimerTask() {
			public void run() {
				checkRevolvingStand = true;
				stateChanged();
			}
		}, 10000);
	}
	
	public String getName() {
		return name;
	}

	// Messages
	public void msgHereIsOrder(Waiter w, String choice, int table) {
		//print("msgHereIsOrder received");
		orders.add(new Order(w, choice, table, OrderState.Pending));
		stateChanged();
	}
	
//	public void msgFailedToFulfillRequest(Market ma, String item, int amount) {
//		//print("msgFailedToFulfillRequest received");
//		foods.get(item).state = FoodState.Rejected;
//		foods.get(item).rejectedAmount = amount;
//		foods.get(item).rejectedMarkets.add(ma);
//		stateChanged();
//	}
//	
//	public void msgReplenishFood(String item, int amount) {
//		//print("msgReplenishFood received");
//		foods.get(item).state = FoodState.None;
//		foods.get(item).amount += amount;
//		foods.get(item).rejectedAmount = 0;
//		foods.get(item).rejectedMarkets.clear();
//		stateChanged();
//	}
	
	/*
	void msgTimerDone(Order o) {
		print("msgTimerDone received");
		o.state = OrderState.Done;
		stateChanged();
	}
	*/
	
	public void msgGotFood(int position) {
		plateHere[position] = false;
		cookGui.removePlatedItem(position);
		stateChanged();
	}
	
	public void msgAtFridge() {
		atFridge.release();
		stateChanged();
	}
	
	public void msgAtGrill() {
		atGrill.release();
		stateChanged();
	}
	
	public void msgAtPlating() {
		atPlating.release();
		stateChanged();
	}
	
	public void msgAtStand() {
		atStand.release();
		stateChanged();
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
//			if(orderFood) {
//				OrderFoodThatIsLow();
//				return true;
//			}
//			for(Food f : foods.values())
//				if(f.state == FoodState.Rejected) {
//					OrderRejectedFood(f);
//					return true;
//				}
			synchronized(orders) {
				for(Order o : orders)
					if(o.state == OrderState.Done) {
						PlateFood(o);
						return true;
					}
			}
			synchronized(orders) {
				for(Order o : orders)
					if(o.state == OrderState.Pending) {
						TryToCookFood(o);
						return true;
					}
			}
			if(checkRevolvingStand) {
				CheckRevolvingStand();
				return true;
			}
		}
		return false;
	}

	// Actions
	void TryToCookFood(Order o) {
		//print("Doing TryToCookFood");
		log.add(new LoggedEvent("Doing TryToCookFood"));
		int foodAmount = mItemInventory.get(Item.stringToEnum(o.choice));
		if(foodAmount <= 0) {
			o.waiter.msgOutOfFood(o.table, o.choice);
			orders.remove(o);
			return;
		}
		decreaseInventory(Item.stringToEnum(o.choice));
		//ANDRE: Null Pointer Exception in integration?
		if(foodAmount <= FOOD_LOW /*&& !hasOrdered.get(o.choice)*/) {
			mItemsDesired.put(Item.stringToEnum(o.choice), mItemsDesired.get(Item.stringToEnum(o.choice)) + FOOD_ORDER);
			hasOrdered.put(o.choice, true);
		}
		o.state = OrderState.Cooking;
		for(int i = 0; i < cookHere.length; i++)
			if(cookHere[i] == false) {
				o.position = i;
				cookHere[i] = true;
				break;
			}
		cookGui.DoGoToFridge();
		try {
			atFridge.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.setCurrentOrder(o.choice);
		cookGui.DoCooking(o.position);
		try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.DoGoWait();
		o.cookThisOrder();
		cookGui.setCurrentOrder("");
	}
	
	void PlateFood(Order o) {
		//print("Doing PlateFood");
		cookGui.DoCooking(o.position);
		try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.setCurrentOrder(o.choice);
		cookHere[o.position] = false; 
		for(int i = 0; i < plateHere.length; i++)
			if(plateHere[i] == false) {
				o.position = i;
				plateHere[i] = true;
				break;
			}
		cookGui.DoPlating(o.position);
		try {
			atPlating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.DoGoWait();
		o.waiter.msgOrderIsReady(o.choice, o.table, o.position);
		orders.remove(o);
		cookGui.setCurrentOrder("");
	}
	
//	void OrderFoodThatIsLow() {
//		//print("Doing OrderFoodThatIsLow");
//		Map<String, Integer> foodToOrder = new HashMap<String, Integer>();
//		for(Food f : foods.values())
//			if(f.amount <= f.low) {
//				foodToOrder.put(f.type, f.capacity - f.amount);
//				f.state = FoodState.Ordered;
//			}
//		if(!foodToOrder.isEmpty()) {
//			Market m = markets.get(currentMarket % markets.size());
//			m.msgOrderFood(foodToOrder);
//			currentMarket++;
//		}
//		orderFood = false;
//	}
	
//	void OrderRejectedFood(Food f) {
//		//print("Doing OrderRejectedFood");
//		/*
//		if(f.rejectedMarkets.size() == markets.size()) {
//			f.state = FoodState.None;
//			return;
//		}
//		*/
//		for(int i = 0; i < markets.size(); i++)
//			if(!f.rejectedMarkets.contains(markets.get(i))) {
//				Map<String, Integer> foodToOrder = new HashMap<String, Integer>();
//				foodToOrder.put(f.type, f.rejectedAmount);
//				markets.get(i).msgOrderFood(foodToOrder);
//				f.state = FoodState.Ordered;
//				return;
//			}
//		f.state = FoodState.None;
//	}
	
	public void CheckRevolvingStand() {
		//print("Doing CheckRevolvingStand");
		log.add(new LoggedEvent("Doing CheckRevolvingStand"));
		checkRevolvingStand = false;
		cookGui.DoGoToRevolvingStand();
		try {
			atStand.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized(revolvingStand) {
			synchronized(orders) {
				while(!revolvingStand.isEmpty()) {
					orders.add(revolvingStand.remove(0));
					//print("o: " + orders.size() + " && rS: " + revolvingStand.size());
				}
			}
		}
		cookGui.DoGoWait();
		runStandTimer();
	}
	
	//utilities
//	public void addMarket(Market m) { markets.add(m); }
	
	public void setGui(CookGui cg) { cookGui = cg; }
	
	public List<Order> getRevolvingStand() { return revolvingStand; }
	
	public class Order {
		Waiter waiter;
		String choice;
		int table;
		OrderState state;
		Timer timer;
		int position;
		
		public Order(Waiter w, String c, int t, OrderState s) {
			waiter = w;
			choice = c;
			table = t;
			state = s;
			timer = new Timer();
		}
		
		public void cookThisOrder() {
			timer.schedule(new TimerTask() {
				public void run() {
					state = OrderState.Done;
					stateChanged();
				}
			}, cookingTimes.get(choice));
		}
	}
	
//	class Food {
//		String type;
//		int cookingTime;
//		int amount;
//		int low;
//		int capacity;
//		FoodState state;
//		int rejectedAmount;
//		Vector<Market> rejectedMarkets = new Vector<Market>();
//		
//		Food(String t, int cT, int a, int l, int c, FoodState s) {
//			type = t;
//			cookingTime = cT;
//			amount = a;
//			low = l;
//			capacity = c;
//			state = s;
//		}
//	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(0);
	}
}