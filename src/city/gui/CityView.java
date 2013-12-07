package city.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import market.gui.MarketPanel;
import restaurant.restaurant_davidmca.gui.DavidAnimationPanel;
import restaurant.restaurant_duvoisin.gui.AndreRestaurantGui;
import restaurant.restaurant_jerryweb.gui.JerrywebAnimationPanel;
import restaurant.restaurant_maggiyan.gui.MaggiyanAnimationPanel;
import restaurant.restaurant_smileham.gui.SmilehamAnimationPanel;
import restaurant.restaurant_tranac.gui.TranacAnimationPanel;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.Location;
import base.reference.ContactList;

@SuppressWarnings("serial")
public class CityView extends JPanel implements MouseListener, ActionListener {
	
	public HashMap<String, CityCard> cards;
	public List<CityHousing> mCityHousingList; //List to reference GUIs by ID
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

		//DAVID temporarily disabling non-compliant restaurants
		
		//Rest 0		
		AndreRestaurantGui duvoisin = new AndreRestaurantGui(city);
		cards.put("R_aduvoisin", duvoisin);
		
//		//Rest 1
//		CwagonerRestaurantGui cwagoner = new CwagonerRestaurantGui(city);
//		city.citypanel.masterRestaurantList.add(cwagoner.restPanel);
//		cards.put("R_cwagoner", cwagoner);
		
//		//Rest 2
		JerrywebAnimationPanel jerryweb = new JerrywebAnimationPanel(city);
		cards.put("R_jerryweb", jerryweb);
		
//		//Rest 3		
		MaggiyanAnimationPanel maggiyan = new MaggiyanAnimationPanel(city);
		cards.put("R_Maggiyan", maggiyan);
		
		//Rest 4
		DavidAnimationPanel davidmca = new DavidAnimationPanel(city, ContactList.DavidRestaurant);
		cards.put("R_davidmca", davidmca);
		
		//Rest 5
		SmilehamAnimationPanel smileham = new SmilehamAnimationPanel(city);
		cards.put("R_smileham", smileham);
		
//		//Rest 6
		TranacAnimationPanel tranac = new TranacAnimationPanel(city, ContactList.TranacRestaurant);
		cards.put("R_tranac", tranac);
//		TranacRestaurantPanel tranac = new TranacRestaurantPanel(city);
//		city.citypanel.masterRestaurantList.add(tranac);
//		cards.put("R_tranac", tranac);
		
		//Rest 7		
		RexAnimationPanel xurex = new RexAnimationPanel(city);
		cards.put("R_xurex", xurex);
				
		/*
		 * Instantiate Market and Bank Panels and add to the Master Lists
		 */
		
		MarketPanel market0 = new MarketPanel(city, ContactList.sMarketList.get(0));
		cards.put("Costco", market0);
		
		MarketPanel market1 = new MarketPanel(city, ContactList.sMarketList.get(1));
		cards.put("Sams Club", market1);
		/*
		BankPanel bank0 = new BankPanel(city);
		ContactList.sBankList.add(bank0);
		cards.put("Gringotts Bank", bank0);
		
		BankPanel bank1 = new BankPanel(city);
		ContactList.sBankList.add(bank1);
		cards.put("Piggy Bank", bank1);
		*/
		layout = new CardLayout();
		this.setLayout(layout);
		for (String key:cards.keySet()) {
			this.add(cards.get(key), key);
		}

		// Create Houses
		mCityHousingList = new ArrayList<CityHousing>();
		for (int iHouseCount = 0; iHouseCount < 80; iHouseCount++) {
			Location houseLocation = ContactList.cHOUSE_LOCATIONS
					.get(iHouseCount);
			CityHousing newHouse = new CityHousing(city, houseLocation.mX,
					houseLocation.mY, iHouseCount);
			addView(newHouse.mPanel, "House " + iHouseCount);
			city.citypanel.addStatic(newHouse);
			mCityHousingList.add(newHouse);
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
