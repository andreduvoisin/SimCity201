package market.gui;

import java.awt.*;

import market.Market;

import java.util.*;

import base.Item;

/**
 * This class paints the market inventory on the screen.
 * It updates as the market inventory is updated.
 * 
 * @author Angelica Huyen Tran
 */
public class MarketItemsGui implements Gui {
	private Market mMarket;
	
	private Map<ItemGui, Coordinates> mItems;
	
	public MarketItemsGui(Market m) {
		mMarket = m;
		
		//populate list of items
	}
	
	public void updatePosition() {
		//no need to update position
		//instead update inventory?
	}
	
	public void draw(Graphics2D g) {
		//draw all items
	}
	
/* Utilities */
	public boolean isPresent() {
		return true;
	}
	
/* Classes */
	class ItemGui {
		Item mItem;
		int mNumber;
		
		ItemGui(Item i) {
			mItem = i;
			
		}
	}
}
