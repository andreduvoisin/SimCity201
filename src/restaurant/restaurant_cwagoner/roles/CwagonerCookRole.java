package restaurant.restaurant_cwagoner.roles;

import base.BaseRole;
import base.interfaces.Person;
import restaurant.restaurant_cwagoner.gui.*;
import restaurant.restaurant_cwagoner.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;


public class CwagonerCookRole extends BaseRole implements CwagonerCook {
	
	public CwagonerCookRole(Person person) {
		super(person);

		// Name of food, cooking time (ms), initial quantity of food, maximum capacity of food
		addMenuItem("Steak",	8000, 1, 5);
		addMenuItem("Chicken",	6000, 1, 4);
		addMenuItem("Salad",	2000, 1, 3);
		addMenuItem("Pizza",	4000, 1, 2);
	}
	
	public String getName() {
		return "CwagonerCook " + mPerson.getName();
	}
	
	CwagonerCashier cwagonerCashier;
	CwagonerCookGui gui;
	private Semaphore animationFinished = new Semaphore(0, true);
	
	// Remembers which market was previously ordered from
	int marketNum = 0;
	
	// Remembers if currently ordering
	boolean ordering = false;
	
	
	// DATA

	// Orders uses try-catch (method 2) instead of 'synchronized'
	public List<Order> Orders = new ArrayList<Order>();
	HashMap<String, MenuItem> MenuItems = new HashMap<String, MenuItem>();
	List<CwagonerMarket> Markets =
			Collections.synchronizedList(new ArrayList<CwagonerMarket>());
	
	Timer cookingTimer = new Timer();
	
	
	// MESSAGES
	
	// From GUI
	public void msgAnimationFinished() {
		animationFinished.release();
	}
	
	// From waiter delivering customer's order
	public void msgHeresAnOrder(CwagonerWaiter w, int tableNum, String food) {
		print("Received msgHeresAnOrder(" + w.getName() + ", table " + tableNum + ", " + food + ")");
		
		Orders.add(new Order(w, tableNum, food));
		stateChanged();
	}
	
	// From market (completely out of food - remove from list)
	public void msgOutOfFood(CwagonerMarket m) {
		print("Received msgOutOfFood(" + m.getName() + ")");
		
		Markets.remove(m);
		ordering = false;
		stateChanged();
	}
	
	// From market (can't fulfill order)
	public void msgCantFulfillOrder(CwagonerMarket m) {
		print("Received msgCantFulfillOrder(" + m.getName() + ")");

		ordering = false;
		stateChanged();
	}
	
	// From market (fulfills at least part of order)
	public void msgOrderFulfilled(HashMap<String, Integer> fulfillList) {
		print("Received msgOrderFulfilled");

		for (String food : fulfillList.keySet()) {
			addStock(food, fulfillList.get(food));
		}

		ordering = false;
		stateChanged();
	}
	
	// From cashier - didn't pay market for order, so remove from list
	public void msgDontOrderFrom(CwagonerMarket m) {
		Markets.remove(m);
		if (marketNum == Markets.size()) {
			marketNum = 0;
		}
	}
	
	
	// SCHEDULER
	
