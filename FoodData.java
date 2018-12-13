package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {
    
    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    
    /**
     * Public constructor
     * Initialize the foodItemList as well as the hashMap of strings
     * and BP Trees
     */
    public FoodData() {
    	foodItemList = new ArrayList<FoodItem>();
    	indexes = new HashMap<String, BPTree<Double, FoodItem>>();
    	indexes.put("calories", new BPTree<Double,FoodItem>(3));
    	indexes.put("fat", new BPTree<Double,FoodItem>(3));
    	indexes.put("carbohydrate", new BPTree<Double,FoodItem>(3));
    	indexes.put("fiber", new BPTree<Double,FoodItem>(3));
    	indexes.put("protein", new BPTree<Double,FoodItem>(3));
    }
    
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
    	
    	File inputFile = new File(filePath);
    	try {
	    	Scanner in = new Scanner(inputFile);
	    	
	    	while(in.hasNextLine()) {
	    		String line = in.nextLine();
	    		String[] values = line.split(",");
	    		
	    		if(values.length == 12) {
	    			try {
		    			String id = values[0];
		    			String name = values[1];
		    			String calories = values[2];
		    			Double calorieCount = Double.parseDouble(values[3]);
		    			String fat = values[4];
		    			Double fatCount = Double.parseDouble(values[5]);
		    			String carbohydrates = values[6];
		    			Double carbohydratesCount = Double.parseDouble(values[7]);
		    			String fiber =  values[8];
		    			Double fiberCount = Double.parseDouble(values[9]);
		    			String protein = values[10];
		    			Double proteinCount = Double.parseDouble(values[11]);
	    			
		    			if(!(calories.equalsIgnoreCase("calories") && fat.equalsIgnoreCase("fat") && carbohydrates.equalsIgnoreCase("carbohydrate")
		    					&& fiber.equalsIgnoreCase("fiber") && protein.equalsIgnoreCase("protein"))) {
		    			}
		    			else {
		    				//construct new foodItem
		    				
		    				FoodItem newItem = new FoodItem(id, name);
		    				newItem.addNutrient(calories, calorieCount);
		    				newItem.addNutrient(fat, fatCount);
		    				newItem.addNutrient(carbohydrates, carbohydratesCount);
		    				newItem.addNutrient(fiber, fiberCount);
		    				newItem.addNutrient(protein, proteinCount);
		    				
		    				addFoodItem(newItem);
		    				
		    			}
		    				
		    			
	    			}catch(NumberFormatException e) {
	    				System.err.println(e.getMessage());
	    			}
	    		}
	    	}
	    	in.close();
    	
    	}catch(Exception e) {
    		System.err.println(e.getMessage());
    	}
    	
    	//maintain sort
    	Collections.sort(foodItemList, (a,b) -> a.getName().toLowerCase().compareTo(b.getName().toLowerCase()));
    }
    
    /**
     * Adds the foodItem to each of the BPTrees associated with each
     * nutrient value in the hashMap
     * @param newItem
     */
    private void addFoodToHashMap(FoodItem newItem) {
    	//Get the map of strings representing nutrients
    	Map<String, Double> nutrientMap = newItem.getNutrients();
    	
    	//for each nutrient string, we want to insert the item
    	//into the BPTree
    	for(String s : nutrientMap.keySet()) {
    		indexes.get(s).insert(nutrientMap.get(s), newItem);;
    	}
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
    	
    	//We will store the filtered food here
    	List<FoodItem> filteredList = new ArrayList<FoodItem>();
    	
    	//for each foodItem, see if it matches the substring in a case-insensitive manner
    	for(FoodItem food : foodItemList) {
    		if(food.getName().toLowerCase().contains(substring.toLowerCase())) {
    			filteredList.add(food);
    		}
    	}
    	
    	//return the list in sorted form
        Collections.sort(filteredList, (a, b) -> a.getName().compareTo(b.getName()));
        return filteredList;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
    	
    	List<List<FoodItem>> filteredList = new ArrayList<List<FoodItem>>();
    	//A list of the lists that will need to be interesected
    	for(String rule : rules) {
    		String[] ruleArray = rule.split(" ");
    		String nutrient = ruleArray[0];
    		String comparator = ruleArray[1];
    		Double value = Double.parseDouble(ruleArray[2]);
    		
    		//apply the range search on each query
    		List<FoodItem> filteredFood = indexes.get(nutrient).rangeSearch(value, comparator);
			//add the result of the query to our overall list
    		//we want to intersect
    		filteredList.add(filteredFood);
    	}
    	
    	//call the static helper class for intersecting lists
    	List<FoodItem> resultList = FoodListOperations.intersectLists(filteredList);
    	
    	return resultList;
    			
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
    	foodItemList.add(foodItem);
    	addFoodToHashMap(foodItem);
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }
    
    /**
     * Saves the food present in this FoodData instance to the
     * specified file in a comma-separated manner
     * @param filename
     */
    public void saveFoodItems(String filename) {
    	File saveFile = new File(filename);
    	
    	//start with sorted food items
    	Collections.sort(foodItemList, (a,b) -> a.getName().toLowerCase().compareTo(b.getName().toLowerCase()));
    	
    	//save to the file
    	try {
    		FileWriter f = new FileWriter(saveFile);
    		BufferedWriter b = new BufferedWriter(f);
    		for(FoodItem food : foodItemList) {
    			Double calories = food.getNutrientValue("calories");
    			Double fat = food.getNutrientValue("fat");
    			Double carbohydrate = food.getNutrientValue("carbohydrate");
    			Double fiber = food.getNutrientValue("fiber");
    			Double protein = food.getNutrientValue("protein");
    			
    			String output = food.getID() + "," + food.getName() + "," + "calories" + ","
    			+ calories + "," + "fat" + "," + fat + "," + "carbohydrate" + "," + carbohydrate + ","
    			+ "fiber" + "," + fiber + "," + "protein" + "," + protein;
    			
    			b.write(output);
    			b.newLine();
    		}
    		b.close();
    		f.close();
    	}catch(Exception e) {
    		System.err.println(e.getMessage());
    	}
    	
    	
    }
    

}
