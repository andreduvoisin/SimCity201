package city.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;




public class CityGui extends JFrame implements ActionListener{
	JFrame animationFrame = new JFrame("SimCity Animation");
	CityPanel animationPanel = new CityPanel();
    private CityPanel CityPanel = new CityPanel();

	
	
	
	static final int hSpacing = 30;
	static final int vSpacing = 0;
	static final int xIndexing = 50;
	static final int yIndexing = 50;
	
	public CityGui() {
		int WINDOWX = 1024;
        int WINDOWY = 768;

        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(WINDOWX, yIndexing , WINDOWX, WINDOWY);
        animationFrame.setVisible(false);
    	add(animationPanel);
    	
    	setBounds(xIndexing, yIndexing, WINDOWX, WINDOWY);
    	
    	setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));
    	
    	Dimension cityDim = new Dimension(WINDOWX, (int) (WINDOWY * .4));
    	CityPanel.setPreferredSize(cityDim);
    	
    	
    	
	}
	
	public static void main(String[] args) {
        CityGui gui = new CityGui();
        gui.setTitle("Team 28 City");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
