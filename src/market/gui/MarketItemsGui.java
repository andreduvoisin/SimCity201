package market.gui;

import java.awt.*;

import market.gui.MarketPanel.EnumMarketType;
import market.roles.MarketCashierRole;

import java.util.*;

import base.Item.EnumItemType;


/**
 * This class paints the market inventory on the screen.
 * It updates as the market inventory is updated.
 * 
 * @author Angelica Huyen Tran
 */
public class MarketItemsGui implements MarketBaseGui {
	MarketCashierRole mCashier;
	private EnumMarketType mMarketType;
	private Map<ItemGui, MarketCoordinates> mItems = new HashMap<ItemGui, MarketCoordinates>();
	private int xBase = 300, yBase = 30;
	private static final int SIZE = 20;
	private static final int sBaseInventory = 5;
	
	public MarketItemsGui(EnumMarketType t) {
		mMarketType = t;
		//populate list of items; hack right now
		if(t == EnumMarketType.FOOD) {
			mItems.put(new ItemGui(EnumItemType.STEAK,Color.RED), new MarketCoordinates(xBase, yBase));
			mItems.put(new ItemGui(EnumItemType.CHICKEN,Color.RED), new MarketCoordinates(xBase, yBase+100));
			mItems.put(new ItemGui(EnumItemType.SALAD,Color.RED), new MarketCoordinates(xBase, yBase+200));
			mItems.put(new ItemGui(EnumItemType.PIZZA,Color.RED), new MarketCoordinates(xBase, yBase+300));
		}
		else
			mItems.put(new ItemGui(EnumItemType.CAR,Color.RED), new MarketCoordinates(xBase, yBase+400));
	}
	
	public void updatePosition() {
		//no need to update position
	}
	
	public void draw(Graphics2D g) {
		//draw all items
		for(ItemGui i : mItems.keySet()) {
			MarketCoordinates c = mItems.get(i);
			g.setColor(i.mColor);
			for(int j=0;j<i.mNumber;j++) {
				g.fillRect(c.getX()+30*j,c.getY(),SIZE,SIZE);
			}
		}
	}
	
/* Utilities */
	public void decreaseItemCount(EnumItemType i) {
		for(ItemGui item : mItems.keySet()) {
			if(item.mItem == i) {
				item.mNumber--;
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
	
	public boolean isPresent() {
		return true;
	}
	
/* Classes */
	class ItemGui {
		EnumItemType mItem;
		int mNumber;
		Color mColor;
		
		ItemGui(EnumItemType i, Color c) {
			mItem = i;
			mColor = c;
			mNumber = sBaseInventory;
		}
	}
}
