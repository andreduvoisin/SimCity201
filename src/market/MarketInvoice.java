package market;

public class MarketInvoice {
	public MarketOrder mOrder;
	public final int mTotal;
	public int mPayment;
	public int mMarketBankNumber;
	public boolean mPaid; //signed by the cashier
	
	public MarketInvoice(MarketOrder order, int cost, int number){
		mOrder = order;
		mTotal = cost;
		mMarketBankNumber = number;
	}
}
