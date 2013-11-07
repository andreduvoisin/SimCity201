package restaurant_smileham;

import restaurant_smileham.interfaces.Customer;
import restaurant_smileham.interfaces.Waiter;
import restaurant_smileham.agent.Check;

public class Order {
	public Waiter mWaiter;
	public Table mTable;
	public Customer mCustomer;
	public Food mFood;
	public int mCash;
	public int mChange;
	public Check mCheck;
	public enum EnumOrderStatus {WAITING, ATTABLE, ORDERING, ORDERED, PENDING, COOKING, READY, DELIVERING, DELIVERED, GETTINGCHECK, GOTCHECK, DELIVEREDCHECK, PAID, DONE};
	public EnumOrderStatus mOrderStatus; 
	
	public Order(Waiter waiter, Table table, Customer customer, EnumOrderStatus status){
		mWaiter = waiter;
		mTable = table;
		mCustomer = customer;
		mFood = null; //hasn't been decided yet
		mOrderStatus = status;
		mCash = customer.getCash();
		mChange = 0;
		mCheck = null;
	}
}
