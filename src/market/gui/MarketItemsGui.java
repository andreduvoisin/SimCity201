package market.gui;

import java.awt.*;
import market.roles.MarketCashierRole;
import java.util.*;
import base.Item.EnumMarketItemType;


/**
 * This class paints the market inventory on the screen.
 * It updates as the market inventory is updated.
 * 
 * @author Angelica Huyen Tran
 */
public class MarketItemsGui implements MarketBaseGui {
	MarketCashierRole mCashier;
	private Map<ItemGui, MarketCoordinates> mItems = new HashMap<ItemGui, MarketCoordinates>();
	private int xBase = 10, yBase = 20;
	private static final int SIZE = 20;
	
	public MarketItemsGui(MarketCashierRole c) {
		
		//populate list of items; hack right now
		mItems.put(new ItemGui(EnumMarketItemType.STEAK,Color.RED), new MarketCoordinates(xBase, yBase));
		mItems.put(new ItemGui(EnumMarketItemType.CHICKEN,Color.RED), new MarketCoordinates(xBase+30, yBase));
		mItems.put(new ItemGui(EnumMarketItemType.SALAD,Color.RED), new MarketCoordinates(xBase+60, yBase));
		mItems.put(new ItemGui(EnumMarketItemType.PIZZA,Color.RED), new MarketCoordinates(xBase+90, yBase));
		mItems.put(new ItemGui(EnumMarketItemType.CAR,Color.RED), new MarketCoordinates(xBase+120, yBase));
		
	}
	
	public void updatePosition() {
		//no need to update position
		//instead update inventory?
	/*	for(ItemGui i : mItems.keySet()) {
			i.mNumber = mCashier.getInventory(i.mItem);
		}
		
	*/
	}
	
	public void draw(Graphics2D g) {
		//draw all items
		for(ItemGui i : mItems.keySet()) {
			MarketCoordinates c = mItems.get(i);
			g.setColor(i.mColor);
			for(int j=0;j<i.mNumber;j++) {
				System.out.println("La");
			//	c.setY(c.getY()+30);
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
		EnumMarketItemType mItem;
		int mNumber;
		Color mColor;
		
		ItemGui(EnumMarketItemType i, Color c) {
			mItem = i;
			mColor = c;
			mNumber = 5;
		}
	}
}
