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

	public boolean onBus;
	
	public int mDestinationPathType = 0; //Walking as default
	
	public CityPerson(PersonAgent person, SimCityGui gui, int x, int y) {
		super(x, y, Color.ORANGE, person.getName());
		rectangle = new Rectangle(x, y, 5, 5);
		mPerson = person;
		this.gui = gui;
		this.enable();
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
        	boolean yOldInBlock = (previousY > iBlock.mY1 && previousY < iBlock.mY2);
        	if (this.rectangle.intersects(iBlock.rectangle)) {
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
		Location closeParking = findNearestParkingLot(mLocation);
		Location destParking = findNearestParkingLot(mFinalDestination);
		Location destCorner = findNearestCorner(mFinalDestination);
		
		if (mPerson.hasCar() && mUsingCar) {
			//if at corner closest to destination, walk to destination
			if (mLocation.equals(destParking)){
				System.out.println("Reached destParking");
				x = findNearestCorner(mLocation).mX;
				y = findNearestCorner(mLocation).mY;
				mDestination = new Location(mFinalDestination.mX, mFinalDestination.mY);
				mFinalDestination = null;
				//walk to destination
				mUsingCar = false;
				mDestinationPathType = 0; //Walking
			}
			//if at closest parking lot, drive to parking lot nearest destination
			else if (mLocation.equals(closeParking)) {
				System.out.println("Reached closeParking");
				mUsingCar = true;
				//calculate corners
				int currentCornerNum = -1;
				int destCornerNum = -1;
				for (int iParking = 0; iParking < ContactList.cPARKINGLOTS.size(); iParking++){
					if (mLocation.equals(ContactList.cPARKINGLOTS.get(iParking))){
						currentCornerNum = iParking;
					}
					if (destParking.equals(ContactList.cPARKINGLOTS.get(iParking))){
						destCornerNum = iParking;
					}
				}
				mDestinationPathType = findPathType(currentCornerNum, destCornerNum);
				mDestination = destParking;
			}
			else {
				mUsingCar = true;
				mDestinationPathType = 1;
				mDestination = closeParking;
			}
		}
		if (mUsingBus) {
			//do bus stuff??? CHASE MAGGI: 1 Do this
				DoTakeBus(getBusStop(x, y), getBusStop(destCorner.mX, destCorner.mY));
		}
	}
	
	
	public void paint(Graphics g) {
		rectangle.x = x;
		rectangle.y = y;
		if(isActive) {
			if (! onBus) {
				//if(SimCityGui.GRADINGVIEW) {
					g.drawString(mPerson.getName(),x,y);
					if (mUsingCar) {
						g.setColor(Color.cyan);
						rectangle.height = 10;
						rectangle.width = 10;
						g.fillRect(x, y, 10, 10);
					}
					else {
						g.setColor(Color.yellow);
						rectangle.height = 5;
						rectangle.width = 5;
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
		// 4 - Diagonal SE
		// 5 - Diagonal NW
		switch (current) {
		case 0:
			switch (dest) {
			case 1: type = 1; break;
			case 2: type = 4; break; //SE
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
			case 0: type = 5; break; //NW
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
		return isActive;
	}

	@Override
	public void setPresent(boolean state) {
		isActive = state;
	}
	
    // Just got off bus
	public void NewDestination(Location location) {
		enable();
		x = location.mX;
		y = location.mY;
		mDestination.mX = mFinalDestination.mX;
		mDestination.mY = mFinalDestination.mY;
		mFinalDestination = null;
	}
}