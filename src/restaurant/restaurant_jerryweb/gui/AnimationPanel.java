package restaurant.restaurant_jerryweb.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {
	
	static final int tableWidth = 50;
	static final int tableHeight = 50;
	static final int table1Xpos = 200;
	static final int table1Ypos = 250;
	
	static final int table2Xpos = 310;
	static final int table2Ypos = 185;

	static final int table3Xpos = 370;
	static final int table3Ypos = 100;
	
	static final int kitchenXpos = 225;
	static final int kitchenYpos = 50;
	static final int kitchenWidth = 125;
	static final int kitchenHeight = 20;
	
	static final int grillXpos = 225;
	static final int grillYpos = 5;
	static final int grillWidth = 125;
	static final int grillHeight = 20;
	
	static final int cashierXpos = 50;
	static final int cashierYpos = 200;
	static final int cashierWidth = 20;
	static final int cashierHeight = 20;
	
	static final int cookXpos = 275;
	static final int cookYpos = 26;
	static final int cookWidth = 20;
	static final int cookHeight = 20;
	
	static final int panelXpos = 0;
	static final int panelYpos = 0;
	static final int timerCount = 5;
	
    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
    	setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(timerCount, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(panelXpos, panelYpos, WINDOWX, WINDOWY ); //This centers the screen on the restaurant scene with the table located in it... if not located at 0,0 then 
        //part of the RestaurantPanel would show

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(table1Xpos, table1Ypos, tableWidth , tableHeight);//200 and 250 need to be table params(they give the location of the table in the panel.
        //50 and 50 give the dimensions of the table width and length respectively
        g2.setColor(Color.ORANGE);
        g2.fillRect(table2Xpos, table2Ypos, tableWidth , tableHeight);
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(table3Xpos, table3Ypos, tableWidth , tableHeight);
        //g2.drawString(arg0, arg1, arg2);
        
        g2.setColor(Color.CYAN);
        g2.fillRect(cashierXpos, cashierYpos, cashierWidth , cashierHeight);
        
        g2.setColor(Color.lightGray);
        g2.fillRect(kitchenXpos, kitchenYpos, kitchenWidth, kitchenHeight);
        
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(grillXpos, grillYpos, grillWidth, grillHeight);
        
        g2.setColor(Color.YELLOW);
        g2.fillRect(cookXpos, cookYpos, cookWidth, cookHeight);
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }

	public void addGui(WaiterGui gui) {
		guis.add(gui);
		
	}
}
