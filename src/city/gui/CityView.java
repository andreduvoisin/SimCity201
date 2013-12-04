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
import restaurant.restaurant_cwagoner.gui.CwagonerRestaurantGui;
import restaurant.restaurant_davidmca.gui.DavidRestaurantGui;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantGui;
import restaurant.restaurant_jerryweb.gui.JerrywebRestaurantGui;
import restaurant.restaurant_maggiyan.gui.MaggiyanRestaurantGui;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_tranac.gui.TranacRestaurantPanel;
//import restaurant.restaurant_tranac.gui.TranacRestaurantPanel; // ANGELICA: error here
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import bank.gui.BankPanel;
import base.ContactList;
import base.Location;

@SuppressWarnings("serial")
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

		CwagonerRestaurantGui cwagoner = new CwagonerRestaurantGui(city);
		cards.put("R_cwagoner", cwagoner);

		AndreRestaurantGui aduvoisin = new AndreRestaurantGui(city);
		cards.put("R_aduvoisin", aduvoisin);
		
		DavidRestaurantGui davidmca = new DavidRestaurantGui(city);
		cards.put("R_davidmca", davidmca);
		
		SmilehamAnimationPanel smileham = new SmilehamAnimationPanel(city);
		cards.put("R_smileham", smileham);
		
		JerrywebRestaurantGui jerryweb = new JerrywebRestaurantGui(city);
		cards.put("R_jerryweb", jerryweb);
		
		cards.put("R_tranac", new TranacRestaurantPanel(city));
		
		MaggiyanRestaurantGui maggiyan = new MaggiyanRestaurantGui(city); 
		cards.put("R_Maggiyan", maggiyan);
		
		RexAnimationPanel xurex = new RexAnimationPanel(city);
		cards.put("R_xurex", xurex);
				
		/*
		 * Instantiate Market and Bank Panels and add to the Master Lists
		 */
		
		MarketPanel market0 = new MarketPanel(city, EnumMarketType.BOTH);
		city.citypanel.masterMarketList.add(market0);
		cards.put("Costco", market0);
		
		MarketPanel market1 = new MarketPanel(city, EnumMarketType.BOTH);
		city.citypanel.masterMarketList.add(market1);
		cards.put("Sams Club", market1);
		
		BankPanel bank0 = new BankPanel(city);
		city.citypanel.masterBankList.add(bank0);
		cards.put("Gringotts Bank", bank0);
		
		BankPanel bank1 = new BankPanel(city);
		city.citypanel.masterBankList.add(bank1);
		cards.put("Piggy Bank", bank1);
		
		layout = new CardLayout();
		this.setLayout(layout);
		for (String key:cards.keySet()) {
			this.add(cards.get(key), key);
		}

		// Create Houses
		for (int iHouseCount = 0; iHouseCount < 80; iHouseCount++) {
			Location houseLocation = ContactList.cHOUSE_LOCATIONS
					.get(iHouseCount);
			CityHousing newHouse = new CityHousing(city, houseLocation.mX,
					houseLocation.mY, iHouseCount, 50.00);
			addView(newHouse.mPanel, "House " + iHouseCount);
			city.citypanel.addStatic(newHouse);
			city.citypanel.masterHouseList.add(newHouse);
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
