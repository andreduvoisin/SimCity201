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
	private final int TIMERDELAY = 8;
	
	public MarketPanel() {
		setSize(600,400);
		setVisible(true);
		setBackground(Color.BLACK);
		
		testGuis();
		
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
	
	private void testGuis() {
		guis.add(new MarketItemsGui(null));
		guis.add(new MarketCashierGui(null));
		guis.add(new MarketDeliveryTruckGui(null));
		guis.add(new MarketCustomerGui(null));
		guis.add(new MarketWorkerGui(null));
	}
}
