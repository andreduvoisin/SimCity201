package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import transportation.roles.CommuterRole;
import base.Block;
import base.Location;
import base.PersonAgent;

public class CityPerson extends CityComponent {
	
	private String name = "";
	PersonAgent mPerson = null;
	SimCityGui gui;
	
	List<Location> corners = new ArrayList<Location>();
	
	//Car Data
	List<Block> blocks = new ArrayList<Block>(); 
	List<Location> parkingLots = new ArrayList<Location>(); 
	
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
		
		/*Parking lot locations
		 * D A
		 * C B
		 */
		parkingLots.add(new Location(500, 90)); 
		parkingLots.add(new Location(500, 500));
		parkingLots.add(new Location(90, 500)); 
		parkingLots.add(new Location(90, 90));
		
		/*Pedestrian and bus corner locations
		 * 4 1
		 * 3 2
		 */
		corners.add(new Location(500, 95)); 
		corners.add(new Location(500, 500));
		corners.add(new Location(95, 500)); 
		corners.add(new Location(95, 95));
		
		/*Gui Grid Blocks 
		 * Used to to maintain car gui movement
		 * NOTE: No blocks are needed for traveling paths AD, DC, CB, BA 
		 */
		//Paths: AB, BC, CD, DA
		blocks.add(new Block (100, 80, 500, 520)); 
		blocks.add(new Block (80, 100, 520, 500)); 
		//Paths: AC, CA
		blocks.add(new Block (90, 90, 280, 300)); 
		blocks.add(new Block (90, 90, 300, 280)); 
		blocks.add(new Block (300, 320, 500, 500)); 
		blocks.add(new Block (320, 300, 500, 500)); 
		//Paths: BD, DB
		blocks.add(new Block (100, 300, 280, 500)); 
		blocks.add(new Block (100, 320, 300, 500)); 
		blocks.add(new Block (300, 100, 500, 280)); 
		blocks.add(new Block (320, 100, 500, 300)); 
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


        //B* Algorithm :)
        /*
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
	
	public int getBusStop(int x, int y){
		for(int i = 0; i < corners.size(); i++){
			if(x == corners.get(i).mX && y == corners.get(i).mY){
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
		//TOP RIGHT
		if (destination.mX > 300 && destination.mY < 300) {
			return corners.get(0); 
		}
		//BOTTOM RIGHT
		else if (destination.mX >= 300 && destination.mY > 300) {
			return corners.get(1); 
		}
		//BOTTOM LEFT
		else if (destination.mX < 300 && destination.mY > 300) {
			return corners.get(2); 
		}
		//TOP LEFT
		else if (destination.mX < 300 && destination.mY < 300) {
			return corners.get(3); 
		}
		else {
			return null;
		}
	}
	
	/*
	 * Finds closest parking lot to destination
	 * 
	 * 3 0 
	 * 2 1 
	 * 
	 */
	public Location findNearestParkingLot(Location location){
		//TOP RIGHT
		if (location.mX > 300 && location.mY < 300) {
			return parkingLots.get(0); 
		}
		//BOTTOM RIGHT
		else if (location.mX > 300 && location.mY > 300) {
			return parkingLots.get(1); 
		}
		//BOTTOM LEFT
		else if (location.mX < 300 && location.mY > 300) {
			return parkingLots.get(2); 
		}
		//TOP LEFT
		else if (location.mX < 300 && location.mY < 300) {
			return parkingLots.get(3); 
		}
		else {
			return null;
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
		xDestination = mNextDestination.mX;
		yDestination = mNextDestination.mY;
		mNextDestination = null;
	}
}
