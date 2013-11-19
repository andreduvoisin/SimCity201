package market.roles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.Invoice;
import market.Item;
import market.Order;
import market.Item.EnumMarketItemType;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;
import market.interfaces.Cashier;
import market.interfaces.Cook;
import market.interfaces.Customer;
import market.interfaces.Worker;
import base.Role;

public class MarketCashierRole extends Role implements Cashier{
	int mNumWorkers = 0;
	
//	Data
	Map<EnumMarketItemType, Integer> mInventory;
	List<Worker> mWorkers;
	static int mWorkerIndex;
	int mCash;

	List<Order> mOrders;
	List<Invoice> mInvoices;
	
//	Messages
	public void msgOrderPlacement(Order order){
		mOrders.add(order);
		order.mEvent = EnumOrderEvent.ORDER_PLACED;
		stateChanged();
	}

	public void msgPayingForOrder(Invoice invoice){
		if (invoice.mTotal == invoice.mPayment){
			invoice.mOrder.mEvent = EnumOrderEvent.ORDER_PAID;
		}
		else{
//			throw error?
		}
		stateChanged();
	}
	
//	Scheduler
	public boolean pickAndExecuteAnAction(){
		//notify customer if an order has been placed
		if (mOrders.size() > 0){
			for (Order iOrder : mOrders){
				if ((iOrder.mStatus == EnumOrderStatus.PLACED) && (iOrder.mEvent == EnumOrderEvent.ORDER_PLACED)){
					iOrder.mStatus = EnumOrderStatus.PAYING;
					notifyPerson(iOrder);
					return true;
				}

				if ((iOrder.mStatus == EnumOrderStatus.PAID) && (iOrder.mEvent == EnumOrderEvent.ORDER_PAID)){
					iOrder.mStatus = EnumOrderStatus.ORDERING;
					fulfillOrder(iOrder);
					return true;
				}
			}
		}	
		return false;
	}
	
//	Actions
	private void notifyPerson(Order order){
		Map<EnumMarketItemType, Integer> canFulfill = new HashMap<Item.EnumMarketItemType, Integer>();
		int cost = 0;

		//set cost
		for (EnumMarketItemType iItemType : order.mItems.keySet()){
			int amountCanFulfill = Math.max(order.mItems.get(iItemType), mInventory.get(iItemType));
			canFulfill.put(iItemType, amountCanFulfill);
//			cost += cost of item * amountCanFulfill;
		}

		Role personRole = order.mPersonRole;
		Invoice invoice = new Invoice(order, cost);

		//if a cook
		if (personRole instanceof Cook){
			Cook cook = (Cook) order.mPersonRole;
			cook.msgInvoiceToPerson(canFulfill, invoice);
		}

		//if a customer
		else if (personRole instanceof Customer){
			Customer customer = (Customer) order.mPersonRole;
			customer.msgInvoiceToPerson(canFulfill, invoice);
		}
	}

	void fulfillOrder(Order order){
		order.mWorker = mWorkers.get(mWorkerIndex++ % mNumWorkers); //TODO: here or in worker?
		order.mWorker.msgFulfillOrder(order);
	}
	
	
	//ACCESSORS
	public int getNumWorkers(){
		return mNumWorkers;
	}

	
}
