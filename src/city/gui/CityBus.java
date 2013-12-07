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

	private TransportationBus mBus;

	List<Location> mStopCoords;

	private int mStopNumber;
	private Location destination = new Location(0, 0);
	private boolean mTraveling, firstRun = true;
	BufferedImage front, right, left, back;

	/**
	 * Creates new CityBus
	 * @param b Bus "driver"
	 * @param busNum Index of this instance of bus
	 */
	public CityBus(TransportationBus b) {
		mBus = b;
		mTraveling = true;
		mStopNumber = 0;
		mStopCoords = base.reference.ContactList.cBUS_STOPS;

		// Inherited from CityComponent
		x = mStopCoords.get(mStopNumber).mX;
		y = mStopCoords.get(mStopNumber).mY;


		front = null;
		right = null;
		left = null;
		back = null;
		
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

		isActive = true;

		// Set initial destination
		destination.mX = mStopCoords.get(mStopNumber + 1).mX;
		destination.mY = mStopCoords.get(mStopNumber + 1).mY;
	}


	public void paint(Graphics g) {
		draw((Graphics2D)g);
	}

	public void updatePosition() {
		if (x < destination.mX)			x++;
        else if (x > destination.mX)	x--;

        if (y < destination.mY)			y++;
        else if (y > destination.mY)	y--;

        if (x == destination.mX && y == destination.mY && mTraveling) {
        	mStopNumber = (mStopNumber + 1) % 4;
        	mBus.msgGuiArrivedAtStop();
			mTraveling = false;
			firstRun = false;
        }
        
        setX(x); setY(y);
	}

	@Override
	public void draw(Graphics2D g) {
		if(SimCityGui.GRADINGVIEW) {
			g.setColor(Color.YELLOW);
			g.fill3DRect(x, y, 25, 25, true);
			g.setColor(Color.BLACK);
			g.drawString("Bus", x + 2 , y + 15);
		} else {
			if (mStopNumber == 0) g.drawImage(left, x, y, null);
			// hack firstRun
			if (mStopNumber == 1) g.drawImage(front, x, y, null);
			if (mStopNumber == 2) g.drawImage(right, x, y, null);
			if (mStopNumber == 3) g.drawImage(back, x, y, null);
	
			if (firstRun) g.drawImage(front, x, y, null);
		}
	}

	@Override
	public boolean isPresent() { return true; }

	@Override
	public void setPresent(boolean state) { }


	public void DoAdvanceToNextStop() {
        mTraveling = true;
        destination.setTo(mStopCoords.get(mStopNumber));
	}
}
