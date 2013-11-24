package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class CityMarket extends CityComponent{
	private String marketName = "";
	
	public CityMarket(int x, int y) {
		super(x, y, Color.blue, "Market 1");
		rectangle = new Rectangle(x, y, 90, 90);
	}
	
	public CityMarket(int x, int y, String ID) {
		super(x, y, Color.blue, ID);
		rectangle = new Rectangle(x, y, 90, 90);
		marketName = ID;
	}

	@Override
	public void updatePosition() {

	}
	
	public void paint(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, 90, 90);
		g.fill3DRect(x, y, 90, 90, true);
		g.setColor(Color.WHITE);
		g.drawString(marketName,x + 25 , y + 50);
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
