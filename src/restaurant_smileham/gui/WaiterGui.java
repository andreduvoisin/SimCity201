package restaurant_smileham.gui;


import java.awt.Color;
import java.awt.Graphics2D;

import restaurant_smileham.Table;
import restaurant_smileham.WaitingArea;
import restaurant_smileham.agents.HostAgent;
import restaurant_smileham.agents.WaiterAgent;

public class WaiterGui implements Gui {

    private WaiterAgent mWaiterAgent;
    private static final int cPOS_DOOR_X = -20;
    private static final int cPOS_DOOR_Y = -20;
    public static final int cWAITER_LENGTH = 20;
    public static final int cPOS_WAITERS_X = 400;
    public static final int cPOS_WAITERS_Y = 60;
    public static final int cWAITER_SPACING = 40;
    
    private static final int cSPEED = HostAgent.cSPEED_LIMIT;
    
    private int mPosX = cPOS_DOOR_X;
    private int mPosY = cPOS_DOOR_Y;
    private int mDestinationX = cPOS_DOOR_X; 
    private int mDestinationY = cPOS_DOOR_Y;
    
    private int mPosDefaultX;
    private int mPosDefaultY;

    private static int numWaiter = 0;
    
    public WaiterGui(WaiterAgent agent) {
        mWaiterAgent = agent;
        numWaiter++;
        mPosDefaultX = cPOS_WAITERS_X;
        mPosDefaultY = cPOS_WAITERS_Y + cWAITER_SPACING*numWaiter;
    }

    public void updatePosition() {
    	
    	//Move X and Y
	        if (mPosX < mDestinationX)
	            mPosX += cSPEED;
	        else if (mPosX > mDestinationX)
	            mPosX -= cSPEED;
	
	        if (mPosY < mDestinationY)
	            mPosY += cSPEED;
	        else if (mPosY > mDestinationY)
	            mPosY -= cSPEED;
        
	    //Call semaphores and messages
        //if the waiter reaches the destination
        if (mPosX == mDestinationX && mPosY == mDestinationY) {
        	//if at door
        	if (mDestinationX == cPOS_DOOR_X && mDestinationY == cPOS_DOOR_Y){
        		 //TODO Shane: 5 change these to messages
        	}
        	
        	//if at pickup area
        	else if (mDestinationX == WaitingArea.getWaiterPickupX() && mDestinationY == WaitingArea.getWaiterPickupY()){
        		mWaiterAgent.msgAnimationAtPickupArea();
        	}
        	
        	//if at cook
        	else if (mDestinationX == CookGui.getWaiterPlatingX() && mDestinationY == CookGui.getWaiterPlatingY()){
        		mWaiterAgent.msgAnimationAtCook();
        	}
        	
        	//if at table
        	else{
        		if (mWaiterAgent.semAtTable.availablePermits() == 0) mWaiterAgent.semAtTable.release();
        	}
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(mPosX, mPosY, cWAITER_LENGTH, cWAITER_LENGTH);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(int tableNum) {
    	mWaiterAgent.semAtTable.drainPermits();
        mDestinationX = Table.getX(tableNum) + cWAITER_LENGTH;
        mDestinationY = Table.getY(tableNum) - cWAITER_LENGTH;
    }

    public void DoGoHome() {
        mDestinationX = mPosDefaultX;
        mDestinationY = mPosDefaultY;
    }
    
    public void DoGoToCook(){
    	mDestinationX = CookGui.getWaiterPlatingX();
        mDestinationY = CookGui.getWaiterPlatingY();
    }
    
    public void DoGoToPickupArea(){
		mDestinationX = WaitingArea.getWaiterPickupX();
		mDestinationY = WaitingArea.getWaiterPickupY();
	}

    public int getXPos() {
        return mPosX;
    }

    public int getYPos() {
        return mPosY;
    }
}
