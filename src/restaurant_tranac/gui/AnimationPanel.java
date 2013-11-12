package restaurant_tranac.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

//    private final int WINDOWX = 450;
//    private final int WINDOWY = 475;
	private final int WINDOWX = 626;
	private final int WINDOWY = 507;
    private final int TIMERDELAY = 8;
    private BufferedImage background;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        setBackground(Color.white);
 
    	Timer timer = new Timer(TIMERDELAY, this );
    	timer.start();
    	
    	background = null;
    	try {
    	java.net.URL imageURL = this.getClass().getClassLoader().getResource("restaurant_tranac/gui/images/restaurant.png");
    	background = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    }

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        g2.drawImage(background,0,0,null);

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

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
    
    public void addGui(HostGui gui) {
    	guis.add(gui);
    }

	public void addGui(CashierGui gui) {
		guis.add(gui);
	}
}
