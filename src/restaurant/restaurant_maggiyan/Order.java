package restaurant_maggiyan;


import restaurant_maggiyan.interfaces.Waiter;

public class Order {
	
	public int cookingPosition = 0;
	public enum state {pending, cooking, done, finished};
	
	public Order(Waiter waiter, String choice, int tableNum){
		w = waiter;
		c = choice; 
		table = tableNum; 
		s = state.pending;
		pickedUp = false; 
		if(cookingPosition < 3){
			cookingPos = cookingPosition; 
			cookingPosition++; 
		}
		
	}
	
	public boolean pickedUp; 
	public int cookingPos; 
	public Waiter w; 
	public String c; 
	public int table; 
	public state s; 
	
}
