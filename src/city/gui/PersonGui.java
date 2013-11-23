package city.gui;

import java.awt.*;

import base.Gui;
import base.Location;
import base.PersonAgent;

public class PersonGui implements Gui {

    private PersonAgent agent = null;
    static final int PERSONSIZE = 20;	// Size of each side of person (square).
    
    SimCityGui gui;
    
    private int xPos = 0, yPos = 0;//default waiter position
    private int xDestination = 0, yDestination = 0;//default start position

    public PersonGui(PersonAgent agent, SimCityGui gui) {
        this.agent = agent;
        this.gui = gui;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        
        if (xPos == xDestination && yPos == yDestination)
        	agent.msgAnimationDone();
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, PERSONSIZE, PERSONSIZE);
        g.setColor(Color.BLACK);
		g.drawString("P", xPos, yPos);
    }
    
    public void DoGoTo(Location location) {
        xDestination = location.mX;
        yDestination = location.mY;
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public boolean isPresent() {
		return false;
	}
}
