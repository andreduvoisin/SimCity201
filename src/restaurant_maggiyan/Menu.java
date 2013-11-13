package restaurant_maggiyan;

import java.util.*;

public class Menu{
	
	List<FoodChoice> MenuOptions = new ArrayList<FoodChoice>(); 
	int cheapFoodIndex = 2; 
	
	Menu(){
		FoodChoice Steak = new FoodChoice("Steak", 15.99);
		FoodChoice Chicken = new FoodChoice("Chicken", 10.99);
		FoodChoice Salad = new FoodChoice("Salad", 5.99);
		FoodChoice Pizza = new FoodChoice("Pizza", 8.99);
		
		
		MenuOptions.add(Steak); 
		MenuOptions.add(Chicken); 
		MenuOptions.add(Salad);
		MenuOptions.add(Pizza); 
		
    }
	public String getCheapestFood(){
		return MenuOptions.get(cheapFoodIndex).name; 
	}
	
	public String get(int num){
		return MenuOptions.get(num).name; 
	}
	
	public class FoodChoice{
		String name;
		double price;
		boolean available; 
		
		FoodChoice(String n, double p){
			name = n;
			price = p;
			available = true; 
		}
	}
	
}