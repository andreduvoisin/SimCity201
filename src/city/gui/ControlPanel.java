package city.gui;

import javax.swing.*;

import base.Time;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ControlPanel extends JPanel implements ActionListener {
	private CityGui gui;
	
	private final int WINDOWX = 300;
	private final int WINDOWY = 700;
	
	JLabel title = new JLabel("Control Panel");
	JButton pauseButton = new JButton("Start SimCity201");
	
	JLabel dayTitle = new JLabel("Day of Week:");
	String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	JComboBox currentDay = new JComboBox(days);
	
	JLabel scenarioTitle = new JLabel("Load Selected Scenario:");
	JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JPanel view = new JPanel();
	
	public ControlPanel(CityGui gui) {
		this.gui = gui;
		
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Font font = title.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		attributes.put(TextAttribute.SIZE, 20);
		title.setFont(font.deriveFont(attributes));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(title);
		
		Dimension pauseBtnDim = new Dimension(WINDOWX - 10, 30);
		pauseButton.setPreferredSize(pauseBtnDim);
		pauseButton.setMinimumSize(pauseBtnDim);
		pauseButton.setMaximumSize(pauseBtnDim);
		pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(pauseButton);
		
		dayTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(dayTitle);
		
		Dimension dayCBDim = new Dimension(WINDOWX - 10, 25);
		currentDay.setPreferredSize(dayCBDim);
		currentDay.setMinimumSize(dayCBDim);
		currentDay.setMaximumSize(dayCBDim);
		add(currentDay);
		
		scenarioTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(scenarioTitle);
		
		// ScrollingPane
		view.setLayout(new BoxLayout((Container)view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        
        Dimension scrollPaneDim = new Dimension(WINDOWX - 10, 100);
        pane.setPreferredSize(scrollPaneDim);
        pane.setMinimumSize(scrollPaneDim);
        pane.setMaximumSize(scrollPaneDim);
        
        Dimension panelSize = new Dimension(scrollPaneDim.width, (int)(scrollPaneDim.height / 7));
    	JPanel temp = new JPanel();
    	temp.setPreferredSize(panelSize);
    	temp.setMinimumSize(panelSize);
    	temp.setMaximumSize(panelSize);
    	temp.add(new JLabel("Default Scenario"));
    	view.add(temp);
    	
        add(pane);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
