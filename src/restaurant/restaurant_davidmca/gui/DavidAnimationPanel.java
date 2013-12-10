package restaurant.restaurant_davidmca.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import restaurant.restaurant_davidmca.DavidRestaurant;
import restaurant.restaurant_davidmca.Table;
import base.Gui;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings({"serial","static-access"})
public class DavidAnimationPanel extends CityCard implements ActionListener {
	
	public static DavidRestaurant restaurant;

	private final int tableSize = 50;
	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	
	/*
	 * Constructor
	 */

	public DavidAnimationPanel(SimCityGui city, DavidRestaurant rest) {
		super(city);
		this.restaurant = rest;
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		Timer timer = new Timer(Time.cSYSCLK / 40, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint(); // Will have paintComponent called
		synchronized (restaurant.guis) {
			for (Gui gui : restaurant.guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);

		for (Table table : restaurant.tables) {
			g.setColor(Color.ORANGE);
			g.fillRect(table.getX(), table.getY(), tableSize, tableSize);
		}

		for (Gui gui : restaurant.guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}

}
