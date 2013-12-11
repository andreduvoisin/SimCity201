package housing.gui;

import housing.roles.HousingBaseRole;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import base.Gui;
import city.gui.CityHousing;

/*
 * @author David Carr
 */

public class HousingPersonGui implements Gui {

	public HousingBaseRole housingrole;
	
	//Initial Positions
	private int xPos = 250; 
	private int yPos = -50;
	private int xDestination = 75;
	private int yDestination = 265;
	private boolean currentlyAnimating;
	private boolean present;
	
	Timer timer = new Timer(); 

//	//Furniture Positions
//	private int CHAIRXPOS = 280; 
//	private int CHAIRYPOS = 210; 
//	private int DiningTableDim = 50; 
//
//	//Person Positions
//	private int COUCHXPOS = 75;
//	private int COUCHYPOS = 265;
	
	//Animation Images
	private BufferedImage image, food;
	
	//----Person Positions----
	
	//Dining table chair Position
	private int eatingXPos = 280; 
	private int eatingYPos = 210; 
	private boolean showFood = false; 
	
	//Couch or Rest Position
	private int restingXPos = 75;
	private int restingYPos = 265; 
	
	//Maintenance Positions
	private int CORNERONEX = 115; 
	private int CORNERONEY = 120;
	private int CORNER2X = 420; 
	private int CORNER2Y = 110;
	private int CORNER3X = 445; 
	private int CORNER3Y = 275;
	
	private int maintenanceXPos = 30; 
	private int maintenanceYPos = 150; 
	
	//Party Variables
	private int dist = 2;
	private int xDirection = dist;
	private int yDirection = dist;
	
	private static int BOUNDS = 500;
	
	private boolean party = false;
	
	//private static int GUISIZE = 20;

	public HousingPersonGui(){ 
		super(); 
		
		image = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/person.png");
    		image = ImageIO.read(imageURL);
    		java.net.URL foodURL = this.getClass().getClassLoader().getResource("city/gui/images/food.png");
    		food = ImageIO.read(foodURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
    	}
	}
	
	public void updatePosition() {
		if (party){
			if(xPos <= 0){
				xDirection = dist;
			}
			else if(xPos >= BOUNDS){
				xDirection = -dist;
			}
			if(yPos <= 0){
				yDirection = dist;
			}
			else if (yPos >= BOUNDS){
				yDirection = -dist;
			}
			xPos += xDirection;
			yPos += yDirection;
		}
		else{
			if (xPos < xDestination)
				xPos += 1;
			else if (xPos > xDestination)
				xPos -= 1;
	
			if (yPos < yDestination)
				yPos += 1;
			else if (yPos > yDestination)
				yPos -= 1;
			
			if (xPos == xDestination && yPos == yDestination && currentlyAnimating){
				if(xDestination == eatingXPos && yDestination == eatingYPos){
					currentlyAnimating = false;
					showFood = true; 
					timer.schedule(new TimerTask() {
						public void run() {
							showFood = false; 
							housingrole.msgDoneAnimating();
						}
					},
					4000); 
				}
				else{
					currentlyAnimating = false;
					housingrole.msgDoneAnimating();
				}
				
				
			}
		}
	}

	public void draw(Graphics2D g) {
		g.drawImage(image, xPos, yPos, null);
//		g.setColor(Color.BLUE);
//		g.fillRect(xPos, yPos, GUISIZE, GUISIZE);
		if(showFood){
			g.drawImage(food, eatingXPos + 30, eatingYPos + 20, null);
		}
	}

	@Override
	public boolean isPresent() {
		return present;
	}

	public void DoGoToHouse(CityHousing h) {
		xDestination = h.xLocation;
		yDestination = h.yLocation;
		currentlyAnimating = true;
	}

	public void DoLeaveHouse(CityHousing h) {
		xDestination = -20;
		yDestination = -20;
		currentlyAnimating = true;
	}

	public void DoCookAndEatFood() {
		xDestination = eatingXPos; 
		yDestination = eatingYPos; 
		currentlyAnimating = true;
	}

	public void DoMaintainHouse() {
		xDestination = maintenanceXPos;
		yDestination = maintenanceYPos; 
		currentlyAnimating = true;
	}
	
	public void DoMaintainHouseC1() {
		xDestination = CORNERONEX;
		yDestination = CORNERONEY; 
		currentlyAnimating = true;
	}
	
	public void DoMaintainHouseC2() {
		xDestination = CORNER2X;
		yDestination = CORNER2Y; 
		currentlyAnimating = true;
	}
	
	public void DoMaintainHouseC3() {
		xDestination = CORNER3X;
		yDestination = CORNER3Y; 
		currentlyAnimating = true;
	}

	@Override
	public void setPresent(boolean state) {
		present = state;
	}
	
	public void DoParty(){
		party = true;
	}
	
	public void DoStopParty(){
		party = false;
	}
	
	public void DoGoRelax(){
		xDestination = restingXPos; 
		yDestination = restingYPos; 
			
		currentlyAnimating = true; 
	}

	@Override
	public void setFired(boolean state) {
		// TODO Auto-generated method stub
		
	}
}
