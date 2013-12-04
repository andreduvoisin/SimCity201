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
import base.Time;
import city.gui.CityCard;
import city.gui.SimCityGui;

public class TranacRestaurantPanel extends CityCard implements ActionListener {
	static TranacRestaurantPanel instance;
	
	private final int WINDOWX = 626;
	private final int WINDOWY = 507;
    private BufferedImage background;

    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    public TranacRestaurantCashierRole mCashier = new TranacRestaurantCashierRole();
    public TranacRestaurantCookRole mCook = new TranacRestaurantCookRole();
    public TranacRestaurantHostRole mHost = new TranacRestaurantHostRole();
    
    private TranacHostGui hostGui = new TranacHostGui(mHost);
    private TranacCookGui cookGui = new TranacCookGui(mCook);
    private TranacCashierGui cashierGui = new TranacCashierGui(mCashier);
      
    private Vector<TranacRestaurantWaiterRole> mWaiters = new Vector<TranacRestaurantWaiterRole>();
    private Vector<TranacRestaurantCustomerRole> mCustomers = new Vector<TranacRestaurantCustomerRole>();

    public TranacRestaurantPanel(SimCityGui city) {
    	super(city);
    	setBounds(0,0,WINDOWX, WINDOWY);
    	setBackground(Color.white);
    	
    	this.instance = this;
    	
    	mCashier.setGui(cashierGui);
    	mCook.setGui(cookGui);
    	mHost.setGui(hostGui);
    	
    	guis.add(hostGui);
    	guis.add(cookGui);
    	guis.add(cashierGui);
 
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
    
    public void addCustomer(TranacRestaurantCustomerRole c) {
    	mCustomers.add(c);
    	c.setHost(mHost);
    	c.setCashier(mCashier);

    	c.msgGotHungry();
    }
    
    public void addWaiter(TranacRestaurantWaiterRole w) {
    	mWaiters.add(w);
    	mHost.addWaiter(w);
    }
    
    public static TranacRestaurantPanel getInstance() {
    	return instance;
    }
    
    public int getWaiters() {
    	return mWaiters.size();
    }
}
