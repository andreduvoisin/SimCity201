package restaurant.restaurant_maggiyan;


import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;

public class Order {
	
	public static int cookingPosition = 0;
	public enum state {pending, cooking, done, finished};
	
	public Order(MaggiyanWaiter waiter, String choice, int tableNum){
		w = waiter;
		c = choice; 
		table = tableNum; 
		s = state.pending;
		pickedUp = false; 
		if(cookingPosition < 3){
			cookingPos = cookingPosition; 
			cookingPosition++; 
		}
		else{
			cookingPos = 0; 
		}
		
	}
	
	public boolean pickedUp; 
	public int cookingPos; 
	public MaggiyanWaiter w; 
	public String c; 
	public int table; 
	public state s; 
	
}
