package housing.gui;

import housing.roles.HousingLandlordRole;

import java.awt.Color;
import java.awt.Graphics2D;

import base.Gui;
/*
 * @author David Carr
 */

public class HousingLandlordGui implements Gui {
	
	HousingLandlordRole landlord;
	
	private int xPos, yPos = 20;
	private int xDestination, yDestination = 20;
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
			landlord.msgDoneAnimating();
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

}
