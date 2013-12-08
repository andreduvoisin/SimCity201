package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

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
	public boolean mUsingBus = false;
	
	static final int xIndex = 10;
	static final int yIndex = 10;
	
	public boolean visible;
	public boolean onBus;
	
	public int mDestinationPathType = 0; //Walking as default
	
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
        		visible = false;
        		mPerson.msgAnimationDone();
        		mPerson.postCommute();
        	}
    	}
        
        //Check intersections (if going into busy intersection - stay)
        for (int iB = 0; iB<ContactList.cINTERSECTIONBLOCKS.size(); iB++) {
        	boolean xNewInBlock = (x > ContactList.cINTERSECTIONBLOCKS.get(iB).mX1 && x < ContactList.cINTERSECTIONBLOCKS.get(iB).mX2);
        	boolean yNewInBlock = (y > ContactList.cINTERSECTIONBLOCKS.get(iB).mY1 && y < ContactList.cINTERSECTIONBLOCKS.get(iB).mY2);
        	if (xNewInBlock && yNewInBlock){
        		if (SimCityGui.getInstance().citypanel.intersections.get(iB).mOccupant != this) {
	        		x = previousX;
	        		y = previousY;
        		}
        		return;
        	}
        }


        //B* Algorithm
        List<Block> blocks = null;
        blocks = ContactList.cNAVBLOCKS.get(mDestinationPathType);
        
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
			mUsingCar = false;
			//walk to destination
			mDestinationPathType = 0; //Walking
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
					mDestinationPathType = findPathType(currentCornerNum, destCornerNum);
					mDestination = destParking;
				}
				//else bus to same corner
				else{
					//do bus stuff??? CHASE MAGGI: 1 Do this
					if(mUsingBus) {
						DoTakeBus(getBusStop(x, y), getBusStop(destCorner.mX, destCorner.mY));
					}
				}
			}
			//if not at a corner, walk to person corner
			else{
				mDestinationPathType = 0; //Walking
				mDestination = closeCorner;
			}
		}

	}
	
	
	public void paint(Graphics g) {
		rectangle.x = x;
		rectangle.y = y;
		if(visible) {
			if (! onBus) {
				//if(SimCityGui.GRADINGVIEW) {
					g.drawString(mPerson.getName(),x,y);
					if (mUsingCar) {
						g.setColor(Color.cyan);
						g.fillRect(x, y, 15, 15);
					}
					else {
						g.setColor(Color.yellow);
						g.fillRect(x, y, 5, 5);
					}
					g.setColor(Color.WHITE);
					g.drawString(name, x - 10, y);
				}
				else {
					g.setColor(color);
					g.fillRect(x, y, 5, 5);
					g.setColor(Color.WHITE);
					g.drawString(name, x - 10, y);
				}
			//}
		}
	}
	
	
	public void DoTakeBus(int currentStop, int destinationStop){
		mPerson.mCommuterRole.msgAtBusStop(currentStop, destinationStop);
		mUsingBus = false;
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
	
	public int findPathType(int current, int dest) {
		int type = -1;
		// 0 1
		// 3 2

		// 1 - Clockwise
		// 2 - Counterclockwise 
		// 3 - Diagonal NE/SW
		// 4 - Diagonal NW/SE
		switch (current) {
		case 0:
			switch (dest) {
			case 1: type = 1; break;
			case 2: type = 4; break;
			case 3: type = 2; break;
			} break;
		case 1:
			switch (dest) {
			case 0: type = 2; break;
			case 2: type = 1; break;
			case 3: type = 3; break;
			} break;
		case 2:
			switch (dest) {
			case 0: type = 4; break;
			case 1: type = 2; break;
			case 3: type = 1; break;
			}
			break;
		case 3:
			switch (dest) {
			case 0: type = 1; break;
			case 1: type = 3; break;
			case 2: type = 2; break;
			} break;
		}
		return type;
	}
	
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