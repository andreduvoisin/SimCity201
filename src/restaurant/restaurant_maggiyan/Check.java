package restaurant_maggiyan;

import restaurant_maggiyan.interfaces.Customer;
import restaurant_maggiyan.interfaces.Waiter;

public class Check{
    
    Menu menu = new Menu(); 
    Customer customer;
    Waiter waiter; 
    String choice;
    private double checkTotal;
    boolean paid;
    
    Check(Waiter w, Customer c, String fc){
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