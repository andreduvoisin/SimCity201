package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

// CHASE: gui
public class TransportationBusRiderGui {

	boolean mBoarded;

	/**
	 * Created in TransportationBusRiderRole when person arrives at BusStop
	 */
	public TransportationBusRiderGui() {
		mBoarded = false;
	}

	// Once boarded, don't draw anymore ("inside" the bus)
	public void draw(Graphics2D g) {
		if (! mBoarded) {
			g.setColor(Color.yellow);
			// CHASE: use ContactList sizing
			g.fillRect(0, 0, 0, 0);
		}
	}

	public void DoBoardBus() {
		mBoarded = true;
	}

	public void DoExitBus() {
		mBoarded = false;
	}
}
