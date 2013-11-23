package market.gui;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;

import restaurant.restaurant_tranac.gui.Gui;

import java.awt.*;
import java.awt.event.*;

public class MarketPanel extends JPanel implements ActionListener {
	private List<Gui> guis = new ArrayList<Gui>();
	private final int TIMERDELAY = 8;
	
	public MarketPanel() {
		setSize(600,400);
		setVisible(true);
		setBackground(Color.BLACK);
		
		Timer timer = new Timer(TIMERDELAY, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
		
		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}
}
