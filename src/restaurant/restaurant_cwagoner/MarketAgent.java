package restaurant.restaurant_cwagoner;

import restaurant.restaurant_cwagoner.agent.Agent;
import restaurant.restaurant_cwagoner.interfaces.*;

import java.util.*;

public class MarketAgent extends Agent implements Market {
	
	String name;
	// Generates order preparation times & initial quantities
	Random rand = new Random();
	
	// Start with 0-4 of each food
	public MarketAgent(int num) {
		name = "Market" + num;
		money = 0.0;
		
		myStock.put("Steak",	new Stock(Math.abs(rand.nextInt()) % 8, 4));
		myStock.put("Chicken",	new Stock(Math.abs(rand.nextInt()) % 8, 3));
		myStock.put("Salad",	new Stock(Math.abs(rand.nextInt()) % 8, 1));
		myStock.put("Pizza",	new Stock(Math.abs(rand.nextInt()) % 8, 2));

		print("Born w/" + myStock.get("Steak").quantity + " St");
		print("Born w/" + myStock.get("Chicken").quantity + " Ch");
		print("Born w/" + myStock.get("Salad").quantity + " Sa");
		print("Born w/" + myStock.get("Pizza").quantity + " Pi");
	}
	
	// DATA
	
	double money;

	// Market's inventory
	HashMap<String, Stock> myStock = new HashMap<String, Stock>();
	
	// Stores orders from cook
	List<Order> Orders =
			Collections.synchronizedList(new ArrayList<Order>());
	
	Timer timer = new Timer();
	
	
	// MESSAGES
	
	// From cook
	public void msgNeedFood(Cook c, Cashier ca, HashMap<String, Integer> orderedStock) {
		print("Received msgNeedFood");
		
		Orders.add(new Order(c, ca, orderedStock));
		stateChanged();
	}
	
	// From cashier
	public void msgPayment(Cashier ca, double payment) {
		print("Received msgPayment(cashier, $" + payment + ")");
		
		synchronized(Orders) {
			for (Order o : Orders) {
				if (o.cashier.equals(ca)) {
					money += payment;
					
					if (payment >= o.price) {
						Orders.remove(o);
						break;
					}
					else {
						o.state = Order.State.unpaid;
						break;
					}
				}
			}
		}

		stateChanged();
	}
	
	
	// SCHEDULER
	
	protected boolean pickAndExecuteAnAction() {

		synchronized(Orders) {
			for (Order o : Orders) {
				if (o.state.equals(Order.State.unpaid)) {
					StopSellingToRestaurant(o);
					return true;
				}
			}
		}
			
		synchronized(Orders) {
			for (Order o : Orders) {
				if (o.state.equals(Order.State.delivered)) {
					ChargeForOrder(o);
					return true;
				}
			}
		}
			
		synchronized(Orders) {
			for (Order o : Orders) {
				if (o.state.equals(Order.State.prepared)) {
					DeliverOrder(o);
					return true;
				}
			}
		}
			
		synchronized(Orders) {
			for (Order o : Orders) {
				if (o.state.equals(Order.State.received)) {
					Prepare(o);
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	// ACTIONS
	
	private void StopSellingToRestaurant(Order o) {
		print("StopSellingToRestaurant()");
		
		o.cashier.msgDontOrderAgain(this);
		Orders.remove(o);
		stateChanged();
	}
	
	private void ChargeForOrder(Order o) {
		print("ChargeForOrder()");
		
		o.state = Order.State.charged;
		o.cashier.msgPayForOrder(this, o.price);
		stateChanged();
	}
	
	private void DeliverOrder(final Order o) {
		print("DeliverOrder()");
		
		o.state = Order.State.delivering;
		
		timer.schedule(new TimerTask() {
			public void run() {
				o.state = Order.State.delivered;
				o.cook.msgOrderFulfilled(o.fulfillList);
				stateChanged();
			}
		}, 1000 * (Math.abs(rand.nextInt()) % 10 + 10));
		
		stateChanged();
	}
	
	private void Prepare(final Order o) {
		print("Prepare()");
		
		o.state = Order.State.preparing;
		
		int i = 1;
		for (String food : o.requestList.keySet()) {
			// If market has at least one requested item
			// (of which at least one was requested), continue preparing
			if (o.requestList.get(food) > 0
				&& quantityRemainingOf(food) > 0) break;
			
			// If out of all requested items, tell cook, and stop trying to prepare
			if (i == o.requestList.size()) {
				o.cook.msgCantFulfillOrder(this);
				Orders.remove(o);
				return;
			}
			i++;
		}
		
		// Fill fulfillList with whatever market can deliver
		for (String food : o.requestList.keySet()) {
			if (quantityRemainingOf(food) >= o.requestList.get(food)) {
				print("Adding " + o.requestList.get(food) + " " + food + "s to shipment");
				o.fulfillList.put(food, o.requestList.get(food));
			}
			else {
				print("Adding " + quantityRemainingOf(food) + " " + food + "s to shipment");
				o.fulfillList.put(food, quantityRemainingOf(food));
			}
		}
		
		// Remove given parts of order from (this) market's inventory
		for (String food : o.fulfillList.keySet()) {
			removeInventory(food, o.fulfillList.get(food));
			o.price += priceOf(food) * o.fulfillList.get(food);
		}
		
		timer.schedule(new TimerTask() {
			public void run() {
				for (String food : o.fulfillList.keySet()) {
					print("Readying " + o.fulfillList.get(food) + " " + food + "s");
				}
				o.state = Order.State.prepared;
				stateChanged();
			}
		}, 1000 * (Math.abs(rand.nextInt()) % 10 + 5));

		stateChanged();
	}
	
	
	// CLASSES
	
	public class Stock {
		int quantity;
		double price;
		
		Stock(int q, double p) {
			quantity = q;
			price = p;
		}
	}
	
	private static class Order {
		Cook cook;
		Cashier cashier;
		double price;
		HashMap<String, Integer> requestList = new HashMap<String, Integer>();
		HashMap<String, Integer> fulfillList = new HashMap<String, Integer>();
		
		public enum State { received, preparing, prepared, delivering, delivered, charged, unpaid }
		private State state;
		
		Order(Cook c, Cashier ca, HashMap<String, Integer> order) {
			cook = c;
			cashier = ca;
			price = 0.0;

			for (String food : order.keySet()) {
				requestList.put(food, order.get(food));
			}
			
			state = State.received;
		}
	}
	
	
	// Inventory accessors
	
	private int quantityRemainingOf(String food) {
		return myStock.get(food).quantity;
	}
	
	// Replaces current value of given key with new, lower value
	private void removeInventory(String food, int qToRemove) {
		Stock oldStock = myStock.get(food);
		Stock newStock = new Stock(oldStock.quantity - qToRemove, oldStock.price);
		myStock.put(food, newStock);
	}
	
	private double priceOf(String food) {
		return myStock.get(food).price;
	}
	
	public String getName() {
		return name;
	}
}
