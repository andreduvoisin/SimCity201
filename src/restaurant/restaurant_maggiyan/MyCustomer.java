package restaurant.restaurant_maggiyan;

import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;

public class MyCustomer {
	
	public enum CustomerState{waiting, seated, askedToOrder, readyToOrder, gaveOrder, waitingForFood, orderGiven, foodIsCooking, foodOrderReady, eating, checkReady, receivedCheck, done, finished, needsToReOrder, reordering}; 
	public MaggiyanCustomer c; 
	public int table; 
	public String choice; 
	public CustomerState s; 
	public Check check;
	public int orderPos; 
	
	public MyCustomer(MaggiyanCustomer customer, int tableNum, CustomerState state){
		c = customer; 
		table = tableNum; 
		s = state; 
		check = null; 
	}
	
	public void setCheck(Check c){
		check = c; 
	}
	
	//For JUnit Testing
	public double getTotal(Check check){
		return check.getCheckTotal(); 
	}
	
}
