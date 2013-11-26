package restaurant.restaurant_tranac.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import city.gui.CityCard;
import city.gui.SimCityGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

import restaurant.restaurant_tranac.roles.*;

public class RestaurantPanel_at extends CityCard implements ActionListener {
	static RestaurantPanel_at instance;
	
	private final int WINDOWX = 626;
	private final int WINDOWY = 507;
    private final int TIMERDELAY = 8;
    private BufferedImage background;

    private List<Gui> guis = new ArrayList<Gui>();
    
    public RestaurantCashierRole_at mCashier = new RestaurantCashierRole_at();
    public RestaurantCookRole_at mCook = new RestaurantCookRole_at();
    public RestaurantHostRole_at mHost = new RestaurantHostRole_at();
    
    private HostGui_at hostGui = new HostGui_at(mHost);
    private CookGui_at cookGui = new CookGui_at(mCook);
    private CashierGui_at cashierGui = new CashierGui_at(mCashier);
      
    private Vector<RestaurantWaiterRole_at> mWaiters = new Vector<RestaurantWaiterRole_at>();
    private Vector<RestaurantCustomerRole_at> mCustomers = new Vector<RestaurantCustomerRole_at>();

    public RestaurantPanel_at(SimCityGui city) {
    	super(city);
    	setBounds(0,0,WINDOWX, WINDOWY);
    	setBackground(Color.white);
    	
    	this.instance = this;
    	
    	guis.add(hostGui);
    	guis.add(cookGui);
    	guis.add(cashierGui);
 
    	Timer timer = new Timer(TIMERDELAY, this );
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
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        
		repaint();
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        if(background != null)
        	g2.drawImage(background,0,0,null);

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(Gui gui) {
        guis.add(gui);
    }
    
    public void addCustomer(RestaurantCustomerRole_at c) {
    	mCustomers.add(c);
    }
    
    public void addWaiter(RestaurantWaiterRole_at w) {
    	mWaiters.add(w);
    	mHost.addWaiter(w);
    }
    
    public static RestaurantPanel_at getInstance() {
    	return instance;
    }
    
    public int getWaiters() {
    	return mWaiters.size();
    }
}
