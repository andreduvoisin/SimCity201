package market.gui;

import java.awt.*;
import javax.swing.*;
import market.gui.MarketPanel;

public class MarketGui extends JFrame {
	MarketPanel marketPanel = new MarketPanel();

	public MarketGui() {
		setBounds(50,50,600,400);
		Dimension dim = new Dimension(600,400);
		marketPanel.setPreferredSize(dim);
		marketPanel.setMaximumSize(dim);
		marketPanel.setMinimumSize(dim);
		add(marketPanel);
	}
	
	public static void main(String[] args) {
		MarketGui gui = new MarketGui();
		gui.setTitle("Market Test");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
