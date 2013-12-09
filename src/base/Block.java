package base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import city.gui.CityComponent;

public class Block extends CityComponent {
	
	public int mX1; 
	public int mX2; 
	public int mY1; 
	public int mY2; 
	
	public Block(int x1, int y1, int x2, int y2){
		mX1 = x1;
		mX2 = x2;
		mY1 = y1; 
		mY2 = y2; 
		rectangle = new Rectangle(mX1, mY1, mX2-mX1, mY2-mY1);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.cyan);
		g.drawRect(mX1, mY1, mX2-mX1, mY2-mY1);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.cyan);
		g.drawRect(mX1, mY1, mX2-mX1, mY2-mY1);
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
		// TODO Auto-generated method stub
		
	}
}
