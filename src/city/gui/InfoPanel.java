package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel {

	SimCityGui city;
	JLabel text;
	public static final int INFO_WIDTH = 500, INFO_HEIGHT = 100;
	
	public InfoPanel(SimCityGui city) {
		this.city = city;
		this.setPreferredSize(new Dimension(INFO_WIDTH, INFO_HEIGHT));
		this.setBackground(Color.LIGHT_GRAY);
		this.setVisible(true);
		
		text = new JLabel("Info Panel");
		text.setForeground(Color.black);
		text.setFont(new Font("Sans Serif", Font.PLAIN, 60));
		add(text);
	}
	
	public void setText(String s) {
		text.setText(s);
	}

}
