package housing.gui;

import housing.roles.HousingRenterRole;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import base.Gui;
import base.PersonAgent;

/*
 * @author David Carr
 */

public class HousingHouseGuiPanel extends JPanel implements ActionListener {
	static HousingHouseGuiPanel instance = null;

	private final int WINDOWX = 300;
	private final int WINDOWY = 400;
	private List<Gui> guis = new ArrayList<Gui>();

	private HousingHouseGuiPanel() {
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
		HousingResidentGui gui1 = new HousingResidentGui();
		HousingResidentGui gui2 = new HousingResidentGui();
		renter1.setGui(gui1);
		renter2.setGui(gui2);
		this.addGui(gui1);
		this.addGui(gui2);
	}
	
	public static HousingHouseGuiPanel getInstance() {
		if (instance == null) {
			instance = new HousingHouseGuiPanel();
		}
		return instance;
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
