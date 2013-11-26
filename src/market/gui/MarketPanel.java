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
	public enum EnumMarketType {FOOD, CAR};
	
	private List<MarketBaseGui> guis = new ArrayList<MarketBaseGui>();
	private List<MarketWorkerGui> mWorkerGuis = new ArrayList<MarketWorkerGui>();
	private List<MarketCustomerGui> mCustomerGuis = new ArrayList<MarketCustomerGui>();
	private EnumMarketType mMarketType;
	
	private MarketItemsGui mItemGui;
	private Timer timer;
	private final int TIMERDELAY = 8;
	
	public MarketPanel(SimCityGui city, EnumMarketType t) {
		super(city);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		setBackground(Color.MAGENTA);
		
		mMarketType = t;
		mItemGui = new MarketItemsGui(mMarketType);
		
		timer = new Timer(TIMERDELAY, this);
		timer.start();
//		addGuis();
//		testGuis();
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
	
		for(MarketBaseGui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}
	
	private void addGuis() {
		guis.add(mItemGui);
		PersonAgent p = new PersonAgent();
		MarketCashierRole r = new MarketCashierRole(p,mMarketType);
		guis.add(new MarketCashierGui(r));
		p = new PersonAgent();
		MarketCustomerRole r2 = new MarketCustomerRole(p);
		guis.add(new MarketCustomerGui(r2));
		p = new PersonAgent();
		MarketWorkerRole r3 = new MarketWorkerRole(p);
		guis.add(new MarketWorkerGui(r3));
	}
	
	public void testGuis() {
		MarketCustomerGui c = (MarketCustomerGui)guis.get(2);
		c.DoWaitForOrder();
	}
	
	public void addGui(MarketBaseGui g) {
		guis.add(g);
		if(g instanceof MarketWorkerGui) {
			mWorkerGuis.add((MarketWorkerGui)g);
			((MarketWorkerGui) g).setItemsGui(mItemGui);
		}
		else if (g instanceof MarketCustomerGui) {
			mCustomerGuis.add((MarketCustomerGui)g);
		}
	}
	
	public void removeGui(MarketBaseGui g) {
		guis.remove(g);
		if(g instanceof MarketWorkerGui) {
			mWorkerGuis.remove((MarketWorkerGui)g);
		}
		else if (g instanceof MarketCustomerGui) {
			mCustomerGuis.remove((MarketCustomerGui)g);
		}
	}
}
