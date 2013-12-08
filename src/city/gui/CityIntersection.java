package city.gui;

import java.awt.Color;
import java.awt.Graphics;
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

	public void paint(Graphics g) {
		if (mOccupied) {
			super.color = Color.red;
		}
		else {
			super.color = Color.green;
		}
		super.paint(g);
	}

	@Override
	public void updatePosition() {
		synchronized (SimCityPanel.getInstance().movings) {
			System.out.println(SimCityPanel.getInstance().movings.size());
			for (CityComponent c : SimCityPanel.getInstance().movings) {
				if (this.collidesWith(c)) {
					mOccupied = true;
					this.mColor = Color.red;
				} else {
					mOccupied = false;
					this.mColor = Color.green;
				}
			}
		}
	}

	public void setOccupied(boolean b) {
		mOccupied = b;
	}

}
