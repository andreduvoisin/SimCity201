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
			java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/citypanel-house.png");
			image = ImageIO.read(imageURL);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePosition() {

	}
	
	public void paint(Graphics g) {
		if(!mPanel.guis.isEmpty())
			color = Color.PINK;
		else
			color = Color.BLUE;
		if(SimCityGui.GRADINGVIEW) {
			g.setColor(color);
			g.fillRect(x, y, 20, 20);
			g.setColor(Color.WHITE);
			
			String prefixNum = "";
			switch(mHouseNum) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
					prefixNum += "0";
					break;
			}
			
			g.drawString(prefixNum + mHouseNum, x + 5, y + 15);
		} else {
			g.drawImage(image, x, y, null);
		}
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
