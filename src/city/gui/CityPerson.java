package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import base.Block;
import base.ContactList;
import base.Location;
import base.PersonAgent;

public class CityPerson extends CityComponent {
	
	private String name = "";
	PersonAgent mPerson = null;
	SimCityGui gui;
	
	public Location mDestination = null; 
	public Location mFinalDestination = null;
	public boolean mUsingCar = false;
	public boolean mUsingBus = true;
	public boolean mGettingCar = false;
	public boolean mDeliverying = false;
	
	static final int xIndex = 10;
	static final int yIndex = 10;

	public boolean onBus;
	
	public int mDestinationPathType = 0; //Walking as default
	
	public CityPerson(PersonAgent person, SimCityGui gui, Location start) {
		super(start.mX, start.mY, Color.ORANGE, person.getName());
		rectangle = new Rectangle(start.mX, start.mX, 5, 5);
		mPerson = person;
		this.gui = gui;
		this.enable();
	}

	@Override
	public void updatePosition() {
		int previousX = x;
		int previousY = y;
		
		if (mDestination != null) {
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
		}
		
		if (mUsingCar) {
			rectangle.setBounds(x, y, 10, 10);
		}
		else {
			rectangle.setBounds(x, y, 5, 5);
		}
		
        //Check intersections (if going into busy intersection - stay)
        for (CityIntersection iIntersect: SimCityGui.getInstance().citypanel.intersections) {
        	if(rectangle.intersects(iIntersect.rectangle)) {
        		if (iIntersect.mOccupant != this) {
	        		x = previousX;
	        		y = previousY;
        		}
        		return;
        	}
        }
        
        if (mUsingCar) {
			rectangle.setBounds(x, y, 10, 10);
		}
		else {
			rectangle.setBounds(x, y, 5, 5);
		}

        //B* Algorithm
        
        for (Block iBlock : ContactList.cNAVBLOCKS.get(mDestinationPathType)){
			if (rectangle.intersects(iBlock.rectangle)) {
				rectangle.x = previousX;
				if (rectangle.intersects(iBlock.rectangle)) {
					rectangle.x = x;
					rectangle.y = previousY;
				}
			}
        }
        
       x = rectangle.x;
       y = rectangle.y;
	}
	
	public void DoGoToDestination(Location location){
		this.enable(); 
		
//		mFinalDestination = ContactList.getDoorLocation(location);
		mFinalDestination = location;
		//calculate intermediate destination
		Location mLocation = new Location(x, y);
		Location closeCorner = findNearestCorner(mLocation);
		Location closeParking = findNearestParkingLot(mLocation);
		Location destParking = findNearestParkingLot(mFinalDestination);
		Location destCorner = findNearestCorner(mFinalDestination);
		
		//If person has a car
		if (mDeliverying ||(mPerson.hasCar() && !mGettingCar)) {	//ANGELICA
			if (mDeliverying || mPerson.hasCar()) {
				//if at corner closest to destination, walk to destination
				if (mLocation.equals(destParking)){
					x = closeCorner.mX;
					y = closeCorner.mY;
					mDestination = ContactList.getDoorLocation(mFinalDestination);
					mFinalDestination = null;
					//walk to destination
					mUsingCar = false;
					mGettingCar = true;
					mDestinationPathType = 0; //Walking
				}
				//if at closest parking lot, drive to parking lot nearest destination
				else if (mLocation.equals(closeParking)) {
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
					mPerson.print("route :"+mDestinationPathType);
					if (mDestinationPathType == 6) {
						x = 50;
						y = 65;
						destParking = new Location(50, 535);
					}
					mDestination = destParking;
				}
				else {
					mUsingCar = true;
					mDestinationPathType = 1;
					mDestination = closeParking;
				}
			}
		}
		
		//Else - if person does not have car, use bus
		else {
			if (mLocation.equals(destCorner)) {
				mUsingCar = false;
				mDestinationPathType = 0;
				mDestination = ContactList.getDoorLocation(mFinalDestination);
				mFinalDestination = null;
			}
			else {
				if (mLocation.equals(closeCorner)) {
//					mPerson.print("got to closecorner");
					if (mUsingBus) {
						mPerson.print("IN HEREUSING BUS");
						DoTakeBus(getBusStop(x, y), getBusStop(destCorner.mX, destCorner.mY));
					}
				} else {
					mUsingCar = false;
					mGettingCar = false;
					mDestinationPathType = 0;
					mDestination = closeCorner;
				}
			}
		}
	}
	
	
	public void paint(Graphics g) {
//		rectangle.x = x;
//		rectangle.y = y;
		if(isActive) {
			if (! onBus) {
//				if(SimCityGui.GRADINGVIEW) {
					g.setColor(color.white);
					g.drawString(mPerson.getName(),x,y);
					if(mDeliverying) {			//ANGELICA
						g.setColor(Color.MAGENTA);
						g.fillRect(x,y,10,10);
					}
					else if (mUsingCar) {
						g.setColor(Color.cyan);
						g.fillRect(x, y, 10, 10);
					}
					else {
						g.setColor(Color.yellow);
						g.fillRect(x, y, 5, 5);
					}
//					g.setColor(Color.WHITE);
//					g.drawString(name, x - 10, y);
//				}
//				else {
//					g.setColor(color);
//					g.fillRect(x, y, 5, 5);
//					g.setColor(Color.WHITE);
//					g.drawString(name, x - 10, y);
//				}
			}
		}
	}
	
	
	public void DoTakeBus(int currentStop, int destinationStop){
		mPerson.print("dotakebus");
		mPerson.mCommuterRole.msgAtBusStop(currentStop, destinationStop);
		mUsingBus = false;
	}

	public void DoBoardBus() {
//		mDestination = findNearestParkingLot(new Location(x, y));
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
			case 3: type = 6; break; 
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