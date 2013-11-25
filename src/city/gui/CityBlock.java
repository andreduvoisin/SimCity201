package city.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class CityBlock extends CityComponent {

	public CityBlock(int posX, int posY, int spanX, int spanY, Color color){
		super(posX, posY, color, "Block");
		rectangle = new Rectangle(posX, posY, spanX, spanY);
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

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub

	}

}
