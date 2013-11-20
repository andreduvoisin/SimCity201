package city.gui;

import javax.swing.*;

import base.Time;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class ControlPanel extends JPanel implements ActionListener {
	private CityGui gui;
	
	private final int WINDOWX = 300;
	private final int WINDOWY = 700;
	
	static final int panelX = 700;
	static final int panelY = 0;
	
	
	public ControlPanel(CityGui gui) {
		this.gui = gui;
		
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		add(new JLabel("<html><pre><u>Testing...</u><br></pre></html>"));
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
