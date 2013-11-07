package restaurant.agents;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.management.monitor.Monitor;

import restaurant.Food;
import restaurant.Food.EnumFoodOptions;
import restaurant.Order;
import restaurant.Order.EnumOrderStatus;
import restaurant.gui.CookGui;
import restaurant.gui.LabelGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import agent.Agent;

public class CookAgent extends Agent implements Cook {
	//Member Variables
	private String mName;
	private Timer mTimer;
	private Set<Order> mOrders;
	private Map<EnumFoodOptions, Food> mInventory;
	private Map<EnumFoodOptions, Integer> mIncomingInventory;
	private List<EnumFoodOptions> mFoodsOut;
	private List<Market> mMarkets;
	private Set<Waiter> mWaiters;
	private int mNumMarkets;
	private boolean mFoodArrived; //change this to states next version
	
	//Labels
	private LabelGui mFoodsCooking;
	private LabelGui mFoodsPlated;
	
	//Semaphores
	public Semaphore semOrderResponse = new Semaphore(0);
	public Semaphore semAtFridge = new Semaphore(0);
	public Semaphore semAtPlating = new Semaphore(0);
	public Semaphore semAtCooking = new Semaphore(0);

	//GUI
	private CookGui mCookGui;
	private RestaurantGui mGUI;
	
	
	//-----------------------------------------------CONSTRUCTOR-----------------------------------------------
	public CookAgent(String name, RestaurantGui gui){
		super();
		mName = name;
		mGUI = gui;
		print("Constructor");
		
		//Set up Cook
		mCookGui = new CookGui(this);
    	mGUI.getAnimationPanel().addGui(mCookGui);
    	
    	//Set up inventory map
    	mInventory = new HashMap<EnumFoodOptions, Food>(EnumFoodOptions.values().length); //4 
    	mInventory.put(EnumFoodOptions.STEAK, new Food(EnumFoodOptions.STEAK, Food.cCOOKTIME_STEAK, Food.sQuantitySteak, Food.cTHRESHOLD, Food.cCAPACITY));
    	mInventory.put(EnumFoodOptions.CHICKEN, new Food(EnumFoodOptions.CHICKEN, Food.cCOOKTIME_CHICKEN, Food.sQuantityChicken, Food.cTHRESHOLD, Food.cCAPACITY));
    	mInventory.put(EnumFoodOptions.SALAD, new Food(EnumFoodOptions.SALAD, Food.cCOOKTIME_SALAD, Food.sQuantitySalad, Food.cTHRESHOLD, Food.cCAPACITY));
    	mInventory.put(EnumFoodOptions.PIZZA, new Food(EnumFoodOptions.PIZZA, Food.cCOOKTIME_PIZZA, Food.sQuantityPizza, Food.cTHRESHOLD, Food.cCAPACITY));
    	mIncomingInventory = new HashMap<EnumFoodOptions, Integer>(); 
    	mFoodsOut = new ArrayList<EnumFoodOptions>();
    	mMarkets = new ArrayList<Market>();
    	mWaiters = new HashSet<Waiter>();
    	
    	mFoodsCooking = new LabelGui("Cooking", CookGui.cLABEL_COOKING_X, CookGui.cLABEL_COOKING_Y, mGUI);
    	mFoodsPlated = new LabelGui("Plated", CookGui.cLABEL_PLATING_X, CookGui.cLABEL_PLATING_Y, mGUI);
    	
		mTimer = new Timer();
		mOrders = new HashSet<Order>();//TODO 0
		mNumMarkets = 0;
		mFoodArrived = false;
		
		startThread();
	}
	
	// -----------------------------------------------MESSAGES---------------------------------------------------
	public void msgMakeFood(Order order){
		print("Message: msgMakeFood()");
		
		mWaiters.add(order.mWaiter); //if not in waiter list
		
		mOrders.add(order);
		stateChanged();
	}
	
	public void msgOrderResponse(EnumFoodOptions food, int newIncomingAmount){
		print("Message: msgOrderResponse(" + food + ", " + newIncomingAmount + ")");
		int alreadyIncomingFood = 0;
		if (mIncomingInventory.get(food) != null) alreadyIncomingFood = mIncomingInventory.get(food);
		mIncomingInventory.put(food, alreadyIncomingFood + newIncomingAmount); //increase incoming food by response
		semOrderResponse.release();
	}
	
	public void msgOrderFulfillment(EnumFoodOptions food, int amountArrived){
		print("Message: msgOrderFulfillment(" + food + ", " + amountArrived + ")");
		Food existingFood = mInventory.get(food);
		int incomingFood = 0;
		if (mIncomingInventory.get(food) != null) incomingFood = mIncomingInventory.get(food);
		existingFood.mQuantity += incomingFood; //add new food to inventory
		mIncomingInventory.remove(food);
		mFoodsOut.remove(food);
		
		mFoodArrived = true;
		stateChanged();
	}
	
	
	public void msgAnimationAtFridge(){
		if (semAtFridge.availablePermits() == 0) semAtFridge.release();
	}
	
	public void msgAnimationAtPlating(){
		if (semAtPlating.availablePermits() == 0) semAtPlating.release();
	}
	
	public void msgAnimationAtCooking(){
		if (semAtCooking.availablePermits() == 0) semAtCooking.release();
	}
	

