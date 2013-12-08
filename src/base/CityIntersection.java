package base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import city.gui.CityComponent;

public class CityIntersection extends CityComponent {
	
	public CityIntersection(int xpos, int ypos, int width, int height) {
		super(xpos, ypos, Color.green);
		rectangle = new Rectangle(xpos, ypos, width, height);
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
		// TODO Auto-generated method stub
		
	}

}
