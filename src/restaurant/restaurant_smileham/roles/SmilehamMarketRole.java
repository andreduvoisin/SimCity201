package restaurant.restaurant_smileham.roles;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.Menu;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.interfaces.SmilehamCashier;
import restaurant.restaurant_smileham.interfaces.SmilehamCook;
import restaurant.restaurant_smileham.interfaces.SmilehamMarket;
import base.BaseRole;
import base.interfaces.Person;

public class SmilehamMarketRole extends BaseRole implements SmilehamMarket{
	//Constants
	public static final int cSTEAK_QUANTITY = 1;
	public static final int cCHICKEN_QUANTITY = 1;
	public static final int cSALAD_QUANTITY = 1;
	public static final int cPIZZA_QUANTITY = 1;
	
	public static final int cDELIVERY_DELAY = 5; //in seconds
	
	
	//Member Variables
	private String mName;
	private SmilehamCook mCook;
	private SmilehamCashier mCashier;
	private Timer mTimer;
	private Map<EnumFoodOptions, Integer> mInventory;
	private Map<EnumFoodOptions, Integer> mOrderedFood;
	
	//-----------------------------------------------CONSTRUCTOR-----------------------------------------------
	public SmilehamMarketRole(Person person){
		super(person);
		mName = person.getName();
		print("Smileham Market Created");
    	
    	//Set up inventory map
    	mInventory = new HashMap<EnumFoodOptions, Integer>();
    	mInventory.put(EnumFoodOptions.STEAK, cSTEAK_QUANTITY);
    	mInventory.put(EnumFoodOptions.CHICKEN, cCHICKEN_QUANTITY);
    	mInventory.put(EnumFoodOptions.SALAD, cSALAD_QUANTITY);
    	mInventory.put(EnumFoodOptions.PIZZA, cPIZZA_QUANTITY);
    	mOrderedFood = new HashMap<EnumFoodOptions, Integer>();
    	
    	mCashier = SmilehamAnimationPanel.getCashier();
    	mCook = SmilehamAnimationPanel.getCook();
    	
		mTimer = new Timer();
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
			final SmilehamMarket market = this;
			
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
