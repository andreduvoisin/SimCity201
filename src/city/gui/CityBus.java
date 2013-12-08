package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import transportation.TransportationBus;
import base.Location;

public class CityBus extends CityComponent {

	int mBusSize = 25;

	private TransportationBus mBus;
	List<Location> mStopCoords = base.ContactList.cBUS_STOPS;

	private int mStopNumber = 0;
	private Location destination = new Location(0, 0);
	private boolean mTraveling;
	BufferedImage front, right, left, back, current;

	/**
	 * Creates new CityBus
	 * @param b Bus "driver"
	 * @param busNum Index of this instance of bus
	 */
	public CityBus(TransportationBus b) {
		mBus = b;
		mTraveling = false;

		// x, y inherited from CityComponent
		x = mStopCoords.get(mStopNumber).mX;
		y = mStopCoords.get(mStopNumber).mY;

		// Set initial destination
		destination.mX = mStopCoords.get(mStopNumber + 1).mX;
		destination.mY = mStopCoords.get(mStopNumber + 1).mY;

		initializeImages();
	}

	private void initializeImages() {
		try {
			java.net.URL frontURL = this.getClass().getClassLoader().getResource("city/gui/images/bus/front_sm.png");
			front = ImageIO.read(frontURL);
			java.net.URL rightURL = this.getClass().getClassLoader().getResource("city/gui/images/bus/rightside_sm.png");
			right = ImageIO.read(rightURL);
			java.net.URL backURL = this.getClass().getClassLoader().getResource("city/gui/images/bus/back_sm.png");
			back = ImageIO.read(backURL);
			java.net.URL leftURL = this.getClass().getClassLoader().getResource("city/gui/images/bus/leftside_sm.png");
			left = ImageIO.read(leftURL);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		draw((Graphics2D)g);
	}

	public void updatePosition() {
		if (x < destination.mX)			x++;
        else if (x > destination.mX)	x--;

        if (y < destination.mY)			y++;
        else if (y > destination.mY)	y--;

        if (mTraveling && x == destination.mX && y == destination.mY) {
			mTraveling = false;
        	mBus.msgGuiArrivedAtStop();
        }
        
        setX(x); setY(y);
	}

	@Override
	public void draw(Graphics2D g) {
		if(SimCityGui.GRADINGVIEW) {
			g.setColor(Color.YELLOW);
			g.fill3DRect(x, y, mBusSize, mBusSize, true);
			g.setColor(Color.BLACK);
			g.drawString("Bus", x + 2 , y + 15);
		} else {
			g.drawImage(current, x, y, null);
		}
	}

	@Override
	public boolean isPresent() { return true; }

	@Override
	public void setPresent(boolean state) { }


	public void DoAdvanceToNextStop() {
		mStopNumber = (mStopNumber + 1) % mStopCoords.size();
        destination.setTo(mStopCoords.get(mStopNumber));
		mTraveling = true;

        if (mStopNumber == 0)		current = back;
        else if (mStopNumber == 1)	current = right;
        else if (mStopNumber == 2)	current = front;
        else if (mStopNumber == 3)	current = left;
	}
}
