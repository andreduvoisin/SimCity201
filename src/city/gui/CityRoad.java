package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CityRoad extends CityComponent {
	
	private RoadDirection direction;
	
	public CityRoad(int x, RoadDirection direction) {
		super(x, 0, Color.black, "Road");
		this.direction = direction;
		if (direction == RoadDirection.HORIZONTAL)
			rectangle = new Rectangle(0, x, 1000, 20);
		else
			rectangle = new Rectangle(x, 0, 20, 1000);
	}

	public CityRoad(int x, RoadDirection direction, String I) {
		super(x, 0, Color.gray, I);
		this.direction = direction;
	}

	public void updatePosition() {
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
