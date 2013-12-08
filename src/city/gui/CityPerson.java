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
	
	public Location mDestination = new Location(0, 0); //just to avoid null pointers
	public Location mFinalDestination = null;
	public boolean mUsingCar = false;
	
	static final int xIndex = 10;
	static final int yIndex = 10;
	
	public boolean visible;
	public boolean onBus;
	
	/* 0 - Car Paths: AB, BC, CD, DA
	 * 1 - Car Paths: BD, DB
	 * 2 - Car Paths: AC, CA
	 * 3 - Walking
	 */
	public int mDestinationPathType = 3;
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
		int previousX = x;
		int previousY = y;
		
		if (x < mDestination.mX)		x++;
        else if (x > mDestination.mX)	x--;

        if (y < mDestination.mY)		y++;
        else if (y > mDestination.mY)	y--;
        
        //if at destination
        if (x == mDestination.mX && y == mDestination.mY) {
        	if(mFinalDestination != null){
        		DoGoToDestination(mFinalDestination);
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
        switch(mDestinationPathType){
	        case 0: 
	        case 1: 
	        case 2:
	        	blocks = ContactList.cCARBLOCKS.get(mDestinationPathType); break;
	        case 3:
	        	blocks = ContactList.cPERSONBLOCKS; break;
        }
        
        for (Block iBlock : blocks){
        	boolean xNewInBlock = (x > iBlock.mX1 && x < iBlock.mX2);
        	boolean yNewInBlock = (y > iBlock.mY1 && y < iBlock.mY2);
        	if (xNewInBlock && yNewInBlock){
        		if (xNewInBlock){
        			x = previousX;
        		}else{
        			y = previousY;
        		}
        	}
        }
	}
	
	
	public void DoGoToDestination(Location location){
		this.enable(); 
		
		mFinalDestination = location;
		
		
		//calculate intermediate destination
		Location mLocation = new Location(x, y);
		Location closeCorner = findNearestCorner(mLocation);
		Location closeParking = findNearestParkingLot(mLocation);
		Location destCorner = findNearestCorner(mFinalDestination);
		
		//if at corner closest to destination, walk to destination
		if (mLocation.equals(destCorner)){
			//walk to destination
			mDestinationPathType = 3;
			mDestination = mFinalDestination;
			mFinalDestination = null;
		}else{
			//if at a corner
			if (mLocation.equals(closeCorner)){
				//if using car, drive to corner closest to destination
				if (mUsingCar){
					//calculate 0-3 destpath
					
				}
				//else bus to same corner
				else{
					//do bus stuff??? CHASE: 2 Do this
				}
			}
			//if not at a corner, walk to corner
			else{
				mDestinationPathType = 3;
				
			}
		}
		
		
			
		
		

	}
	
	
	
	
	
	//MAGGI: reorganize once done with transportation 
	//Drives to parking lot closest to destination 
	public void DoDriveToDestination(Location location){
		this.enable(); 
		mFinalDestination = location;
		
		mDestination.mX = parkingLot.mX;
		mDestination.mY = parkingLot.mY; 
	}
	
	

	//Already at corner closest to destination
	public void DoGoToNextDestination(){
		if(mPerson.hasCar()){
			
		}
		
		Location cornerLocation = findNearestCorner(mFinalDestination); 
		// If already at corner location nearest mNextDestination, don't need to take
		// the bus (since it would take you to a corner farther from mNextDestination;
		// walk to mNextDestination.
		if (x == cornerLocation.mX && y == cornerLocation.mY) {
			//Walk to destination from corner location/bus stop
			mDestination.mX = mFinalDestination.mX;
			mDestination.mY = mFinalDestination.mY;
			mFinalDestination = null; 
		}
		
		// Otherwise, can get to a closer corner by taking the bus
		else {
			DoTakeBus(getBusStop(x, y), getBusStop(cornerLocation.mX, cornerLocation.mY)); 
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
		mDestination.mX = mFinalDestination.mX;
		mDestination.mY = mFinalDestination.mY;
		mFinalDestination = null;
	}
}
