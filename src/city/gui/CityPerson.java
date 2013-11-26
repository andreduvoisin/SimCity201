package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import test.mock.PersonGuiInterface;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Position;
import base.ContactList;
import base.Location;
import base.PersonAgent;

public class CityPerson extends CityComponent implements PersonGuiInterface {
	
	private String name = "";
	PersonAgent mPerson = null;
	boolean atDestination = true;
	SimCityGui gui;
	
	private int xDestination = 120, yDestination = 35;
	
	static final int waiterWidth = 10;
	static final int waiterHeight = 10;
	static final int xIndex = 10;
	static final int yIndex = 10;
	
	public boolean visible;
	
	AStarTraversal aStar;
	AStarTraversal aStarNW;
	AStarTraversal aStarNE;
	AStarTraversal aStarSW;
	AStarTraversal aStarSE;
	Position currentPosition;
	Queue<Position> goToPosition = new LinkedList<Position>();
	static int numTicks = 0;
	
//	public CityPerson(int x, int y){
//		super(x,y, Color.ORANGE, "Unnamed Person");
//		
//		rectangle = new Rectangle(x, y, 5, 5);
//		
//		setUpAStar();
//	}
//	
//	public CityPerson(int x, int y, String ID){
//		super(x,y, Color.ORANGE, ID);
//		rectangle = new Rectangle(x, y, 5, 5);
//		name = ID;
//		
//		setUpAStar();
//	}
	
	public CityPerson(PersonAgent person, SimCityGui gui) {
		super(0,0, Color.ORANGE, person.getName());
		rectangle = new Rectangle(0, 0, 5, 5);
		mPerson = person;
		this.gui = gui;
		
		setUpAStar();
	}
	
	public void setUpAStar() {
		aStar = new AStarTraversal(CityPanel.grid);
		aStarNW = new AStarTraversal(CityPanel.gridNW);
		aStarNE = new AStarTraversal(CityPanel.gridNE);
		aStarSW = new AStarTraversal(CityPanel.gridSW);
		aStarSE = new AStarTraversal(CityPanel.gridSE);
		currentPosition = new Position(x / CityPanel.ASC, x / CityPanel.ASC);
		//currentPosition.moveInto(aStar.getGrid());
	}

	@Override
	public void updatePosition() {
		numTicks++;
		
		int previousX = x;
		int previousY = y;
		
		if (x < xDestination)
            x++;
        else if (x > xDestination)
            x--;

        if (y < yDestination)
            y++;
        else if (y > yDestination)
            y--;
        
        if(x == xDestination && y == yDestination){
        	this.disable();
        	atDestination = true; //SHANE: 0 where is this used? andre: I don't think it is, don't know why it's here.
        	mPerson.msgAnimationDone(); //SHANE: Add person then enable this line
        }
        
//        if(numTicks % 5 == 0) {
//        	if(!goToPosition.isEmpty()) {
//        		Position temp = goToPosition.poll();
//        		xPos = temp.getX() * CityPanel.ASC;
//        		yPos = temp.getY() * CityPanel.ASC;
//        	}
//        }


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
		// A* TEST DO NOT DELETE :D
//		for(int i = 0; i < 120; i++)
//			for(int j = 0; j < 120; j++)
//				if(CityPanel.grid[i][j].tryAcquire()) {
//					g.setColor(Color.WHITE);
//					g.fillRect(i * 5, j * 5, 5, 5);
//					CityPanel.grid[i][j].release();
//				} else {
//					g.setColor(Color.BLACK);
//					g.fillRect(i * 5, j * 5, 5, 5);
//				}
		
//		for(int i = 0; i < 60; i++)
//			for(int j = 0; j < 60; j++) {
//				if(CityPanel.gridNW[i][j].tryAcquire()) {
//					g.setColor(Color.WHITE);
//					g.fillRect(i * 5, j * 5, 5, 5);
//					CityPanel.gridNW[i][j].release();
//				} else {
//					g.setColor(Color.BLACK);
//					g.fillRect(i * 5, j * 5, 5, 5);
//				}
//				
//				if(CityPanel.gridNE[i][j].tryAcquire()) {
//					g.setColor(Color.WHITE);
//					g.fillRect(i * 5 + 300, j * 5, 5, 5);
//					CityPanel.gridNE[i][j].release();
//				} else {
//					g.setColor(Color.BLACK);
//					g.fillRect(i * 5 + 300, j * 5, 5, 5);
//				}
//				
//				if(CityPanel.gridSW[i][j].tryAcquire()) {
//					g.setColor(Color.WHITE);
//					g.fillRect(i * 5, j * 5 + 300, 5, 5);
//					CityPanel.gridSW[i][j].release();
//				} else {
//					g.setColor(Color.BLACK);
//					g.fillRect(i * 5, j * 5 + 300, 5, 5);
//				}
//				
//				if(CityPanel.gridSE[i][j].tryAcquire()) {
//					g.setColor(Color.WHITE);
//					g.fillRect(i * 5 + 300, j * 5 + 300, 5, 5);
//					CityPanel.gridSE[i][j].release();
//				} else {
//					g.setColor(Color.BLACK);
//					g.fillRect(i * 5 + 300, j * 5 + 300, 5, 5);
//				}
//			}
		
		g.setColor(color);
		g.fillRect(x, y, 5, 5);
		//g.fill3DRect(x, y, 20, 20, false);
		g.setColor(Color.WHITE);
		g.drawString(name, x - 10, y);
	}
	/*
	@Override
	public void draw(Graphics2D g) {
		if (visible){
	        g.setColor(Color.BLACK);
	        g.fillRect(xPos, yPos, waiterWidth, waiterHeight);
		}
	}
	*/
	public void DoGoToDestination(int x, int y){
		atDestination = false;
		this.enable(); 
		xDestination = x;
		yDestination = y;
	}
	
