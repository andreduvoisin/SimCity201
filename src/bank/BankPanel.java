package bank;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.gui.CityCard;
import city.gui.SimCityGui;
import base.interfaces.Person;


public class BankPanel extends CityCard implements ActionListener{
	private static BankPanel instance = null;
	private int WINDOWX = 500;
	private int WINDOWY = 500;
	
	private Image bufferImage;
    private Dimension bufferSize;
    public List<Person> masterPersonList = Collections.synchronizedList(new ArrayList<Person>());
	
	static final int panelXpos = 0;
	static final int panelYpos = 0;
	static final int timerCount = 5;	
	
	public BankPanel(SimCityGui city) {
		super(city);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();
		
		//Time mTime = new Time();
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

}
