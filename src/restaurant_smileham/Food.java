package restaurant_smileham;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Food {
	//Used in Order, primarily by Cook
	
	//Constants
	public static final int cCOOKTIME_STEAK = 4;
	public static final int cCOOKTIME_CHICKEN = 2;
	public static final int cCOOKTIME_SALAD = 1;
	public static final int cCOOKTIME_PIZZA = 2;
	public static final Map<EnumFoodOptions, Integer> cCOOKTIMES;
    static {
        Map<EnumFoodOptions, Integer> map = new HashMap<EnumFoodOptions, Integer>();
        map.put(EnumFoodOptions.STEAK, cCOOKTIME_STEAK);
        map.put(EnumFoodOptions.CHICKEN, cCOOKTIME_CHICKEN);
        map.put(EnumFoodOptions.SALAD, cCOOKTIME_SALAD);
        map.put(EnumFoodOptions.PIZZA, cCOOKTIME_PIZZA);
        cCOOKTIMES = Collections.unmodifiableMap(map);
    }
    
	public static int sQuantitySteak = 0;
	public static int sQuantityChicken = 0;
	public static int sQuantitySalad = 0;
	public static int sQuantityPizza = 1;
	public static final int cTHRESHOLD = 3; //for all foods
	public static final int cCAPACITY = 10; //for all foods
	
	public enum EnumFoodOptions {STEAK, CHICKEN, SALAD, PIZZA};
	
	//Member Variables
	public EnumFoodOptions mChoice;
	public int mTime;
	public int mQuantity;
	public int mThreshold;
	public int mCapacity;
	
	
	public Food(EnumFoodOptions choice){
		mChoice = choice;
	}
	
	public Food(EnumFoodOptions choice, int time, int quantity, int threshold, int capacity){
		mChoice = choice;
		mTime = time;
		mQuantity = quantity;
		mThreshold = threshold;
		mCapacity = capacity;
		
		
	}
	
	public String toString(){
		return mChoice.name();
	}
}
