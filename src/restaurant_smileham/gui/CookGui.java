package restaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.Semaphore;

import restaurant.WaitingArea;
import restaurant.agents.CookAgent;
import restaurant.agents.HostAgent;

public class CookGui implements Gui {

    private CookAgent mCookAgent;
    public static final int cCOOK_LENGTH = 20;
    
    //PLATING
	public static final Color cPLATINGAREA_COLOR = Color.lightGray;
    public static final int cPLATINGAREA_X = 100;
    public static final int cPLATINGAREA_Y = 250;
    public static final int cPLATINGAREA_WIDTH = 100;
    public static final int cPLATINGAREA_HEIGHT = 20;
    private int atPlatingX(){return cPLATINGAREA_X;}
    private int atPlatingY(){return cPLATINGAREA_Y + cPLATINGAREA_HEIGHT;}

    //COOKING
	public static final Color cCOOKINGAREA_COLOR = Color.black;
    public static final int cCOOKINGAREA_X = 100;
    public static final int cCOOKINGAREA_Y = 350;
    public static final int cCOOKINGAREA_WIDTH = 100;
    public static final int cCOOKINGAREA_HEIGHT = 20;
    private int atCookingX(){return cCOOKINGAREA_X;}
    private int atCookingY(){return cCOOKINGAREA_Y - cCOOKINGAREA_HEIGHT;}
    
    //FRIDGE
    public static final Color cFRIDGE_COLOR = Color.red;
    public static final int cFRIDGE_X = 180;
    public static final int cFRIDGE_Y = 300;
    public static final int cFRIDGE_WIDTH = 20;
    public static final int cFRIDGE_HEIGHT = 20;
    private int atFridgeX(){return cFRIDGE_X - cCOOK_LENGTH;}
    private int atFridgeY(){return cFRIDGE_Y;}
    
    //LABELS (Label is in CookAgent)
    public static final int cLABEL_COOKING_X = 200;
    public static final int cLABEL_COOKING_Y = 365;
    public static final int cLABEL_PLATING_X = 200;
    public static final int cLABEL_PLATING_Y = 265;
    
    //MOVEMENT
    private int mPosX = 100;
    private int mPosY = 300;
    private int mDestinationX = mPosX;
    private int mDestinationY = mPosY;
    private static final int cSPEED = HostAgent.cSPEED_LIMIT;
    
	public Semaphore semAtDestination = new Semaphore(0);


    //CONSTRUCTOR
    public CookGui(CookAgent cook) {
    	mCookAgent = cook;
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
        //if the cook reaches the destination
        if (mPosX == mDestinationX && mPosY == mDestinationY) {
            if (semAtDestination.availablePermits() == 0) semAtDestination.release();
        	//if at fridge
        	if (mDestinationX == atFridgeX() && mDestinationY == atFridgeY()){
        		 mCookAgent.msgAnimationAtFridge();
        	}
        	
        	//if at plating
        	else if (mDestinationX == atPlatingX() && mDestinationY == atPlatingY()){
       		 mCookAgent.msgAnimationAtPlating();
        	}
        	
        	//if at cooking
        	else if (mDestinationX == atCookingX() && mDestinationY == atCookingY()){
       		 mCookAgent.msgAnimationAtCooking();
        	}
        }
    }

	private void acquireSemaphore(Semaphore semaphore){
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillRect(mPosX, mPosY, cCOOK_LENGTH, cCOOK_LENGTH);
    }

    public boolean isPresent() {
        return true;
    }

//    public int getXPos() {
//        return mPosX;
//    }
//
//    public int getYPos() {
//        return mPosY;
//    }
    
    public void DoGoToFridge(){
    	acquireSemaphore(semAtDestination);
    	mDestinationX = atFridgeX();
        mDestinationY = atFridgeY();
    }
    
    public void DoGoToPlating(){
    	acquireSemaphore(semAtDestination);
    	mDestinationX = atPlatingX();
        mDestinationY = atPlatingY();
    }
    
    public void DoGoToCooking(){
    	acquireSemaphore(semAtDestination);
    	mDestinationX = atCookingX();
        mDestinationY = atCookingY();
    }
    
    public static int getWaiterPlatingX(){
    	return cPLATINGAREA_X;
    }
    
    public static int getWaiterPlatingY(){
    	return cPLATINGAREA_Y - WaiterGui.cWAITER_LENGTH;
    }
}
