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

import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantPanel;
import restaurant.restaurant_davidmca.gui.DavidRestaurantPanel;

import base.BaseRole;
import base.ContactList;
import base.Item.EnumItemType;
import base.interfaces.Person;
import base.interfaces.Role;

public class RestaurantCookRole extends BaseRole implements RestaurantCookInterface, RestaurantBaseInterface {
        
        Role subRole = null;
        int restaurantID;
        int mRestaurantBankNumber;
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

        		//David
                if (restaurantID == 4) {
                        subRole = DavidRestaurantPanel.getInstance().cook;
                        subRole.setPerson(super.mPerson);
                        //ANGELICA: get restaurant SSN
                }
                //Maggi 
                if (restaurantID == 3) {
                    subRole = MaggiyanRestaurantPanel.getRestPanel().cook;
                    subRole.setPerson(super.mPerson);
                }
            
                //TODO DAVID add if statements for all the other restaurants
        }
        
        public void setPerson(Person person){
                super.mPerson = person;
        }
        
        public boolean pickAndExecuteAnAction() {
                return subRole.pickAndExecuteAnAction();
        }

        /** MarketCookCustomerRole Data, Actions, Scheduler, etc **/

        protected Map<EnumItemType, Integer> mItemInventory = new HashMap<EnumItemType, Integer>();
        protected Map<EnumItemType, Integer> mItemsDesired = new HashMap<EnumItemType, Integer>();
        
        Map<EnumItemType, Integer> mCannotFulfill = new HashMap<EnumItemType, Integer>();
        
        List<MarketOrder> mOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
        List<MarketInvoice> mInvoices        = Collections.synchronizedList(new ArrayList<MarketInvoice>());
        
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
                MarketOrder o = new MarketOrder(mItemsDesired, this);
                
                for(EnumItemType item : mItemsDesired.keySet()) {
                        mItemsDesired.put(item,0);
                }
                
                mOrders.add(o);
        }
        
        private void placeOrder(MarketOrder o) {
                mMarketCashier.msgOrderPlacement(o);
        }
        
        private void payAndProcessOrder(MarketInvoice i) {
                i.mPayment = i.mTotal;
                
                ContactList.SendPayment(mRestaurantBankNumber, i.mMarketBankNumber, i.mPayment);
                
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
        }
        
/* Utilities */
        public void setMarketCashier(MarketCashier c) {
                mMarketCashier = c;
        }
}
