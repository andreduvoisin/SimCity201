package restaurant.restaurant_maggiyan.interfaces;

import java.util.List;

import restaurant.restaurant_maggiyan.Order;
import restaurant.restaurant_maggiyan.gui.MaggiyanCookGui;

public interface MaggiyanCook {

	public abstract String getName();

	//From Waiter
	public abstract void msgHereIsOrder(MaggiyanWaiter w, String choice,
			int table);

	public abstract void msgPickedUpOrder(int pos);

	//From Animation
	public abstract void msgAnimationReady();

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public abstract boolean pickAndExecuteAnAction();

	public abstract void ClearPlatingArea();

	//Utilities
	public abstract List<Order> getRevolvingStand(); 
	
	public abstract Order findOrder(int pos);

	public abstract void setGui(MaggiyanCookGui c);

}