/*	When implementing this into the gui:
	This is a rough idea of what the BusGui needs to do
	Move it into the correct package (make sure to change the import of Bus below)
	Feel free to refactor it as desired
*/

package transportation;

import java.awt.Dimension;
import java.util.ArrayList;

public class TransportationBusGui {
	
	private int mBusNumber;
	private int mStopNumber, xPos, yPos, xDest, yDest;
	private boolean mTraveling;
	
	//CHASE: create array of bus stop coordinates
	ArrayList<Dimension> mStopCoords = new ArrayList<Dimension>();

	private TransportationBusDispatch mBusDispatch;

	public TransportationBusGui(TransportationBusDispatch b, int busNum) {
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
