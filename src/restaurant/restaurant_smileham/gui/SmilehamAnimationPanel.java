package restaurant.restaurant_smileham.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Timer;

import restaurant.restaurant_smileham.Table;
import restaurant.restaurant_smileham.WaitingArea;
import restaurant.restaurant_smileham.interfaces.SmilehamCashier;
import restaurant.restaurant_smileham.interfaces.SmilehamCook;
import restaurant.restaurant_smileham.interfaces.SmilehamHost;
import restaurant.restaurant_smileham.interfaces.SmilehamWaiter;
import restaurant.restaurant_smileham.roles.SmilehamCashierRole;
import restaurant.restaurant_smileham.roles.SmilehamCookRole;
import restaurant.restaurant_smileham.roles.SmilehamCustomerRole;
import restaurant.restaurant_smileham.roles.SmilehamHostRole;
import restaurant.restaurant_smileham.roles.SmilehamWaiterRole;
import base.BaseRole;
import city.gui.CityCard;
import city.gui.SimCityGui;

public class SmilehamAnimationPanel extends CityCard implements ActionListener {
	private final int WINDOWX = 500;
    private final int WINDOWY = 500;
    private List<Gui> guis = new ArrayList<Gui>();
    
    //Data
    private static SmilehamHostRole mHost;
    private static SmilehamCookRole mCook;
    private static SmilehamCashierRole mCashier;
    private static Vector<SmilehamCustomerRole> mCustomers;
    
    public static SmilehamAnimationPanel mInstance;
    
    //CONSTRUCTOR
    public SmilehamAnimationPanel(SimCityGui city) {
    	super(city);
    	mInstance = this;
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);

        mCustomers = new Vector<SmilehamCustomerRole>();
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    }
    
    public static void addPerson(BaseRole role) {
    	if (role instanceof SmilehamCustomerRole){
    		SmilehamCustomerRole customer = (SmilehamCustomerRole) role;
    		mCustomers.add(customer);
    		customer.msgGotHungry();
    	}
    	else if (role instanceof SmilehamWaiterRole){
    		SmilehamWaiterRole waiter = (SmilehamWaiterRole) role;
    		
    		SmilehamHost host = waiter.getHost();
            host.msgAddWaiter((SmilehamWaiter)waiter);
            
    	}
    	else if (role instanceof SmilehamHostRole){
    		mHost = (SmilehamHostRole) role;
    	}
    	else if (role instanceof SmilehamCookRole){
    		mCook = (SmilehamCookRole) role;
    	}
    	else if (role instanceof SmilehamCashierRole){
    		mCashier = (SmilehamCashierRole) role;
    	}
    }
    

	public void actionPerformed(ActionEvent e) {
		synchronized (guis) {
        	for(Gui gui : guis) {
                if (gui.isPresent()) {
                    gui.updatePosition();
                }
            }
		}
		repaint();  //Will have paintComponent called
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //draw the tables
        g2.setColor(Table.cTABLE_COLOR);
        for (int iTableNum = 0; iTableNum < SmilehamHostRole.cNUM_TABLES; iTableNum++){
        	g2.fillRect(Table.getX(iTableNum), Table.getY(iTableNum), Table.cTABLE_WIDTH, Table.cTABLE_HEIGHT);
        }
        
        //draw waiting area
        g2.setColor(WaitingArea.cWAITINGAREA_COLOR);
        g2.fillRect(WaitingArea.cWAITINGAREA_X, WaitingArea.cWAITINGAREA_Y, WaitingArea.cWAITINGAREA_WIDTH, WaitingArea.cWAITINGAREA_HEIGHT);

        //draw plating area
        g2.setColor(CookGui.cPLATINGAREA_COLOR);
        g2.fillRect(CookGui.cPLATINGAREA_X, CookGui.cPLATINGAREA_Y, CookGui.cPLATINGAREA_WIDTH, CookGui.cPLATINGAREA_HEIGHT);
        
        //draw cooking area
        g2.setColor(CookGui.cCOOKINGAREA_COLOR);
        g2.fillRect(CookGui.cCOOKINGAREA_X, CookGui.cCOOKINGAREA_Y, CookGui.cCOOKINGAREA_WIDTH, CookGui.cCOOKINGAREA_HEIGHT);
        
        //draw fridge
        g2.setColor(CookGui.cFRIDGE_COLOR);
        g2.fillRect(CookGui.cFRIDGE_X, CookGui.cFRIDGE_Y, CookGui.cFRIDGE_WIDTH, CookGui.cFRIDGE_HEIGHT);
        
        //animation
        synchronized (guis) {
            for(Gui gui : guis) {
                if (gui.isPresent()) {
                    gui.draw(g2);
                }
            }
		}
        
    }
    
	public void addGui(Gui gui) {
		guis.add(gui);
	}
	
	public void removeGui(Gui gui){
		guis.remove(gui);
	}
	
	public static SmilehamHost getHost(){
		return mHost;
	}
	
	public static SmilehamCashier getCashier(){
		return mCashier;
	}

	public static SmilehamCook getCook() {
		return mCook;
	}
}
