package restaurant_maggiyan.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

	public static int XPOS = 200; 
	public static int YPOS = 250; 
	public static int GWIDTH = 50;
	public static int GHEIGHT = 50; 
	public static int FRAMESPEED = 10; 
	
	
    private final int WINDOWX = 1000;
    private final int WINDOWY = 350;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        setBackground(Color.white);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(FRAMESPEED, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Waiting area for customers 
        g2.setColor(Color.RED);
        g2.fillRect(50, 0, GWIDTH*10, GHEIGHT);

        
        
        
        
        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(XPOS, YPOS, GWIDTH, GHEIGHT);//200 and 250 need to be table params
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(XPOS+100, YPOS, GWIDTH, GHEIGHT);//200 and 250 need to be table params
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(XPOS+200, YPOS, GWIDTH, GHEIGHT);//200 and 250 need to be table params
        
        //Kitchen and Cook
        
        g2.setColor(Color.CYAN);
        g2.fillRect(XPOS+350, YPOS, GWIDTH, GHEIGHT*4);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(XPOS+350+GWIDTH, YPOS, GWIDTH*3, GHEIGHT*4);
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(XPOS+350+GWIDTH+GWIDTH*3-GWIDTH, YPOS, GWIDTH, GWIDTH);
        g2.fillRect(XPOS+350+GWIDTH+GWIDTH*3-GWIDTH, YPOS + GWIDTH + 25, GWIDTH, GWIDTH);
        g2.fillRect(XPOS+350+GWIDTH+GWIDTH*3-GWIDTH, YPOS + GHEIGHT*4 - GWIDTH, GWIDTH, GWIDTH);
        
        //g2.setColor(Color.BLUE);
        //g2.fillRect(XPOS+410, YPOS+50, 20, 20);
        
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
//            if(gui instanceof CustomerGui){
//            	showChoice((CustomerGui)gui); 
//            }
//            if(gui instanceof WaiterGui){
//            	showChoice((WaiterGui)gui);
//            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
//    public void showChoice(CustomerGui gui){
//    	add(gui.myChoice);
//    	gui.myChoice.setLocation(gui.getXPos(), gui.getYPos() - 10);
//    }
//    
//    public void showChoice(WaiterGui gui){
//    	add(gui.customerChoice);
//    	gui.customerChoice.setLocation(gui.getXPos(), gui.getYPos() - 10);
//    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
}
