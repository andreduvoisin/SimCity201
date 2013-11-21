package market.gui;

import java.awt.*;
import market.roles.MarketWorkerRole;

public class WorkerGui implements Gui {
	private MarketWorkerRole mAgent;
	
	private int xPos = 50, yPos = 50;
	private int xDestination = 0, yDestination = 100;
	private static final int SIZE = 20;
	
	public WorkerGui(MarketWorkerRole agent) {
		mAgent = agent;
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
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, SIZE, SIZE);
	}
	
	public boolean isPresent() {
		return true;
	}
}
