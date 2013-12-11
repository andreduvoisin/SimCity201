package restaurant.restaurant_maggiyan.test.mock;


import java.util.ArrayList;
import java.util.List;

import restaurant.restaurant_maggiyan.Order;
import restaurant.restaurant_maggiyan.gui.MaggiyanCookGui;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanCook;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;

public class MockCook extends Mock implements MaggiyanCook {
	
	public List<Order> revolvingStand = new ArrayList<Order>(); 
	
	public MockCook(String name){
		super(name); 
	}

	@Override
	public void msgHereIsOrder(MaggiyanWaiter w, String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPickedUpOrder(int pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void ClearPlatingArea() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Order findOrder(int pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGui(MaggiyanCookGui c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Order> getRevolvingStand() {
		log.add(new LoggedEvent("Added an order to the revolving stand"));
		return revolvingStand;
	}
	

}
