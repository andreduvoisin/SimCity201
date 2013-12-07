package restaurant.intermediate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.MarketInvoice;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.interfaces.MarketCashier;
import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.intermediate.interfaces.RestaurantCookInterface;
import restaurant.restaurant_davidmca.gui.DavidAnimationPanel;
import restaurant.restaurant_davidmca.roles.DavidCookRole;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_duvoisin.roles.AndreCookRole;
import restaurant.restaurant_maggiyan.gui.MaggiyanAnimationPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanCookRole;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCookRole;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.gui.TranacAnimationPanel;
import restaurant.restaurant_tranac.roles.TranacCookRole;
import restaurant.restaurant_xurex.RexCookRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.Item.EnumItemType;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;
import base.reference.ContactList;

public class RestaurantCookRole extends BaseRole implements RestaurantCookInterface, RestaurantBaseInterface {
        
        static int totalCooks = 0;
        
        public Role subRole = null;

        int mRestaurantID;
        public int DEFAULT_FOOD_QTY = 100;
        
        public RestaurantCookRole(Person person, int restaurantID){
                super(person); 
                this.mRestaurantID = restaurantID;
                
                //populate inventory
        		mItemInventory.put(EnumItemType.STEAK,DEFAULT_FOOD_QTY);
        		mItemInventory.put(EnumItemType.CHICKEN,DEFAULT_FOOD_QTY);
        		mItemInventory.put(EnumItemType.SALAD,DEFAULT_FOOD_QTY);
        		mItemInventory.put(EnumItemType.PIZZA,DEFAULT_FOOD_QTY);
        }
        
        public void setPerson(Person person){
            super.mPerson = person;
        	switch(mRestaurantID){
				case 0: //andre
					subRole = new AndreCookRole(super.mPerson, this);
					AndreRestaurantPanel.instance.addCook((AndreCookRole) subRole);
					break;
//				case 1: //chase
//					subRole = new CwagonerCookRole(super.mPerson);
//					CwagonerRestaurantPanel.cook = (CwagonerCookRole) subRole;
//					break;
//				case 2: //jerry
//					subRole = new JerrywebCookRole(super.mPerson);
//					JerrywebRestaurantPanel.cook = (JerrywebCookRole) subRole;
//					break;
				case 3: //maggi
					subRole = new MaggiyanCookRole(super.mPerson, this);
					MaggiyanAnimationPanel.addPerson((MaggiyanCookRole) subRole);
					break;
				case 4: //david
					subRole = new DavidCookRole(super.mPerson, this);
					DavidAnimationPanel.addCook((DavidCookRole) subRole);
					break;
				case 5: //shane
					subRole = new SmilehamCookRole(super.mPerson, this);
					SmilehamAnimationPanel.addPerson((SmilehamCookRole) subRole);
					break;
				case 6: //angelica
					subRole = new TranacCookRole(mPerson, this);
					TranacRestaurant.addPerson((TranacCookRole)subRole);
					break;
				case 7: //rex
					subRole = new RexCookRole(super.mPerson, this);
					RexAnimationPanel.addPerson((RexCookRole) subRole);
					break;
			}
       }
        
        public boolean pickAndExecuteAnAction() {
        		if(subRole != null) {
        			if(subRole.pickAndExecuteAnAction())
        				return true;
        		}
        		if(marketPickAndExecuteAnAction())
        			return true;
        		return false;
        }

/** MarketCookCustomerRole Data, Actions, Scheduler, etc **/

        public Map<EnumItemType, Integer> mItemInventory = new HashMap<EnumItemType, Integer>();
    	public Map<EnumItemType, Integer> mItemsDesired = new HashMap<EnumItemType, Integer>();
        
        public Map<EnumItemType, Integer> mCannotFulfill = new HashMap<EnumItemType, Integer>();
        
        public List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
        public List<MarketInvoice> mInvoices = Collections.synchronizedList(new ArrayList<MarketInvoice>());
        
        protected static final int sBaseNeed = 3;
        
