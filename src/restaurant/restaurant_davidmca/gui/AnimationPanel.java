package restaurant.restaurant_davidmca.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import base.Gui;

public class AnimationPanel extends JPanel implements ActionListener {

	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	public AnimationPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);

		this.getSize();

		Timer timer = new Timer(5, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint(); // Will have paintComponent called
		synchronized (guis) {
			for (Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY);

		for (Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}

	public void addGui(WaiterGui g) {
		guis.add(g);
	}

	public void addGui(CustomerGui g) {
		guis.add(g);
	}

	public void addGui(HostGui gui) {
		guis.add(gui);
	}
	
	public void addGui(CookGui gui) {
		guis.add(gui);
	}
}
