package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

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
	}
	
	public void paint(Graphics g){
		g.setColor(color);
		g.fillRect(x, y, 15, 10);
		g.fill3DRect(x, y, 15, 10, true);
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

}
