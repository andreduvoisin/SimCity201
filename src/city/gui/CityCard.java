package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class CityCard extends SimCityPanel {

	public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;
	
	//private BankCard bankCard = new BankCard();
	
	public CityCard(SimCityGui city) {
		super(city);
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		
		background = Color.green;
	}
	
	public CityCard(SimCityGui city, Color c) {
		super(city);
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		
		background = c;
			
	}
	/*
	public void paint(Graphics l) {
		l.setColor(Color.black);
		l.fillRect(0, 0, getWidth(), getHeight());
		l.setColor(Color.ORANGE);
		l.fillRect(100, 100, 20, 20);
	}*/
	



	public void mouseClicked(MouseEvent e) {
		
	}

	
	public void mouseEntered(MouseEvent e) {
		
	}

	
	public void mouseExited(MouseEvent e) {
		
	}

	
	public void mousePressed(MouseEvent e) {
		
	}

	
	public void mouseReleased(MouseEvent e) {
		
	}


	

}
