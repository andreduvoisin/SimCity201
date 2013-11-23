package market;

public class MarketInvoice {
	public MarketOrder mOrder;
	public final int mTotal;
	public int mPayment;
	public boolean mPaid; //signed by the cashier
	
	public MarketInvoice(MarketOrder order, int cost){
		mOrder = order;
		mTotal = cost;
	}
}
