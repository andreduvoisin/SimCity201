package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import base.ConfigParser;
import base.Time;
import base.interfaces.Person;

public class CityPanel extends JPanel implements ActionListener{
	private static CityPanel instance = null;
	private int WINDOWX = 700;
	private int WINDOWY = 600;
	private Image bufferImage;
    private Dimension bufferSize;
    List<Person> masterPersonList = Collections.synchronizedList(new ArrayList<Person>());
	
	static final int panelXpos = 0;
	static final int panelYpos = 0;
	static final int timerCount = 5;
	
	static final int mainStreetWidth = 20;
	static final int mainStreetLength = 450;
	static final int mainStreetPieceTopXPos = 100;
	static final int mainStreetPieceTopYPos = 100;
	
	static final int mainStreetPieceLeftXPos = 100;
	static final int mainStreetPieceLeftYPos = 100;
	
	static final int mainStreetPieceRightXPos = 550;
	static final int mainStreetPieceRightYPos = 100;
	
	static final int mainStreetPieceBottomXPos = 100;
	static final int mainStreetPieceBottomYPos = 550;
	
	private CityGui gui;
	private List<Gui> guis = new ArrayList<Gui>();
	
	public CityPanel(CityGui gui) {
		this.gui = gui;
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();
		
		Time mTime = new Time();
   	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}
	
	public static CityPanel getInstanceOf() {
		if (instance == null) {
			instance = new CityPanel(CityGui.getInstanceOf());
		}
		return instance;
	}

	 public void paintComponent(Graphics g) {
		 Graphics2D g2 = (Graphics2D)g;
		 
		 //Clear the screen by painting a rectangle the size of the frame
	     g2.setColor(Color.green);//getBackground());
	     g2.fillRect(panelXpos, panelYpos, WINDOWX, WINDOWY ); //This centers the screen on the restaurant scene with the table located in it... if not located at 0,0 then 
	     //part of the RestaurantPanel would show
	     
	     g2.setColor(Color.LIGHT_GRAY);
	     g2.fillRect(100, 95, 450, 5);
	     
	     //top horizontal main street piece 
	     g2.setColor(Color.BLACK);
	     g2.fillRect( mainStreetPieceTopXPos,  mainStreetPieceTopYPos, mainStreetLength, mainStreetWidth);
	     
	     //left vertical main street piece 
	     g2.setColor(Color.BLACK);
	     g2.fillRect(mainStreetPieceLeftXPos, mainStreetPieceLeftYPos, mainStreetWidth, mainStreetLength);
	     
	     //right vertical main street piece 
	     g2.setColor(Color.BLACK);
	     g2.fillRect(mainStreetPieceRightXPos, mainStreetPieceRightYPos, mainStreetWidth, mainStreetLength);
	     
	     //bottom main street piece 
	     g2.setColor(Color.BLACK);
	     g2.fillRect(mainStreetPieceBottomXPos, mainStreetPieceBottomYPos, mainStreetLength + 20, mainStreetWidth );
	     
	   
	     
	     
	     
	     
	 }
}
