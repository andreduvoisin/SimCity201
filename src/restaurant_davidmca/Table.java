package restaurant_davidmca;

import restaurant_davidmca.interfaces.Customer;

public class Table {
	Customer occupiedBy;
	int tableNumber;
	int xpos;
	int ypos;
	int capacity;
	int current;

	public Table(int tableNumber, int xPosition, int yPosition, int seats) {
		this.tableNumber = tableNumber;
		this.xpos = xPosition;
		this.ypos = yPosition;
		this.capacity = seats;
	}

	void setOccupant(Customer cust) {
		occupiedBy = cust;
		current++;
	}

	void setUnoccupied() {
		occupiedBy = null;
		current--;
	}

	Customer getOccupant() {
		return occupiedBy;
	}

	boolean isOccupied() {
		if (current == capacity) {
			return true;
		}
		return false;
	}

	public int getX() {
		return xpos;
	}

	public int getY() {
		return ypos;
	}

	public String toString() {
		return "table " + tableNumber;
	}
}