	public void DoGoToDestination(Location location){
		atDestination = false;
		this.enable();
		xDestination = location.mX;
		yDestination = location.mY;
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
	
	// A*
    //this is just a subroutine for waiter moves. It's not an "Action"
    //itself, it is called by Actions.
    public void guiMoveFromCurrentPostionTo(Position to){
		//System.out.println("[Gaut] " + guiWaiter.getName() + " moving from " + currentPosition.toString() + " to " + to.toString());
    	to = new Position(to.getX() / CityPanel.ASC, to.getY() / CityPanel.ASC);
		AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);
		List<Position> path = aStarNode.getPath();
		Boolean firstStep   = true;
		Boolean gotPermit   = true;
	
		for (Position tmpPath: path) {
		    //The first node in the path is the current node. So skip it.
		    if (firstStep) {
				firstStep   = false;
				continue;
		    }
	
		    //Try and get lock for the next step.
		    int attempts    = 1;
		    gotPermit       = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());
	
		    //Did not get lock. Lets make n attempts.
		    while (!gotPermit && attempts < 3) {
				//System.out.println("[Gaut] " + guiWaiter.getName() + " got NO permit for " + tmpPath.toString() + " on attempt " + attempts);
		
				//Wait for 1sec and try again to get lock.
				try { Thread.sleep(1000); }
				catch (Exception e){}
		
				gotPermit   = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());
				attempts ++;
		    }
	
		    //Did not get lock after trying n attempts. So recalculating path.            
		    if (!gotPermit) {
				//System.out.println("[Gaut] " + guiWaiter.getName() + " No Luck even after " + attempts + " attempts! Lets recalculate");
				guiMoveFromCurrentPostionTo(to);
				break;
		    }
	
		    //Got the required lock. Lets move.
		    //System.out.println("[Gaut] " + guiWaiter.getName() + " got permit for " + tmpPath.toString());
		    currentPosition.release(aStar.getGrid());
		    currentPosition = new Position(tmpPath.getX(), tmpPath.getY());
		    System.out.println("x: " + currentPosition.getX() + " && y: " + currentPosition.getY());
		    goToPosition.add(currentPosition);
		    //DoGoToDestination(currentPosition.getX() * 5, currentPosition.getY() * 5);
		}
		
		/*
		boolean pathTaken = false;
		while (!pathTaken) {
		    pathTaken = true;
		    //print("A* search from " + currentPosition + "to "+to);
		    AStarNode a = (AStarNode)aStar.generalSearch(currentPosition,to);
		    if (a == null) {//generally won't happen. A* will run out of space first.
			System.out.println("no path found. What should we do?");
			break; //dw for now
		    }
		    //dw coming. Get the table position for table 4 from the gui
		    //now we have a path. We should try to move there
		    List<Position> ps = a.getPath();
		    Do("Moving to position " + to + " via " + ps);
		    for (int i=1; i<ps.size();i++){//i=0 is where we are
			//we will try to move to each position from where we are.
			//this should work unless someone has moved into our way
			//during our calculation. This could easily happen. If it
			//does we need to recompute another A* on the fly.
			Position next = ps.get(i);
			if (next.moveInto(aStar.getGrid())){
			    //tell the layout gui
			    guiWaiter.move(next.getX(),next.getY());
			    currentPosition.release(aStar.getGrid());
			    currentPosition = next;
			}
			else {
			    System.out.println("going to break out path-moving");
			    pathTaken = false;
			    break;
			}
		    }
		}
		*/
    }
}
