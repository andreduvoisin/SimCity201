package housing.gui;

import housing.House;
import housing.roles.HousingBaseRole;

import java.awt.Color;
import java.awt.Graphics2D;

import base.Gui;

/*
 * @author David Carr
 */

public class HousingPersonGui implements Gui {

	HousingBaseRole housingrole;

	private int xPos, yPos = 40;
	private int xDestination, yDestination = 40;
	private boolean currentlyAnimating;

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
		return true;
	}

	public void DoGoToHouse(House h) {
		xDestination = h.xLocation;
		yDestination = h.yLocation;
		currentlyAnimating = true;
	}

	public void DoLeaveHouse(House h) {
		xDestination = -20;
		yDestination = -20;
		currentlyAnimating = true;
	}

	public void DoCookAndEatFood() {

		currentlyAnimating = true;
	}

	public void DoMaintainHouse() {

		currentlyAnimating = true;
	}

}
