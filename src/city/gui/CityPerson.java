package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import base.Location;
import base.PersonAgent;

public class CityPerson extends CityComponent{
	
	private String name = "";
	private PersonAgent agent = null;
	private boolean atDestination = true;
	SimCityGui gui;
	
	private int xPos = 20, yPos = 20;
	private int xDestination = 120, yDestination = 35;
	public int previousX = 0;
	public int previousY = 0;
	
	static final int waiterWidth = 10;
	static final int waiterHeight = 10;
	static final int xIndex = 10;
	static final int yIndex = 10;
	
	public boolean visible;
	
	public CityPerson(int x, int y){
		super(x,y, Color.ORANGE, "Unnamed Person");
		rectangle = new Rectangle(x, y, 5, 5);
	}
	
	public CityPerson(int x, int y, String ID){
			super(x,y, Color.ORANGE, ID);
			rectangle = new Rectangle(x, y, 5, 5);
			name = ID;
	}
	
	public CityPerson(PersonAgent P, SimCityGui gui) {
		agent = P;
		this.gui = gui;
	}

	@Override
	public void updatePosition() {
		previousX = xPos;
		previousY = yPos;
		if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        
        if(xPos == xDestination && yPos == yDestination){
        	visible = false; 
        }
	}
	
	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(xPos, yPos, 5, 5);
		//g.fill3DRect(x, y, 20, 20, false);
		g.setColor(Color.WHITE);
		g.drawString(name, xPos - 10, yPos);
	}
	/*
	@Override
	public void draw(Graphics2D g) {
		if (visible){
	        g.setColor(Color.BLACK);
	        g.fillRect(xPos, yPos, waiterWidth, waiterHeight);
		}
	}
	*/
	public void DoGoToDestination(int x, int y){
		atDestination = false;
		visible = true; 
		xDestination = x;
		yDestination = y;
	}
	
	public void DoGoToDestination(Location location){
		atDestination = false;
		visible = true; 
		xDestination = location.mX;
		yDestination = location.mY;
	}
	
	public void setVisible(){
		visible = true;
	}
	
	public void setInvisible(){
		visible = false;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}
	

}
