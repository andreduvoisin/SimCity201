package restaurant.restaurant_tranac.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import restaurant.restaurant_tranac.roles.TranacRestaurantCashierRole;
import restaurant.restaurant_tranac.roles.TranacRestaurantCookRole;
import restaurant.restaurant_tranac.roles.TranacRestaurantCustomerRole;
import restaurant.restaurant_tranac.roles.TranacRestaurantHostRole;
import restaurant.restaurant_tranac.roles.TranacRestaurantWaiterRole;
import base.BaseRole;
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings("serial")
public class TranacAnimationPanel extends CityCard implements ActionListener {
	public static TranacAnimationPanel instance;

	private final int WINDOWX = 626;
	private final int WINDOWY = 507;
    private BufferedImage background;
    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    private static TranacRestaurantCashierRole mCashier;
    private static TranacRestaurantCookRole mCook;
    private static TranacRestaurantHostRole mHost;
      
    private static Vector<TranacRestaurantWaiterRole> mWaiters = new Vector<TranacRestaurantWaiterRole>();
    private static Vector<TranacRestaurantCustomerRole> mCustomers = new Vector<TranacRestaurantCustomerRole>();

    public TranacAnimationPanel(SimCityGui city) {
    	super(city);
    	setBounds(0,0,WINDOWX, WINDOWY);
    	setBackground(Color.white);
    	
    	TranacAnimationPanel.instance = this;
 
    	Timer timer = new Timer(Time.cSYSCLK/25, this );
    	timer.start();
    	
    	background = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/restaurant.png");
    	background = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }

	public void actionPerformed(ActionEvent e) {
		synchronized(guis) {
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
		}
		
		repaint();
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        if(background != null)
        	g2.drawImage(background,0,0,null);

        synchronized(guis) {
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
        }
    }

    public void addGui(Gui gui) {
    	synchronized(guis) {
        guis.add(gui);
    	}
    }
    
    public void addPerson(BaseRole role){
    	if (role instanceof TranacRestaurantCustomerRole){
    		TranacRestaurantCustomerRole customer = (TranacRestaurantCustomerRole) role;
    		mCustomers.add(customer);
    		customer.setHost(mHost);
    		customer.setCashier(mCashier);
    		customer.msgGotHungry();
    	}
    	else if (role instanceof TranacRestaurantWaiterRole){
    		TranacRestaurantWaiterRole waiter = (TranacRestaurantWaiterRole) role;
    		mWaiters.add(waiter);
        	mHost.addWaiter(waiter);
    	}
    	else if (role instanceof TranacRestaurantCashierRole){
    		TranacRestaurantCashierRole cashier = (TranacRestaurantCashierRole) role;
    		//ANGELICA: 1 add necessary logic here
    	}
    }
    
    public static TranacAnimationPanel getInstance() {
    	return instance;
    }
    
    public static int getNumWaiters() {
    	return mWaiters.size();
    }
    
    public static TranacRestaurantCashierRole getCashier(){
    	return mCashier;
    }
    
    public static TranacRestaurantCookRole getCook(){
    	return mCook;
    }
    
    public static TranacRestaurantHostRole getHost(){
    	return mHost;
    }
    
}
