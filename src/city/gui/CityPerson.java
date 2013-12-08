package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import transportation.roles.CommuterRole;

import base.Location;
import base.PersonAgent;
import base.Block;
import base.ContactList;

public class CityPerson extends CityComponent {
	
	private String name = "";
	PersonAgent mPerson = null;
	SimCityGui gui;
		
	
	public int xDestination = 120, yDestination = 35;
	public Location mNextDestination = null;
	
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
        	if(mNextDestination != null){
        		DoGoToNextDestination();
        	}else{
        		this.disable(); 
        		mPerson.msgAnimationDone();
        	}
       
        	
    	}


        //B* Algorithm
        boolean xNewInBlock = false;
        boolean yNewInBlock = false;
        
        
        
        
        /*
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
        */
	}
	
	public void paint(Graphics g) {
		if(SimCityGui.GRADINGVIEW) {
			g.drawString(mPerson.getName(),x,y);
			g.setColor(color);
			g.fillRect(x, y, 5, 5);
			g.setColor(Color.WHITE);
			g.drawString(name, x - 10, y);
		}
		else if(mPerson.hasCar()) {
			//paint car gui
		}
		else {
			g.setColor(color);
			g.fillRect(x, y, 5, 5);
			g.setColor(Color.WHITE);
			g.drawString(name, x - 10, y);
		}
	}
	
	public void DoDriveToDestination2(Location location){
		mNextDestination = location; 
		//PATHS: AB, BC, CD, DA
		if(y == mNextDestination.mY ){
			
		}
		
//		//atDestination = false;
//		this.enable();
//		mNextDestination = location;
//		
//		if (mNextDestination == null){
//			
//		//set final location and go to corner of block first
//		mNextDestination = location;
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
//		xDestination = mNextDestination.mX;
//		yDestination = mNextDestination.mY;
//		mNextDestination = null;
//		}
		
	}
	
	public void DoGoToDestinationOld(Location destination){
		this.enable(); 
		mNextDestination = destination;//correct?

		if(mPerson.hasCar()){
			//MAGGI ANGELICA: write car gui code
			//Suggestion: Just drive to the building..?
		}

		/* Given no car, what if it's closer to walk straight there than to take a bus?
		 * NOTE: this could cause people to seldom take the bus, which would be bad. We'll see.
		 *
		 * totalBusDistance = distance to nearest corner
		 * 						+ distance from bus-exit corner to destination
		 * totalWalkDist ance = distance from current location to destination
		 *
		 * if totalWalkDistance < totalBusDistance, then walk
		 */

		Location myLocation = new Location(x, y);
		Location cornerNearCurrent = findNearestCorner(myLocation);
		Location cornerNearDest = findNearestCorner(destination);
		
		//CHASE: Is it better to just calculate the closest corner vs distance b/c 
		// 		 we don't know if it's safe to assume b* will take the path specified in totalBusDistance
		int totalBusDistance = Math.abs(cornerNearCurrent.mX - x)
								+ Math.abs(cornerNearCurrent.mY - y)
								+ Math.abs(cornerNearDest.mX - destination.mX)
								+ Math.abs(cornerNearDest.mY - destination.mY);

		int totalWalkDistance = Math.abs(destination.mX - x)
								+ Math.abs(destination.mY - y);

		if (totalWalkDistance < totalBusDistance) {
			DoWalkToDestination();
		}
		else{
			DoWalkToNearestCorner(myLocation); // created earlier
			//Checks if the closest corner to person is also closest corner to destination
		}
	}
	//MAGGI: reorganize once done with transportation 
	//Drives to parking lot closest to destination 
	public void DoDriveToDestination(Location location){
		this.enable(); 
		mNextDestination = location;
		Location parkingLot = findNearestParkingLot(location); 
		xDestination = parkingLot.mX;
		yDestination = parkingLot.mY; 
	}
	
	public void DoGoToDestination(Location location){
		this.enable(); 
		mNextDestination = location; 
		Location myLocation = new Location(x, y);
		
		//Walk to nearest corner 
		Location cornerLocation = findNearestCorner(myLocation);
		xDestination = cornerLocation.mX;
		yDestination = cornerLocation.mY; 

	}

	//Already at corner closest to destination
	public void DoGoToNextDestination(){
		if(mPerson.hasCar()){
			
		}
		
		Location cornerLocation = findNearestCorner(mNextDestination); 
		// If already at corner location nearest mNextDestination, don't need to take
		// the bus (since it would take you to a corner farther from mNextDestination;
		// walk to mNextDestination.
		if (x == cornerLocation.mX && y == cornerLocation.mY) {
			//Walk to destination from corner location/bus stop
			xDestination = mNextDestination.mX;
			yDestination = mNextDestination.mY;
			mNextDestination = null; 
		}
		// Otherwise, can get to a closer corner by taking the bus
		else {
			DoTakeBus(getBusStop(x, y), getBusStop(cornerLocation.mX, cornerLocation.mY)); 
		}	
	}
	
	public void DoWalkToNearestCorner(Location location){
		Location cornerLocation = findNearestCorner(location);
		xDestination = cornerLocation.mX;
		yDestination = cornerLocation.mY; 
		
	}
	
	public void DoWalkToDestination(){
		xDestination = mNextDestination.mX;
		yDestination = mNextDestination.mY;
		mNextDestination = null; 
	}
	
	public void DoTakeBus(int currentStop, int destinationStop){
		CommuterRole cRole = (CommuterRole) mPerson.getCommuterRole();
		cRole.msgAtBusStop(currentStop, destinationStop); 
	}
	
	public int getBusStop(int x, int y){
		for(int i = 0; i < ContactList.cBUS_STOPS.size(); i++){
			if(x == ContactList.cPERSONCORNERS.get(i).mX && y == ContactList.cPERSONCORNERS.get(i).mY){
				return i;
			}
		}	
		return -1; 
	}
	
	/**
	 * Finds closest corner location to desired destination
	 * <pre>
	 * 3  0 //CHASE: do these correlate with busStop numbers?
	 * 2  1
	 * </pre>
	 * @param destination Target position in form of a Location object
	 * @return (Location object) corners.get(determined nearest corner)
	 */
	public Location findNearestCorner(Location destination){
		//TOP LEFT
		if (destination.mX < 300 && destination.mY < 300) {
			return ContactList.cPERSONCORNERS.get(0); 
		}
		//TOP RIGHT
		if (destination.mX > 300 && destination.mY < 300) {
			return ContactList.cPERSONCORNERS.get(0); 
		}
		//BOTTOM RIGHT
		if (destination.mX >= 300 && destination.mY > 300) {
			return ContactList.cPERSONCORNERS.get(1); 
		}
		//BOTTOM LEFT
		if (destination.mX < 300 && destination.mY > 300) {
			return ContactList.cPERSONCORNERS.get(2); 
		}
		return null;
	}

	public Location findNearestParkingLot(Location location){
		//TOP LEFT
		if (location.mX < 300 && location.mY < 300) {
			return ContactList.cPARKINGLOT0;
		}
		//TOP RIGHT
		if (location.mX > 300 && location.mY < 300) {
			return ContactList.cPARKINGLOT1;
		}
		//BOTTOM RIGHT
		if (location.mX > 300 && location.mY > 300) {
			return ContactList.cPARKINGLOT2; 
		}
		//BOTTOM LEFT
		if (location.mX < 300 && location.mY > 300) {
			return ContactList.cPARKINGLOT3;
		}
		//Else
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
