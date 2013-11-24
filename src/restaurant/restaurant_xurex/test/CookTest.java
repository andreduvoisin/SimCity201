package restaurant.restaurant_xurex.test;

import java.util.HashMap;
import java.util.Map;

import restaurant.restaurant_xurex.agents.CookAgent;
import restaurant.restaurant_xurex.agents.CookAgent.MarketOrderState;
import restaurant.restaurant_xurex.test.mock.MockCookGui;
import restaurant.restaurant_xurex.test.mock.MockMarket;
import restaurant.restaurant_xurex.test.mock.MockWaiter;
import restaurant.restaurant_xurex.agents.CookAgent.OrderState;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CookAgent's basic interaction
 * with waiters and markets.
 *
 *
 * @author Rex Xu
 */
public class CookTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CookAgent cook;
	
	MockWaiter waiter;
	MockMarket market1;
	
	MockCookGui cookGui;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cook = new CookAgent("cook");
		waiter = new MockWaiter("waiter");
		market1 = new MockMarket("market1");
		cookGui = new MockCookGui("cookGui");
		
		cook.addMarket(market1);
		cook.setGui(cookGui);
	}	
	
	public void testOne_OneCookOrder_OneMarketOrder(){
	//  setUp()
		
	//	Preconditions
		assertTrue("Cook has two markets", cook.markets.size() == 1);
		assertTrue("Cook has no cookOrders", cook.orders.isEmpty());
		assertTrue("Cook has no marketOrders", cook.marketOrders.isEmpty());
		assertTrue("Stand has no orders", cook.revolvingStand.isEmpty());
		
		//1: addToStand (waiter, choice, table)
		cook.addToStand(waiter, "Salad", 1);
		
		assertTrue("Stand has one order", cook.revolvingStand.size() == 1);
		
		//2: paea: add to orders
		assertTrue("paea: add to orders", cook.pickAndExecuteAnAction());
		
		assertTrue("Stand has no orders", cook.revolvingStand.isEmpty());
		assertTrue("Cook has one order", cook.orders.size() == 1);
		assertTrue("Order state is pending", cook.orders.get(0).s == OrderState.pending);

		//3: paea: TryToCookOrder (order) -> CheckInventory() is called
		assertTrue("paea: TryToCookOrder", cook.pickAndExecuteAnAction());
		
		assertTrue("Order state is cooking", cook.orders.get(0).s == OrderState.cooking);
		assertTrue("Cook has one marketOrder", cook.marketOrders.size() == 1);
		assertTrue("Market received HereIsOrder", market1.log.containsString("HereIsOrder"));
		
		//4: msgTimerDone (cookOrder)
		cook.orders.get(0).s = OrderState.cooked;
		
		//5: paea: ServeOrder(order)
		assertTrue("paea: serveOrder", cook.pickAndExecuteAnAction());
		
		assertTrue("Order state is served", cook.orders.get(0).s == OrderState.served);
		assertTrue("Waiter received OrderIsReady", waiter.log.containsString("OrderIsReady: Salad 1"));
	
		//6: msgMarketCanFulfillOrder
		Map<String, Integer> provided = new HashMap<String, Integer>();
		provided.put("Salad", 14);
		cook.MarketCanFulfill(market1, provided);
		
		assertTrue("Waiter has marketOrder with provided", cook.marketOrders.get(0).provided ==provided);
		
		//7: msgOrderIsReady (market)
		cook.OrderIsReady(market1);
		
		assertTrue("Market Order state is ready", cook.marketOrders.get(0).state == MarketOrderState.ready);
		
		//8: paea: RefillInventory (marketOrder)
		assertTrue("paea: RefillInventory", cook.pickAndExecuteAnAction());
		
		assertTrue("Inventory has 15 salads. Instead: "+
					cook.Inventory.get("Salad").quantity,
					cook.Inventory.get("Salad").quantity == 15);
		assertTrue("Inventory has 15 steaks. Instead: "+
					cook.Inventory.get("Steak").quantity, 
					cook.Inventory.get("Steak").quantity == 10);
		assertTrue("Inventory has 15 pizzas. Instead: "+
					cook.Inventory.get("Pizza").quantity, 
					cook.Inventory.get("Pizza").quantity == 10);
		assertTrue("Inventory has 15 chickens. Instead: "+
					cook.Inventory.get("Chicken").quantity, 
					cook.Inventory.get("Chicken").quantity == 10);
		assertTrue("paea: return false", !cook.pickAndExecuteAnAction());
		
	}
	
}
