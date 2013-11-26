package market;

import java.util.Map;

import base.Item.EnumItemType;
import base.interfaces.Role;
import market.interfaces.MarketCashier;
import market.interfaces.MarketDeliveryTruck;
import market.interfaces.MarketWorker;

public class MarketOrder {
	public static enum EnumOrderStatus {CARTED, PLACED, PAYING, PAID, ORDERING, DELIVERING, BEING_DELIVERED, FULFILLING, DONE};
	public EnumOrderStatus mStatus;
	public static enum EnumOrderEvent {ORDER_PLACED, RECEIVED_INVOICE, ORDER_PAID, TOLD_TO_FULFILL, TOLD_TO_SEND, TOLD_TO_DELIVER, READY_TO_DELIVER, RECEIVED_ORDER, NONE};
	public EnumOrderEvent mEvent;
	public Map<EnumItemType, Integer> mItems;
	public Role mPersonRole;
	public MarketWorker mWorker;
	public MarketCashier mCashier;
	public MarketDeliveryTruck mDeliveryTruck;
	public int mRestaurantNumber;
	
	public MarketOrder(Map<EnumItemType, Integer> items, Role person) {
		mItems = items;
		mPersonRole = person;
		mStatus = EnumOrderStatus.CARTED;
		mEvent = EnumOrderEvent.NONE;
	}
	
	public void setRestaurantNumber(int n) {
		mRestaurantNumber = n;
	}
}
