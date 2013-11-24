package housing.gui;

import housing.roles.HousingBaseRole;

import java.awt.Color;
import java.awt.Graphics2D;

import base.Gui;
import city.gui.CityHousing;

/*
 * @author David Carr
 */

public class HousingPersonGui implements Gui {

	public HousingBaseRole housingrole;
	
	//Initial Positions
	private int xPos = 40; 
	private int yPos = 40;
	private int xDestination = 40;
	private int yDestination = 40;
	private boolean currentlyAnimating;
	private boolean present;

	//Person Positions
	private int eatingXPos = 210; 
	private int eatingYPos = 280; 
	private int restingXPos = 200;
	private int restingYPos = 75; 
	private int maintenanceXPos = 30; 
	private int maintenanceYPos = 150; 
	
	private static int GUISIZE = 20;

	public void updatePosition() {
		if (xPos < xDestination)
			xPos += 1;
		else if (xPos > xDestination)
			xPos -= 1;

		if (yPos < yDestination)
			yPos += 1;
		else if (yPos > yDestination)
			yPos -= 1;
		
		if (xPos == xDestination && yPos == yDestination && currentlyAnimating) {
			currentlyAnimating = false;
			housingrole.msgDoneAnimating();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, GUISIZE, GUISIZE);
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

	@Override
	public void setPresent(boolean state) {
		present = state;
	}
	
	public void DoGoRelax(){
		xDestination = restingXPos; 
		yDestination = restingYPos; 
			
		currentlyAnimating = true; 
	}
}
