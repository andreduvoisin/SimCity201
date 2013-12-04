package housing.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import base.Gui;
import city.gui.CityCard;
import city.gui.SimCityGui;

/*
 * @author David Carr, Maggi Yang
 */

public class HousingGuiPanel extends CityCard implements ActionListener {

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private List<Gui> guis = new ArrayList<Gui>();
	
	//Furniture Positions
	private int CHAIRXPOS = 280; 
	private int CHAIRYPOS = 210; 
	private int DiningTableDim = 50; 

	//Person Positions
	private int COUCHXPOS = 75;
	private int COUCHYPOS = 265;
	
	//Maintenance Hack Coordinates
	private int CORNERONEX = 115; 
	private int CORNERONEY = 120;
	private int CORNER2X = 420; 
	private int CORNER2Y = 110;
	private int CORNER3X = 445; 
	private int CORNER3Y = 275;
//	private int CORNER4X = 290; 
//	private int CORNER4Y = 100;
//	private int CORNER5X = 290; 
//	private int CORNER5Y = 100;
	
	//Images
	private BufferedImage image;
	private BufferedImage person; 
	private int bgPosx = 0;
	private int bgPosy = 0;
	
	public HousingGuiPanel(SimCityGui city) {
		super(city);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		this.getSize();
		Timer timer = new Timer(20, this);
		timer.start();
		
		//Background Image 
		image = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/homeinterior.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	//hack for person
		person = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/person.png");
    	person = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}

	}

	public void actionPerformed(ActionEvent e) {
		repaint();
		 
		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
	}


	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);
		g2.drawImage(image, bgPosx, bgPosy, null);
		
		//Table for eating 
//        g2.setColor(Color.YELLOW);
        //g2.fillRect(DiningTableXPos, DiningTableYPos, DiningTableDim, DiningTableDim);
		//Hack for person positions
//        g2.drawImage(person, COUCHXPOS, COUCHYPOS, null);
//        g2.drawImage(person, CHAIRXPOS, CHAIRYPOS, null);
//        g2.drawImage(person, CORNERONEX, CORNERONEY, null);
//        g2.drawImage(person, CORNER2X, CORNER2Y, null);
//        g2.drawImage(person, CORNER3X, CORNER3Y, null);
        
        
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
