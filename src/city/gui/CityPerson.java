package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import base.Location;
import base.PersonAgent;

public class CityPerson extends CityComponent{
	
	private String name = "";
	private PersonAgent person = null;
	private boolean atDestination = true;
	SimCityGui gui;
	
	private int xDestination = 120, yDestination = 35;
	
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
		person = P;
		this.gui = gui;
	}

	@Override
	public void updatePosition() {
		if (x < xDestination)
            x++;
        else if (x > xDestination)
            x--;

        if (y < yDestination)
            y++;
        else if (y > yDestination)
            y--;
        
        if(x == xDestination && y == yDestination){
        	this.disable();
        	atDestination = true; //SHANE: 0 where is this used?
//        	person.msgAnimationDone(); //SHANE: Add person then enable this line
        }
	}

        //Hack A*
//        boolean xOldInBlock = ((previousX > 95) && (previousX < 500));
//        boolean yOldInBlock = ((previousY > 95) && (previousY < 500));
//        boolean xNewInBlock = ((xPos > 95) && (xPos < 500));
//        boolean yNewInBlock = ((yPos > 95) && (yPos < 500));
//        
//        if (xNewInBlock && yNewInBlock){
//        	if (xOldInBlock && yNewInBlock){
//        		yPos = previousY;
//        	}else{
//        		xPos = previousX;
//        	}
//        }

	
	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, 5, 5);
		//g.fill3DRect(x, y, 20, 20, false);
		g.setColor(Color.WHITE);
		g.drawString(name, x - 10, y);
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
		this.enable(); 
		xDestination = x;
		yDestination = y;
	}
	
	public void DoGoToDestination(Location location){
		atDestination = false;
		this.enable();
		xDestination = location.mX;
		yDestination = location.mY;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		return visible;
	}

	@Override
	public void setPresent(boolean state) {
		visible = state;
	}
	

}
