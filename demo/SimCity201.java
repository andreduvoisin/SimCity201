import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SimCity201 extends JFrame {
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	
	public SimCity201() {
		setVisible( true );
		setSize( 500, 500 );
		
		setLayout( new BorderLayout() );
		
		cityPanel = new CityPanel();
		cityPanel.setPreferredSize( new Dimension(500, 250) );
		cityPanel.setMaximumSize( new Dimension(500, 250) );
		cityPanel.setMinimumSize( new Dimension(500, 250) );
		
		cardLayout = new CardLayout();

		buildingPanels = new JPanel();
		buildingPanels.setLayout( cardLayout );
		buildingPanels.setMinimumSize( new Dimension( 500, 250 ) );
		buildingPanels.setMaximumSize( new Dimension( 500, 250 ) );
		buildingPanels.setPreferredSize( new Dimension( 500, 250 ) );
		buildingPanels.setBackground(Color.yellow);
		
		//Create the BuildingPanel for each Building object
		ArrayList<Building> buildings = cityPanel.getBuildings();
		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			BuildingPanel bp = new BuildingPanel( b, i, this );
			b.setBuildingPanel( bp );
			buildingPanels.add( bp, "" + i );
		}
		
		add( BorderLayout.NORTH, cityPanel );
		add( BorderLayout.SOUTH, buildingPanels );
	}
	
	public void displayBuildingPanel( BuildingPanel bp ) {
		System.out.println("abc");
		System.out.println( bp.getName() );
		cardLayout.show( buildingPanels, bp.getName() );
	}
	
	public static void main( String[] args ) {
		SimCity201 sc = new SimCity201();
		
	}
}
