package restaurant.restaurant_xurex.interfaces;

import java.awt.Graphics2D;

import restaurant.restaurant_xurex.gui.RestaurantGui;

public interface CookGui_ {

	public abstract void updatePosition();

	public abstract void draw(Graphics2D g);

	public abstract void DoDisplayOrder(String choice, int table);

	public abstract void DoRemoveOrder(int table);

	public abstract void DoDisplayServe(String choice, int table);

	public abstract void DoRemoveServe(int table);

	public abstract boolean isPresent();

	public abstract void DoGoToTable(int table);

	public abstract void DoGoHome();

	public abstract void DoGoRef();

	public abstract boolean atRef();

	public abstract boolean atHome();

	public abstract boolean atTable(int table);

	public abstract void setGui(RestaurantGui gui);

	public abstract int getXPos();

	public abstract int getYPos();

}