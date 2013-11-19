package city.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class CityPanel extends JPanel implements ActionListener{
	private int WINDOWX = 600;
	private int WINDOWY = 600;
	private Image bufferImage;
    private Dimension bufferSize;
	
	static final int panelXpos = 0;
	static final int panelYpos = 0;
	static final int timerCount = 5;
	
	private List<Gui> guis = new ArrayList<Gui>();
	
	public CityPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();
		
		Timer timer = new Timer(timerCount, this );
    	timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	 public void paintComponent(Graphics g) {
		 Graphics2D g2 = (Graphics2D)g;
		 
		 //Clear the screen by painting a rectangle the size of the frame
	     g2.setColor(getBackground());
	     g2.fillRect(panelXpos, panelYpos, WINDOWX, WINDOWY ); //This centers the screen on the restaurant scene with the table located in it... if not located at 0,0 then 
	     //part of the RestaurantPanel would show
	     
	     
	 }
}
