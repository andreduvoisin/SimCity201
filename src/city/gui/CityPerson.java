package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.text.Position;

import base.ContactList;
import base.Location;
import base.PersonAgent;

public class CityPerson extends CityComponent {
	
	private String name = "";
	PersonAgent mPerson = null;
	boolean atDestination = true;
	SimCityGui gui;
	
	public int xDestination = 120, yDestination = 35;
	public Location mFinalDestination = null;
	
	static final int waiterWidth = 10;
	static final int waiterHeight = 10;
	static final int xIndex = 10;
	static final int yIndex = 10;
	
	public boolean visible;
//	
//	Position currentPosition;
//	Queue<Position> goToPosition = new LinkedList<Position>();
	static int numTicks = 0;
	
	public CityPerson(PersonAgent person, SimCityGui gui, int x, int y) {
		super(x, y, Color.ORANGE, person.getName());
		rectangle = new Rectangle(0, 0, 5, 5);
		mPerson = person;
		this.gui = gui;
	}

	@Override
	public void updatePosition() {
		numTicks++;
		
		int previousX = x;
		int previousY = y;
		
		if (x < xDestination)		x++;
        else if (x > xDestination)	x--;

        if (y < yDestination)		y++;
        else if (y > yDestination)	y--;
        
        if (x == xDestination && y == yDestination) {
        	this.disable();
        	atDestination = true; //SHANE: 0 where is this used? andre: I don't think it is, don't know why it's here.
        	mPerson.msgAnimationDone(); //SHANE: Add person then enable this line
    	}


        //B* Algorithm :)
        
        boolean xOldInBlock = 	(((previousX > ContactList.cGRID_POINT1-5) && (previousX < ContactList.cGRID_POINT2)) || 
								((previousX > ContactList.cGRID_POINT3-5) && (previousX < ContactList.cGRID_POINT4)) ||
								((previousX > ContactList.cGRID_POINT5-5) && (previousX < ContactList.cGRID_POINT6)) ||
        						((previousX > ContactList.cGRID_POINT7-5) && (previousX < ContactList.cGRID_POINT8))
        						);
        boolean yOldInBlock = 	(((previousY > ContactList.cGRID_POINT1-5) && (previousY < ContactList.cGRID_POINT2)) || 
								((previousY > ContactList.cGRID_POINT3-5) && (previousY < ContactList.cGRID_POINT4)) ||
								((previousY > ContactList.cGRID_POINT5-5) && (previousY < ContactList.cGRID_POINT6)) ||
								((previousY > ContactList.cGRID_POINT7-5) && (previousY < ContactList.cGRID_POINT8))
								);
        boolean xNewInBlock = 	(((x > ContactList.cGRID_POINT1-5) && (x < ContactList.cGRID_POINT2)) || 
								((x > ContactList.cGRID_POINT3-5) && (x < ContactList.cGRID_POINT4)) ||
								((x > ContactList.cGRID_POINT5-5) && (x < ContactList.cGRID_POINT6)) ||
								((x > ContactList.cGRID_POINT7-5) && (x < ContactList.cGRID_POINT8))
								);
		boolean yNewInBlock = 	(((y > ContactList.cGRID_POINT1-5) && (y < ContactList.cGRID_POINT2)) || 
								((y > ContactList.cGRID_POINT3-5) && (y < ContactList.cGRID_POINT4)) ||
								((y > ContactList.cGRID_POINT5-5) && (y < ContactList.cGRID_POINT6)) ||
								((y > ContactList.cGRID_POINT7-5) && (y < ContactList.cGRID_POINT8))
								);
        
        if (xNewInBlock && yNewInBlock){
        	if (xOldInBlock && yNewInBlock){
        		y = previousY;
        	}else{
        		x = previousX;
        	}
        }
	}
	
	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, 5, 5);
		g.setColor(Color.WHITE);
		g.drawString(name, x - 10, y);
	}
	
	public void DoGoToDestination(Location location){
		atDestination = false;
		this.enable();
		mFinalDestination = location;

		if (mFinalDestination == null){
			if (mPerson.mJobType.equals(PersonAgent.EnumJobType.TRANSPORTATION)) {
				mFinalDestination = new Location(location.mX, location.mY);
				int boardAtStop = gui.citypanel.busDispatch.getBusStopClosestTo(new Location(x, y));
				xDestination = ContactList.cBUS_STOPS.get(boardAtStop).mX;
				yDestination = ContactList.cBUS_STOPS.get(boardAtStop).mY;
			}
			else{
				//set final location and go to corner of block first
				mFinalDestination = location;
				if (location.mX < 180){
					xDestination = 95;
				}else{
					xDestination = 500;
				}
				if (location.mY < 180){
					yDestination = 95;
				}else{
					yDestination = 500;
				}
			}
		}
		else {
			xDestination = mFinalDestination.mX;
			yDestination = mFinalDestination.mY;
			mFinalDestination = null;
		}
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
	
    // Just got off bus
	public void NewDestination(Location location) {
		visible = true;
		x = location.mX;
		y = location.mY;
		xDestination = mFinalDestination.mX;
		yDestination = mFinalDestination.mY;
		mFinalDestination = null;
	}
}
