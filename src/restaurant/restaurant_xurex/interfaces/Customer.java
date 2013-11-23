package restaurant.restaurant_xurex.interfaces;

import java.util.Map;

import restaurant.restaurant_xurex.interfaces.Waiter;


public interface Customer {
	
		// HOST MESSAGES //
		public abstract void FullRestaurant();
		
		// WAITER MESSAGES //
		public abstract void FollowMe(Waiter waiter, Map<String, Integer> menu, int table);
		
		public abstract void WhatWouldYouLike();
		
		public abstract void PleaseReorder(String choice);
		
		public abstract void HereIsFoodAndBill(float bill);
		
		// CASHIER MESSAGES //
		public abstract void HereIsChange(float change);
		
		// ANIMATION MESSAGES //
		public abstract void gotHungry();

		// UTILITIES //
		public abstract String getName();

		public abstract String getChoice();
		
		public abstract void SetChoice(String choice);

}