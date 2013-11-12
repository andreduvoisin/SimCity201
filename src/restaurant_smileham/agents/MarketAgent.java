package restaurant_smileham.agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant_smileham.Menu;
import restaurant_smileham.Food.EnumFoodOptions;
import restaurant_smileham.agent.Agent;
import restaurant_smileham.gui.RestaurantGui;
import restaurant_smileham.interfaces.Cashier;
import restaurant_smileham.interfaces.Cook;
import restaurant_smileham.interfaces.Market;

public class MarketAgent extends Agent implements Market{
	//Constants
	public static final int cSTEAK_QUANTITY = 1;
	public static final int cCHICKEN_QUANTITY = 1;
	public static final int cSALAD_QUANTITY = 1;
	public static final int cPIZZA_QUANTITY = 1;
	
	public static final int cDELIVERY_DELAY = 5; //in seconds
	
	
	//Member Variables
	private String mName;
	private Cook mCook;
	private Cashier mCashier;
	private Timer mTimer;
	private Map<EnumFoodOptions, Integer> mInventory;
	private Map<EnumFoodOptions, Integer> mOrderedFood;

	//GUI
	private RestaurantGui mGUI;
	
	
	//-----------------------------------------------CONSTRUCTOR-----------------------------------------------
	public MarketAgent(String name, int steak, int chicken, int salad, int pizza, RestaurantGui gui){
		super();
		mName = name;
		mGUI = gui;
		print("Constructor");
    	
    	//Set up inventory map
    	mInventory = new HashMap<EnumFoodOptions, Integer>();
    	mInventory.put(EnumFoodOptions.STEAK, steak);
    	mInventory.put(EnumFoodOptions.CHICKEN, chicken);
    	mInventory.put(EnumFoodOptions.SALAD, salad);
    	mInventory.put(EnumFoodOptions.PIZZA, pizza);
    	mOrderedFood = new HashMap<EnumFoodOptions, Integer>();
    	
    	mCashier = mGUI.getRestaurantPanel().getCashier();
    	mCook = mGUI.getRestaurantPanel().getCook();
    	
		mTimer = new Timer();
		
		startThread();
	}
	
	// -----------------------------------------------MESSAGES---------------------------------------------------
	public void msgOrderFood(EnumFoodOptions choice, int amount){
		print("Message: msgOrderFood(" + choice + ", " + amount + ")");
		//Add order to things to do
		mOrderedFood.put(choice, Math.min(amount, mInventory.get(choice)));
		mInventory.put(choice, mInventory.get(choice) - mOrderedFood.get(choice)); //decrease available food
		stateChanged();
	}
	
	public void msgPayingMarket(int amount){
		print("Message msgPayingMarket(" + amount + ")");
		//do nothing
	}

	//-----------------------------------------------SCHEDULER-----------------------------------------------
	public boolean pickAndExecuteAnAction() {
		synchronized (mOrderedFood.keySet()) {
			for (EnumFoodOptions iFood : mOrderedFood.keySet()){
				fulfillOrder(iFood);
			}
		}
		return false;
	}

	// -----------------------------------------------ACTIONS-----------------------------------------------

	//Methods
		private void fulfillOrder(final EnumFoodOptions choice){
			print("Action: fulfillOrder()");
			
			mCook.msgOrderResponse(choice, mOrderedFood.get(choice));
			final int amount = mOrderedFood.get(choice);
			final Market market = this;
			
			if (amount == 0) return; //if no food to deliver, return early
			
			mOrderedFood.remove(choice);
			
			mTimer.schedule(new TimerTask() {
				public void run() {
					print("Fulfilling Order");
					mCook.msgOrderFulfillment(choice, amount);
					
					int price = Menu.cFOOD_PRICES.get(choice);
					int bill = price*amount;
					mCashier.msgMarketBill(market, bill);
				}
			},
			cDELIVERY_DELAY*1000);
		}
		
	//ACCESSORS
		public String getName() {
			return mName;
		}
		
		public boolean isOut(){
			for (EnumFoodOptions iFood : EnumFoodOptions.values()){
				if (mInventory.get(iFood) != 0) return false;
			}
			return true;
		}
		
		public String toString() {
			return "[Market " + getName() + "]";
		}
}
