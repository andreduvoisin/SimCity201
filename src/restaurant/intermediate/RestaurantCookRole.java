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
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantPanel;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_smileham.roles.SmilehamCookRole;
import restaurant.restaurant_tranac.gui.RestaurantPanel_at;
import restaurant.restaurant_xurex.RexCookRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.ContactList;
import base.Item.EnumItemType;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCookRole extends BaseRole implements RestaurantCookInterface, RestaurantBaseInterface {
        
        static int totalCooks = 0;
        
        public Role subRole = null;

        int restaurantID;
        int mRestaurantID;
        protected static int DEFAULT_FOOD_QTY = 5;
        
        public RestaurantCookRole(Person person){
                super(person);
                
                mItemInventory.put(EnumItemType.STEAK,DEFAULT_FOOD_QTY);
                mItemInventory.put(EnumItemType.CHICKEN,DEFAULT_FOOD_QTY);
                mItemInventory.put(EnumItemType.SALAD,DEFAULT_FOOD_QTY);
                mItemInventory.put(EnumItemType.PIZZA,DEFAULT_FOOD_QTY);
        }
        
        public RestaurantCookRole() {
                super();
        }
        
        public void setRestaurant(int restaurantID) {
            mRestaurantID = restaurantID;

                //TODO DAVID add if statements for all the other restaurants
        	switch(restaurantID){
				case 0: //andre
					subRole = AndreRestaurantPanel.getInstance().cook;
					subRole.setPerson(super.mPerson);
					break;
				case 1: //chase
					break;
				case 2: //jerry
	//				subRole = JerrywebRestaurantPanel.getInstance().cook;
	//				subRole.setPerson(super.mPerson);
					break;
				case 3: //maggi
					 subRole = MaggiyanRestaurantPanel.getRestPanel().cook;
	                 subRole.setPerson(super.mPerson);
					break;
				case 4: //david
                    subRole = DavidRestaurantPanel.getInstance().cook;
                    subRole.setPerson(super.mPerson);
					break;
				case 5: //shane
					subRole = new SmilehamCookRole(super.mPerson);
					SmilehamAnimationPanel.addPerson((SmilehamCookRole) subRole);
					break;
				case 6: //angelica
					subRole= RestaurantPanel_at.getInstance().mCook;
					subRole.setPerson(mPerson);
					break;
				case 7: //rex
					subRole = RexAnimationPanel.getCook();
					subRole.setPerson(super.mPerson);
					RexAnimationPanel.addPerson((RexCookRole)subRole);
					break;
			}
       }
        
        public void setPerson(Person person){
                super.mPerson = person;
        }
        
        public boolean pickAndExecuteAnAction() {
        		if(subRole != null) {
        			if(subRole.pickAndExecuteAnAction())
        				return true;
        		}
        		if(marketPickAndExecuteAnAction())
        			return true;
        		return false;
      //          subRole.pickAndExecuteAnAction();
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
        public void msgInvoiceToPerson(Map<EnumItemType,Integer> cannotFulfill, MarketInvoice invoice) {
                mInvoices.add(invoice);
                mCannotFulfill = cannotFulfill;
                invoice.mOrder.mEvent = EnumOrderEvent.RECEIVED_INVOICE;
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
                                payAndProcessOrder(invoice);
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
                //check efficiency of method
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
                mMarketCashier.msgOrderPlacement(o);
        }
        
        private void payAndProcessOrder(MarketInvoice i) {
                i.mPayment = i.mTotal;
                
                //ANGELICA: HACK FOR UNIT TESTING
               	ContactList.SendPayment(mPerson.getSSN(), i.mMarketBankNumber, i.mPayment);
                
                for(EnumItemType item : mCannotFulfill.keySet()) {
                        mItemsDesired.put(item, mItemsDesired.get(item)+mCannotFulfill.get(item));
                }
                
                mMarketCashier.msgPayingForOrder(i);
                mInvoices.remove(i);
        }
        
        private void completeOrder(MarketOrder o) {
                for(EnumItemType item : o.mItems.keySet()) {
                        mItemInventory.put(item, mItemInventory.get(item)+o.mItems.get(item));
                }
                mOrders.remove(o);
        }
        
/* Utilities */
        public void setMarketCashier(MarketCashier c) {
                mMarketCashier = c;
        }
        
        public void decreaseInventory(EnumItemType i) {
        	mItemInventory.put(i,mItemInventory.get(i)-1);
        }
}
