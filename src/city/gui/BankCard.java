package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;

import restaurant.restaurant_smileham.Menu;
import restaurant.restaurant_smileham.gui.RestaurantGui;

public class BankCard extends CityCard{
	public static final int CARD_WIDTH = 500, CARD_HEIGHT = 500;
	
	public BankCard(SimCityGui city) {
		super(city);
		
		JLabel staffLabel = new JLabel();
        staffLabel.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table>"
                + "<tr><td>Host:</td><td>" + "</td></tr></table>"
                		+ "<h3><u> Menu</u></h3>"
                		+ "<table><tr><td>Steak</td><td>$" + Menu.cSTEAK_PRICE + "</td></tr>"
                		+ "<tr><td>Chicken</td><td>$" + Menu.cCHICKEN_PRICE + "</td></tr>"
                		+ "<tr><td>Salad</td><td>$" + Menu.cSALAD_PRICE + "</td></tr>"
                		+ "<tr><td>Pizza</td><td>$" + Menu.cPIZZA_PRICE + "</td></tr></table><br></html>");
        add(staffLabel);
	}
}
