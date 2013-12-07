package city.gui;

import housing.gui.HousingGuiPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CityHousing extends CityComponent {
	
	public int xLocation, yLocation;
	public HousingGuiPanel mPanel;
	public int mHouseNum;
	private BufferedImage image;
	
	public CityHousing(SimCityGui city, int x, int y, int ID) {
		super(x, y, Color.blue, "House "+ID);
		mHouseNum = ID;
		xLocation = x;
		yLocation = y;
		rectangle = new Rectangle(x, y, 20, 20);
		mPanel = new HousingGuiPanel(city);
		
		image = null;
		try {
			java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/house.png");
			image = ImageIO.read(imageURL);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePosition() {

	}
	
	public void paint(Graphics g) {
		//g.drawImage(image, x, y, null);
		
		g.setColor(color);
		g.fillRect(x, y, 20, 20);
		g.setColor(Color.WHITE);
		g.drawString("H"+mHouseNum,x + 7 , y + 17);
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		return true;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}	

}
