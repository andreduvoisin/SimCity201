package market.gui;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;

import base.PersonAgent;
import city.gui.CityCard;
import city.gui.SimCityGui;
import market.gui.*;
import market.roles.*;

import java.awt.*;
import java.awt.event.*;

public class MarketPanel extends CityCard implements ActionListener {
	private static final int WINDOWX = 500, WINDOWY = 500;
	public enum EnumMarketType {BOTH, CAR, FOOD};
	
	static MarketPanel instance;
	
	private List<MarketBaseGui> guis = Collections.synchronizedList(new ArrayList<MarketBaseGui>());
	private List<MarketWorkerGui> mWorkerGuis = new ArrayList<MarketWorkerGui>();
	private List<MarketCustomerGui> mCustomerGuis = new ArrayList<MarketCustomerGui>();
	private EnumMarketType mMarketType;
	
	private MarketItemsGui mItemGui;
	
	public MarketCashierRole mCashier;
	public MarketDeliveryTruckRole mDeliveryTruck = new MarketDeliveryTruckRole();
	public MarketCashierGui mCashierGui;
	
	private Timer timer;
	private final int TIMERDELAY = 8;
	
	public MarketPanel(SimCityGui city, EnumMarketType t) {
		super(city);
		setSize(WINDOWX, WINDOWY);
//		setVisible(true);
		setBackground(Color.MAGENTA);
		instance = this;
		
		mCashier = new MarketCashierRole(t);
		mCashierGui = new MarketCashierGui(mCashier);
		mCashier.setGui(mCashierGui);
		guis.add(mCashierGui);
		
		mMarketType = t;
		mItemGui = new MarketItemsGui(mMarketType);
		guis.add(mItemGui);
		
		timer = new Timer(TIMERDELAY, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		for(MarketBaseGui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
	
		repaint();
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
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
			System.out.println("added waiter gui!" + guis.size());

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
	
	/** Utilities */
	public static MarketPanel getInstance() {
		return instance;
	}
}
