package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import com.sun.activation.registries.MailcapParseException;

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
	public boolean mUsingCar = true;
	
	static final int xIndex = 10;
	static final int yIndex = 10;
	
	public boolean visible;
	public boolean onBus;
	
	/* 0 - No Blocks
	 * 1 - Car Paths: AB, BC, CD, DA
	 * 2 - Car Paths: BD, DB
	 * 3 - Car Paths: AC, CA
	 * 4 - Walking
	 */
	public int mDestinationPathType = 4;
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
//        for (Block iBlock : ContactList.cINTERSECTIONBLOCKS){
//        	boolean xNewInBlock = (x > iBlock.mX1 && x < iBlock.mX2);
//        	boolean yNewInBlock = (y > iBlock.mY1 && y < iBlock.mY2);
//        	if (xNewInBlock && yNewInBlock){
//        		x = previousX;
//        		y = previousY;
//        		return;
//        	}
//        }


        //B* Algorithm
        List<Block> blocks = null;
        switch(mDestinationPathType){
	        case 0: 
	        case 1: 
	        case 2:
	        case 3:
	        	blocks = ContactList.cCARBLOCKS.get(mDestinationPathType); break;
	        case 4:
	        	blocks = ContactList.cPERSONBLOCKS; break;
	        	//SHANE: 3 combine carblocks and personblocks into blocks
        }
        
        for (Block iBlock : blocks){
        	boolean xNewInBlock = (x > iBlock.mX1 && x < iBlock.mX2);
        	boolean yNewInBlock = (y > iBlock.mY1 && y < iBlock.mY2);
        	boolean yOldInBlock = (previousY > iBlock.mY1 && previousY < iBlock.mY2);
        	if (xNewInBlock && yNewInBlock){
        		if (yOldInBlock){
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
		Location destParking = findNearestParkingLot(mFinalDestination);
		Location destCorner = findNearestCorner(mFinalDestination);
		
		//if at corner closest to destination, walk to destination
		if (mLocation.equals(destCorner)){
			//walk to destination
			mDestinationPathType = 3;
			mDestination = mFinalDestination;
			mFinalDestination = null;
		}else{
			//if at a corner (not closest to dest)
			if (mLocation.equals(closeCorner)){
				//if using car, drive to parking lot closest to destination
				if (mUsingCar){
					//calculate corners
					int currentCornerNum = -1;
					int destCornerNum = -1;
					for (int iCorner = 0; iCorner < ContactList.cPERSONCORNERS.size(); iCorner++){
						if (mLocation.equals(ContactList.cPERSONCORNERS.get(iCorner))){
							currentCornerNum = iCorner;
						}
						if (destCorner.equals(ContactList.cPERSONCORNERS.get(iCorner))){
							destCornerNum = iCorner;
						}
					}
					
					//clockwise not needed
					if (currentCornerNum == (destCornerNum + 1) % 4) mDestinationPathType = 0;
					//check counter-clockwise
					else if (currentCornerNum == (destCornerNum - 1) % 4) mDestinationPathType = 1;
					//check BD diagonal
					else if (currentCornerNum == 1 || currentCornerNum == 3) mDestinationPathType = 2;
					//check AC diagonal
					else if (currentCornerNum == 0 || currentCornerNum == 2) mDestinationPathType = 3;
					else{
						mPerson.print("PROBLEM WITH CITY PEROSON MOVEMENT IN DOGOTODESTINATION METHOD");
					}
					
					mDestination = destParking;
				}
				//else bus to same corner
				else{
					//do bus stuff??? CHASE MAGGI: 1 Do this
//					DoTakeBus(getBusStop(x, y), getBusStop(closeCorner.mX, closeCorner.mY));
				}
			}
			//if not at a corner, walk to person corner
			else{
				mDestinationPathType = 3;
				mDestination = closeCorner;
			}
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
			return ContactList.cPERSONCORNERS.get(1); 
		}
		//BOTTOM RIGHT
		if (destination.mX >= 300 && destination.mY > 300) {
			return ContactList.cPERSONCORNERS.get(2); 
		}
		//BOTTOM LEFT
		if (destination.mX < 300 && destination.mY > 300) {
			return ContactList.cPERSONCORNERS.get(3); 
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