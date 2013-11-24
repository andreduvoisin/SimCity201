package housing.gui;

import housing.roles.HousingRenterRole;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import city.gui.CityCard;
import city.gui.SimCityGui;
import base.Gui;
import base.PersonAgent;

/*
 * @author David Carr
 */

public class HousingHouseGuiPanel extends CityCard implements ActionListener {

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private List<Gui> guis = new ArrayList<Gui>();

	public HousingHouseGuiPanel(SimCityGui city, Color background) {
		super(city, background);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		this.getSize();
		Timer timer = new Timer(20, this);
		timer.start();
		testHousingGui();
	}
		
	public void testHousingGui() {
		PersonAgent mPerson = new PersonAgent();
		PersonAgent mPerson2 = new PersonAgent();
		HousingRenterRole renter1 = new HousingRenterRole(mPerson);
		HousingRenterRole renter2 = new HousingRenterRole(mPerson2);
		HousingPersonGui gui1 = new HousingPersonGui();
		HousingPersonGui gui2 = new HousingPersonGui();
		renter1.setGui(gui1);
		renter2.setGui(gui2);
		this.addGui(gui1);
		this.addGui(gui2);
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
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

	public void addGui(Gui g) {
		guis.add(g);
	}

}
