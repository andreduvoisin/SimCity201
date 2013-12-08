package base;

public class Block {
	
	public int mX1; 
	public int mX2; 
	public int mY1; 
	public int mY2; 
	
	public Block(int x1, int x2, int y1, int y2){
		mX1 = x1;
		mX2 = x2;
		mY1 = y1; 
		mY2 = y2; 
	}
	
	public Block(Location first, Location second){
		mX1 = first.mX;
		mX2 = second.mX;
		mY1 = first.mY;
		mY2 = second.mY;
	}
}
