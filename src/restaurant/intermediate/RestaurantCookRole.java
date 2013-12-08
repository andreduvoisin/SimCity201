package restaurant.intermediate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import city.gui.trace.AlertTag;
import market.MarketInvoice;
import market.MarketOrder;
import market.MarketOrder.EnumOrderEvent;
import market.MarketOrder.EnumOrderStatus;
import market.interfaces.MarketCashier;
import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.intermediate.interfaces.RestaurantCookInterface;
import restaurant.restaurant_davidmca.DavidRestaurant;
import restaurant.restaurant_davidmca.roles.DavidCookRole;
import restaurant.restaurant_duvoisin.AndreRestaurant;
import restaurant.restaurant_duvoisin.roles.AndreCookRole;
import restaurant.restaurant_jerryweb.JerrywebCookRole;
import restaurant.restaurant_jerryweb.JerrywebRestaurant;
import restaurant.restaurant_maggiyan.gui.MaggiyanAnimationPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanCookRole;
import restaurant.restaurant_smileham.SmilehamRestaurant;
import restaurant.restaurant_smileham.roles.SmilehamCookRole;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.roles.TranacCookRole;
import restaurant.restaurant_xurex.RexCookRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.ContactList;
import base.Item.EnumItemType;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCookRole extends BaseRole implements RestaurantCookInterface, RestaurantBaseInterface {
        
        static int totalCooks = 0;
        
        public Role subRole = null;

        int mRestaurantID;
        public int DEFAULT_FOOD_QTY = 10;	//ANGELICA:
        
        public RestaurantCookRole(Person person, int restaurantID){
                super(person); 
                this.mRestaurantID = restaurantID;
                
                //populate maps
        		mItemInventory.put(EnumItemType.STEAK,DEFAULT_FOOD_QTY);
        		mItemInventory.put(EnumItemType.CHICKEN,DEFAULT_FOOD_QTY);
        		mItemInventory.put(EnumItemType.SALAD,DEFAULT_FOOD_QTY);
        		mItemInventory.put(EnumItemType.PIZZA,DEFAULT_FOOD_QTY);
        		
        		mHasCreatedOrder.put(EnumItemType.STEAK,false);
        		mHasCreatedOrder.put(EnumItemType.CHICKEN,false);
        		mHasCreatedOrder.put(EnumItemType.SALAD,false);
        		mHasCreatedOrder.put(EnumItemType.PIZZA,false);
        		
        		mItemsDesired.put(EnumItemType.STEAK,0);
        		mItemsDesired.put(EnumItemType.CHICKEN,1);
        		mItemsDesired.put(EnumItemType.SALAD,2);
        		mItemsDesired.put(EnumItemType.PIZZA, 0);
        }
        
        public void setPerson(Person person){
            super.mPerson = person;
        	switch(mRestaurantID){
				case 0: //andre
					subRole = new AndreCookRole(super.mPerson, this);
					if(AndreRestaurant.cook == null) {
						AndreRestaurant.addCook((AndreCookRole) subRole);
					} else {
						subRole = AndreRestaurant.cook;
					}
					break;
//				case 1: //chase
//					subRole = new CwagonerCookRole(super.mPerson);
//					CwagonerRestaurantPanel.cook = (CwagonerCookRole) subRole;
//					break;
				case 2: //jerry
					subRole = new JerrywebCookRole(super.mPerson, this);
					JerrywebRestaurant.cook = ((JerrywebCookRole) subRole);
					break;
				case 3: //maggi
					subRole = new MaggiyanCookRole(super.mPerson, this);
					MaggiyanAnimationPanel.addPerson((MaggiyanCookRole) subRole);
					break;
				case 4: //david
					subRole = new DavidCookRole(super.mPerson, this);
					if (DavidRestaurant.cook == null) {
						DavidRestaurant.addCook((DavidCookRole) subRole);
					} else {
						subRole = DavidRestaurant.cook;
					}
					break;
				case 5: //shane
					subRole = new SmilehamCookRole(super.mPerson, this);
					if (SmilehamRestaurant.mCook == null) {
						SmilehamRestaurant.addPerson((SmilehamCookRole) subRole);
					} else {
						subRole = SmilehamRestaurant.mCook;
					}
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
    		if(marketPickAndExecuteAnAction())		//ANGELICA: change priority back
    			return true;
        		if(subRole != null) {
        			if(subRole.pickAndExecuteAnAction())
        				return true;
        		}
        		return false;
        }

/** MarketCookCustomerRole Data, Actions, Scheduler, etc **/

        public Map<EnumItemType, Integer> mItemInventory = new HashMap<EnumItemType, Integer>();
    	public Map<EnumItemType, Integer> mItemsDesired = new HashMap<EnumItemType, Integer>();
        
        public Map<EnumItemType, Integer> mCannotFulfill = new HashMap<EnumItemType, Integer>();
        
        public Map<EnumItemType, Boolean> mHasCreatedOrder = new HashMap<EnumItemType, Boolean>();
        
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
        		print("Creating an order.",AlertTag.R6);	//ANGELICA: hack
        		Map<EnumItemType,Integer> items = new HashMap<EnumItemType,Integer>();
                
                for(EnumItemType item : mItemsDesired.keySet()) {
                		items.put(item, mItemsDesired.get(item));
                        mItemsDesired.put(item,0);
                        mHasCreatedOrder.put(item,true);
                }
                
                MarketOrder o = new MarketOrder(items, this);
                o.setRestaurantNumber(mRestaurantID);
                mOrders.add(o);
        }
        
        private void placeOrder(MarketOrder o) {
        		print("Placing an order "+o,AlertTag.R6);	//ANGELICA: hack
        		int m;
        		if(mMarketCashier == null) {
        			m = (int) (Math.random() % 2);
        			mMarketCashier = ContactList.sMarketList.get(m).mCashier;
        		}
                mMarketCashier.msgOrderPlacement(o);
                //ANGELICA: 0 fill in each restaurant
                RestaurantCashierRole restaurantCashier = null;
                
                switch(mRestaurantID) {
                case 0:	//andre
                	restaurantCashier = AndreRestaurant.cashier.mRole;
                	break;
                case 1: //chase
                	
                	break;
                case 2: //jerry
                	restaurantCashier = JerrywebRestaurant.cashier.mRole;
                	break;
                case 3: //maggi
                	
                	break;
                case 4: //david
                	restaurantCashier = DavidRestaurant.cashier.mRole;
                	break;
                case 5: //shane
                	
                	break;
                case 6: //angel
                	restaurantCashier = TranacRestaurant.mCashier.mRole;
                	break;
                case 7: //rex
                	 
                	break;
                }
                
                restaurantCashier.msgPlacedMarketOrder(o,mMarketCashier);
        }
        
        private void processOrder(MarketInvoice i) {   
        		print("Processing order "+ i.mOrder,AlertTag.R6);	//ANGELICA: hack
                for(EnumItemType item : mCannotFulfill.keySet()) {
                        mItemsDesired.put(item, mItemsDesired.get(item)+mCannotFulfill.get(item));
                }
        }
        
        private void completeOrder(MarketOrder o) {
        		print("Complete order.",AlertTag.R6);	//ANGELICA: hacks
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

        public void setInventory(EnumItemType i, int n) {
        	mItemInventory.put(i,n);
        }
        
        @Override
    	public Location getLocation() {
    		return ContactList.cRESTAURANT_LOCATIONS.get(mRestaurantID);
    	}
}
