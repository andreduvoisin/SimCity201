package restaurant_davidmca.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import restaurant_davidmca.Table;
import restaurant_davidmca.agents.CookAgent;

public class CookGui implements Gui {

	private CookAgent agent = null;

	private int xPos, yPos = -20;
	private int xHome = 300, yHome = 600;
	private int xDestination = 300, yDestination = 600;
	private int FoodSize;
	private static int CookSize = 20;
	private String labelText = "";

	private boolean currentlyAnimating;

	public CookGui(CookAgent agent) {
		currentlyAnimating = false;
		this.agent = agent;
		xPos = xHome;
		yPos = yHome;
	}

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
			agent.msgDoneAnimating();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, CookSize, CookSize);
		g.setColor(Color.BLACK);
		g.drawString(labelText, xPos, yPos);
		g.setColor(Color.BLACK);
		g.fillRect(xPos+CookSize, yPos+CookSize, FoodSize, FoodSize);
	}

	public boolean isPresent() {
		return true;
	}

	public void setLabelText(String text) {
		currentlyAnimating = true;
		labelText = text;
	}
	
	public void setFood() {
		FoodSize = 10;
	}
	
	public void removeFood() {
		FoodSize = 0;
	}

	public void DoGoToFridge() {
		currentlyAnimating = true;
		xDestination = 300;
		yDestination = 500;
	}

	public void DoGoToPlating() {
		currentlyAnimating = true;
		xDestination = 250;
		yDestination = 600;
	}

	public void DoGoToGrill(int grillnum) {
		currentlyAnimating = true;
		xDestination = 400;
		yDestination = 550+grillnum*10;
	}
	
	public void DoGoToHome() {
		currentlyAnimating = true;
		xDestination = xHome;
		yDestination = yHome;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}
