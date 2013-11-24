package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class CityBus extends CityComponent {

	public CityBus(int x, int y) {
		super(x, y, Color.yellow, "Bus 1");
		rectangle = new Rectangle(x,y,15,10);
	}
	
	public CityBus(int x, int y, String ID) {
		super(x, y, Color.yellow, ID);
		rectangle = new Rectangle(x,y,15,10);
	}

	//@Override
	public void updatePosition() {
		
	}
	
	public void paint(Graphics g){
		g.setColor(color);
		g.fillRect(x, y, 15, 10);
		g.fill3DRect(x, y, 15, 10, true);
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

}
