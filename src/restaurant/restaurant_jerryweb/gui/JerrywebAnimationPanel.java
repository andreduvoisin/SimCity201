package restaurant.restaurant_jerryweb.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import restaurant.restaurant_jerryweb.JerrywebRestaurant;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

public class JerrywebAnimationPanel extends CityCard implements ActionListener{
	
	static final int tableWidth = 50;
	static final int tableHeight = 50;
	static final int table1Xpos = 200;
	static final int table1Ypos = 250;
	
	static final int table2Xpos = 310;
	static final int table2Ypos = 185;

	static final int table3Xpos = 370;
	static final int table3Ypos = 100;
	
	static final int kitchenXpos = 225;
	static final int kitchenYpos = 50;
	static final int kitchenWidth = 125;
	static final int kitchenHeight = 20;
	
	static final int grillXpos = 225;
	static final int grillYpos = 5;
	static final int grillWidth = 125;
	static final int grillHeight = 20;
	
	static final int cashierXpos = 50;
	static final int cashierYpos = 200;
	static final int cashierWidth = 20;
	static final int cashierHeight = 20;
	
	static final int cookXpos = 275;
	static final int cookYpos = 26;
	static final int cookWidth = 20;
	static final int cookHeight = 20;
	
	static final int panelXpos = 0;
	static final int panelYpos = 0;
	
    private final int WINDOWX = 500;
    private final int WINDOWY = 500;
    private Image bufferImage;
    private Dimension bufferSize;
    /*
    private static JerrywebHostRole mHost;
    private static JerrywebCookRole mCook;
    private static JerrywebCashierRole mCashier;*/
    public static JerrywebRestaurant restaurant;
    //private static Vector<JerrywebCustomerRole> mCustomers;
    //public static JerrywebAnimationPanel mInstance;
    private List<Gui> guis = new ArrayList<Gui>();

    public JerrywebAnimationPanel(SimCityGui city, JerrywebRestaurant r) {
    	super(city);
    	setSize(WINDOWX, WINDOWY);
    	setVisible(true);
    	this.restaurant = r; 
        //mInstance = this;
        //bufferSize = this.getSize();
        //mCustomers = new Vector<JerrywebCustomerRole>();
    	Timer timer = new Timer(Time.cSYSCLK/10, this );
    	timer.start();
    }
    /*
    public static void addPerson(BaseRole role){
    	if (role instanceof JerrywebCustomerRole){
    		JerrywebCustomerRole customer = (JerrywebCustomerRole) role;
    		mCustomers.add(customer);
    		customer.gotHungry();
    	}
    	else if (role instanceof JerrywebCustomerRole){
    		JerrywebWaiterRole waiter = (JerrywebWaiterRole) role;
    		JerrywebHostRole host = waiter.getHost();
    		host.addWaiter((JerrywebWaiterRole)waiter);
    	}
    	else if (role instanceof JerrywebHostRole){
    		mHost = (JerrywebHostRole) role;
    	}
    	else if (role instanceof JerrywebCookRole){
    		mCook = (JerrywebCookRole) role;
    	}
    	else if (role instanceof JerrywebCashierRole){
    		mCashier = (JerrywebCashierRole) role;
    	}
    }*/
	public void actionPerformed(ActionEvent e) {
		
			synchronized (restaurant.guis) {
	        	for(Gui gui : restaurant.guis) {
	                if (gui.isPresent()) {
	                    gui.updatePosition();
	                }
	            }
			}
			repaint();  //Will have paintComponent called
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(panelXpos, panelYpos, WINDOWX, WINDOWY ); //This centers the screen on the restaurant scene with the table located in it... if not located at 0,0 then 
        //part of the RestaurantPanel would show

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(table1Xpos, table1Ypos, tableWidth , tableHeight);//200 and 250 need to be table params(they give the location of the table in the panel.
        //50 and 50 give the dimensions of the table width and length respectively
        g2.setColor(Color.ORANGE);
        g2.fillRect(table2Xpos, table2Ypos, tableWidth , tableHeight);
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(table3Xpos, table3Ypos, tableWidth , tableHeight);
        //g2.drawString(arg0, arg1, arg2);
        
        g2.setColor(Color.CYAN);
        g2.fillRect(cashierXpos, cashierYpos, cashierWidth , cashierHeight);
        
        g2.setColor(Color.lightGray);
        g2.fillRect(kitchenXpos, kitchenYpos, kitchenWidth, kitchenHeight);
        
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(grillXpos, grillYpos, grillWidth, grillHeight);
        
        g2.setColor(Color.YELLOW);
        g2.fillRect(cookXpos, cookYpos, cookWidth, cookHeight);

        synchronized(restaurant.guis){
        	for(Gui gui : restaurant.guis) {
        		if (gui.isPresent()) {
        			gui.draw(g2);
        		}
        	}
        }
    }
    /*
    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }

	public void addGui(WaiterGui gui) {
		guis.add(gui);
		
	}
	
	public void removeGui(Gui gui){
		guis.remove(gui);
	}
	/*
	public static JerrywebHostRole getHost(){
		return mHost;
	}
	
	public static JerrywebCashierRole getCashier(){
		return mCashier;
	}
	
	public static JerrywebCookRole getCook(){
		return mCook;
	}*/
}
