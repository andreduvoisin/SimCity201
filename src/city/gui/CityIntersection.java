package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import base.Block;

public class CityIntersection extends CityComponent {

	CityComponent mOccupant = null;
	Color mColor = null;
	int realX;
	int realY;
	int realWidth;
	int realHeight;

	public CityIntersection(Block b) {
		super(b.mX1, b.mY1, Color.green);
		realX = b.mX1;
		realY = b.mY1;
		realWidth = b.mX2 - b.mX1;
		realHeight = b.mY2 - b.mY1;
		rectangle = new Rectangle(b.mX1, b.mY1, b.mX2 - b.mX1, b.mY2 - b.mY1);
	}

	@Override
	public void draw(Graphics2D g) {
	}
	
	public void paint(Graphics g) {
		g.setColor(mColor);
		g.fillRect(realX, realY, realWidth, realHeight);
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

	public void setOccupant(CityComponent occupant) {
		this.mOccupant = occupant;
		if (mOccupant != null) {
			mColor = Color.red;
		}
		else {
			mColor = Color.green;
		}
	}

}
