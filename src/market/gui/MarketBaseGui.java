package market.gui;

import java.awt.Graphics2D;

public interface MarketBaseGui {

    public void updatePosition();
    public void draw(Graphics2D g);
    public boolean isPresent();

}
