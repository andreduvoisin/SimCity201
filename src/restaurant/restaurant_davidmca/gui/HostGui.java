package restaurant.restaurant_davidmca.gui;


import java.awt.Graphics2D;

import restaurant.restaurant_davidmca.interfaces.Host;
import base.Gui;

public class HostGui implements Gui {

    @SuppressWarnings("unused")
	private Host agent = null;

    public HostGui(Host agent) {
        this.agent = agent;
    }

    public void updatePosition() {
    }

    public void draw(Graphics2D g) {
    }

    public boolean isPresent() {
        return true;
    }

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}
}