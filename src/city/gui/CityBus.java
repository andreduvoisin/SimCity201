package city.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import transportation.TransportationBusDispatch;
import base.Location;

public class CityBus extends CityComponent {

	private TransportationBusDispatch mBusDispatch;

	// CHASE: get list from main GUI
	ArrayList<Location> mStopCoords = new ArrayList<Location>();

	private int mBusNumber;
	private int mStopNumber,
				mSize = 40,
				mXDest, mYDest;
	private boolean mTraveling;


	public CityBus(TransportationBusDispatch b, int busNum) {
		mStopCoords.add(new Location(0, 0));
		mStopCoords.add(new Location(500, 0));
		mStopCoords.add(new Location(500, 500));
		mStopCoords.add(new Location(0, 500));

		mBusDispatch = b;
		mBusNumber = busNum;
		mTraveling = true;
		mStopNumber = 0;

		// Inherited from CityComponent
		x = mStopCoords.get(mStopNumber).mX;
		y = mStopCoords.get(mStopNumber).mY;


		rectangle = new Rectangle(0,0,mSize,mSize);
		setX(x); setY(y);


		isActive = true;
		color = Color.yellow;

		// Set initial destination
		mXDest = mStopCoords.get(mStopNumber + 1).mX;
		mYDest = mStopCoords.get(mStopNumber + 1).mY;
	}


	public void updatePosition() {
		if (x < mXDest)			x++;
        else if (x > mXDest)	x--;

        if (y < mYDest)			y++;
        else if (y > mYDest)	y--;

        if (x == mXDest && y == mYDest && mTraveling) {
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
	}

	@Override
	public boolean isPresent() { return true; }

	@Override
	public void setPresent(boolean state) { }


	public void DoAdvanceToNextStop() {
        mStopNumber = (mStopNumber + 1) % mStopCoords.size();
        mTraveling = true;

        mXDest = mStopCoords.get(mStopNumber).mX;
        mYDest = mStopCoords.get(mStopNumber).mY;
	}
}
