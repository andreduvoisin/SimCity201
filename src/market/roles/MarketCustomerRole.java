package market.roles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.interfaces.Cashier;
import market.interfaces.Cook;
import market.interfaces.Customer;
import market.interfaces.Worker;
import market.other.Invoice;
import market.other.Item;
import market.other.Item.EnumMarketItemType;
import market.other.Order;
import market.other.Order.EnumOrderEvent;
import market.other.Order.EnumOrderStatus;
import base.Role;

public class MarketCustomerRole extends Role implements Customer{

	//DATA
	
	//mCash inherited from Person
	Map<Item, Integer> mItemInventory; //personal inventory
	Map<Item, Integer> mItemsDesired; //not ordered yet

	List<Order> mOrders;
	List<Invoice> mInvoices;

	Cashier mCashier;

	int mMarketToOrderFrom = 0; //TODO: use for market switching % Market.getNumMarkets
	
	//MESSAGES
	
//	void msgInvoiceToPerson(Map<Item, Integer> cannotFulfill, Invoice invoice){
//		mInvoices.add(invoice);
//
//		//not being fulfilled
//		for (iItem : cannotFulfill){
//			add int to mItemsDesired
//		}
//
//		invoice.mOrder.mEvent = EnumOrderEvent.RECEIVED_INVOICE;
//		stateChanged();
//	}


	void msgHereIsCustomerOrder(Order order){

//		for ecah item in order{
//			add to mItems
//		}

		order.mEvent = EnumOrderEvent.RECEIVED_ORDER;
		stateChanged();
	}
	
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction(){

		//form order

		for (Order iOrder : mOrders){
			if ((iOrder.mStatus == EnumOrderStatus.CARTED) && (true)){
				iOrder.mStatus = EnumOrderStatus.PLACED;
				placeOrder(iOrder);
				return true;
			}
			if ((iOrder.mStatus == EnumOrderStatus.PAYING) && (iOrder.mEvent == EnumOrderEvent.RECEIVED_INVOICE)){
				iOrder.mStatus = EnumOrderStatus.PAID;
				payForOrder(iOrder);
				return true;
			}

			if ((iOrder.mStatus == EnumOrderStatus.FULFILLING) && (iOrder.mEvent == EnumOrderEvent.RECEIVED_ORDER)){
				iOrder.mStatus = EnumOrderStatus.DONE;
				removeOrder(iOrder);
				return true;
			}
		}

		return false;
	}
	
	
	//ACTIONS
	private void formOrder(){
		//TODO: form order
	}

	private void placeOrder(Order order){
		mCashier.msgOrderPlacement(order);
	}

	private void payForOrder(Order order){
		Invoice invoice;
		for (Invoice iInvoice : mInvoices){
			if (iInvoice.mOrder == order) invoice = iInvoice;
			break;
		}
		if (invoice == null){
//			throw error
		}

		if (invoice.mTotal > mCash){
//			throw error?
		}

		mCash -= invoice.mTotal;
		invoice.mPayment += invoice.mTotal;

		mCashier.msgPayingForOrder(invoice);
	}

	private void removeOrder(Order order){
		remove from mOrders and mInvoices
	}

}
