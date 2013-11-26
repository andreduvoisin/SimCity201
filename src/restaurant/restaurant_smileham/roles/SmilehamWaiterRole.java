package restaurant.restaurant_smileham.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.restaurant_smileham.Food;
import restaurant.restaurant_smileham.Food.EnumFoodOptions;
import restaurant.restaurant_smileham.Menu;
import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.Order.EnumOrderStatus;
import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.agent.Check;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.gui.SmilehamRestaurantPanel;
import restaurant.restaurant_smileham.gui.WaiterGui;
import restaurant.restaurant_smileham.interfaces.Cashier;
import restaurant.restaurant_smileham.interfaces.Cook;
import restaurant.restaurant_smileham.interfaces.Customer;
import restaurant.restaurant_smileham.interfaces.Host;
import restaurant.restaurant_smileham.interfaces.Waiter;
import base.BaseRole;

/**
 * Restaurant Host Agent
 */
public class SmilehamWaiterRole extends BaseRole implements Waiter {
	
	//Constants
	public static final int cBREAK_LENGTH = 10; //in seconds
	
	//Agents
	private Cashier mCashier;
	
	//Member Variables
	private String mName;
	private boolean mOnBreak;
	private List<Order> mOrders;
	private List<EnumFoodOptions> mFoodsOut;
	private Host mHost;
	private Cook mCook;
	private Timer mTimer;
	private boolean mWantBreak;
//	private boolean mMenuOutdated;
	
	//Semaphores //SHANE: 5 public private
	public Semaphore semAtTable = new Semaphore(0);
	private Semaphore semAtPickupArea = new Semaphore(0);
	public Semaphore semAtCook = new Semaphore(0);
	public Semaphore semGettingOrder = new Semaphore(0);
	public Semaphore semGettingCheck = new Semaphore(0);

	//GUI
	private WaiterGui mWaiterGui;
//	private SmilehamRestaurantGui mGUI;
	private SmilehamAnimationPanel mAnimationPanel;
	
	
	//-----------------------------------------------CONSTRUCTOR-----------------------------------------------
	public SmilehamWaiterRole(String name, Host host, Cook cook, Cashier cashier, SmilehamAnimationPanel animationPanel) {
		super();
		mName = name;
//		mAnimationPanel = animationPanel;
		mAnimationPanel = SmilehamRestaurantPanel.mAnimationPanel;
		print("Constructor");
		
		//Set up Waiter
		mWaiterGui = new WaiterGui(this);
		mAnimationPanel.addGui(mWaiterGui);
		
		mOnBreak = false;
		mOrders = Collections.synchronizedList(new ArrayList<Order>());
		mFoodsOut = new ArrayList<EnumFoodOptions>();
		mHost = host;
		mCook = cook;
		mCashier = cashier;
		mTimer = new Timer();
//		mMenuOutdated = false;
		mWantBreak = false;
		
//		startThread();
	}

	
	// -----------------------------------------------MESSAGES-------------------------------------------------------
	
	//Normative Scenarios
		public void msgSeatCustomer(Table table, Customer customer){
			print("Message: msgSeatCustomer()");
			Order order = new Order((Waiter) this, table, customer, EnumOrderStatus.WAITING);
			mOrders.add(order);
			stateChanged();
		}
		
		public void msgReadyToOrder(Customer customer){
			print("Message: msgReadyToOrder()");
			for (Order iOrder : mOrders){
				if (iOrder.mCustomer == customer){
					iOrder.mOrderStatus = EnumOrderStatus.ORDERING;
					break;
				}
			}
			stateChanged();
		}
		
		public void msgHereIsMyChoice(Customer customer, EnumFoodOptions choice){
			print("Message: msgHereIsMyChoice()" + choice);
			for (Order iOrder : mOrders){
				if (iOrder.mCustomer == customer){
					iOrder.mFood = new Food(choice);
					break;
				}
			}
			semGettingOrder.release(); //allow waiter to leave table
			//state changes to ORDERED
			stateChanged();
		}
		
		public void msgNotGettingFood(Customer customer){
			print("Message: msgNotGettingFood()");
			for (Order iOrder : mOrders){
				if (iOrder.mCustomer == customer){
					iOrder.mOrderStatus = EnumOrderStatus.DONE;
					break;
				}
			}
			semGettingOrder.release(); //allow waiter to leave table
			//state changes to DONE
			stateChanged();
		}
		
		public void msgOrderIsReady(Order order, List<EnumFoodOptions> foods){
			print("Message: msgOrderIsReady()");
			mFoodsOut = foods;
			order.mOrderStatus = EnumOrderStatus.READY;
			stateChanged();
		}
		
