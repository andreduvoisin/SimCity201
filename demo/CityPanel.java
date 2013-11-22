import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class CityPanel extends JPanel implements MouseListener {
	ArrayList<Building> buildings;

	
	public CityPanel() {
		buildings = new ArrayList<Building>();

		for ( int i=0; i<2; i++ ) {
			for ( int j=0; j<5; j++ ) {
				Building b = new Building( i*40+ 10, j*40 + 10, 20, 20 );
				buildings.add( b );
			}
		}

		addMouseListener( this );
	}

		public void paintComponent( Graphics g ) {
			System.out.println("hello1");

			Graphics2D g2 = (Graphics2D)g;
			g2.setColor( Color.black );
			
			for ( int i=0; i<buildings.size(); i++ ) {
				Building b = buildings.get(i);
				g2.fill( b );
			}
		}

		public ArrayList<Building> getBuildings() {
			return buildings;
		}
		
		public void mouseClicked(MouseEvent me) {
			//Check to see which building was clicked
			System.out.println("hello2");

			for ( int i=0; i<buildings.size(); i++ ) {
				Building b = buildings.get(i);
				if ( b.contains( me.getX(), me.getY() ) ) {
					b.displayBuilding();
				}
			}
			
			
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
}
