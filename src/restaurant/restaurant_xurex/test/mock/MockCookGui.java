package restaurant.restaurant_xurex.test.mock;


import java.awt.Graphics2D;

import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import restaurant.restaurant_xurex.interfaces.CookGui_;

public class MockCookGui extends Mock implements CookGui_ {

	public MockCookGui(String name) {
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
	public void DoDisplayOrder(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoRemoveOrder(int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoDisplayServe(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoRemoveServe(int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void DoGoToTable(int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoRef() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean atRef() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean atHome() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean atTable(int table) {
		// TODO Auto-generated method stub
		return false;
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

	@Override
	public void setAnimationPanel(RexAnimationPanel animationPanel) {
		// TODO Auto-generated method stub
		
	}

	
}