		public void msgOutOfFood(Order order, List<EnumFoodOptions> foods){
			print("Message: msgOutOfFood");
			mFoodsOut = foods;
			if (order.mOrderStatus == EnumOrderStatus.PENDING) order.mOrderStatus = EnumOrderStatus.ORDERING;
			stateChanged();
		}
		
		public void msgDoneEating(Customer customer){
			print("Message: msgDoneEating()");
			for (Order iOrder : mOrders){
				if (iOrder.mCustomer == customer){
					iOrder.mOrderStatus = EnumOrderStatus.DONE;
					break;
				}
			}
			stateChanged();
		}
	
	//On break situation
		public void msgBreakReply(boolean reply){
			print("Message: msgBreakReply()");
			if (reply){
				mOnBreak = true;
				mTimer.schedule(new TimerTask() {
					public void run() {
						print("Done with break");
						mOnBreak = false;
						stateChanged();
					}
				},
				cBREAK_LENGTH * 1000);
			}
		}
	
	//Market situation
		
		public void msgNewMenu(List<EnumFoodOptions> foods){
			print("Message: msgNewMenu");
			mFoodsOut = foods;
//			mMenuOutdated = false;
		}
		
	//Want Break
		public void msgWantBreak(){
			print("Message: msgWantBreak()");
			mWantBreak = true;
			stateChanged();
		}
		
	//Customer Paying
		public void msgReadyForCheck(EnumFoodOptions choice, Customer customer){
			print("Message: msgReadyForCheck()");
			for (Order iOrder : mOrders){
				if (iOrder.mCustomer == customer){
					iOrder.mOrderStatus = EnumOrderStatus.GETTINGCHECK;
					break;
				}
			}
			stateChanged();
		}

		public void msgHereIsCheck(Order order, Check check){
			print("Message: msgHereIsCheck(" + order.mChange + ")");
			order.mOrderStatus = EnumOrderStatus.GOTCHECK;
			order.mCheck = check;
			stateChanged();
		}
	
		public void msgCustomerLeaving(Customer customer){
			for (Order iOrder : mOrders){
				if (iOrder.mCustomer == customer){
					iOrder.mOrderStatus = EnumOrderStatus.DONE;
					break;
				}
			}
			stateChanged();
		}
		
		
		
		
		public void msgAnimationAtPickupArea(){
			if (semAtPickupArea.availablePermits() == 0) semAtPickupArea.release();
		}
		
		public void msgAnimationAtCook(){
			if (semAtCook.availablePermits() == 0) semAtCook.release();
		}
		
		
		
