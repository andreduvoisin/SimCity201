package restaurant_tranac.gui;

import base.Agent;
import restaurant_tranac.agents.CustomerAgent;
import restaurant_tranac.agents.WaiterAgent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.sun.org.apache.bcel.internal.util.ClassLoader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Vector;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame{
	AnimationPanel animationPanel = new AnimationPanel();
	JPanel panel = new JPanel();
	
	private final int WINDOWX = 1100;
	private final int WINDOWY = 560;
	private final int RESTX = 400;
	private final int RESTY = 520;
	private final int ANIMATIONX = 626;
	private final int ANIMATIONY = 520;
	private final int GAP = 20;
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {

    	setBounds(GAP, GAP, WINDOWX, WINDOWY);
    	setLayout(new FlowLayout());
    	
        Dimension restDim = new Dimension(RESTX, RESTY);
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel);
        
        Dimension aniDim = new Dimension(ANIMATIONX,ANIMATIONY);
        animationPanel.setPreferredSize(aniDim);
        animationPanel.setMinimumSize(aniDim);
        animationPanel.setMaximumSize(aniDim);
        animationPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.setBorder(new EmptyBorder(GAP,GAP,GAP,GAP));
    	panel.add(animationPanel);
    	add(panel);
    }

    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setEnabled(WaiterAgent w) {
    	restPanel.setEnabled(w);
    }
    
    public void setEnabled(CustomerAgent c) {
    	restPanel.setEnabled(c);
    }
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("The Kingdom Hearts Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
