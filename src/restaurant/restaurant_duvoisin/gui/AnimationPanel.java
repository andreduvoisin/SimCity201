package restaurant.restaurant_duvoisin.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 900;
    private final int WINDOWY = 900;
    static final int TIMERDELAY = 20;
    static final int IDLE_X = 10;
    static final int IDLE_Y = 52;
    static final int IDLE_SIZE_X = 30;
    static final int IDLE_SIZE_Y = 305;
    static final int WAIT_X = 10;
    static final int WAIT_Y = 10;
    static final int WAIT_SIZE_X = 430;
    static final int WAIT_SIZE_Y = 30;
    
    static final int GRILL_X = 45;
    static final int GRILL_Y = 351;
    static final int GRILL_SIZE_X = 400;
    static final int GRILL_SIZE_Y = 14;
    
    static final int FRIDGE_X = 425;
    static final int FRIDGE_Y = 328;
    static final int FRIDGESIZE = 20;
    /*
    private Image bufferImage;
    private Dimension bufferSize;
	*/
    private List<Gui> guis = new ArrayList<Gui>();
    private TableGui myTables = new TableGui();
    
    Timer timer;

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        //bufferSize = this.getSize();
 
    	timer = new Timer(TIMERDELAY, this);
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

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(myTables.getTableX(0), myTables.getTableY(0), myTables.getTableSize(0), myTables.getTableSize(0));
        g2.fillRect(myTables.getTableX(1), myTables.getTableY(1), myTables.getTableSize(1), myTables.getTableSize(1));
        g2.fillRect(myTables.getTableX(2), myTables.getTableY(2), myTables.getTableSize(2), myTables.getTableSize(2));
        
        //Idle Area for Waiters
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(IDLE_X, IDLE_Y, IDLE_SIZE_X, IDLE_SIZE_Y);
        g2.setColor(Color.BLACK);
        //g2.drawString("Idle Area", IDLE_X - 14, IDLE_Y - 15);
        
        //Waiting Area for Customers
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(WAIT_X, WAIT_Y, WAIT_SIZE_X, WAIT_SIZE_Y);
        g2.setColor(Color.BLACK);
        //g2.drawString("Waiting Area", 5, 15);
        
        // Grill
        g2.setColor(Color.CYAN);
        g2.fillRect(GRILL_X, GRILL_Y, GRILL_SIZE_X, GRILL_SIZE_Y);
        
        // Plating Area
        g2.setColor(Color.CYAN);
        g2.fillRect(GRILL_X, GRILL_Y - 40, GRILL_SIZE_X, GRILL_SIZE_Y);
        
        // Refrigerator
        g2.setColor(Color.BLACK);
        g2.fillRect(FRIDGE_X, FRIDGE_Y, FRIDGESIZE, FRIDGESIZE);

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
        gui.setTables(myTables);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
        gui.setTables(myTables);
    }
    
    public void addGui(CookGui gui) {
        guis.add(gui);
    }
    
    public void pauseAnimations() {
    	if(timer.isRunning()) { timer.stop(); }
    }
    
    public void resumeAnimations() {
    	if(!timer.isRunning()) { timer.start(); }
    }
}
