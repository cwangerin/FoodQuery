package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class with static methods to help with food operations
 * Methods include calculating nutrients for a list of food items
 * and intersecting lists, a fundamental operation for this program
 *
 */
public class FoodListOperations {

	/**
	 * calcuates the mealSummary for a given foodItemList
	 * @return [calories, fat, carbohydrate, fiber, protein]
	 */
	public static Double[] calculateNutrients(List<FoodItem> foodItemList) {
		//Initialize values to zero
		
		Double totalCalories = new Double(0);
		Double totalFat = new Double(0);
		Double totalCarbohydrate = new Double(0);
		Double totalFiber = new Double(0);
		Double totalProtein = new Double(0);

		//loop through the list and compute the sum
		for(FoodItem food : foodItemList) {
			totalCalories += food.getNutrientValue("calories");
			totalFat += food.getNutrientValue("fat");
			totalCarbohydrate += food.getNutrientValue("carbohydrate");
			totalFiber += food.getNutrientValue("fiber");
			totalProtein += food.getNutrientValue("protein");
		}
		
		Double[] totals = {totalCalories, totalFat, totalCarbohydrate, totalFiber, totalProtein};
		
		//return the array of totals
		return totals;
	}
	
	/**
	 * Method that takes in a list of lists of food items to intersect
	 * @param foodLists
	 * @return the list of items that are present in all of the lists
	 */
	public static List<FoodItem> intersectLists(List<List<FoodItem>> foodLists) {
		
		Iterator<List<FoodItem>> iterator = foodLists.iterator();
		List<FoodItem> intersectedList; // list that will keep track of the intersection
		if(iterator.hasNext()) {
			intersectedList = new ArrayList<FoodItem>(iterator.next());
			//need to copy list to avoid changing what was passed in
		
			while(iterator.hasNext()) {
				List<FoodItem> curList = iterator.next();
				intersectedList.retainAll(curList);
				//retain all performs the intersection
			}
			return intersectedList;
		}
		
		//return blank array list if nothing to intersect
		return new ArrayList<FoodItem>();
	}
	
}
