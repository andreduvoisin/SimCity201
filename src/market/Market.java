package market;

import market.interfaces.*;
import java.util.*;

public class Market {
	private Cashier mCashier;
	private List<MarketItem> mInventory = Collections.synchronizedList(new ArrayList<MarketItem>());
	private double mCash;
	
	private static final double steakPrice = 15.99;
	private static final double chickenPrice = 10.99;
	private static final double saladPrice = 5.99;
	private static final double pizzaPrice = 8.99;
	private static final int mBaseInventory = 30;

	public Market() {
		//populate inventory
		
		mInventory.add(new MarketItem("Steak", mBaseInventory, steakPrice));
		mInventory.add(new MarketItem("Chicken", mBaseInventory, chickenPrice));
		mInventory.add(new MarketItem("Salad", mBaseInventory, saladPrice));
		mInventory.add(new MarketItem("Pizza", mBaseInventory, pizzaPrice));
	}
	
	public void setCashier(Cashier c) {
		mCashier = c;
	}
	
	public Cashier getCashier() {
		return mCashier;
	}
	
	public List<MarketItem> getInventory() {
		return mInventory;
	}
	
	public void setInventory(MarketItem i, int n) {
		for(MarketItem item : mInventory) {
			if(item.mName.equals(i.mName)) {
				item.mInventory = n;
			}
		}
	}
}
