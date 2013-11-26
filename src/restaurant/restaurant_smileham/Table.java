package restaurant.restaurant_smileham;

import java.awt.Color;

import restaurant.restaurant_smileham.interfaces.SmilehamCustomer;



public class Table {
	public static final Color cTABLE_COLOR = Color.BLUE;
	public static final int cTABLE_X = 100;
	public static final int cTABLE_Y = 150;
	public static final int cTABLE_WIDTH = 50;
	public static final int cTABLE_HEIGHT = 50;
	public static final int cTABLE_SPACING = 100;
	public static final int cNUM_ROWS = 3;

	private SmilehamCustomer mOccupiedBy;

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

	public SmilehamCustomer getOccupiedBy() {
		return mOccupiedBy;
	}
	
	public void setOccupant(SmilehamCustomer cust) {
		mOccupiedBy = cust;
	}

	public void setUnoccupied() {
		mOccupiedBy = null;
	}

	public SmilehamCustomer getOccupant() {
		return mOccupiedBy;
	}

	public boolean isOccupied() {
		return mOccupiedBy != null;
	}

	public String toString() {
		return "table " + mTableNumber;
	}
}