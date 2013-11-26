package base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import restaurant.restaurant_smileham.Food.EnumFoodOptions;

public class Item {
	
	public static final double cPRICE_STEAK = 6;
	public static final double cPRICE_CHICKEN = 4;
	public static final double cPRICE_SALAD = 2;
	public static final double cPRICE_PIZZA = 5;
	public static final double cPRICE_CAR = 200;
	public static enum EnumItemType {CHICKEN, PIZZA, SALAD, STEAK, CAR};
	
	public static Map<EnumItemType, Double> cMARKET_PRICES;
    static {
        Map<EnumItemType, Double> map = new HashMap<EnumItemType, Double>();
        map.put(EnumItemType.STEAK, cPRICE_STEAK);
        map.put(EnumItemType.CHICKEN, cPRICE_CHICKEN);
        map.put(EnumItemType.SALAD, cPRICE_SALAD);
        map.put(EnumItemType.PIZZA, cPRICE_PIZZA);
        map.put(EnumItemType.CAR, cPRICE_CAR);
        cMARKET_PRICES = Collections.unmodifiableMap(map);
    }
    
	EnumItemType mItemType;
	
	public Item(EnumItemType itemType){
		mItemType = itemType;
	}
	
    public static EnumItemType stringToEnum(String t) {
        for(EnumItemType iType : EnumItemType.values()) {
                if(iType.toString().equalsIgnoreCase(t))
                        return iType;
        }
        return null;
    }

    public static String enumToString(EnumItemType i) {
        return i.toString();
    }
    
    public static EnumItemType enumToEnum(EnumFoodOptions i) {
    	for(EnumItemType iType : EnumItemType.values()) {
    		if(iType.toString().equalsIgnoreCase(i.toString()))
    			return iType;
    	}
    	return null;
    }
}
