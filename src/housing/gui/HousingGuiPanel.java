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

import base.Gui;
import base.PersonAgent;
import city.gui.CityCard;
import city.gui.SimCityGui;

/*
 * @author David Carr
 */

public class HousingGuiPanel extends CityCard implements ActionListener {

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private List<Gui> guis = new ArrayList<Gui>();
	
	//Furniture Positions
	private int DiningTableXPos = 200; 
	private int DiningTableYPos = 300; 
	private int DiningTableDim = 50; 

	public HousingGuiPanel(SimCityGui city) {
		super(city);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		this.getSize();
		Timer timer = new Timer(20, this);
		timer.start();
		
		testHousingGui(); 
	}
		
	public void testHousingGui() {
//		PersonAgent mPerson = new PersonAgent();
//		PersonAgent mPerson2 = new PersonAgent();
//		HousingRenterRole renter1 = new HousingRenterRole(mPerson);
//		HousingRenterRole renter2 = new HousingRenterRole(mPerson2);
//		HousingPersonGui gui1 = new HousingPersonGui();
//		HousingPersonGui gui2 = new HousingPersonGui();
//		renter1.setGui(gui1);
//		renter2.setGui(gui2);
//		this.addGui(gui1);
//		this.addGui(gui2);
		
		PersonAgent mPerson = new PersonAgent();
		HousingRenterRole renter1 = new HousingRenterRole(mPerson);
		HousingPersonGui gui1 = new HousingPersonGui();
		//renter1.setGui(gui1);
		gui1.setPresent(true);
		this.addGui(gui1);
		
		//renter1.mHungry = true; 
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
//	public void paintComponent(Graphics g){
//		Graphics2D g2 = (Graphics2D)g;
//
//        //Clear the screen by painting a rectangle the size of the frame
//        g2.setColor(getBackground());
//        g2.fillRect(0, 0, WINDOWX, WINDOWY );
//
//        //Table for eating 
//        g2.setColor(Color.YELLOW);
//        g2.fillRect(50, 50, 50, 50);
//	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);
		
		//Table for eating 
        g2.setColor(Color.YELLOW);
        g2.fillRect(DiningTableXPos, DiningTableYPos, DiningTableDim, DiningTableDim);
		
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
