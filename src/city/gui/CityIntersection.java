package city.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import base.Block;

public class CityIntersection extends CityComponent {

	boolean mOccupied = false;
	Color mColor = null;

	public CityIntersection(Block b) {
		super(b.mX1, b.mY1, Color.green);
		rectangle = new Rectangle(b.mX1, b.mY1, b.mX2 - b.mX1, b.mY2 - b.mY1);
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updatePosition() {
		
	}

	public void setOccupied(boolean b) {
		mOccupied = b;
		if (mOccupied) {
			super.color = Color.red;
		}
		else {
			super.color = Color.green;
		}
	}

}
