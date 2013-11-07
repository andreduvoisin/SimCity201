package restaurant_smileham;

import java.awt.Color;

import restaurant_smileham.interfaces.Customer;



public class Table {
	public static final Color cTABLE_COLOR = Color.BLUE;
	public static final int cTABLE_X = 100;
	public static final int cTABLE_Y = 150;
	public static final int cTABLE_WIDTH = 50;
	public static final int cTABLE_HEIGHT = 50;
	public static final int cTABLE_SPACING = 100;
	public static final int cNUM_ROWS = 3;

	private Customer mOccupiedBy;

	private int mTableNumber;

	public Table(int tableNumber) {
		mTableNumber = tableNumber;
	}
	
	public static int getX(int tableNumber){
		return cTABLE_X + cTABLE_SPACING*(tableNumber % 3);
	}
	
	public static int getY(int tableNumber){
		return cTABLE_Y + cTABLE_SPACING*((int) (tableNumber / 3));
	}
	
	public int getTableNumber(){
		return mTableNumber;
	}

	public Customer getOccupiedBy() {
		return mOccupiedBy;
	}
	
	public void setOccupant(Customer cust) {
		mOccupiedBy = cust;
	}

	public void setUnoccupied() {
		mOccupiedBy = null;
	}

	public Customer getOccupant() {
		return mOccupiedBy;
	}

	public boolean isOccupied() {
		return mOccupiedBy != null;
	}

	public String toString() {
		return "table " + mTableNumber;
	}
}