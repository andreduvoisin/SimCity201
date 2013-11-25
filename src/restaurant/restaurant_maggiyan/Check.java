package restaurant.restaurant_maggiyan;

import restaurant.restaurant_maggiyan.interfaces.MaggiyanCustomer;
import restaurant.restaurant_maggiyan.interfaces.MaggiyanWaiter;

public class Check{
    
	public Menu menu = new Menu(); 
	public MaggiyanCustomer customer;
	public MaggiyanWaiter waiter; 
	public String choice;
	public double checkTotal;
	public boolean paid;
    
	public Check(MaggiyanWaiter w, MaggiyanCustomer c, String fc){
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