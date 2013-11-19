package market.interfaces;

<<<<<<< HEAD
import market.other.*;
=======
import market.Order;
import restaurant_smileham.test.mock.EventLog;
>>>>>>> d65a9f4f8f4e36d25ab48e460bae9382495de18b

/**
 * Worker interface for SimCity Markets.
 * 
 * @author Angelica Huyen Tran
 */

public interface Worker {
	
	public abstract void msgFulfillOrder(Order o);
	
	public abstract void msgOrderFulfilled(Order o);
}