        MarketCashier mMarketCashier;
        
/* Messages */
        public void msgCannotFulfillItems(MarketOrder o, Map<EnumItemType,Integer> cannotFulfill) {
        	mCannotFulfill = cannotFulfill;
        	for(MarketOrder io : mOrders) {
        		if(io == o) {
        			io.mEvent = EnumOrderEvent.RECEIVED_INVOICE;
        			break;
        		}
        	}
        	stateChanged();
        }
        
        public void msgHereIsCookOrder(MarketOrder o) {
        	o.mEvent = EnumOrderEvent.RECEIVED_ORDER;
        	stateChanged();
        }
        
/* Scheduler */
        public boolean marketPickAndExecuteAnAction() {
                for(MarketInvoice invoice : mInvoices) {
                        MarketOrder order = invoice.mOrder;
                        if(order.mStatus == EnumOrderStatus.PAYING && order.mEvent == EnumOrderEvent.RECEIVED_INVOICE) {
                                order.mStatus = EnumOrderStatus.PAID;
                                processOrder(invoice);
                                return true;
                        }
                }
                for(MarketOrder order : mOrders) {
                        if(order.mStatus == EnumOrderStatus.FULFILLING && order.mEvent == EnumOrderEvent.RECEIVED_ORDER) {
                                order.mStatus = EnumOrderStatus.DONE;
                                completeOrder(order);
                                return true;
                        }
                }
                for(MarketOrder order : mOrders) {
                        if(order.mStatus == EnumOrderStatus.CARTED) {
                                order.mStatus = EnumOrderStatus.PLACED;
                                placeOrder(order);
                                return true;
                        }
                }
                for(EnumItemType i : mItemsDesired.keySet()) {
                        if(mItemsDesired.get(i) != 0) {
                                createOrder();
                                return true;
                        }
                }
                return false;
        }

/* Actions */
        private void createOrder() {
        		Map<EnumItemType,Integer> items = new HashMap<EnumItemType,Integer>();
                
                for(EnumItemType item : mItemsDesired.keySet()) {
                		items.put(item, mItemsDesired.get(item));
                        mItemsDesired.put(item,0);
                }
                
                MarketOrder o = new MarketOrder(items, this);
                o.setRestaurantNumber(mRestaurantID);
                mOrders.add(o);
        }
        
        private void placeOrder(MarketOrder o) {
        		int m;
        		if(mMarketCashier == null) {
        			m = (int) (Math.random() % 2);
        			mMarketCashier = ContactList.sMarketList.get(m).mCashier;
        		}
                mMarketCashier.msgOrderPlacement(o);
                //ANGELICA: fill in each restaurant
                RestaurantCashierRole restaurantCashier = null;
                /*
                switch(mRestaurantID) {
                case 0:
                	restaurantCashier = 
                case 1:
                	
                case 2:
                	
                case 3:
                	
                case 4:
                
                case 5:
                	
                case 6:
                	
                case 7:
                	 
                }
                */
                restaurantCashier.msgPlacedMarketOrder(o,mMarketCashier);
        }
        
        private void processOrder(MarketInvoice i) {   
                for(EnumItemType item : mCannotFulfill.keySet()) {
                        mItemsDesired.put(item, mItemsDesired.get(item)+mCannotFulfill.get(item));
                }
        }
        
        private void completeOrder(MarketOrder o) {
                for(EnumItemType item : o.mItems.keySet()) {
                        mItemInventory.put(item, mItemInventory.get(item)+o.mItems.get(item));
                }
                mOrders.remove(o);
        }
        
/* Utilities */
        public void setMarketCashier(int n) {
        		mMarketCashier = ContactList.sMarketList.get(n).mCashier;
        }
        
        public void decreaseInventory(EnumItemType i) {
        	mItemInventory.put(i,mItemInventory.get(i)-1);
        }

        @Override
    	public Location getLocation() {
    		return ContactList.cRESTAURANT_LOCATIONS.get(mRestaurantID);
    	}
}