	public boolean pickAndExecuteAnAction() {
		
		// If not ordering/already ordered
		if (! ordering) {
			
			// Check for markets
			if (Markets.size() > 0) {
				
				// Check for low quantities
				for (String food : MenuItems.keySet()) {
					if (MenuItems.get(food).quantity < 2) {
						OrderStock();
						return true;
					}
				}
			}
		}
		
		try {
			for (Order o : Orders) {
				if (o.state.equals(Order.State.readyToDeliver)) {
					AlertWaiter(o);
					return true;
				}
			}
		
			for (Order o : Orders) {
				if (o.state.equals(Order.State.received)) {
					Prepare(o);
					return true;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
			
		return false;
	}
	
	
	// ACTIONS
	
	private void OrderStock() {
		print("OrderStock()");
		
		ordering = true;
		
		HashMap<String, Integer> request = new HashMap<String, Integer>();
		
		for (String food : MenuItems.keySet()) {
			int numToOrder = MenuItems.get(food).capacity - MenuItems.get(food).quantity;
			if (numToOrder > 0) {
				print("Adding to order: " + numToOrder + " " + food + "s");
				request.put(food, numToOrder);
			}
		}
		
		Markets.get(marketNum % Markets.size()).msgNeedFood(this, cwagonerCashier, request);
		
		marketNum++;
		stateChanged();
	}
	
	private void AlertWaiter(Order o) {
		print("AlertWaiter() " + o.waiter.getName() + ", table " + o.tableNum + ", " + o.food);

		
		// Move food from cooking to plating
		gui.DoGoToCooking();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		gui.DoDrawFood(o.food.substring(0, 2));
		gui.DoGoToPlating();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		
		gui.DoClearFood();
		gui.DoGoToHomePosition();
		
		o.waiter.msgOrderReady(o.tableNum);
		Orders.remove(o);
		stateChanged();
	}
	
	private void Prepare(final Order o) {
		print("Prepare(" + o.waiter.getName() + ", table " + o.tableNum + ", " + o.food +")");
		
		o.state = Order.State.cooking;
		
		
		// Check if food exists in fridge
		gui.DoDrawFood(o.food.substring(0, 2) + "?");
		gui.DoGoToFridge();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}

		
		if (quantityRemainingOf(o.food) == 0) {
			o.waiter.msgOutOfFood(o.tableNum);
			gui.DoClearFood();
			gui.DoGoToHomePosition();
			Orders.remove(o);
			return;
		}
		preparedOneOf(o.food);
		
		
		// Take food to cooking area
		gui.DoDrawFood(o.food.substring(0, 2));
		gui.DoGoToCooking();
		try { animationFinished.acquire(); } catch (InterruptedException e) {}
		gui.DoClearFood();
		gui.DoGoToHomePosition();
		
		// Schedule timer task based on correct hashmap times
		cookingTimer.schedule(new TimerTask() {
			public void run() {
				o.state = Order.State.readyToDeliver;
				stateChanged();
			}
		}, cookTimeOf(o.food));
		// Timer finishes, changing order state to ready

		stateChanged();
	}
	
	
	// CLASSES
	
	public static class Order {
		private CwagonerWaiter waiter;
		private int tableNum;
		private String food;
		public enum State { received, cooking, readyToDeliver };
		private State state;
		
		public Order(CwagonerWaiter w, int table, String choice) {
			waiter = w;
			tableNum = table;
			food = choice;
			state = State.received;
		}
	}

	private class MenuItem {
		int cookTime;
		int quantity;
		int capacity;
		
		/**
		 * @param time Cooking/Preparation time of the given food
		 * @param q Starting quantity of the given food
		 * @param c Maximum Capacity of the given food
		 */
		MenuItem(int time, int q, int c) {
			cookTime = time;
			quantity = q;
			capacity = c;
		}
	}
	
	
	// MenuItems accessors
	
	private void addMenuItem(String food, int cookTime, int startingQuantity, int capacity) {
		MenuItems.put(food, new MenuItem(cookTime, startingQuantity, capacity));
	}
	
	private int cookTimeOf(String food) {
		return MenuItems.get(food).cookTime;
	}
	
	private int quantityRemainingOf(String food) {
		return MenuItems.get(food).quantity;
	}
	
	private int capacityOf(String food) {
		return MenuItems.get(food).capacity;
	}
	
	private void preparedOneOf(String food) {
		MenuItems.get(food).quantity--;
	}
	
	// Adds quantity delivered by market to current quantity
	private void addStock(String food, int quantity) {
		MenuItems.put(food, new MenuItem(cookTimeOf(food),
										 quantity + quantityRemainingOf(food),
										 capacityOf(food)));
	}
	
	
	// Market-adding hack (from GUI)
	public void addMarket(CwagonerMarket m) {
		Markets.add(m);
		stateChanged();
	}
	
	public void setCashier(CwagonerCashier c) {
		cwagonerCashier = c;
	}
	
	public void setGui(CwagonerCookGui g) {
		gui = g;
	}
}
