package restaurant.restaurant_tranac.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.intermediate.RestaurantCookRole;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.gui.TranacCookGui;
import restaurant.restaurant_tranac.interfaces.TranacCook;
import restaurant.restaurant_tranac.interfaces.TranacWaiter;
import base.BaseRole;
import base.ContactList;
import base.Item;
import base.Item.EnumItemType;
import base.Location;
import base.interfaces.Person;
import city.gui.trace.AlertTag;

/**
 * Restaurant Cook Agent
 */
public class TranacCookRole extends BaseRole implements TranacCook {
		public RestaurantCookRole mRole;
        private TranacCookGui cookGui;
        
        public enum OrderState {Pending, Cooking, Plated, PickedUp, Done, Finished};
        private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
        
        private Map<EnumItemType,Integer> mCookTimes = new HashMap<EnumItemType,Integer>();
        
        private final int NGRILLS = 3;
        private final int NPLATES = 4;
        private Map<Integer, Boolean> grills = new HashMap<Integer, Boolean>();
        private Map<Integer, Boolean> plates = new HashMap<Integer, Boolean>();
        
        private Timer cookingTimer = new Timer();
        
        private final int baseTime = 5000;
        private final int baseNeed = 3;
        
        private int mItemThreshold = 3;
        
        private Semaphore inTransit = new Semaphore(0, true);
        
        /* Revolving Stand Stuff */
        public List<Order> revolvingStand = Collections.synchronizedList(new ArrayList<Order>());
        private Timer standTimer = new Timer();
        
        public void addOrderToStand(TranacWaiter w, String c, int t) {
        	synchronized(revolvingStand) {
        		revolvingStand.add(new Order(w,c,t));
        	}
        }
        
        private void checkStand() {
        	synchronized(revolvingStand) {
        		synchronized(orders) {
        			Iterator<Order> itRS = revolvingStand.iterator();
        			
        			while(itRS.hasNext()) {
        				orders.add(itRS.next());
        				revolvingStand.remove(itRS);
        				itRS.remove();
        			}
        		}
        	}
        }
        
        /* Revolving Stand Stuff */
        
        public TranacCookRole(Person p, RestaurantCookRole r) {
                super(p);
                cookGui = new TranacCookGui(this);
                TranacRestaurant.addGui(cookGui);
                TranacRestaurant.addPerson(this);
                mRole = r;
                
                //create cook times
                mCookTimes.put(EnumItemType.STEAK,(int)(baseTime*2));
                mCookTimes.put(EnumItemType.CHICKEN,(int)(baseTime*1.75));
                mCookTimes.put(EnumItemType.SALAD,(int)(baseTime));
                mCookTimes.put(EnumItemType.PIZZA,(int)(baseTime));
                
                //initialize grills and plates
                for(int i=1;i<=NGRILLS;i++)
                        grills.put(i,false);
                for(int i=1;i<=NPLATES;i++)
                        plates.put(i, false);

                //start revolving standTimer
                standTimer.scheduleAtFixedRate((new TimerTask() {        //runs a new timer to "cook" the food
                    public void run() {
                        checkStand();}
                    }),new Date(System.currentTimeMillis()+8000),
                    8000);
        }

        /** Messages */

        public void msgHereIsOrder(TranacWaiter w, String c, int t) {
                orders.add(new Order(w,c,t));
                stateChanged();
        }
        
        public void msgOrderPickedUp(TranacWaiter w, String c) {
                synchronized(orders) {
                        for(Order o : orders) {
                                if(o.waiter == w && o.choice.toString().equalsIgnoreCase(c)) {
                                        o.s = OrderState.PickedUp;
                                }
                        }
                }
                stateChanged();
        }
        
        public void msgOrderDone(Order o) {
                o.s = OrderState.Done;
                stateChanged();
        }

        /** Animation Messages */
        public void msgAnimationAtGrill() {
                inTransit.release();
    //            stateChanged();
        }
        
        public void msgAnimationAtPlate() {
                inTransit.release();
      //          stateChanged();
        }
        
