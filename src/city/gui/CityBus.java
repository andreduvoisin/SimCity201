package city.gui;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import transportation.TransportationBusDispatch;
import base.Location;

import transportation.TransportationBusDispatch;


public class CityBus extends CityComponent {
	
	private int mBusNumber;
	private int mStopNumber, xPos, yPos, xDest, yDest;
	private boolean mTraveling;
	
	//CHASE: create array of bus stop coordinates
	ArrayList<Dimension> mStopCoords = new ArrayList<Dimension>();
	
	public CityBus(int x, int y) {
		super(x, y, Color.yellow, "Bus 1");
		rectangle = new Rectangle(x,y,15,10);
	private TransportationBusDispatch mBusDispatch;

	// CHASE: get list from main GUI
	ArrayList<Location> mStopCoords = new ArrayList<Location>();

	private int mBusNumber;
	private int mStopNumber,
				mSize = 25,
				mXDest, mYDest;
	private boolean mTraveling;


	/**
	 * Creates new CityBus
	 * @param b Bus "driver"
	 * @param busNum Index of this instance of bus
	 */
	public CityBus(TransportationBusDispatch b, int busNum) {
		// CHASE: get rid of these
		mStopCoords.add(new Location(60, 60));
		mStopCoords.add(new Location(60, 515));
		mStopCoords.add(new Location(515, 515));
		mStopCoords.add(new Location(515, 60));

		mBusDispatch = b;
		mBusNumber = busNum;
		mTraveling = true;
		mStopNumber = 0;
		
		// If using ArrayList<Dimension>
		xPos = mStopCoords.get(mStopNumber).width;
		yPos = mStopCoords.get(mStopNumber).height;
		xDest = mStopCoords.get(mStopNumber + 1).width;
		yDest = mStopCoords.get(mStopNumber + 1).height;
	}
	
	public CityBus(int x, int y, String ID) {
		super(x, y, Color.yellow, ID);
		rectangle = new Rectangle(x,y,15,10);mBusDispatch = b;
		
		mBusNumber = busNum;
		mTraveling = true;
		mStopNumber = 0;
		
		// If using ArrayList<Dimension>
		xPos = mStopCoords.get(mStopNumber).width;
		yPos = mStopCoords.get(mStopNumber).height;
		xDest = mStopCoords.get(mStopNumber + 1).width;
		yDest = mStopCoords.get(mStopNumber + 1).height;


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
		g.setColor(Color.white);
		g.drawString("Bus", x + mSize / 2, y + mSize);
	}

	private TransportationBusDispatch mBusDispatch;
/*
	public void TransportationBusGui(TransportationBusDispatch b, int busNum) {
		
	}
*/
    public void updatePosition() {
        if (xPos < xDest)		xPos++;
        else if (xPos > xDest)	xPos--;

        if (yPos < yDest)		yPos++;
        else if (yPos > yDest)	yPos--;

        if (xPos == xDest && yPos == yDest) {
			if (mTraveling) {
        		mBusDispatch.msgGuiArrivedAtStop(mBusNumber);
			}
        }
    }
	

	public void DoAdvanceToNextStop() {
		mStopNumber = (mStopNumber + 1) % mStopCoords.size();
		mTraveling = true;

		xDest = mStopCoords.get(mStopNumber).width;
		yDest = mStopCoords.get(mStopNumber).height;
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
