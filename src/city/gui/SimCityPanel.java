package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public abstract class SimCityPanel extends JPanel implements ActionListener, MouseListener {

	static SimCityPanel instance;
	
	protected SimCityGui city;
	protected List<CityComponent> statics;
	public List<CityComponent> movings;
	public List<CityIntersection> intersections;
	
	protected Color background;
	protected Timer timer;
	private BufferedImage backgroundImage;

	
	public SimCityPanel(SimCityGui city) {
		this.city = city;
		instance = this;
		statics = Collections.synchronizedList(new ArrayList<CityComponent>());
		movings = Collections.synchronizedList(new ArrayList<CityComponent>());
		intersections = Collections.synchronizedList(new ArrayList<CityIntersection>());
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
		if(!SimCityGui.GRADINGVIEW)
			g.drawImage(backgroundImage, -15, -15, null);
		
		synchronized(statics) {
			for (CityComponent c:statics) {
				c.paint(g);
			}
		}
		
		synchronized(intersections) {
			for (CityIntersection c:intersections) {
				c.paint(g);
			}
		}
		
		synchronized(movings) {
			for (CityComponent c:movings) {
				c.paint(g);
			}
		}
		
	}
	
	public void moveComponents() {
		synchronized(intersections) {
			for (CityIntersection c:intersections) {
				c.updatePosition();
			}
		}
		synchronized(movings) {
			for (CityComponent c:movings) {
				c.updatePosition();
				synchronized(intersections) {
					for (CityIntersection ci:intersections) {
						if (c.collidesWith(ci)) {
							ci.setOccupied(true);
						}
					}
				}
			}
		}
	}
	/*
	public void addGui(WPersonGui gui) {
		guis.add(gui);
	}
	*/
	public void addStatic(CityComponent c) {
		synchronized(statics) {
			statics.add(c);
		}
	}
	
	public void addMoving(CityComponent c) {
		synchronized(movings) {
			movings.add(c);
		}
	}
	
	public void addIntersection(CityIntersection c) {
		synchronized(intersections) {
			intersections.add(c);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		this.repaint();
	}

	public static SimCityPanel getInstance() {
		return instance;
	}

}
