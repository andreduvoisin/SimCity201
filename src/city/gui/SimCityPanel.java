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

import base.Location;

@SuppressWarnings("serial")
public abstract class SimCityPanel extends JPanel implements ActionListener, MouseListener {

	static SimCityPanel instance;
	
	protected SimCityGui city;
	protected static List<CityComponent> statics;
	public List<CityComponent> movings;
	public List<CityIntersection> intersections;
	public List<Location> crashes;
	public enum EnumCrashType { NONE, PERSON_VEHICLE, VEHICLE_VEHICLE };
	public EnumCrashType mCrashScenario = EnumCrashType.NONE;
	
	protected Color background;
	protected Timer timer;
	private BufferedImage backgroundImage;
	private BufferedImage bloodImage;
	private BufferedImage fireImage;
	
	public SimCityPanel(SimCityGui city) {
		this.city = city;
		instance = this;
		statics = Collections.synchronizedList(new ArrayList<CityComponent>());
		movings = Collections.synchronizedList(new ArrayList<CityComponent>());
		intersections = Collections.synchronizedList(new ArrayList<CityIntersection>());
		crashes = Collections.synchronizedList(new ArrayList<Location>());
		timer = new Timer(10, this);
		timer.start();
		
		backgroundImage = null;
		try {
			java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/citypanel-bg.png");
			backgroundImage = ImageIO.read(imageURL);
			java.net.URL imageURL2 = this.getClass().getClassLoader().getResource("city/gui/images/Blood_Splatter.png");
			bloodImage = ImageIO.read(imageURL2);		
			java.net.URL imageURL3 = this.getClass().getClassLoader().getResource("city/gui/images/Blood_Splatter.png");
			fireImage = ImageIO.read(imageURL3);		
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		drawComponents(g);
		moveComponents();
	}
	
	
	public void drawComponents(Graphics g) {
		if(!SimCityGui.GRADINGVIEW)
			g.drawImage(backgroundImage, 0, 0, null);
		
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
		
		if (mCrashScenario != EnumCrashType.NONE) {
			for (Location crash : crashes) {
				g.drawImage(bloodImage, crash.mX, crash.mY, null);
			}
			synchronized (movings) {
				for (CityComponent c : movings) {
					for (CityComponent x : movings) {
						if (mCrashScenario == EnumCrashType.PERSON_VEHICLE) {
							if (!(c instanceof CityBus)
									&& !(x instanceof CityBus)
									&& c.collidesWith(x)) {
								if (c.isActive && x.isActive) {
									crashes.add(new Location(c.x, c.y));
									c.disable();
									x.disable();
								}
							}
						} else {
							if (c.collidesWith(x)) {
								if (c.isActive && x.isActive) {
									crashes.add(new Location(c.x, c.y));
									c.disable();
									x.disable();
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void moveComponents() {
		
		synchronized(movings) {
			for (CityComponent c:movings) {
				if (c.isActive()) c.updatePosition();
			}
		}
		synchronized (intersections) {
			for (CityIntersection ci : intersections) {
				ci.setOccupant(null);
				synchronized (movings) {
					for (CityComponent cc : movings) {
						if (ci.isActive() && cc.isActive) {
							if (ci.collidesWith(cc)) {
								ci.setOccupant(cc);
							}
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
