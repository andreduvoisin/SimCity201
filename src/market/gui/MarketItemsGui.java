package market.gui;

import java.awt.*;

import market.Market;

import java.util.*;


/**
 * This class paints the market inventory on the screen.
 * It updates as the market inventory is updated.
 * 
 * @author Angelica Huyen Tran
 */
public class MarketItemsGui implements MarketBaseGui {
	private Map<ItemGui, MarketCoordinates> mItems;
	private int xBase = 10, yBase = 300;
	private static final int SIZE = 20;
	
	public MarketItemsGui() {
		
		//populate list of items; hack right now
		mItems.put(new ItemGui("Steak",Color.RED), new MarketCoordinates(xBase, yBase));
		mItems.put(new ItemGui("Chicken",Color.RED), new MarketCoordinates(xBase+30, yBase));
		mItems.put(new ItemGui("Salad",Color.RED), new MarketCoordinates(xBase+60, yBase));
		mItems.put(new ItemGui("Pizza",Color.RED), new MarketCoordinates(xBase+90, yBase));
		mItems.put(new ItemGui("Car",Color.RED), new MarketCoordinates(xBase+120, yBase));
		
	}
	
	public void updatePosition() {
		//no need to update position
		//instead update inventory?
		for(ItemGui i : mItems.keySet()) {
			i.mNumber = mMarket.getInventory(i.mItem);
		}
	}
	
	public void draw(Graphics2D g) {
		//draw all items
		for(ItemGui i : mItems.keySet()) {
			MarketCoordinates c = mItems.get(i);
			g.setColor(i.mColor);
			for(int j=0;j<i.mNumber;j++) {
				c.setY(c.getY()+30);
				g.fillRect(c.getX(),c.getY(),SIZE,SIZE);
			}
		}
	}
	
/* Utilities */
	public boolean isPresent() {
		return true;
	}
	
/* Classes */
	class ItemGui {
		String mItem;
		int mNumber;
		Color mColor;
		
		ItemGui(String i, Color c) {
			mItem = i;
			mColor = c;
		}
	}
}
