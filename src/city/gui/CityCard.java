package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CityCard extends JPanel implements ActionListener, MouseListener {

	protected SimCityGui city;
	protected Color background;
	public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;
	
	public CityCard(SimCityGui city) {
		this.city = city;
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
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
