package city.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import base.PersonAgent;

public class CityPerson implements Gui{
	
	private PersonAgent agent = null;
	private boolean atDestination = true;
	SimCityGui gui;
	
	private int xPos = 20, yPos = 20;
	private int xDestination = 120, yDestination = 35;
	public int previousX = 0;
	public int previousY = 0;
	
	static final int waiterWidth = 10;
	static final int waiterHeight = 10;
	static final int xIndex = 10;
	static final int yIndex = 10;
	
	public CityPerson(PersonAgent P, SimCityGui gui) {
		agent = P;
		this.gui = gui;
	}

	@Override
	public void updatePosition() {
		previousX = xPos;
		previousY = yPos;
		if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
	}

	@Override
	public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(xPos, yPos, waiterWidth, waiterHeight);
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public void DoGoToDestination(int x, int y){
		atDestination = false;
		xDestination = x;
		yDestination = y;
	}
	

}
