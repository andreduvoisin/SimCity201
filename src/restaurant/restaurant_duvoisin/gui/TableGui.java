package restaurant.restaurant_duvoisin.gui;

import java.util.ArrayList;
import java.util.List;

public class TableGui {
	static int numTables = 3;
	private List<TableInfo> tables = new ArrayList<TableInfo>();
	class TableInfo {
		int tableX;
		int tableY;
		int tableSize;
		
		TableInfo(int x, int y, int size) {
			tableX = x;
			tableY = y;
			tableSize = size;
		}
	}
	
	static final int xStart = 100;
	static final int xIncrement = 100;
	static final int yStart = 225;
	static final int yIncrement = 0;
	static final int tSize = 50;
	
	public TableGui() {
		for(int i = 0; i < numTables; i++)
			tables.add(new TableInfo(xStart + (xIncrement * i), yStart + (yIncrement * i), tSize));
	}
	
	public int getTableX(int table) { return tables.get(table).tableX; }
	public int getTableY(int table) { return tables.get(table).tableY; }
	public int getTableSize(int table) { return tables.get(table).tableSize; }
	public int getNumTables() { return numTables; }
}

