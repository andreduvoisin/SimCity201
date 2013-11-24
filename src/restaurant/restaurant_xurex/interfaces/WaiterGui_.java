package restaurant.restaurant_xurex.interfaces;

import java.awt.Graphics2D;

import restaurant.restaurant_xurex.gui.RestaurantGui;

public interface WaiterGui_ {

	public abstract void updatePosition();

	public abstract void draw(Graphics2D g);

	public abstract void DoServeFood(String choice);

	public abstract void DoCleanFood();

	public abstract void DoDisplayOrder(String choice, int table);

	public abstract void DoRemoveOrder(int table);

	public abstract boolean isPresent();

	public abstract void DoGoToTable(int table);

	public abstract void DoGoHome();

	public abstract void DoGoBase();

	public abstract boolean atHome();

	public abstract boolean atBase();

	public abstract boolean atTable(int table);

	public abstract void setBreak();

	public abstract void backToWork();

	public abstract void setWaiterEnabled();

	public abstract void setWaiterEnabled(String name);

	public abstract void setGui(RestaurantGui gui);

	public abstract int getXPos();

	public abstract int getYPos();

}