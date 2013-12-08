package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import transportation.roles.CommuterRole;
import base.Block;
import base.ContactList;
import base.Location;
import base.PersonAgent;

public class CityPerson extends CityComponent {
	
	private String name = "";
	PersonAgent mPerson = null;
	SimCityGui gui;
		
	
	public int xDestination = 120, yDestination = 35;
	public Location mNextDestination = null;
	
	static final int xIndex = 10;
	static final int yIndex = 10;
	
	public boolean visible;
	public boolean onBus;
	
	/* 0 - Car Paths: AB, BC, CD, DA
	 * 1 - Car Paths: BD, DB
	 * 2 - Car Paths: AC, CA
	 * 3 - Walking
	 */
	public int mDestinationPath = -1;
//	Queue<Location> goToPosition = new LinkedList<Position>();
	
	public CityPerson(PersonAgent person, SimCityGui gui, int x, int y) {
		super(x, y, Color.ORANGE, person.getName());
		rectangle = new Rectangle(0, 0, 5, 5);
		mPerson = person;
		this.gui = gui;
		
		visible = true;
	}

	@Override
	public void updatePosition() {
		if(visible) {
			int previousX = x;
			int previousY = y;
			
			if (x < xDestination)		x++;
	        else if (x > xDestination)	x--;
	
	        if (y < yDestination)		y++;
	        else if (y > yDestination)	y--;
	        
	        //if at destination
	        if (x == xDestination && y == yDestination) {
	        	if(mNextDestination != null){
	        		DoGoToNextDestination();
	        	}else{
	        		this.disable(); 
	        		mPerson.msgAnimationDone();
	        	}
	    	}
	        
	        //Check intersections (if going into busy intersection - stay)
	        for (Block iBlock : ContactList.cINTERSECTIONBLOCKS){
	        	boolean xNewInBlock = (x > iBlock.mX1 && x < iBlock.mX2);
	        	boolean yNewInBlock = (y > iBlock.mY1 && y < iBlock.mY2);
	        	if (xNewInBlock && yNewInBlock){
	        		x = previousX;
	        		y = previousY;
	        		return;
	        	}
	        }
	
	
	        //B* Algorithm
	        List<Block> blocks = null;
	        switch(mDestinationPath){
		        case 0: 
		        case 1: 
		        case 2:
		        	blocks = ContactList.cCARBLOCKS.get(mDestinationPath); break;
		        case 3:
		        	blocks = ContactList.cPERSONBLOCKS; break;
	        }
	        
//	        for (Block iBlock : blocks){
//	        	boolean xNewInBlock = (x > iBlock.mX1 && x < iBlock.mX2);
//	        	boolean yNewInBlock = (y > iBlock.mY1 && y < iBlock.mY2);
//	        	if (xNewInBlock && yNewInBlock){
//	        		if (xNewInBlock){
//	        			x = previousX;
//	        		}else{
//	        			y = previousY;
//	        		}
//	        	}
//	        }
		}
	}
	
	public void paint(Graphics g) {
		if(visible) {
			if (! onBus) {
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
	
	public void DoWalkToDestination(){
		xDestination = mNextDestination.mX;
		yDestination = mNextDestination.mY;
		mNextDestination = null; 
	}
	
	public void DoTakeBus(int currentStop, int destinationStop){
		CommuterRole cRole = (CommuterRole) mPerson.getCommuterRole();
		cRole.msgAtBusStop(currentStop, destinationStop); 
	}

	public void DoBoardBus() {
		onBus = true;
	}

	public void DoExitBus(int destStop) {
		onBus = false;
		x = base.ContactList.cBUS_STOPS.get(destStop).mX;
		y = base.ContactList.cBUS_STOPS.get(destStop).mY;
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
		// Auto-generated method stub
		
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
