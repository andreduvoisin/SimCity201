package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import transportation.TransportationBusDispatch;
import base.Location;

public class CityBus extends CityComponent {

	private TransportationBusDispatch mBusDispatch;

	List<Location> mStopCoords;

	private int mStopNumber,
				mSize = 25;
	private Location destination = new Location(0, 0);
	private boolean mTraveling;
	BufferedImage front, right, left, back;

	/**
	 * Creates new CityBus
	 * @param b Bus "driver"
	 * @param busNum Index of this instance of bus
	 */
	public CityBus(TransportationBusDispatch b, List<Location> stopCoords) {
		mBusDispatch = b;
		mTraveling = true;
		mStopNumber = 0;
		mStopCoords = stopCoords;

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
			java.net.URL rightURL = this.getClass().getClassLoader().getResource("city/gui/images/bus/leftside_sm.png");
			right = ImageIO.read(rightURL);
			java.net.URL backURL = this.getClass().getClassLoader().getResource("city/gui/images/bus/rightside_sm.png");
			back = ImageIO.read(backURL);
			java.net.URL leftURL = this.getClass().getClassLoader().getResource("city/gui/images/bus/back_sm.png");
			left = ImageIO.read(leftURL);
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		rectangle = new Rectangle(0,0,mSize,mSize);
		setX(x); setY(y);


		isActive = true;
		color = Color.yellow;

		// Set initial destination
		destination.mX = mStopCoords.get(mStopNumber + 1).mX;
		destination.mY = mStopCoords.get(mStopNumber + 1).mY;
	}


	public void paint(Graphics g) {
		drawComponents(g);
	}

	public void drawComponents(Graphics g) {
		if (mStopNumber == 0) g.drawImage(front, x, y, null);
		else if (mStopNumber == 1) g.drawImage(right, x, y, null);
		else if (mStopNumber == 2) g.drawImage(back, x, y, null);
		else if (mStopNumber == 3) g.drawImage(left, x, y, null);
	}

	public void updatePosition() {
		if (x < destination.mX)			x++;
        else if (x > destination.mX)	x--;

        if (y < destination.mY)			y++;
        else if (y > destination.mY)	y--;

        if (x == destination.mX && y == destination.mY && mTraveling) {
        	mStopNumber = (mStopNumber + 1) % mStopCoords.size();
        	mBusDispatch.msgGuiArrivedAtStop();
			mTraveling = false;
        }
        
        setX(x); setY(y);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect(x, y, mSize, mSize);
		g.fill3DRect(x, y, mSize, mSize, true);
		g.setColor(Color.white);
		g.drawString("Bus", x + mSize / 2, y + mSize);
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
