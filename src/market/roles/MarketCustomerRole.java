package market.roles;

import java.util.List;
import java.util.Map;

import market.Invoice;
import market.Order;
import market.Order.EnumOrderEvent;
import market.Order.EnumOrderStatus;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import base.BaseRole;
import base.Item.EnumMarketItemType;
import base.interfaces.Person;

public class MarketCustomerRole extends BaseRole implements Customer{
	//DATA
	Person mPerson;
		//mCash accessed from Person

	List<Order> mOrders;
	List<Invoice> mInvoices;

	Cashier mCashier;

	int mMarketToOrderFrom = 0; //TODO: use for market switching % Market.getNumMarkets
	
	//MESSAGES
	@Override
	public void msgInvoiceToPerson(Map<EnumMarketItemType, Integer> cannotFulfill, Invoice invoice) {
		mInvoices.add(invoice);
		Map<EnumMarketItemType, Integer> mItemsDesired = mPerson.getItemsDesired();

		//not being fulfilled
		for (EnumMarketItemType iItem : cannotFulfill.keySet()){
			mItemsDesired.put(iItem, mItemsDesired.get(iItem) + cannotFulfill.get(iItem));
		}

		invoice.mOrder.mEvent = EnumOrderEvent.RECEIVED_INVOICE;
		stateChanged();
	}


	public void msgHereIsCustomerOrder(Order order){
		
		Map<EnumMarketItemType, Integer> items = order.mItems;
		Map<EnumMarketItemType, Integer> mItemInventory = mPerson.getItemInventory();
		//for each item in order
		for (EnumMarketItemType iItem : items.keySet()){
			mItemInventory.put(iItem, mItemInventory.get(iItem) + items.get(iItem)); //add to inventory
		}
		
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
		Invoice invoice = getInvoice(order);
		if (invoice == null){
			//throw error?
		}

//		if (invoice.mTotal > mPerson.getCash()){
////			throw error?
//		}
		
		//TODO: 1 Pay by bank transfer?
		
//		mCash -= invoice.mTotal;
		invoice.mPayment += invoice.mTotal;

		mCashier.msgPayingForOrder(invoice);
	}

	private void removeOrder(Order order){
//		remove from mOrders and mInvoices
	}

	
	//ACCESSORS
	private Invoice getInvoice(Order order){
		Invoice invoice = null;
		for (Invoice iInvoice : mInvoices){
			if (iInvoice.mOrder == order) invoice = iInvoice;
			break;
		}
		return invoice;
	}

}
