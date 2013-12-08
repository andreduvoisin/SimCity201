package city.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import base.Location;



public class CityClosed extends CityComponent {
	
	//Animation Upgrades 
	private BufferedImage image;
	
	//Static Variables
	private static final int span = 80;

	public CityClosed(int posX, int posY){
		super(posX, posY);
		/*image = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/closed.png");
    		image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	disable();*/
	}
	
	public CityClosed(Location location){
		super(location.mX, location.mY);
		/*image = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/closed.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}*/
    	disable();
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		if (isActive)
			g.fillRect(x, y, span, span);
			//g.drawImage(image, x, y, null);
	}
	
	public void paint(Graphics2D g){
		if (isActive) {
			draw(g);
		}
	}

	@Override
	public boolean isPresent() {
		return isActive();
	}

	@Override
	public void setPresent(boolean state) {
		if (state)
			enable();
		disable();
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub

	}

}
