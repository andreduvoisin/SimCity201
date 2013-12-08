package market.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import market.Market;
import base.Item.EnumItemType;
import city.gui.CityCard;
import city.gui.SimCityGui;

@SuppressWarnings("serial")
public class MarketPanel extends CityCard implements ActionListener {
	private static final int WINDOWX = 500, WINDOWY = 500;
		
	private Market mMarket;
	
//	public MarketCashierRole mCashier;
//	public MarketDeliveryTruckRole mDeliveryTruck;
//	public MarketCashierGui mCashierGui;
	
	private Timer timer;
	private final int TIMERDELAY = 8;
	
	BufferedImage image;
	
	public MarketPanel(SimCityGui city, Market market) {
		super(city);
		setSize(WINDOWX, WINDOWY);
		mMarket = market;
		
		mMarket.mItemsGui = new MarketItemsGui();
		mMarket.mGuis.add(mMarket.mItemsGui);
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
		
		timer = new Timer(TIMERDELAY, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		synchronized(mMarket.mGuis) {
		for(MarketBaseGui gui : mMarket.mGuis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
		//ANGELICA:
		}
		repaint();
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(Color.white);
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

	//	g2.drawImage(image,0,0,null);
		
		synchronized(mMarket.mGuis) {
		for(MarketBaseGui gui : mMarket.mGuis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
		}
	}

	public void addGui(MarketBaseGui g) {
		synchronized(mMarket.mGuis) {
			mMarket.mGuis.add(g);
		}
	}
	
	public void removeGui(MarketBaseGui g) {
		synchronized(mMarket.mGuis) {
			mMarket.mGuis.remove(g);
		}
	}
	
	public void setInventory(EnumItemType i, int n) {
		mMarket.mCashier.setInventory(i,n);
		mMarket.mItemsGui.setInventory(i,n);
	}
}
