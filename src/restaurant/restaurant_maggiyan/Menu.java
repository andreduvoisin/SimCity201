package restaurant.restaurant_maggiyan;

import java.util.ArrayList;
import java.util.List;

public class Menu{
	
	public List<FoodChoice> MenuOptions = new ArrayList<FoodChoice>(); 
	public int cheapFoodIndex = 2; 
	
	public Menu(){
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
		public String name;
		public double price;
		public boolean available; 
		
		public FoodChoice(String n, double p){
			name = n;
			price = p;
			available = true; 
		}
	}
	
}