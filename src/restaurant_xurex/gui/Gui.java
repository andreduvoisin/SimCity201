package restaurant_xurex.gui;

import java.awt.*;

public interface Gui {

    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();
    
	public class Point{
		private int x;
		private int y;
		public int getX(){
			return x;
		}
		public int getY(){
			return y;
		}
		Point(int x, int y){
			this.x=x; this.y=y;
		}
	};
}
