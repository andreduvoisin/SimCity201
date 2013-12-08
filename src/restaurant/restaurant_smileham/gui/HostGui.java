package restaurant.restaurant_smileham.gui;


import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;
import base.Gui;

public class HostGui implements Gui {

    private SmilehamHostRole mHostAgent;
    private static final int cPOS_DEFAULT_X = -20;
    private static final int cPOS_DEFAULT_Y = -20;
    private static final int cHOST_LENGTH = 20;
    
    private int mPosX = cPOS_DEFAULT_X;
    private int mPosY = cPOS_DEFAULT_Y;
    private int mDestinationX = cPOS_DEFAULT_X; 
    private int mDestinationY = cPOS_DEFAULT_Y;

    public HostGui(SmilehamHostRole hostAgent) {
        mHostAgent = hostAgent;
    }

    public void updatePosition() {
//        if (mPosX < mDestinationX)
//            mPosX++;
//        else if (mPosX > mDestinationX)
//            mPosX--;
//
//        if (mPosY < mDestinationY)
//            mPosY++;
//        else if (mPosY > mDestinationY)
//            mPosY--;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(mPosX, mPosY, cHOST_LENGTH, cHOST_LENGTH);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(SmilehamCustomerRole customer, int tableNum) {
        mDestinationX = Table.getX(tableNum) + cHOST_LENGTH;
        mDestinationY = Table.getY(tableNum) - cHOST_LENGTH;
    }

    public void DoLeaveCustomer() {
        mDestinationX = cPOS_DEFAULT_X;
        mDestinationY = cPOS_DEFAULT_Y;
    }

    public int getXPos() {
        return mPosX;
    }

    public int getYPos() {
        return mPosY;
    }

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}
}
