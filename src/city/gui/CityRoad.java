package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class CityRoad extends CityComponent {
	
	private RoadDirection direction;
	private int ROADWIDTH = 50; 
	private int ROADLENGTH = 600;
	
	public CityRoad(int x, RoadDirection direction) {
		super(x, 0, Color.black, "Road");
		this.direction = direction;
		if (direction == RoadDirection.HORIZONTAL)
			rectangle = new Rectangle(0, x, ROADLENGTH, ROADWIDTH);
		else if(direction == RoadDirection.VERTICAL)
			rectangle = new Rectangle(x, 0, ROADWIDTH, ROADLENGTH);
		
	}

	public CityRoad(int x, RoadDirection direction, String I) {
		super(x, 0, Color.gray, I);
		this.direction = direction;
	}

	public void updatePosition() {
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}
}
