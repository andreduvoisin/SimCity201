package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import base.ContactList;
import base.Location;
import base.PersonAgent;

public class CityPerson extends CityComponent {
	
	private String name = "";
	PersonAgent mPerson = null;
	SimCityGui gui;
	
	List<Location> corners = new ArrayList<Location>(); 
	
	public int xDestination = 120, yDestination = 35;
	public Location mNextDestination = null;
	
	static final int waiterWidth = 10;
	static final int waiterHeight = 10;
	static final int xIndex = 10;
	static final int yIndex = 10;
	
	public boolean visible;

//	Queue<Location> goToPosition = new LinkedList<Position>();
	//static int numTicks = 0;
	
	public CityPerson(PersonAgent person, SimCityGui gui, int x, int y) {
		super(x, y, Color.ORANGE, person.getName());
		rectangle = new Rectangle(0, 0, 5, 5);
		mPerson = person;
		this.gui = gui;
		
		corners.add(new Location(500, 95)); 
		corners.add(new Location(95, 95));
		corners.add(new Location(95, 500)); 
		corners.add(new Location(500, 500));
		
	}

	@Override
	public void updatePosition() {
		//numTicks++;
		
		int previousX = x;
		int previousY = y;
		
		if (x < xDestination)		x++;
        else if (x > xDestination)	x--;

        if (y < yDestination)		y++;
        else if (y > yDestination)	y--;
        
        if (x == xDestination && y == yDestination) {
        	//this.disable();
        	//mPerson.msgAnimationDone(); //SHANE: Add person then enable this line
        	
        	if(mNextDestination != null){
        		DoGoToNextDestination();
        	}else{
        		this.disable(); 
        		mPerson.msgAnimationDone();
        	}
       
        	
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
	
//	public void DoGoToDestination(Location location){
//		atDestination = false;
//		this.enable();
//		mFinalDestination = location;
//		
//		if (mFinalDestination == null){
//			
//		//set final location and go to corner of block first
//		mFinalDestination = location;
//		if (location.mX < 180){
//			xDestination = 95;
//		}else{
//			xDestination = 500;
//		}
//		if (location.mY < 180){
//			yDestination = 95;
//		}else{
//			yDestination = 500;
//		}
//		
//		//SHANE: Change up to add queue of destinations
//		
//		xDestination = mFinalDestination.mX;
//		yDestination = mFinalDestination.mY;
//		mFinalDestination = null;
//		}
//		
//	}
	
	public void DoGoToDestination(Location location){
		this.enable(); 
		if(mPerson.hasCar()){
			//MAGGI ANGELICA: write car gui code
			//Suggestion: Just drive to the building..? 
		}else{
			Location myLocation = new Location(x, y);
			DoGoToCorner(myLocation); 
			//Checks if the closest corner to person is also closest corner to destination
	
		}
	}
	
	//Already at corner closest to destination
	public void DoGoToNextDestination(){
		Location cornerLocation = findNearestCorner(mNextDestination); 
		// If already at corner location nearest mNextDestination, don't need to take
		// the bus (since it would take you to a corner farther from mNextDestination;
		// walk to mNextDestination.
		if (x == cornerLocation.mX && y == cornerLocation.mY) {
			DoWalkToDestination(); 
		}
		// Otherwise, can get to a closer corner by taking the bus
		else {
			DoTakeBus(); 
		}	
	}
	
	public void DoGoToCorner(Location location){
		Location cornerLocation = findNearestCorner(location);
		xDestination = cornerLocation.mX;
		yDestination = cornerLocation.mY; 
		
	}
	
	public void DoWalkToDestination(){
		xDestination = mNextDestination.mX;
		yDestination = mNextDestination.mY;
		mNextDestination = null; 
	}
	
	public void DoTakeBus(){
		
	}
	
	public void DoGoToBusStop(Location location){
		
	}

	/**
	 * Finds closest corner location to desired destination
	 * @param destination Target position in form of a Location object
	 * @return (Location object) corners.get(determined nearest corner)
	 */
	public Location findNearestCorner(Location destination){
		// Top
			// right
			if (destination.mX > 180 && destination.mY < 180) {
				return corners.get(0); 
			}
			// left
			else if (destination.mX < 180 && destination.mY < 180) {
				return corners.get(1); 
			}
		// Bottom
			// right
			else if (destination.mX > 180 && destination.mY > 180) {
				return corners.get(3); 
			}
			// left
			else if (destination.mX < 180 && destination.mY > 180) {
				return corners.get(2); 
			}
			else
				return null;
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
		xDestination = mNextDestination.mX;
		yDestination = mNextDestination.mY;
		mNextDestination = null;
	}
}
