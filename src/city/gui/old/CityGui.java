package city.gui.old;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class CityGui extends JFrame implements ActionListener{
	private static CityGui instance = null;
	JFrame cityFrame = new JFrame("SimCity201 - Team 28");
	OldCityPanel cityPanel = new OldCityPanel(this);
    
    ControlPanel controlPanel = new ControlPanel(this);
	
	static final int hSpacing = 30;
	static final int vSpacing = 0;
	static final int xIndexing = 50;
	static final int yIndexing = 50;
	
	static final int WINDOWX = 1020;
	static final int WINDOWY = 700;
	static final int CITYX = 700;
	static final int CITYY = 700;
	static final int CONTROLX = 300;
	static final int CONTROLY = 700;
	
	public CityGui() {
        cityFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cityFrame.setBounds(xIndexing, yIndexing , WINDOWX, WINDOWY);
        cityFrame.setVisible(false);
    	
    	setBounds(xIndexing, yIndexing, WINDOWX, WINDOWY);
    	setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    	
    	Dimension cityDim = new Dimension(CITYX, CITYY);
    	cityPanel.setPreferredSize(cityDim);
    	cityPanel.setMinimumSize(cityDim);
    	cityPanel.setMaximumSize(cityDim);
        add(cityPanel);
    	
    	Dimension controlDim = new Dimension(CONTROLX, CONTROLY);
    	controlPanel.setPreferredSize(controlDim);
    	controlPanel.setMinimumSize(controlDim);
        controlPanel.setMaximumSize(controlDim);
    	add(controlPanel);
	}
	/*
	public static void main(String[] args) {
        CityGui gui = new CityGui();
        gui.setTitle("SimCity201 - Team 28");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    */
	public static CityGui getInstanceOf() {
		if (instance == null) {
			instance = new CityGui();
		}
		return instance;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
