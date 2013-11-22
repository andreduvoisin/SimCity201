package reference.demo;
import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;

public class BuildingPanel extends JPanel {
	Rectangle2D myRectangle;
	String myName;
	SimCity201 myCity;
	
	public BuildingPanel( Rectangle2D r, int i, SimCity201 sc ) {
		myRectangle = r;
		myName = "" + i;
		myCity = sc;
		
		setBackground( Color.LIGHT_GRAY );
		setMinimumSize( new Dimension( 500, 250 ) );
		setMaximumSize( new Dimension( 500, 250 ) );
		setPreferredSize( new Dimension( 500, 250 ) );
		
		JLabel j = new JLabel( myName );
		add( j );
	}
	
	public String getName() {
		return myName;
	}

	public void displayBuildingPanel() {
		myCity.displayBuildingPanel( this );
		
	}
}
