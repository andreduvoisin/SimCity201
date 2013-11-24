package restaurant.restaurant_xurex.utilities;



public class Order{
	public String choice;
	public int table;
	public int kitchen;
	public OrderState s;
	public Order(String choice, int table){
		this.choice=choice; this.table=table; s=OrderState.sentToCook;
	}
};