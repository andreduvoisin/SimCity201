package restaurant_smileham.gui;


import restaurant_smileham.Table;
import restaurant_smileham.agents.CustomerAgent;
import restaurant_smileham.agents.HostAgent;

import java.awt.*;

public class HostGui implements Gui {

    private HostAgent mHostAgent;
    private static final int cPOS_DEFAULT_X = -20;
    private static final int cPOS_DEFAULT_Y = -20;
    private static final int cHOST_LENGTH = 20;
    
    private int mPosX = cPOS_DEFAULT_X;
    private int mPosY = cPOS_DEFAULT_Y;
    private int mDestinationX = cPOS_DEFAULT_X; 
    private int mDestinationY = cPOS_DEFAULT_Y;

    public HostGui(HostAgent agent) {
        mHostAgent = agent;
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

    public void DoBringToTable(CustomerAgent customer, int tableNum) {
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
}
