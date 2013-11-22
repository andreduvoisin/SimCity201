import java.awt.geom.*;


public class Building extends Rectangle2D.Double {
	BuildingPanel myBuildingPanel;

	public Building( int x, int y, int width, int height ) {
		super( x, y, width, height );
	}
	
	public void displayBuilding() {
		myBuildingPanel.displayBuildingPanel();
	}
	
	public void setBuildingPanel( BuildingPanel bp ) {
		myBuildingPanel = bp;
	}
}
