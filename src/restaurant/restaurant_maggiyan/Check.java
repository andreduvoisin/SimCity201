package restaurant.restaurant_maggiyan;

import restaurant.restaurant_maggiyan.interfaces.Customer;
import restaurant.restaurant_maggiyan.interfaces.Waiter;

public class Check{
    
	public Menu menu = new Menu(); 
	public Customer customer;
	public Waiter waiter; 
	public String choice;
	public double checkTotal;
	public boolean paid;
    
	public Check(Waiter w, Customer c, String fc){
            waiter = w; 
            customer = c;
            choice = fc;
            paid = false; 
            setCheckTotal(0.0);
    }

	public double getCheckTotal() {
		return checkTotal;
	}

	public void setCheckTotal(double checkTotal) {
		this.checkTotal = checkTotal;
	}
    
}