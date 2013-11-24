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
	MarketItemsGui mItemGui = new MarketItemsGui();
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
		guis.add(new MarketDeliveryTruckGui(null));
		guis.add(new MarketCustomerGui(null));
		guis.add(new MarketWorkerGui(null));
	}
	
	public void testGuis() {
		MarketWorkerGui m = (MarketWorkerGui)guis.get(4);
		m.setItemsGui((MarketItemsGui)guis.get(0));
	}
	
	public void addGui(MarketBaseGui g) {
		guis.add(g);
		if(g instanceof MarketWorkerGui)
			((MarketWorkerGui) g).setItemsGui(mItemGui);
	}
}
