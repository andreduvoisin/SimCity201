package restaurant_davidmca.interfaces;

import java.util.List;

import restaurant_davidmca.Check;

public interface Cashier {

	public abstract String getName();

	public abstract void msgPayment(Check chk, double payment);

	public abstract void msgDebtPayment(double amt);

	public abstract void msgComputeBill(Waiter w, Customer c,
			String choice);
	
	public abstract List<Check> getChecks();

	public abstract void msgHereIsInvoice(Market marketAgent, double total);

}