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

import restaurant.restaurant_tranac.roles.TranacCashierRole;
import restaurant.restaurant_tranac.roles.TranacCookRole;
import restaurant.restaurant_tranac.roles.TranacCustomerRole;
import restaurant.restaurant_tranac.roles.TranacHostRole;
import restaurant.restaurant_tranac.roles.TranacWaiterRole;
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
    
    private static TranacCashierRole mCashier;
    private static TranacCookRole mCook;
    private static TranacHostRole mHost;
      
    private static Vector<TranacWaiterRole> mWaiters = new Vector<TranacWaiterRole>();
    private static Vector<TranacCustomerRole> mCustomers = new Vector<TranacCustomerRole>();

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
    
    public static void addPerson(BaseRole role){
    	if (role instanceof TranacCustomerRole){
    		TranacCustomerRole customer = (TranacCustomerRole) role;
    		mCustomers.add(customer);
    		customer.setHost(mHost);
    		customer.setCashier(mCashier);
    		customer.msgGotHungry();
    	}
    	else if (role instanceof TranacWaiterRole){
    		TranacWaiterRole waiter = (TranacWaiterRole) role;
    		mWaiters.add(waiter);
        	mHost.addWaiter(waiter);
    	}
    	else if (role instanceof TranacCashierRole){
    		TranacCashierRole cashier = (TranacCashierRole) role;
    		//ANGELICA: 1 add necessary logic here
    	}
    }
    
    public static TranacAnimationPanel getInstance() {
    	return instance;
    }
    
    public static int getNumWaiters() {
    	return mWaiters.size();
    }
    
    public static TranacCashierRole getCashier(){
    	return mCashier;
    }
    
    public static TranacCookRole getCook(){
    	return mCook;
    }
    
    public static TranacHostRole getHost(){
    	return mHost;
    }
    
}
