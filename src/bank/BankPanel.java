package bank;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import city.gui.CityCard;
import city.gui.SimCityGui;
import base.Time;
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
	
	public BankPanel(SimCityGui city, Color background) {
		super(city, background);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();
		
		//Time mTime = new Time();
	}


	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

}
