package restaurant.restaurant_davidmca;

import restaurant.restaurant_davidmca.interfaces.Customer;

public class Table {
	Customer occupiedBy;
	public int tableNumber;
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

	public void setOccupant(Customer cust) {
		occupiedBy = cust;
		current++;
	}

	public void setUnoccupied() {
		occupiedBy = null;
		current--;
	}

	Customer getOccupant() {
		return occupiedBy;
	}

	public boolean isOccupied() {
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
