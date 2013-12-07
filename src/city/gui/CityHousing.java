package city.gui;

import housing.gui.HousingGuiPanel;
import housing.interfaces.HousingRenter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CityHousing extends CityComponent {
	public int mHouseNum;
	public int xLocation, yLocation;
	public double mRent;
	public HousingRenter mOccupant;
	public enum HousingType{Apartment, House}; 
	public HousingType type; 
	public final double maxAptRent = 450.00;  
	public HousingGuiPanel mPanel;
	
	private BufferedImage image;
	
	public CityHousing(SimCityGui city, int x, int y, int ID, double rent) {
		super(x, y, Color.blue, "House "+ID);
		mHouseNum = ID;
		xLocation = x;
		yLocation = y;
		mOccupant = null;
		mRent = rent;
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
		
		if(rent <= maxAptRent){
			type = HousingType.Apartment; 
		}
		else{
			type = HousingType.House; 
		}	
	}

	//For unit testing
	public CityHousing(int x, int y, int ID, double rent) {
		xLocation = x;
		yLocation = y;
		mRent = rent;
		mOccupant = null;
		mHouseNum = ID;
		if(rent <= maxAptRent){
			type = HousingType.Apartment; 
		}
		else{
			type = HousingType.House; 
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
