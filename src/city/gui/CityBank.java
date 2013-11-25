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
	private int BANKWIDTH = 200;
	private int BANKHEIGHT = 100; 

	public CityBank(int x, int y) {
		super(x, y, Color.green, "Bank 1");
		rectangle = new Rectangle(x, y, BANKWIDTH, BANKHEIGHT);
	}

	public CityBank(int x, int y, String ID) {
		super(x, y, Color.green, ID);
		rectangle = new Rectangle(x, y, BANKWIDTH, BANKHEIGHT);
		bankName = ID;
	}

	public void updatePosition() {

	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fill3DRect(x, y, BANKWIDTH, BANKHEIGHT, true);
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
