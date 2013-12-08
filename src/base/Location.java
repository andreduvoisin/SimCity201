package base;

public class Location {
	public int mX;
	public int mY;
	
	public Location(int x, int y){
		mX = x;
		mY = y;
	}

	public void setTo(Location newLocation) {
		this.mX = newLocation.mX;
		this.mY = newLocation.mY;
	}
	
	public boolean equals(Location location){
		if (this.mX != location.mX) return false;
		if (this.mY != location.mY) return false;
		return true;
	}
	
	public void add(int z){
		mX += z;
		mY += z;
	}
	
	public Location createNew (int z){
		Location newl = new Location(mX, mY);
		newl.add(z);
		return newl;
	}
	
	public Location createNew () {
		Location newl = new Location(mX, mY);
		return newl;
	}
}
