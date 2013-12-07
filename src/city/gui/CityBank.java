package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import base.Location;

public class CityBank extends CityComponent {
	private String bankName = "";
	private int BANKWIDTH = 80;
	private int BANKHEIGHT = 80; 
	
	BufferedImage image;

	public CityBank(int x, int y) {
		super(x, y, Color.green, "Unnamed Bank");
		rectangle = new Rectangle(x, y, BANKWIDTH, BANKHEIGHT);
		
		image = null;
		try {
			java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/hagridshut.png");
			image = ImageIO.read(imageURL);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public CityBank(int x, int y, String ID) {
		super(x, y, Color.green, ID);
		rectangle = new Rectangle(x, y, BANKWIDTH, BANKHEIGHT);
		bankName = ID;
		
		image = null;
		try {
			java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/hagridshut.png");
			image = ImageIO.read(imageURL);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public CityBank(Location location, String ID) {
		super(location.mX, location.mY, Color.green, ID);
		rectangle = new Rectangle(x, y, BANKWIDTH, BANKHEIGHT);
		bankName = ID;
		
		image = null;
		try {
			java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/hagridshut.png");
			image = ImageIO.read(imageURL);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void updatePosition() {

	}

	public void paint(Graphics g) {
		if(SimCityGui.GRADINGVIEW) {
			g.setColor(color);
			g.fill3DRect(x, y, BANKWIDTH, BANKHEIGHT, true);
			g.setColor(Color.WHITE);
			g.drawString(bankName,x + 10 , y + 50);
		} else {
			// We don't use the hut anymore!
			//g.drawImage(image, x, y, null);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}
}
