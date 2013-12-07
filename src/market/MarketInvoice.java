package market;

public class MarketInvoice {
	public MarketOrder mOrder;
	public final int mTotal;
	public int mPayment;
	public int mMarketBankNumber;
	
	public MarketInvoice(MarketOrder order, int cost, int number){
		mOrder = order;
		mTotal = cost;
		mMarketBankNumber = number;
	}
}
