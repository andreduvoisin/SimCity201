package restaurant.intermediate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import market.MarketInvoice;
import market.MarketOrder;
import market.MarketOrder.EnumOrderStatus;
import market.interfaces.MarketCashier;
import restaurant.intermediate.interfaces.RestaurantBaseInterface;
import restaurant.intermediate.interfaces.RestaurantCashierInterface;
import restaurant.restaurant_davidmca.DavidRestaurant;
import restaurant.restaurant_davidmca.roles.DavidCashierRole;
import restaurant.restaurant_duvoisin.AndreRestaurant;
import restaurant.restaurant_duvoisin.roles.AndreCashierRole;
import restaurant.restaurant_jerryweb.JerrywebCashierRole;
import restaurant.restaurant_jerryweb.JerrywebRestaurant;
import restaurant.restaurant_maggiyan.gui.MaggiyanAnimationPanel;
import restaurant.restaurant_maggiyan.roles.MaggiyanCashierRole;
import restaurant.restaurant_smileham.SmilehamRestaurant;
import restaurant.restaurant_smileham.roles.SmilehamCashierRole;
import restaurant.restaurant_tranac.TranacRestaurant;
import restaurant.restaurant_tranac.roles.TranacCashierRole;
import restaurant.restaurant_xurex.RexCashierRole;
import restaurant.restaurant_xurex.gui.RexAnimationPanel;
import base.BaseRole;
import base.ContactList;
import base.Item.EnumItemType;
import base.Location;
import base.interfaces.Person;
import base.interfaces.Role;
import city.gui.trace.AlertTag;

public class RestaurantCashierRole extends BaseRole implements RestaurantCashierInterface, RestaurantBaseInterface {
	
	static int totalCashiers = 0;
	
	Role subRole = null;
	int mRestaurantID;
	private AlertTag mAlertTag;

	public RestaurantCashierRole(Person person, int restaurantID){
		super(person); 
		this.mRestaurantID = restaurantID;
	}
	
	public void setPerson(Person person) {
		super.mPerson = person;
		switch(mRestaurantID){
			case 0: //andre
				mAlertTag = AlertTag.R0;
				subRole = new AndreCashierRole(super.mPerson, this);
				if(AndreRestaurant.cashier == null) {
					AndreRestaurant.cashier = (AndreCashierRole) subRole;
				} else {
					subRole = AndreRestaurant.cashier;
				}
				break;
//			case 1: //chase
//				mAlertTag = AlertTag.R1;
//				subRole = ((CwagonerRestaurantPanel) SimCityGui.getInstance().citypanel.masterRestaurantList.get(1)).cashier;
//				subRole.setPerson(super.mPerson);
//				break;
			case 2: //jerry
				mAlertTag = AlertTag.R2;
				subRole = new JerrywebCashierRole(super.mPerson, this);
				if(JerrywebRestaurant.cashier == null) {
					JerrywebRestaurant.cashier = (JerrywebCashierRole) subRole;
				}
				else {
					//subRole = MaggiyanRestaurant.cahsier;
				}
				break;
			case 3: //maggi
				mAlertTag = AlertTag.R3;
				subRole = new MaggiyanCashierRole(super.mPerson, this);
				MaggiyanAnimationPanel.addPerson((MaggiyanCashierRole) subRole);
				break;
			case 4: //david
				mAlertTag = AlertTag.R4;
				subRole = new DavidCashierRole(super.mPerson, this);
				if (DavidRestaurant.cashier == null) {
					DavidRestaurant.cashier = (DavidCashierRole) subRole;
				} else {
					subRole = AndreRestaurant.cashier;
				}
				break;
			case 5: //shane
				mAlertTag = AlertTag.R5;
				subRole = new SmilehamCashierRole(super.mPerson, this);
				if (SmilehamRestaurant.mCashier == null) {
					SmilehamRestaurant.addPerson((SmilehamCashierRole) subRole);
				} else {
					subRole = SmilehamRestaurant.mCashier;
				}
				break;
			case 6: //angelica
				mAlertTag = AlertTag.R6;
				subRole = new TranacCashierRole(mPerson,this);
				TranacRestaurant.addPerson((TranacCashierRole)subRole);
				break;
			case 7: //rex
				mAlertTag = AlertTag.R7;
				subRole =  new RexCashierRole(super.mPerson, this);
				RexAnimationPanel.addPerson((RexCashierRole) subRole);
				break;
		}
	}
	
	public boolean pickAndExecuteAnAction() {
		if(marketPickAndExecuteAnAction())
			return true;
		if(subRole != null)
			return subRole.pickAndExecuteAnAction();
		return false;
	}
	
	@Override
	public Location getLocation() {
		return ContactList.cRESTAURANT_LOCATIONS.get(mRestaurantID);
	}


/** Market Data, Messages, Scheduler, and Actions */
/* Data */
	public List<MarketBill> mMarketBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	enum EnumBillStatus {PLACED, PAYING};
	
/* Messages */
	public void msgPlacedMarketOrder(MarketOrder o, MarketCashier c) {
		synchronized(mMarketBills) {
			mMarketBills.add(new MarketBill(o, c));
		}
	}
	
	public void msgInvoiceToPerson(Map<EnumItemType, Integer> cannotFulfill, MarketInvoice invoice) {
		synchronized(mMarketBills) {
		for(MarketBill b : mMarketBills) {
			if(b.mOrder == invoice.mOrder) {
				b.mStatus = EnumBillStatus.PAYING;
				b.mInvoice = invoice;
				b.mCannotFulfill = cannotFulfill;
				stateChanged();
			}
		}
		}
	}

/* Scheduler */
	public boolean marketPickAndExecuteAnAction() {
		synchronized(mMarketBills) {
		for(MarketBill b : mMarketBills) {
			if(b.mStatus == EnumBillStatus.PAYING) {
				b.mOrder.mStatus = EnumOrderStatus.PAID;
				verifyAndPayMarketInvoice(b);
				return true;
			}
		}
		}
		return false;
	}
	
/* Actions */
	public void verifyAndPayMarketInvoice(MarketBill b) {
		print("Verifying and paying market bill.", mAlertTag);
		MarketOrder o = b.mOrder;
		MarketInvoice i = b.mInvoice;
		Map<EnumItemType, Integer> cf = b.mCannotFulfill;
		
		for(EnumItemType type : o.mItems.keySet()) {
			int orderNum = o.mItems.get(type);
			int billNum = i.mOrder.mItems.get(type) + cf.get(type);
			if(orderNum != billNum) {
				//message market that invoice is invalid
				mMarketBills.remove(b);
				return;
			}
		}
		i.mPayment = i.mTotal;
		ContactList.SendPayment(getSSN(), i.mMarketBankNumber, i.mTotal);
		b.mMarketCashier.msgPayingForOrder(i);
		
		synchronized(mMarketBills) {
			mMarketBills.remove(b);
		}
	}
	
/* Classes */
	class MarketBill {
		MarketOrder mOrder;
		MarketInvoice mInvoice;
		MarketCashier mMarketCashier;
		Map<EnumItemType, Integer> mCannotFulfill;
		EnumBillStatus mStatus;
		
		MarketBill(MarketOrder o, MarketCashier c) {
			mOrder = o;
			mMarketCashier = c;
			mInvoice = null;
			mStatus = EnumBillStatus.PLACED;
		}
	}
}