        /**
         * Scheduler.  Determine what action is called for, and do it.
         */
        public boolean pickAndExecuteAnAction() {

            synchronized(orders) {
            	for(Order o : orders) {
                if(o.s == OrderState.PickedUp) {
                        removeOrder(o);
                        return true;
                }
            }
            }
            synchronized(orders) {
            for(Order o : orders) {
                if(o.s == OrderState.Done) {
                        plateIt(o);
                        return true;
                }
            }
            }
            synchronized(orders) {
            for(Order o : orders) {
                        if(o.s == OrderState.Pending) {
                                tryToCookIt(o);
                                return true;
                        }
                }
            }
                DoGoToHome();
                return false;
        }

        /** Actions */

        private void tryToCookIt(final Order o) {
                EnumItemType food = o.choice;

                if(mRole.mItemInventory.get(food) < mItemThreshold) {
                    if(!mRole.mHasCreatedOrder.get(food)) {
                    	mRole.mItemsDesired.put(food,baseNeed);
                    }
                }
                
                if(mRole.mItemInventory.get(food) == 0) {
                        print("Out of choice " + food);
                        o.waiter.msgOutOfFood(o.choice.toString(), o.table);
                        orders.remove(o);
                        if(!mRole.mHasCreatedOrder.get(food)) {
                        	mRole.mItemsDesired.put(food,baseNeed);
                        }
                        return;
                }
                
                print("Cooking " + o.choice);
                o.s = OrderState.Cooking;

                for(int i=1;i<=NGRILLS;i++) {
                        if(!grills.get(i)) {
                                o.n = i;
                                grills.put(i, true);
                                break;
                        }
                }
                if(o.n == 0)
                        o.n = 1;
                
                DoGoToGrill(o);
                DoAddFoodItem(o);
                cookingTimer.schedule(new TimerTask() {        //runs a new timer to "cook" the food
                        public void run() {
                                msgOrderDone(o);
                        }
                },mCookTimes.get(food));
                mRole.mItemInventory.put(food,mRole.mItemInventory.get(food)-1);
        }
        
        private void plateIt(Order o) {
                print("Plating " + o.choice.toString());
                o.s = OrderState.Plated;
                DoGoToGrill(o);
                grills.put(o.n, false);
                for(int i=1;i<=NPLATES;i++) {
                        o.n = 0;
                        if(!plates.get(i)) {
                                o.n = i;
                                plates.put(i, true);
                                break;
                        }
                }
                if(o.n == 0)
                        o.n = 1;
                
                DoGoToPlate(o);
                o.waiter.msgOrderDone(o.choice.toString(), o.table, o.n);        //messages waiter order is done
        }

        private void removeOrder(Order o) {
                print("Removing " + o.choice);
                o.s = OrderState.Finished;
                DoRemoveOrder(o);
                plates.put(o.n, false);
                o.n = 0;
        }

        /** Animation Actions */
        private void DoGoToHome() {
                cookGui.DoGoToHome();
        }
        
        private void DoAddFoodItem(Order o) {
                cookGui.DoAddFoodItem(o.choice.toString(), o.n);
        }
        
        private void DoGoToGrill(Order o) {
                cookGui.DoGoToGrill(o.n);
                try {
                        inTransit.acquire();
                }
                catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }
        
        private void DoGoToPlate(Order o) {
                cookGui.DoGoToPlate(o.n, o.choice.toString());   //change to choose correct plate
                try {
                        inTransit.acquire();
                }
                catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }
        
        private void DoRemoveOrder(Order o) {
                cookGui.FoodPickedUp(o.n, o.choice.toString());
        }
        
        /** Utilities */

        public String getName() {
                return mPerson.getName();
        }
        
        public void setGui(TranacCookGui c) {
               cookGui = c;
        }
        
        public TranacCookGui getCookGui() {
                return cookGui;
        }

        /** Classes */
        
        private class Order {
                TranacWaiter waiter;
                EnumItemType choice;
                int table;
                int n;
                OrderState s;
                
                Order(TranacWaiter w, String c, int t) {
                        this.waiter = w;
                        choice = Item.stringToEnum(c);
                        table = t;
                        n = 0;
                        s = OrderState.Pending;
                }
        }

        @Override
    	public Location getLocation() {
    		return ContactList.cRESTAURANT_LOCATIONS.get(6);
    	}
        
        public void Do(String msg) {
    		super.Do(msg, AlertTag.R6);
    	}
    	
    	public void print(String msg) {
    		super.print(msg, AlertTag.R6);
    	}
    	
    	public void print(String msg, Throwable e) {
    		super.print(msg, AlertTag.R6, e);
    	}
}