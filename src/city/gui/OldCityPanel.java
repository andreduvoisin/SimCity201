package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

//import base.ConfigParser;
import base.Time;
import base.interfaces.Person;

public class OldCityPanel extends JPanel implements ActionListener{
	private static OldCityPanel instance = null;
	
	private int WINDOWX = 700;
	private int WINDOWY = 600;
	private Image bufferImage;
    private Dimension bufferSize;
	
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
	
	static final int HSidewalkWidth = 10;
	static final int HSidewalkLength = mainStreetLength + 30;
	
	static final int VSidewalkWidth = 10;
	static final int VSidewalkLength = mainStreetLength + 30;
	
	private CityGui gui;
	private List<Gui> guis = new ArrayList<Gui>();
	
	public OldCityPanel(CityGui gui) {
		this.gui = gui;
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();
		
		Time mTime = new Time();
   	}

	
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}
	
	public static OldCityPanel getInstanceOf() {
		if (instance == null) {
			instance = new OldCityPanel(CityGui.getInstanceOf());
		}
		return instance;
	}

	 public void paintComponent(Graphics g) {
		 Graphics2D g2 = (Graphics2D)g;
		 
		 //Clear the screen by painting a rectangle the size of the frame
	     g2.setColor(Color.green);//getBackground());
	     g2.fillRect(panelXpos, panelYpos, WINDOWX, WINDOWY ); //This centers the screen on the restaurant scene with the table located in it... if not located at 0,0 then 
	     //part of the RestaurantPanel would show
	     
	     //Top Horizontal street piece
	     	//top horizontal main street piece 
	     	g2.setColor(Color.BLACK);
	     	g2.fillRect( mainStreetPieceTopXPos,  mainStreetPieceTopYPos, mainStreetLength, mainStreetWidth);
	     	//outer sidewalk 
	     	g2.setColor(Color.LIGHT_GRAY);
	     	g2.fillRect(mainStreetPieceTopXPos, mainStreetPieceTopXPos - 10	, HSidewalkLength, HSidewalkWidth);
	     	//inner sidewalk
	     	g2.setColor(Color.LIGHT_GRAY);
	     	g2.fillRect(mainStreetPieceTopXPos + 30, mainStreetPieceTopXPos +30	, HSidewalkLength - 30, HSidewalkWidth);
	   	     	
	     //Left Vertical street piece 
	     	//left vertical main street piece 
	     	g2.setColor(Color.BLACK);
	     	g2.fillRect(mainStreetPieceLeftXPos, mainStreetPieceLeftYPos, mainStreetWidth, mainStreetLength);
	     	//outer sidewalk 
	     	g2.setColor(Color.LIGHT_GRAY);
	     	g2.fillRect(mainStreetPieceLeftXPos - 10, mainStreetPieceLeftYPos, HSidewalkWidth, HSidewalkLength);
	     	//inner sidewalk
	     	g2.setColor(Color.LIGHT_GRAY);
	     	g2.fillRect(mainStreetPieceLeftXPos + 30, mainStreetPieceLeftYPos + 30, HSidewalkWidth, HSidewalkLength -30);
	     	
	     //Right Vertical street piece
	     	//right vertical main street piece 
	     	g2.setColor(Color.BLACK);
	     	g2.fillRect(mainStreetPieceRightXPos, mainStreetPieceRightYPos, mainStreetWidth, mainStreetLength);
	     	//outer sidewalk 
	    	g2.setColor(Color.LIGHT_GRAY);
	    	g2.fillRect(mainStreetPieceRightXPos + 30, mainStreetPieceRightYPos, HSidewalkWidth, HSidewalkLength);
	     	//inner sidewalk
	    	g2.setColor(Color.LIGHT_GRAY);
	    	g2.fillRect(mainStreetPieceRightXPos - 10, mainStreetPieceRightYPos + 30, HSidewalkWidth, HSidewalkLength - 30);

	    //Bottom main street piece 
	    	g2.setColor(Color.BLACK);
	    	g2.fillRect(mainStreetPieceBottomXPos, mainStreetPieceBottomYPos, mainStreetLength + 30, mainStreetWidth );
	    	//outer sidewalk 
	    	g2.setColor(Color.LIGHT_GRAY);
	    	g2.fillRect(mainStreetPieceBottomXPos, mainStreetPieceBottomYPos + 30, HSidewalkLength, HSidewalkWidth);
	    	//inner sidewalk
	    	g2.setColor(Color.LIGHT_GRAY);
	    	g2.fillRect(mainStreetPieceBottomXPos + 30, mainStreetPieceBottomYPos - 10, HSidewalkLength - 60, HSidewalkWidth);
	    
	 }
}