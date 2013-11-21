package city.gui;

import javax.swing.*;

import base.Time;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class OldCityPanel extends JPanel implements ActionListener{
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

	 public void paintComponent(Graphics g) {
		 Graphics2D g2 = (Graphics2D)g;
		 
		 //Clear the screen by painting a rectangle the size of the frame
	     g2.setColor(Color.green);//getBackground());
	     g2.fillRect(panelXpos, panelYpos, WINDOWX, WINDOWY ); //This centers the screen on the restaurant scene with the table located in it... if not located at 0,0 then 
	     //part of the RestaurantPanel would show
	     
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