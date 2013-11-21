package market.roles;

import java.util.HashMap;
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

	int mMarketToOrderFrom = 0; //SHANE: 4 use for market switching % Market.getNumMarkets
	
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


	void msgHereIsCustomerOrder(Order order){
		
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
		if (mPerson.getItemsDesired().size() > 0){
			formOrder();
		}

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
		//Deep copy items desired...
		Map<EnumMarketItemType, Integer> desired = mPerson.getItemsDesired();
		mPerson.setItemsDesired(new HashMap<EnumMarketItemType, Integer>()); //clear desired items
			//ANGELICA: Hey does this work as a deep copy for clearing the item and still using it in desired?
		
		Order order = new Order(desired, this);
		mOrders.add(order);
	}

	private void placeOrder(Order order){
		mCashier.msgOrderPlacement(order);
	}

	private void payForOrder(Order order){
		Invoice invoice = getInvoice(order);
		if (invoice == null){
			//ANGELICA: throw error? or does this work?
			removeOrder(order);
			return;
		}

		if (invoice.mTotal > mPerson.getCash()){
			//ANGELICA: throw error?
		}
		

		
		mPerson.setCash(mPerson.getCash() - invoice.mTotal);
		invoice.mPayment += invoice.mTotal;

		//SHANE: 1 Pay by bank transfer?
		//REX: How do you do this?^^
		mCashier.msgPayingForOrder(invoice);
	}

	private void removeOrder(Order order){
		//remove from mOrders and mInvoices
		mOrders.remove(order);
		Invoice invoice = getInvoice(order);
		mInvoices.remove(invoice);
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
