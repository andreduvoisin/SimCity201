package city.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import base.Time;

public class TimeGui extends CityComponent{
	private int mX;
	private int mY;
	private int mXspan;
	private int mYspan;
	private static final int fontSize = 12;
	
	public TimeGui(int x, int y){
		super(x, y);
		mX = x;
		mY = y;
		mXspan = 600 - mX;
		mYspan = 600 - mY;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(mX, mY, mXspan-2, mYspan);
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
//		String hour = Integer.toString(Time.GetHour());
//		String minute = Integer.toString(Time.GetMinute());
		g.drawString("TIME " + Time.GetHour()+ ":" + Time.GetMinute(), x-10 , y + 20);
	}
	
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		return true;
	}

	@Override
	public void setPresent(boolean state) {
		isActive = state;
	}

	@Override
	public void updatePosition() {
		
	}

}
