package base;

import java.util.Map;

public class Item {
	public static enum EnumMarketItemType {CHICKEN, PIZZA, SALAD, STEAK};
	public static Map<EnumMarketItemType, Integer> sPrices;
	//REX: 2 make static map for prices
	
	EnumMarketItemType mItemType;
	
	public Item(EnumMarketItemType itemType){
		mItemType = itemType;
	}
}
