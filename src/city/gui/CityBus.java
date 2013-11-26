package city.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import transportation.TransportationBusDispatch;
import base.Location;

public class CityBus extends CityComponent {

	private TransportationBusDispatch mBusDispatch;

	List<Location> mStopCoords;

	private static int sBusNumber = 0;
	private int mBusNumber;
	private int mStopNumber,
				mSize = 25;
	private Location destination = new Location(0, 0);
	private boolean mTraveling;


	/**
	 * Creates new CityBus
	 * @param b Bus "driver"
	 * @param busNum Index of this instance of bus
	 */
	public CityBus(TransportationBusDispatch b, List<Location> stopCoords) {
		mBusDispatch = b;
		mBusNumber = sBusNumber++;
		mTraveling = true;
		mStopNumber = 0;
		mStopCoords = stopCoords;

		// Inherited from CityComponent
		x = mStopCoords.get(mStopNumber).mX;
		y = mStopCoords.get(mStopNumber).mY;


		rectangle = new Rectangle(0,0,mSize,mSize);
		setX(x); setY(y);


		isActive = true;
		color = Color.yellow;

		// Set initial destination
		destination.mX = mStopCoords.get(mStopNumber + 1).mX;
		destination.mY = mStopCoords.get(mStopNumber + 1).mY;
	}


	public void updatePosition() {
		if (x < destination.mX)			x++;
        else if (x > destination.mX)	x--;

        if (y < destination.mY)			y++;
        else if (y > destination.mY)	y--;

        if (x == destination.mX && y == destination.mY && mTraveling) {
        	mBusDispatch.msgGuiArrivedAtStop(mBusNumber);
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
		System.out.println("current stop " + mStopNumber);
        mStopNumber = (mStopNumber + 1) % mStopCoords.size();

		System.out.println("new stop " + mStopNumber);
        mTraveling = true;

        destination.mX = mStopCoords.get(mStopNumber).mX;
        destination.mY = mStopCoords.get(mStopNumber).mY;
	}
}
