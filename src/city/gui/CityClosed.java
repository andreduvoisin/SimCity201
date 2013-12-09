package city.gui;

import java.awt.Color;
import java.awt.Graphics;
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
		rectangle.x = posX;
		rectangle.y = posY;
		rectangle.height = span;
		rectangle.width = span;
		
		image = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/closed.png");
    		image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	disable();
	}
	
	public CityClosed(Location location){
		super(location.mX, location.mY);
		rectangle.x = location.mX;
		rectangle.y = location.mY;
		rectangle.height = span;
		rectangle.width = span;
		
		image = null;
    	try {
    		java.net.URL imageURL = this.getClass().getClassLoader().getResource("city/gui/images/closed.png");
    	image = ImageIO.read(imageURL);
    	}
    	catch (IOException e) {
    		System.out.println(e.getMessage());
    	}
    	disable();
	}
	
	@Override
	public void draw(Graphics2D g) {
		/*g.setColor(Color.BLACK);
		if (isActive)
			g.fillRect(x, y, span, span);
			//g.drawImage(image, x, y, null);*/
	}
	
	public void paint(Graphics g){
		if (isActive) {
			/*
			g.setColor(Color.BLACK);
			System.out.println("x: "+x+" y: "+y);
			g.fillRect(x, y, span, span);*/
			g.drawImage(image, x, y, null);
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
