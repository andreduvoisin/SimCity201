package restaurant.restaurant_smileham;

import restaurant.restaurant_smileham.agent.Check;
import restaurant.restaurant_smileham.interfaces.SmilehamCustomer;
import restaurant.restaurant_smileham.interfaces.SmilehamWaiter;

public class Order {
	public SmilehamWaiter mWaiter;
	public Table mTable;
	public SmilehamCustomer mCustomer;
	public Food mFood;
	public int mCash;
	public int mChange;
	public Check mCheck;
	public enum EnumOrderStatus {WAITING, ATTABLE, ORDERING, ORDERED, PENDING, COOKING, READY, DELIVERING, DELIVERED, GETTINGCHECK, GOTCHECK, DELIVEREDCHECK, PAID, DONE};
	public EnumOrderStatus mOrderStatus; 
	
	public Order(SmilehamWaiter waiter, Table table, SmilehamCustomer customer, EnumOrderStatus status){
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
