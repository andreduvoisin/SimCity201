package test.mock;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.text.Position;

import base.Location;

public interface PersonGuiInterface {

	public abstract void setUpAStar();

	public abstract void updatePosition();

	public abstract void paint(Graphics g);

	/*
	@Override
	public void draw(Graphics2D g) {
		if (visible){
	        g.setColor(Color.BLACK);
	        g.fillRect(xPos, yPos, waiterWidth, waiterHeight);
		}
	}
	 */
	public abstract void DoGoToDestination(int x, int y);

	public abstract void DoGoToDestination(Location location);

	public abstract void draw(Graphics2D g);

	public abstract boolean isPresent();

	public abstract void setPresent(boolean state);

	// A*
	//this is just a subroutine for waiter moves. It's not an "Action"
	//itself, it is called by Actions.
	public abstract void guiMoveFromCurrentPostionTo(Position to);

}