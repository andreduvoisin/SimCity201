package restaurant.restaurant_jerryweb.gui;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	public List<String> menuItems = new ArrayList<String>();
	//This map holds all of the food items on the menu
	//public Map<Integer,String> menuItems = new HashMap<Integer,String>(4);
	
	public Menu() {
		menuItems.add("salad");
		menuItems.add("pizza");
		menuItems.add("chicken");
		menuItems.add("steak");
		
		
		
			/*menuItems.put(0,"Steak");
			menuItems.put(1,"Chicken");
			menuItems.put(3,"Salad");
			menuItems.put(4,"Pizza");*/
	}
	
	
	
	public String getChoice(String choice){
		//
		for(int i = 0; i<menuItems.size(); i++){
			if(menuItems.get(i).equals(choice) ){
				return menuItems.get(i);
			}
		}
		return null;
	}
}
