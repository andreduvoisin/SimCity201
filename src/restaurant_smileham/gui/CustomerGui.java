package restaurant.gui;

import restaurant.Table;
import restaurant.WaitingArea;
import restaurant.agents.CustomerAgent;
import restaurant.agents.HostAgent;

import java.awt.*;

public class CustomerGui implements Gui{

	private CustomerAgent mCustomer = null;

	//private HostAgent host;
	RestaurantGui mGUI;

	public static final int cINITIAL_X_POSITION = -40;
	public static final int cINITIAL_Y_POSITION = -40;
	public static final int cINITIAL_X_DESTINATION = -40;
	public static final int cINITIAL_Y_DESTINATION = -40;
	public static final int cCUSTOMER_LENGTH = 20;
	
	private static final int cSPEED = HostAgent.cSPEED_LIMIT;
	
	private int mPosX, mPosY;
	private int mDestinationX, mDestinationY;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command mCommand=Command.noCommand;

	

	public CustomerGui(CustomerAgent c, RestaurantGui gui){
		mGUI = gui;
		mCustomer = c;
		mPosX = cINITIAL_X_POSITION;
		mPosY = cINITIAL_Y_POSITION;
		mDestinationX = cINITIAL_X_DESTINATION;
		mDestinationY = cINITIAL_Y_DESTINATION;
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

		if (mPosX == mDestinationX && mPosY == mDestinationY) {
			//if at pickup area
        	if (mDestinationX == WaitingArea.getCustomerPickupX() && mDestinationY == WaitingArea.getCustomerPickupY()){
        		mCustomer.msgAnimationAtPickupArea();
        	}
			
			if (mCommand==Command.GoToSeat) mCustomer.msgAnimationFinishedGoToSeat();
			else if (mCommand==Command.LeaveRestaurant) {
				mCustomer.msgAnimationFinishedLeaveRestaurant();
//				mGUI.setCustomerEnabled(mAgent);
			}
			mCommand=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(mPosX, mPosY, cCUSTOMER_LENGTH, cCUSTOMER_LENGTH);
	}

	public boolean isPresent() {
		return true;
	}

	public void DoGoToTable(int tableNum) {
		mDestinationX = Table.getX(tableNum);
		mDestinationY = Table.getY(tableNum);
		mCommand = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		mDestinationX = cINITIAL_X_DESTINATION;
		mDestinationY = cINITIAL_Y_DESTINATION;
		mCommand = Command.LeaveRestaurant;
	}
	
	public void DoGoToWaitingArea(){
		mDestinationX = WaitingArea.getX();
		mDestinationY = WaitingArea.getY();
	}
	
	public void DoGoToPickupArea(){
		mDestinationX = WaitingArea.getCustomerPickupX();
		mDestinationY = WaitingArea.getCustomerPickupY();
	}
	
	
	public int getX() {
		return mPosX;
	}

	public int getY() {
		return mPosY;
	}
}
