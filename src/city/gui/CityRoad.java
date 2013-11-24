package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class CityRoad extends CityComponent {
	
	private RoadDirection direction;
	private int ROADWIDTH = 30; 
	private int ROADLENGTH = 1000; 
	private int HOUSEBLOCKWIDTH = 20; 
	
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

//	public void paint(Graphics g) {
//		g.setColor(color);
//		if (direction == RoadDirection.HORIZONTAL)
//			g.fillRect(0, x, 1000, 20);
//		else
//			g.fillRect(x, 0, 20, 1000);
//	}

//	public boolean contains(int x, int y) {
//		if (direction == RoadDirection.HORIZONTAL)
//			if (x >= this.x && x <= this.x+20)
//				return true;
//		if (direction == RoadDirection.VERTICAL)
//			if (y >= this.x && y <= this.x+20)
//				return true;
//		return false;
//	}

}
