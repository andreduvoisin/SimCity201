package market.gui;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;

import market.gui.*;
import java.awt.*;
import java.awt.event.*;

public class MarketPanel extends JPanel implements ActionListener {
	private List<MarketBaseGui> guis = new ArrayList<MarketBaseGui>();
	private List<MarketWorkerGui> mWorkerGuis = new ArrayList<MarketWorkerGui>();
	private List<MarketCustomerGui> mCustomerGuis = new ArrayList<MarketCustomerGui>();
	private MarketItemsGui mItemGui = new MarketItemsGui();
	private final int TIMERDELAY = 8;
	
	public MarketPanel() {
		setSize(500,500);
		setVisible(true);
		setBackground(Color.LIGHT_GRAY);
		
		addGuis();
		
		Timer timer = new Timer(TIMERDELAY, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		for(MarketBaseGui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
		
		for(MarketBaseGui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}
	
	private void addGuis() {
		guis.add(new MarketItemsGui());
		guis.add(new MarketCashierGui(null));
		guis.add(new MarketCustomerGui(null));
		guis.add(new MarketWorkerGui(null));
	}
	
	public void testGuis() {
//		MarketWorkerGui m = (MarketWorkerGui)guis.get(3);
//		m.setItemsGui((MarketItemsGui)guis.get(0));
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
