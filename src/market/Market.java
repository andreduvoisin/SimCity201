package market;

import market.interfaces.*;
import java.util.*;

public class Market {
	private MarketCashier mCashier;
	private List<MarketItem> mInventory = Collections.synchronizedList(new ArrayList<MarketItem>());
	private double mCash = 5000;
	
	private static final double steakPrice = 15.99; //ANGELICA SHANE: 2 Please remove all of these and delete this class
	private static final double chickenPrice = 10.99;
	private static final double saladPrice = 5.99;
	private static final double pizzaPrice = 8.99;
	private static final double carPrice = 100.00;
	private static final int mBaseInventory = 3;

	public Market() {
		//populate inventory
		mInventory.add(new MarketItem("Steak", mBaseInventory, steakPrice)); //ANGELICA SHANE: Change this to Item for all 5 items
		mInventory.add(new MarketItem("Chicken", mBaseInventory, chickenPrice));
		mInventory.add(new MarketItem("Salad", mBaseInventory, saladPrice));
		mInventory.add(new MarketItem("Pizza", mBaseInventory, pizzaPrice));
		mInventory.add(new MarketItem("Car", mBaseInventory, carPrice));
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
