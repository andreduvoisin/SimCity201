package restaurant_davidmca;

import java.util.ArrayList;
import java.util.Collection;

public class Stock {
	private String choice;
	private int quantity;
	Collection<MarketAgent> alreadyAskedMarkets = new ArrayList<MarketAgent>();

	public Stock(String ch, int qty) {
		this.setChoice(ch);
		this.setQuantity(qty);
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void decrementQuantity() {
		this.quantity--;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public void blackListMarket(MarketAgent m) {
		alreadyAskedMarkets.add(m);
	}
	
	public Collection<MarketAgent> getAlreadyAskedMarkets() {
		return alreadyAskedMarkets;
	}

}