	//-----------------------------------------------SCHEDULER-----------------------------------------------
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try{
			//On Break
			if ((mOrders.size() == 0) && (mOnBreak)) return false;
			
			//Want to go on break
			if (mWantBreak){
				askForBreak();
				return true;
			}
			
//			if (mMenuOutdated){
//				updateMenu();
//			}
			
			//Waiting
			for (Order iOrder : mOrders){
				synchronized(iOrder){
					if (iOrder.mOrderStatus.equals(EnumOrderStatus.WAITING)){
						seatCustomer(iOrder, iOrder.mTable);
						return true;
					}
				}
			}
			//At Table
				//do nothing
			//Ordering
			synchronized (mOrders){
				for (Order iOrder : mOrders){
					synchronized(iOrder){
						if (iOrder.mOrderStatus.equals(EnumOrderStatus.ORDERING)){
							getOrderFromCustomer(iOrder);
							return true;
						}
					}
				}
			}
			//Ordered
			synchronized (mOrders){
				for (Order iOrder : mOrders){
					synchronized(iOrder){
						if (iOrder.mOrderStatus.equals(EnumOrderStatus.ORDERED)){
							deliverOrder(iOrder);
							return true;
						}
					}
				}
			}
			//Cook's job in here...
			//Ready
			synchronized (mOrders){
				for (Order iOrder : mOrders){
					synchronized(iOrder){
						if (iOrder.mOrderStatus.equals(EnumOrderStatus.READY)){
							getOrderFromCook(iOrder);
							return true;
						}
					}
				}
			}
			//Delivering
			synchronized (mOrders){
				for (Order iOrder : mOrders){
					synchronized(iOrder){
						if (iOrder.mOrderStatus.equals(EnumOrderStatus.DELIVERING)){
							deliverFoodToCustomer(iOrder);
							return true;
						}
					}
				}
			}
			//GETTINGCHECK
			synchronized (mOrders){
				for (Order iOrder : mOrders){
					synchronized(iOrder){
						if (iOrder.mOrderStatus.equals(EnumOrderStatus.GETTINGCHECK)){
							askCashierForCheck(iOrder);
//							cleanUp(iOrder);
							return true;
						}
					}
				}
			}
			//GOTCHECK
			synchronized (mOrders){
				for (Order iOrder : mOrders){
					synchronized(iOrder){
						if (iOrder.mOrderStatus.equals(EnumOrderStatus.GOTCHECK)){
							deliverCheck(iOrder);
							return true;
						}
					}
				}
			}
			//DONE
			synchronized (mOrders){
				for (Order iOrder : mOrders){
					synchronized(iOrder){
						if (iOrder.mOrderStatus.equals(EnumOrderStatus.DONE)){
							cleanUpOrder(iOrder);
							return true;
						}
					}
				}
			}
			
			
			//Go to home position
			goHome();
			
			return false;
		}catch(ConcurrentModificationException e){
			return true;
		}
	}

	
	// -----------------------------------------------ACTIONS-----------------------------------------------

	//METHODS
		private void acquireSemaphore(Semaphore semaphore){
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Normative Scenario
	
		private void seatCustomer(Order order, Table table) {
			print("Action: seatCustomer()");
			
			Customer customer = order.mCustomer;
			customer.msgSitAtTable((Waiter) this, table.getTableNumber(), new Menu());

			mWaiterGui.DoGoToPickupArea();
			acquireSemaphore(semAtPickupArea);
			customer.msgAnimationPickedUp();
			
			
			mWaiterGui.DoGoToTable(table.getTableNumber());
			
			order.mOrderStatus = EnumOrderStatus.ATTABLE;
			acquireSemaphore(semAtTable); //get to table
		}
		
		private void getOrderFromCustomer(Order order){
			print("Action: getOrderFromCustomer()");
			
			mWaiterGui.DoGoToTable(order.mTable.getTableNumber());
			acquireSemaphore(semAtTable);
			order.mOrderStatus = EnumOrderStatus.ORDERED;
			
			Menu menu = new Menu();
			for (EnumFoodOptions iFood : mFoodsOut){
				menu.removeChoice(iFood);
			}
			order.mCustomer.msgWhatWouldYouLike(menu);
			acquireSemaphore(semGettingOrder); //Now has choice and can leave
		}
		
		private void deliverOrder(Order order){
			print("Action: deliverOrder()");
			
			mWaiterGui.DoGoToCook();
			acquireSemaphore(semAtCook);
			
			order.mOrderStatus = EnumOrderStatus.PENDING;
			mCook.msgMakeFood(order);
		}
		
		private void getOrderFromCook(Order order){
			print("Action: getOrderFromCook()");
			
			mWaiterGui.DoGoToCook();
			acquireSemaphore(semAtCook);
			
			order.mOrderStatus = EnumOrderStatus.DELIVERING;
			mCook.refreshLabels();
		}
		
		private void deliverFoodToCustomer(Order order){
			print("Action: deliverFoodToCustomer()");
			
			mWaiterGui.DoGoToCook();
			acquireSemaphore(semAtCook);
			
			mWaiterGui.DoGoToTable(order.mTable.getTableNumber());
			acquireSemaphore(semAtTable);
			
			order.mCustomer.msgHereIsYourFood(order.mFood.mChoice);
			order.mOrderStatus = EnumOrderStatus.DELIVERED;
		}
		
		private void askCashierForCheck(Order order){
			print("Action: askCashierForCheck()");
			mCashier.msgMakeCheck(order);
		}

		private void deliverCheck(Order order){
			print("Action: deliverCheck()");
			order.mCustomer.msgCheckDelivered(order.mCheck); //if customer can't pay, this becomes a loan and the customer has "negative" cash
			order.mOrderStatus = EnumOrderStatus.DELIVEREDCHECK;
			stateChanged();
		}
		
		private void cleanUpOrder(Order order){
			print("Action: cleanUp()");
			mOrders.remove(order);
			mHost.msgLeavingTable(order.mCustomer);
		}
		
		//Want Break
		private void askForBreak(){
			print("Action: askForBreak()");
			mWantBreak = false;
			mHost.msgWantToGoOnBreak((Waiter) this);
		}
		
		private void goHome(){
			mWaiterGui.DoGoHome();
		}
		
		//Menu updated
//		private void updateMenu(){
//			print("Action: updateMenu()");
//			
//			mWaiterGui.DoGoToCook();
//			acquireSemaphore(semAtCook);
//			
//			mCook.msgWhatArrived(this);
//		}
//		
		
		
	//ACCESSORS
		public String getName() {
			return mName;
		}
		
		public void setGui(WaiterGui gui) {
			mWaiterGui = gui;
		}
	
		public WaiterGui getGui() {
			return mWaiterGui;
		}

		public String toString() {
			return "[Waiter " + getName() + "]";
		}

		public List<Order> getOrders() {
			return mOrders;
		}

		public Host getHost() {
			return mHost;
		}

		public Cook getCook() {
			return (Cook)mCook;
		}

		public boolean isWorking() {
			return !mOnBreak;
		}
		

}

