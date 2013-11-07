package restaurant.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.Menu;
import restaurant.Order;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Market;
import agent.Agent;
import agent.Check;

public class CashierAgent extends Agent implements Cashier{
	
	//Constants
	public static final int cRESTAURANT_CASH = 100;
	
	//Member Variables
	private String mName;
	private int mCash;
	
	private List<Order> mOrders;
	private List<Check> mChecksPaid;
	private Map<Market, Integer> mMarketBills;

	//GUI
	private RestaurantGui mGUI;
	
	
	//-----------------------------------------------CONSTRUCTOR-----------------------------------------------
	public CashierAgent(String name, RestaurantGui gui){
		super();
		mName = name;
		mGUI = gui;
		print("Constructor");
		
		mCash = cRESTAURANT_CASH;
		
		mOrders = Collections.synchronizedList(new ArrayList<Order>());
		mChecksPaid = Collections.synchronizedList(new ArrayList<Check>());
		mMarketBills = new HashMap<Market, Integer>();
    	
	}
	
	// -----------------------------------------------MESSAGES---------------------------------------------------
	//Customer Payment
		public void msgMakeCheck(Order order){
			print("Message: msgMakeCheck()");
			mOrders.add(order);
			stateChanged();
		}
		
		public void msgPayingCheck(Check check){
			print("Message: msgHereIsMoney()");
			mChecksPaid.add(check);
			stateChanged();
		}
	
	//Market Payment
		public void msgMarketBill(Market market, int amount){
			print("Message: msgMarketBill(" + amount + ")");
			synchronized (mMarketBills) {
				if (mMarketBills.get(market) != null){
					amount += mMarketBills.get(market);
				}
			mMarketBills.put(market, amount);
			}
			stateChanged();
		}
		
	//-----------------------------------------------SCHEDULER-----------------------------------------------
	public boolean pickAndExecuteAnAction() {
		
		//Customer Payment
			if (mOrders.size() != 0){
				synchronized(mOrders.get(0)){
					makeCheck(mOrders.get(0));
				}
				mOrders.remove(0);
				return true;
			}
			if (mChecksPaid.size() != 0){
				synchronized(mChecksPaid.get(0)){
					giveChange(mChecksPaid.get(0));
				}
				mChecksPaid.remove(0);
				return true;
			}
		
		//Market Payment
			if (mMarketBills.size() != 0){
				synchronized(mMarketBills){
					Market market = (Market) mMarketBills.keySet().toArray()[0];
					int amount = mMarketBills.get(market);
					payMarket(market, amount);
					return true;
				}
			}
		return false;
	}

	// -----------------------------------------------ACTIONS-----------------------------------------------

	//Methods
		public void makeCheck(Order order){
			print("Action: makeCheck()");
			Check check = new Check(order.mCustomer, order.mFood.mChoice , order.mCash);
			order.mWaiter.msgHereIsCheck(order, check);
			stateChanged();
		}
		
		public void giveChange(Check check){
			print("Action: giveChange()");
			int foodPrice = Menu.cFOOD_PRICES.get(check.mChoice);
			int change = check.mCash - foodPrice;
			mCash += foodPrice;
			check.mCustomer.msgGoodToGo(change);
			stateChanged();
		}
		
		public void payMarket(Market market, int amount){
			print("Action: payMarket()");
			
			//can't pay
			if (mCash < amount){
				//Extra Credit!
				mGUI.popupExit("Extra Credit! The cashier must now become a freelance programmer to pay for the things he bought from the market.");
				
			}
			
			mCash -= amount;
			market.msgPayingMarket(amount);
			mMarketBills.remove(market);
			stateChanged();
		}
		
	//ACCESSORS
		public String toString() {
			return "[Cashier " + getName() + "]";
		}
		
		
		public String getName() {
			return mName;
		}

		public void setName(String mName) {
			this.mName = mName;
		}

		public int getCash() {
			return mCash;
		}

		public void setCash(int mCash) {
			this.mCash = mCash;
		}

		public List<Order> getOrders() {
			return mOrders;
		}

		public void setOrders(List<Order> mOrders) {
			this.mOrders = mOrders;
		}

		public List<Check> getChecksPaid() {
			return mChecksPaid;
		}

		public void setChecksPaid(List<Check> mChecksPaid) {
			this.mChecksPaid = mChecksPaid;
		}

		public Map<Market, Integer> getMarketBills() {
			return mMarketBills;
		}

		public void setMarketBills(Map<Market, Integer> mMarketBills) {
			this.mMarketBills = mMarketBills;
		}

		public RestaurantGui getGUI() {
			return mGUI;
		}

		public void setGUI(RestaurantGui mGUI) {
			this.mGUI = mGUI;
		}
}