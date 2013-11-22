package simcity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Not to be confused with CitiBank
 */
public class CityBank extends CityComponent {


	public CityBank(int x, int y) {
		super(x, y, Color.green, "Bank 1");
		rectangle = new Rectangle(x, y, 20, 20);
	}

	public CityBank(int x, int y, String I) {
		super(x, y, Color.green, I);
		rectangle = new Rectangle(x, y, 20, 20);
	}

	public void updatePosition() {

	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, 20, 20);
	}
}
