package market;

public class MarketInvoice {
	public MarketOrder mOrder;
	public final int mTotal;
	public int mPayment;
	public int mMarketBankNumber;
	public boolean mPaid; //signed by the cashier ANGELICA: SHANE: do we need this anymore? since we do all the payments through the bank, cashier never verifies it
	
	public MarketInvoice(MarketOrder order, int cost, int number){
		mOrder = order;
		mTotal = cost;
		mMarketBankNumber = number;
	}
}
