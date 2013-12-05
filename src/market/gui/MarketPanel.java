package market.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import market.roles.MarketCashierRole;
import market.roles.MarketDeliveryTruckRole;
import base.Item.EnumItemType;
import base.reference.Market;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings("serial")
public class MarketPanel extends CityCard implements ActionListener {
	private static final int WINDOWX = 500, WINDOWY = 500;
		
	private static Market mMarket;
	
	private MarketItemsGui mItemGui;
	
	public MarketCashierRole mCashier;
	public MarketDeliveryTruckRole mDeliveryTruck;
	public MarketCashierGui mCashierGui;
	
	private Timer timer;
	private final int TIMERDELAY = 8;
	
	BufferedImage image;
	
	public MarketPanel(SimCityGui city, Market market) {
		super(city);
		setSize(WINDOWX, WINDOWY);
		mMarket = market;
		
		mItemGui = new MarketItemsGui();
		
    /*	image = null;
    	try {
//    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("market/gui/images/background.png");
        	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant/restaurant_tranac/gui/images/restaurant.png");
   		image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
   */ 	
		guis.add(mItemGui);
		
		timer = new Timer(TIMERDELAY, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		synchronized(guis) {
		for(MarketBaseGui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
		}
		repaint();
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.white);
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

	//	g2.drawImage(image,0,0,null);
		
		synchronized(guis) {
		for(MarketBaseGui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
		}
	}

	public void addGui(MarketBaseGui g) {
		synchronized(guis) {
			guis.add(g);
		}
		if(g instanceof MarketWorkerGui) {
			mWorkerGuis.add((MarketWorkerGui)g);
			((MarketWorkerGui) g).setItemsGui(mItemGui);
		}
		else if (g instanceof MarketCustomerGui) {
			mCustomerGuis.add((MarketCustomerGui)g);
		}
	}
	
	public void removeGui(MarketBaseGui g) {
		synchronized(guis) {
			guis.remove(g);
		}
		if(g instanceof MarketWorkerGui) {
			mWorkerGuis.remove((MarketWorkerGui)g);
		}
		else if (g instanceof MarketCustomerGui) {
			mCustomerGuis.remove((MarketCustomerGui)g);
		}
	}
	
	public void setInventory(EnumItemType i, int n) {
		mCashier.setInventory(i,n);
		mItemGui.setInventory(i,n);
	}
}
