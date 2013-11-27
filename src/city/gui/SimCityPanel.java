package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public abstract class SimCityPanel extends JPanel implements ActionListener, MouseListener {

	static SimCityPanel instance;
	
	protected SimCityGui city;
	protected ArrayList<CityComponent> statics;
	public ArrayList<CityComponent> movings;
	protected Color background;
	protected Timer timer;
	private BufferedImage backgroundImage;

	
	public SimCityPanel(SimCityGui city) {
		this.city = city;
		instance = this;
		statics = new ArrayList<CityComponent>();
		movings = new ArrayList<CityComponent>();
		timer = new Timer(10, this);
		timer.start();
		
		backgroundImage = null;
		try {
			java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/hogwartstown.png");
			backgroundImage = ImageIO.read(imageURL);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		moveComponents();
		drawComponents(g);
		
	}
	
	
	public void drawComponents(Graphics g) {
		
		g.drawImage(backgroundImage, -15, -15,null);
		for (CityComponent c:statics) {
			c.paint(g);
			
		}
		
		for (CityComponent c:movings) {
			if(c.isActive){
				c.paint(g);
			}
		}
	}
	
	public void moveComponents() {
		for (CityComponent c:movings) {
			c.updatePosition();
		}

		
	}
	/*
	public void addGui(WPersonGui gui) {
		guis.add(gui);
	}*/
	
	public void addStatic(CityComponent c) {
		statics.add(c);
	}
	
	public void addMoving(CityComponent c) {
		movings.add(c);
	}
	
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}

	public static SimCityPanel getInstance() {
		return instance;
	}

}
