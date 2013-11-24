package restaurant.restaurant_davidmca.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import base.Gui;
import city.gui.CityCard;
import city.gui.SimCityGui;

public class AnimationPanel extends CityCard implements ActionListener {

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private List<Gui> guis = new ArrayList<Gui>();

	public AnimationPanel(SimCityGui city, Color background) {
		super(city, background);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);

		this.getSize();

		Timer timer = new Timer(20, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint(); // Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);

		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}

		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}

	public void addGui(WaiterGui g) {
		guis.add(g);
	}

	public void addGui(CustomerGui g) {
		guis.add(g);
	}

	public void addGui(HostGui gui) {
		guis.add(gui);
	}
	
	public void addGui(CookGui gui) {
		guis.add(gui);
	}
}
