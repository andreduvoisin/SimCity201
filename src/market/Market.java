package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.interfaces.MarketCashier;
import base.Item;

public class Market {
	private MarketCashier mCashier;
	private List<MarketItem> mInventory = Collections.synchronizedList(new ArrayList<MarketItem>());
	private double mCash = 5000;
	
	private static final int mBaseInventory = 3;

	public Market() {
		//populate inventory
		mInventory.add(new MarketItem("Steak", mBaseInventory, Item.cPRICE_STEAK));
		mInventory.add(new MarketItem("Chicken", mBaseInventory, Item.cPRICE_CHICKEN));
		mInventory.add(new MarketItem("Salad", mBaseInventory, Item.cPRICE_SALAD));
		mInventory.add(new MarketItem("Pizza", mBaseInventory, Item.cPRICE_PIZZA));
		mInventory.add(new MarketItem("Car", mBaseInventory, Item.cPRICE_CAR));
	}
	
	public void setCashier(MarketCashier c) {
		mCashier = c;
	}
	
	public MarketCashier getCashier() {
		return mCashier;
	}
	
	public List<MarketItem> getInventory() {
		return mInventory;
	}
	
	public int getInventory(String i) {
		for(MarketItem item : mInventory) {
			if(item.mName.equals(i))
				return item.mInventory;
		}
		return 0;
	}
	
	public void setInventory(String i, int n) {
		for(MarketItem item : mInventory) {
			if(item.mName.equals(i)) {
				item.mInventory = n;
			}
		}
	}
	
	public double getCost(String i) {
		for(MarketItem item : mInventory) {
			if(item.mName.equals(i)) {
				return item.mPrice;
			}
		}
		return 0;
	}
}
