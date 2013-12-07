package restaurant.restaurant_duvoisin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.restaurant_duvoisin.gui.CookGui;
import restaurant.restaurant_duvoisin.gui.CustomerGui;
import restaurant.restaurant_duvoisin.gui.TableGui;
import restaurant.restaurant_duvoisin.gui.WaiterGui;
import restaurant.restaurant_duvoisin.roles.AndreCashierRole;
import restaurant.restaurant_duvoisin.roles.AndreCookRole;
import restaurant.restaurant_duvoisin.roles.AndreCustomerRole;
import restaurant.restaurant_duvoisin.roles.AndreHostRole;
import restaurant.restaurant_duvoisin.roles.AndreSharedWaiterRole;
import restaurant.restaurant_duvoisin.roles.AndreWaiterRole;
import base.Gui;

public class AndreRestaurant {
	public static AndreHostRole host;
	public static AndreCookRole cook;
	public static AndreCashierRole cashier;
	public static List<Gui> guis;
	
	public static TableGui tgui;
	public static Boolean waitHere[] = new Boolean[17];
	public static Boolean idleHere[] = new Boolean[12];
	
	public AndreRestaurant() {
		guis = Collections.synchronizedList(new ArrayList<Gui>());
		
		tgui = new TableGui();
		
		for(int i = 0; i < waitHere.length; i++)
        	waitHere[i] = false;
        for(int i = 0; i < idleHere.length; i++)
        	idleHere[i] = false;
	}
	
	public static void addCook(AndreCookRole role) {
		synchronized(guis) {
			cook = role;
	    	CookGui g = new CookGui(cook);
			cook.setGui(g);
			guis.add(g);
		}
	}

	public static void addCustomer(AndreCustomerRole role) {
		synchronized(guis) {
			CustomerGui g = new CustomerGui(role);
			role.setGui(g);
			guis.add(g);
			
			role.getGui().setHungry();
		}
	}

	public static void addWaiter(AndreWaiterRole role) {
		synchronized(guis) {
			WaiterGui g = new WaiterGui(role);
			role.setGui(g);
			guis.add(g);
			
			host.addWaiter(role);
		}
	}

	public static void addSharedWaiter(AndreSharedWaiterRole role) {
		synchronized(guis) {
			WaiterGui g = new WaiterGui(role);
			role.setGui(g);
			guis.add(g);
			
			host.addWaiter(role);
		}
	}
}