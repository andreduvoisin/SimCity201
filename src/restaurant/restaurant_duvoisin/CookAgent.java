package restaurant.restaurant_duvoisin;

import java.util.*;
import java.util.concurrent.Semaphore;

import restaurant.restaurant_duvoisin.agent.Agent;
import restaurant.restaurant_duvoisin.gui.CookGui;
import restaurant.restaurant_duvoisin.interfaces.Cook;
import restaurant.restaurant_duvoisin.interfaces.Market;
import restaurant.restaurant_duvoisin.interfaces.Waiter;

/**
 * Restaurant Cook Agent
 */
public class CookAgent extends Agent implements Cook {
	List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	enum OrderState { Pending, Cooking, Done };
	Map<String, Food> foods = new HashMap<String, Food>();
	private String name;
	Boolean paused = false;
	Boolean orderFood;
	enum FoodState { None, Ordered, Rejected };
	Vector<Market> markets = new Vector<Market>();
	int currentMarket = 0;
	CookGui cookGui;
	Boolean cookHere[] = new Boolean[26];
	Boolean plateHere[] = new Boolean[26];
	
	private Semaphore atFridge = new Semaphore(0, true);
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atPlating = new Semaphore(0, true);

	public CookAgent(String name) {
		super();
		
		this.name = name;
		
		foods.put("steak", new Food("steak", 15000, 2, 1, 3, FoodState.None));
		foods.put("chicken", new Food("chicken", 12500, 2, 1, 3, FoodState.None));
		foods.put("salad", new Food("salad", 7500, 2, 1, 4, FoodState.None));
		foods.put("pizza", new Food("pizza", 10000, 2, 1, 3, FoodState.None));
		
		orderFood = true;
		stateChanged();
		
		for(int i = 0; i < cookHere.length; i++)
			cookHere[i] = false;
		for(int i = 0; i < plateHere.length; i++)
			plateHere[i] = false;
	}
	
	public String getName() {
		return name;
	}

	// Messages
	public void msgHereIsOrder(Waiter w, String choice, int table) {
		print("msgHereIsOrder received");
		orders.add(new Order(w, choice, table, OrderState.Pending));
		stateChanged();
	}
	
	public void msgFailedToFulfillRequest(Market ma, String item, int amount) {
		print("msgFailedToFulfillRequest received");
		foods.get(item).state = FoodState.Rejected;
		foods.get(item).rejectedAmount = amount;
		foods.get(item).rejectedMarkets.add(ma);
		stateChanged();
	}
	
	public void msgReplenishFood(String item, int amount) {
		print("msgReplenishFood received");
		foods.get(item).state = FoodState.None;
		foods.get(item).amount += amount;
		foods.get(item).rejectedAmount = 0;
		foods.get(item).rejectedMarkets.clear();
		stateChanged();
	}
	
	/*
	void msgTimerDone(Order o) {
		print("msgTimerDone received");
		o.state = OrderState.Done;
		stateChanged();
	}
	*/
	
	// maybe...?
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
	protected boolean pickAndExecuteAnAction() {
		if(!paused) {
			if(orderFood) {
				OrderFoodThatIsLow();
				return true;
			}
			for(Food f : foods.values())
				if(f.state == FoodState.Rejected) {
					OrderRejectedFood(f);
					return true;
				}
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
		}
		return false;
	}

	// Actions
	void TryToCookFood(Order o) {
		print("Doing TryToCookFood");
		Food f = foods.get(o.choice);
		if(f.amount <= 0) {
			o.waiter.msgOutOfFood(o.table, o.choice);
			orders.remove(o);
			return;
		}
		f.amount--;
		if(f.amount <= f.low)
			orderFood = true;
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
		o.cookThisOrder();
		cookGui.setCurrentOrder("");
		cookGui.DoGoWait();
	}
	
	void PlateFood(Order o) {
		print("Doing PlateFood");
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
		o.waiter.msgOrderIsReady(o.choice, o.table, o.position);
		orders.remove(o);
		cookGui.setCurrentOrder("");
		cookGui.DoGoWait();
	}
	
	void OrderFoodThatIsLow() {
		print("Doing OrderFoodThatIsLow");
		Map<String, Integer> foodToOrder = new HashMap<String, Integer>();
		for(Food f : foods.values())
			if(f.amount <= f.low) {
				foodToOrder.put(f.type, f.capacity - f.amount);
				f.state = FoodState.Ordered;
			}
		if(!foodToOrder.isEmpty()) {
			Market m = markets.get(currentMarket % markets.size());
			m.msgOrderFood(foodToOrder);
			currentMarket++;
		}
		orderFood = false;
	}
	
	void OrderRejectedFood(Food f) {
		print("Doing OrderRejectedFood");
		/*
		if(f.rejectedMarkets.size() == markets.size()) {
			f.state = FoodState.None;
			return;
		}
		*/
		for(int i = 0; i < markets.size(); i++)
			if(!f.rejectedMarkets.contains(markets.get(i))) {
				Map<String, Integer> foodToOrder = new HashMap<String, Integer>();
				foodToOrder.put(f.type, f.rejectedAmount);
				markets.get(i).msgOrderFood(foodToOrder);
				f.state = FoodState.Ordered;
				return;
			}
		f.state = FoodState.None;
	}

	// The animation DoXYZ() routines
	
	//utilities
	public void addMarket(Market m) { markets.add(m); }
	
	public void setGui(CookGui cg) { cookGui = cg; }
	
	class Order {
		Waiter waiter;
		String choice;
		int table;
		OrderState state;
		Timer timer;
		int position;
		
		Order(Waiter w, String c, int t, OrderState s) {
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
			}, foods.get(choice).cookingTime);
		}
	}
	
	class Food {
		String type;
		int cookingTime;
		int amount;
		int low;
		int capacity;
		FoodState state;
		int rejectedAmount;
		Vector<Market> rejectedMarkets = new Vector<Market>();
		
		Food(String t, int cT, int a, int l, int c, FoodState s) {
			type = t;
			cookingTime = cT;
			amount = a;
			low = l;
			capacity = c;
			state = s;
		}
	}
}