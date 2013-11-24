package market.gui;

import java.awt.*;
import javax.swing.*;

public class MarketGui extends JFrame {
	static MarketPanel marketPanel = new MarketPanel();
	
	public MarketGui() {
		setLayout(new GridLayout());
		setBounds(50,50,500,500);
		Dimension dim = new Dimension(500,500);
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
		
		marketPanel.testGuis();
	}
}
