package restaurant.restaurant_smileham.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.restaurant_smileham.SmilehamRestaurant;
import base.Gui;

public class LabelGui implements Gui{
	
	private String mLabel;
	private int mPosX;
	private int mPosY;

	public LabelGui(String label, int x, int y){
		mLabel = label;
		mPosX = x;
		mPosY = y;
		
		SmilehamRestaurant.addGui(this);
	}
	
	@Override
	public void updatePosition() {
		//none
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawString(mLabel, mPosX, mPosY);
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public void remove(){
		SmilehamRestaurant.removeGui(this);
	}
	
	public void setLabel(String label){
		mLabel = label;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFired(boolean state) {
		// TODO Auto-generated method stub
		
	}

}
