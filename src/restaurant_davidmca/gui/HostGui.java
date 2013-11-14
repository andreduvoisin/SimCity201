package restaurant_davidmca.gui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;

import restaurant_davidmca.Table;
import restaurant_davidmca.agents.HostAgent;

public class HostGui implements Gui {

    private HostAgent agent = null;
    private static int tableSize = 50;

    public HostGui(HostAgent agent) {
        this.agent = agent;
    }

    public void updatePosition() {
    }

    public void draw(Graphics2D g) {
        Collection<Table> tables = agent.getTables();
		for (Table table: tables) {
			g.setColor(Color.ORANGE);
			g.fillRect(table.getX(), table.getY(), tableSize, tableSize);
		}
    }

    public boolean isPresent() {
        return true;
    }
}