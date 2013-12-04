package restaurant.restaurant_cwagoner.gui;

import java.awt.Graphics2D;

public interface CwagonerGui {

    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();

}
