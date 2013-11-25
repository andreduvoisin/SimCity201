package city.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JPanel;

import market.gui.MarketPanel;
import market.gui.MarketPanel.EnumMarketType;
import restaurant.restaurant_davidmca.gui.RestaurantGui;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_tranac.gui.RestaurantPanel_at;
import bank.gui.BankPanel;

public class CityView extends JPanel implements MouseListener, ActionListener {

	public HashMap<String, CityCard> cards;
	SimCityGui city;
	public static final int VIEW_WIDTH = 500, VIEW_HEIGHT = 500;
	CardLayout layout;
	
	public CityView(SimCityGui city) throws IOException {
		
		this.setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		this.setVisible(true);
		addMouseListener(this);
		this.city = city;
		
		//Card Deck
		cards = new HashMap<String, CityCard>();
		cards.put("null", new CityCard(city));
		cards.put("Road", new CityCard(city));
		
		RestaurantGui davidmca = new RestaurantGui(city);
		cards.put("R_davidmca", davidmca);
		
		SmilehamAnimationPanel smileham = new SmilehamAnimationPanel(city);
		cards.put("R_smileham", smileham);
		
		cards.put("R_tranac", new RestaurantPanel_at(city));
		
		cards.put("R_Maggiyan", new CityCard(city));
		cards.put("Gringotts Bank", new BankPanel(city));
		cards.put("Test Restaurant",  new CityCard(city)); 

		cards.put("Costco", new MarketPanel(city, EnumMarketType.FOOD));
		cards.put("Car Dealership", new MarketPanel(city, EnumMarketType.CAR));

		
		layout = new CardLayout();
		this.setLayout(layout);
		for (String key:cards.keySet()) {
			this.add(cards.get(key), key);
		}
		

		layout.show(this, "null");
	}
	
	public boolean addView(CityCard panel, String key) {
		if (cards.containsKey(key))
			return false;
		cards.put(key, panel);
		this.add(cards.get(key), key);
		return true;
	}
	
	public void setView(String key) {
		if (cards.containsKey(key)) {
			layout.show(this, key);
			city.infopanel.setText(key);
		}
	}

	
	public void actionPerformed(ActionEvent arg0) {
		
	}

	
	public void mouseClicked(MouseEvent e) {
		
	}

	
	public void mouseEntered(MouseEvent e) {
		
	}

	
	public void mouseExited(MouseEvent e) {
		
	}

	
	public void mousePressed(MouseEvent e) {
		
	}

	
	public void mouseReleased(MouseEvent e) {
		
	}

}
