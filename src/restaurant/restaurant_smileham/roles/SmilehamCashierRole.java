package restaurant.restaurant_smileham.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.intermediate.RestaurantCashierRole;
import restaurant.restaurant_smileham.Menu;
import restaurant.restaurant_smileham.Order;
import restaurant.restaurant_smileham.agent.Check;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.interfaces.SmilehamCashier;
import restaurant.restaurant_smileham.interfaces.SmilehamMarket;
import base.BaseRole;
import base.ContactList;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

public class SmilehamCashierRole extends BaseRole implements SmilehamCashier{
	
	//Constants
	public static final int cRESTAURANT_CASH = 100;
	
	//Member Variables
	private RestaurantCashierRole mRole;
	private String mName;
	private int mCash;
	
	private List<Order> mOrders;
	private List<Check> mChecksPaid;
	private Map<SmilehamMarket, Integer> mMarketBills;

	//GUI
	private SmilehamAnimationPanel mAnimationPanel;
	
	
	//-----------------------------------------------CONSTRUCTOR-----------------------------------------------
	public SmilehamCashierRole(Person person, RestaurantCashierRole r){
		super(person);
		mRole = r;
		mName = person.getName();
		mAnimationPanel = SmilehamAnimationPanel.mInstance;
		print("Smileham Cashier Created");
		
		mCash = cRESTAURANT_CASH;
		
		mOrders = Collections.synchronizedList(new ArrayList<Order>());
		mChecksPaid = Collections.synchronizedList(new ArrayList<Check>());
		mMarketBills = new HashMap<SmilehamMarket, Integer>();
    	
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
		public void msgMarketBill(SmilehamMarket market, int amount){
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
					SmilehamMarket market = (SmilehamMarket) mMarketBills.keySet().toArray()[0];
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
		
		public void payMarket(SmilehamMarket market, int amount){
			print("Action: payMarket()");
			
			//can't pay
			if (mCash < amount){
				//Extra Credit!
//				mGUI.popupExit("Extra Credit! The cashier must now become a freelance programmer to pay for the things he bought from the market.");
				
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

		public Map<SmilehamMarket, Integer> getMarketBills() {
			return mMarketBills;
		}

		public void setMarketBills(Map<SmilehamMarket, Integer> mMarketBills) {
			this.mMarketBills = mMarketBills;
		}
		
	    public RestaurantCashierRole getIntermediateRole() {
	    	return mRole;
	    }

		@Override
		public Location getLocation() {
			return ContactList.cRESTAURANT_LOCATIONS.get(5);
		}
		
		public void Do(String msg) {
			super.Do(msg, AlertTag.R5);
		}
		
		public void print(String msg) {
			super.print(msg, AlertTag.R5);
		}
		
		public void print(String msg, Throwable e) {
			super.print(msg, AlertTag.R5, e);
		}
}
