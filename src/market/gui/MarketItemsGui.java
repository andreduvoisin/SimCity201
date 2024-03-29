package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import market.roles.MarketCashierRole;
import base.Item.EnumItemType;
import city.gui.SimCityGui;


/**
 * This class paints the market inventory on the screen.
 * It updates as the market inventory is updated.
 * 
 * @author Angelica Huyen Tran
 */
public class MarketItemsGui implements MarketBaseGui {
	MarketCashierRole mCashier;
	public Map<ItemGui, MarketCoordinates> mItems = new HashMap<ItemGui, MarketCoordinates>();
	private int xBase = 260, yBase = 10;
	public static final int sBaseInventory = 32;
	
	BufferedImage image1;
	BufferedImage image2;
	BufferedImage image3;
	BufferedImage image4;
	BufferedImage image5;
	
	public MarketItemsGui() {
	//	mMarketType = t;
		//populate list of items; hack right now
		
    	image1 = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("market/gui/images/item1.png");
    	image1 = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
    	}
    	
    	image2 = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("market/gui/images/item2.png");
    	image2 = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
    	}
    	
    	image3 = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("market/gui/images/item3.png");
    	image3 = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
    	}
    	
    	image4 = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("market/gui/images/item4.png");
    	image4 = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
    	}
    	
    	image5 = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("market/gui/images/item5.png");
    	image5 = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		//System.out.println(e.getMessage());
    	}
		
		mItems.put(new ItemGui(EnumItemType.STEAK,image1,sBaseInventory), new MarketCoordinates(xBase, yBase));
		mItems.put(new ItemGui(EnumItemType.CHICKEN,image2,sBaseInventory), new MarketCoordinates(xBase, yBase+100));
		mItems.put(new ItemGui(EnumItemType.SALAD,image3,sBaseInventory), new MarketCoordinates(xBase, yBase+200));
		mItems.put(new ItemGui(EnumItemType.PIZZA,image4,sBaseInventory), new MarketCoordinates(xBase, yBase+300));
		mItems.put(new ItemGui(EnumItemType.CAR,image5,sBaseInventory), new MarketCoordinates(xBase, yBase+400));	
	}
	
	public void updatePosition() {
		//no need to update position
	}
	
	public void draw(Graphics2D g) {
		//draw all items
		for(ItemGui i : mItems.keySet()) {
			MarketCoordinates c = mItems.get(i);
			g.setColor(Color.BLACK);
			for(int j=0;j<(int)(i.mNumber);j++) {
				int x = c.getX()+30*(j % 8);
				int y = c.getY() + 20*(int)(j/8);
				if(SimCityGui.GRADINGVIEW)
					g.drawString(i.toString(), x, y);
				else
					g.drawImage(i.mImage,x,y,null);
			}
		}
	}
	
/* Utilities */
	public void decreaseItemCount(EnumItemType i, int n) {
		for(ItemGui item : mItems.keySet()) {
			if(item.mItem == i) {
				item.mNumber = item.mNumber-n;
			}
		}
	}
	
	public MarketCoordinates getItemCoordinates(EnumItemType i) {
		for(ItemGui item : mItems.keySet()) {
			if(item.mItem == i) {
				return mItems.get(item);
			}
		}
		return null;
	}
	
	public int getNum(EnumItemType item) {
		int n = 0;
		for(ItemGui i : mItems.keySet()) {
			if(i.mItem == item) {
				n = i.mNumber;
				break;
			}
		}
		return n;
	}
	
	public void setInventory(EnumItemType i, int n) {
		for(ItemGui item : mItems.keySet()) {
			if(item.mItem.equals(i)) {
				item.mNumber = n;
			}
		}
	}
	
	public boolean isPresent() {
		return true;
	}
	
/* Classes */
	class ItemGui {
		EnumItemType mItem;
		int mNumber;
		BufferedImage mImage;
		
		ItemGui(EnumItemType i, BufferedImage c, int n) {
			mItem = i;
			mImage = c;
			mNumber = n;
		}
		
		public String toString() {
			switch(mItem) {
				case CHICKEN:
					return "CHK";
				case PIZZA:
					return "PIZ";
				case SALAD:
					return "SLD";
				case STEAK:
					return "STK";
				case CAR:
					return "CAR";
				default:
					return null;
			}
		}
	}
}
