package restaurant.restaurant_xurex.test.mock;


import java.awt.Graphics2D;

import restaurant.restaurant_xurex.gui.RexRestaurantGui;
import restaurant.restaurant_xurex.interfaces.WaiterGui_;

public class MockWaiterGui extends Mock implements WaiterGui_ {

	public MockWaiterGui(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoServeFood(String choice) {
		log.add(new LoggedEvent("DoServeFood: "+choice));
	}

	@Override
	public void DoCleanFood() {
		log.add(new LoggedEvent("DoCleanFood"));
	}

	@Override
	public void DoDisplayOrder(String choice, int table) {
		log.add(new LoggedEvent("DoDisplayOrder: "+choice+" "+table));
	}

	@Override
	public void DoRemoveOrder(int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void DoGoToTable(int table) {
		log.add(new LoggedEvent("DoGoToTable: "+table));
	}

	@Override
	public void DoGoHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoBase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean atHome() {
		return true;
	}

	@Override
	public boolean atBase() {
		return true;
	}

	@Override
	public boolean atTable(int table) {
		return true;
	}

	@Override
	public void setBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backToWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWaiterEnabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWaiterEnabled(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getXPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getYPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
