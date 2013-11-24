package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class CityCard extends JPanel implements ActionListener, MouseListener {

	protected SimCityGui city;
	
	public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;
	
	public CityCard(SimCityGui city) {
		this.city = city;
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}
	
	public void mouseExited(MouseEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}
}
