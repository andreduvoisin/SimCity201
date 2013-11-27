package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.gui.CityComponent;
import transportation.roles.TransportationBusRiderRole;

// CHASE: gui
public class TransportationBusRiderGui extends CityComponent implements base.Gui {

	boolean mBoarded;
	TransportationBusRiderRole role;
	int x, y, xDest, yDest;

	/**
	 * Created in TransportationBusRiderRole when person arrives at BusStop
	 */
	public TransportationBusRiderGui(TransportationBusRiderRole role) {
		this.role = role;
		mBoarded = false;
		x = 0;
		y = 0;
	}

	// Once boarded, don't draw anymore ("inside" the bus)
	public void draw(Graphics2D g) {
		if (! mBoarded) {
			g.setColor(Color.orange);
			g.fillRect(x, y, 5, 5);
		}
	}
	
	public void updatePosition() {
		if (! mBoarded) {
			if (x < xDest)		x++;
			else if (x > xDest)	x--;
	
			if (y < yDest)		y++;
			else if (y > yDest)	y--;
			setX(x);
			setY(y);
		}

		System.out.println("x = " + x + ", xDest = " + xDest);
		System.out.println("y = " + y + ", yDest = " + yDest);
		if (x == xDest && y == yDest) {
			role.msgGuiDone();
		}
	}

	public void DoGoToStop(int num) {
		xDest = base.ContactList.cBUS_STOPS.get(num).mX;
		yDest = base.ContactList.cBUS_STOPS.get(num).mY;
	}

	public void DoBoardBus() {
		mBoarded = true;
		role.msgGuiDone();
	}

	public void DoExitBus() {
		mBoarded = false;
		role.msgGuiDone();

		x = xDest;
		y = yDest;
	}

	@Override
	public boolean isPresent() {
		if (! mBoarded) return true;
		return false;
	}

	@Override
	public void setPresent(boolean state) {
		// TODO Auto-generated method stub
		
	}
}
