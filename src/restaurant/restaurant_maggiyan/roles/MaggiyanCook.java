package restaurant.restaurant_maggiyan.roles;

import restaurant.restaurant_maggiyan.Order;
import restaurant.restaurant_maggiyan.gui.MaggiyanCookGui;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;

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
	public abstract void addRStandOrder(MaggiyanWaiter w, String c, int t);

	public abstract Order findOrder(int pos);

	public abstract void setGui(MaggiyanCookGui c);

}