	//-----------------------------------------------SCHEDULER-----------------------------------------------
	public boolean pickAndExecuteAnAction() {
		try{
			//new food arrived
			if (mFoodArrived){
				synchronized(mOrders){
					for (Waiter iWaiter : mWaiters){
	//					iWaiter.msgFoodArrived();
						iWaiter.msgNewMenu(mFoodsOut);
					}
				}
				mFoodArrived = false;
				return true;
			}
			
			//Cook orders
			for (Order iOrder : mOrders){
				synchronized(iOrder){
					if (iOrder.mOrderStatus == EnumOrderStatus.READY){
						iOrder.mWaiter.msgOrderIsReady(iOrder, mFoodsOut);
					}
				}
			}
			for (Order iOrder : mOrders){
				synchronized(iOrder){
					if (iOrder.mOrderStatus == EnumOrderStatus.PENDING){
						
						//if food is out
						EnumFoodOptions foodChoice = iOrder.mFood.mChoice;
						int stock = mInventory.get(foodChoice).mQuantity;
						if (stock == 0){
							mOrders.remove(iOrder); //can only do this because returning true
							mFoodsOut.add(foodChoice);
							iOrder.mWaiter.msgOutOfFood(iOrder, mFoodsOut);
							return true;
						}
						
						cookFood(iOrder.mFood);
					}
				}
			}
			
			//Remove Markets without food
			List<Market> marketsToRemove = new ArrayList<Market>();
			for (Market iMarket : mMarkets){
				if (iMarket.isOut()) marketsToRemove.add(iMarket);
			}
			for (Market iMarket : marketsToRemove){
				mMarkets.remove(iMarket);
			}
			
			//Order Food
			if (mMarkets.size() == 0) return false;
			for (EnumFoodOptions iFood : EnumFoodOptions.values()){
				Food stock = mInventory.get(iFood);
				//if not enough food
				if (stock.mQuantity < Food.cTHRESHOLD){
					orderFood(iFood);
				}
			}
	
			return false;
			
		}catch(ConcurrentModificationException e){
			return true;
		}
	}

	// -----------------------------------------------ACTIONS-----------------------------------------------

	//Methods
		private void acquireSemaphore(Semaphore semaphore){
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void addMarket(){
			Market market = new MarketAgent(
	    			"M" + ++mNumMarkets, 
	    			MarketAgent.cSTEAK_QUANTITY, 
	    			MarketAgent.cCHICKEN_QUANTITY, 
	    			MarketAgent.cSALAD_QUANTITY, 
	    			MarketAgent.cPIZZA_QUANTITY, 
	    			mGUI);
	    	mMarkets.add(market);
	    	stateChanged();
		}
	
		private void cookFood(final Food food){
			print("Action: cookFood(" + food + ")");
			
			
			
			mCookGui.DoGoToFridge();
			acquireSemaphore(semAtFridge);
			
			mInventory.get(food.mChoice).mQuantity--;
				
			//if food amount below threshold
			if (mInventory.get(food.mChoice).mQuantity < Food.cTHRESHOLD){
				orderFood(food.mChoice);
			}
			
			mCookGui.DoGoToCooking();
			acquireSemaphore(semAtCooking);
			
			synchronized (mOrders) {
				for (Order iOrder : mOrders){
					if (iOrder.mFood == food) iOrder.mOrderStatus = EnumOrderStatus.COOKING;
					break;
				}
			}
			refreshLabels();
			
			int cookingTime = Food.cCOOKTIMES.get(food.mChoice); //in seconds
			mTimer.schedule(new TimerTask() {
				public synchronized void run() {
					print("Done cooking: " + food.toString());
					mCookGui.DoGoToCooking();
					acquireSemaphore(semAtCooking);

					mCookGui.DoGoToPlating();
					acquireSemaphore(semAtPlating);
					
					for (Order iOrder : mOrders){
						if (iOrder.mFood == food){
							iOrder.mOrderStatus = EnumOrderStatus.READY;
							break;
						}
					}
					refreshLabels();
					
					stateChanged();
				}
			},
			cookingTime*1000);
		}
		
		private void plateFood(Food food){
			print("Done cooking: " + food.toString());
			
			mCookGui.DoGoToPlating();
			acquireSemaphore(semAtPlating);
			
			for (Order iOrder : mOrders){
				if (iOrder.mFood == food){
					iOrder.mOrderStatus = EnumOrderStatus.READY;
					break;
				}
			}
			refreshLabels();
			stateChanged();
		}
		
		private void orderFood(EnumFoodOptions food){
			print("Action: orderFood(" + food + ")");
			Food stock = mInventory.get(food);
			
			for (Market iMarket : mMarkets){
				//precondition: market isn't out
				int orderAmount = stock.mCapacity - stock.mQuantity; //amount needed to reach capacity
				iMarket.msgOrderFood(food, orderAmount);
				acquireSemaphore(semOrderResponse); //get response back
				
				//if enough food is incoming, don't ask any more markets
				int futureAmount = stock.mQuantity;
				if (mIncomingInventory.get(food) != null) futureAmount += mIncomingInventory.get(food);
				if (futureAmount == stock.mCapacity) break;
			}
		}
		
		public void refreshLabels(){
			String cooking = "Cooking: ";
			String plated = "Plated: ";
			synchronized (mOrders) {
				for (Order iOrder : mOrders){
					if (iOrder.mOrderStatus == EnumOrderStatus.COOKING) cooking += iOrder.mFood.mChoice + " ";
					if (iOrder.mOrderStatus == EnumOrderStatus.READY) plated += iOrder.mFood.mChoice + " ";
				}
			}
			mFoodsCooking.setLabel(cooking);
			mFoodsPlated.setLabel(plated);
		}
		
	//ACCESSORS
		public String getName() {
			return mName;
		}
		
		public void setGui(CookGui gui) {
			mCookGui = gui;
		}
	
		public CookGui getGui() {
			return mCookGui;
		}
	
		public String toString() {
			return "[Cook " + getName() + "]";
		}
	
}
