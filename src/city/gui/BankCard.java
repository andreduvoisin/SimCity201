package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class BankCard extends SimCityGui{
	public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;
	
	public BankCard() {
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		//addMouseListener(this);
		//background = Color.green;
	}
	
	public void paint(Graphics l) {
		l.setColor(Color.ORANGE);
		l.fillRect(100, 100, 20, 20);
	}
		

}
