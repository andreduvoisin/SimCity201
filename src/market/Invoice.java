package market;

public class Invoice {
	public Order mOrder;
	public final int mTotal;
	public int mPayment;
	public boolean mPaid; //signed by the cashier
	
	public Invoice(Order order, int cost){
		mOrder = order;
		mTotal = cost;
	}
}
