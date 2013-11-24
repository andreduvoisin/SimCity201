package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Not to be confused with CitiBank
 */
public class CityBank extends CityComponent {
	private String bankName = "";

	public CityBank(int x, int y) {
		super(x, y, Color.green, "Bank 1");
		rectangle = new Rectangle(x, y, 120, 90);
	}

	public CityBank(int x, int y, String ID) {
		super(x, y, Color.green, ID);
		rectangle = new Rectangle(x, y, 120, 90);
		bankName = ID;
	}

	public void updatePosition() {

	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, 120, 90);
		g.setColor(Color.WHITE);
		g.drawString(bankName,x + 10 , y + 50);
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
