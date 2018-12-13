package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Main class that runs the FoodQuery GUI program
 * 
 *
 */
public class Main extends Application {
	ObservableList<FoodItem> foodObservableList; //observable list for food
	ListView<FoodItem> foodListView; //list view for food
	
	List<FoodItem> filteredByNutrientList; //list to hold filteredByNutrient items
	List<FoodItem> filteredByNameList; //list to hold filteredByName items
	
	//initialize mealObservableList
	ObservableList<FoodItem> mealObservableList = FXCollections.observableArrayList();
	
	FoodData foodData; // field for food data
	List<String> rulesList = new ArrayList<String>(); //hold our query rules
	
	ListView<FoodItem> mealListView; //field for the mealListView
	TextField foodInput,calorieInput, nameFilter; //textFields
	
	MenuBar dropMenu; //drop menu to help with fileds
	VBox vBoxRight; //shows up on right side of our screen
	Label foodCountLabel; //counts total food in list
	Stage rulesStage; //stage to show our rules
	Stage addFoodWindow; //stage to add the food
	String nameQuery; //name query the user enters
	Button sendToMeal; //sendToMeal button
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Food Query");
			BorderPane root = new BorderPane();
			VBox vBoxLeft = new VBox();
			HBox hbox2 = new HBox();
			Scene scene = new Scene(root,1000,720);
			
			//Set how big we want the listView to be
			foodListView = new ListView<>();
			foodListView.setPrefHeight(700);
			foodListView.setPrefWidth(350);
			
			foodInput = new TextField();
			nameFilter = new TextField();
			
			// Right Vertical Box - Meal List
			vBoxRight = new VBox();

			Label mealListLabel = new Label("Meal List");
			mealListLabel.setStyle("-fx-font: 24 segoeui;");
			
			//Set our preferences on the mealListView
			mealListView = new ListView<>();
			mealListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			mealListView.setPrefHeight(700);
			mealListView.setPrefWidth(350);
			mealListView.setItems(mealObservableList.sorted());
			
			Button delMealItemButton = new Button("Delete Food");
			Button calculateSummaryButton = new Button("Calculate Summary");
			
			//when we delete a meal item, we want to get all the selected
			//items from the meal list and remove them from our observable list
			delMealItemButton.setOnAction(e -> {
				ObservableList<FoodItem> delItems = mealListView.getSelectionModel().getSelectedItems();
				Object[] foodArray = delItems.toArray();
				
				for(Object food : foodArray) {
					mealObservableList.remove(food);
				}
				
			});
			
			Label mealSummaryLabel = new Label("Meal Summary");
			mealSummaryLabel.setStyle("-fx-font: 24 segoeui");
			
			Label calorieSummaryLabel = new Label();
			Label fatSummaryLabel = new Label();
			Label carbSummaryLabel = new Label();
			Label fiberSummaryLabel = new Label();
			Label proteinSummaryLabel = new Label();
			
			Label totalCalorieLabel = new Label("Total Calories: ");
			Label totalFatLabel = new Label("Total Fat: ");
			Label totalCarbLabel = new Label("Total Carbs: ");
			Label totalFiberLabel = new Label("Total Fiber: ");
			Label totalProteinLabel = new Label("Total Protein: ");
			
			//Calculate the summary information of the mealList
			calculateSummaryButton.setOnAction(e -> {
				List<FoodItem> calculateList = mealObservableList;
				
				Double[] results = FoodListOperations.calculateNutrients(calculateList);
				
				calorieSummaryLabel.setText(Double.toString(results[0]));
				fatSummaryLabel.setText(Double.toString(results[1]));
				carbSummaryLabel.setText(Double.toString(results[2]));
				fiberSummaryLabel.setText(Double.toString(results[3]));
				proteinSummaryLabel.setText(Double.toString(results[4]));
				
				
			});
			
		
			
			HBox calorieHBox = new HBox();
			HBox fatHBox = new HBox();
			HBox carbHBox = new HBox();
			HBox fiberHBox = new HBox();
			HBox proteinHBox = new HBox();
			
			//Set our nutrition summary hboxes and labels
			calorieHBox.getChildren().add(totalCalorieLabel);
			calorieHBox.getChildren().add(calorieSummaryLabel);
			fatHBox.getChildren().add(totalFatLabel);
			fatHBox.getChildren().add(fatSummaryLabel);
			carbHBox.getChildren().add(totalCarbLabel);
			carbHBox.getChildren().add(carbSummaryLabel);
			fiberHBox.getChildren().add(totalFiberLabel);
			fiberHBox.getChildren().add(fiberSummaryLabel);
			proteinHBox.getChildren().add(totalProteinLabel);
			proteinHBox.getChildren().add(proteinSummaryLabel);
			
			//Set our right vbox for the mealList Nodes
			vBoxRight.getChildren().add(mealListLabel);
			vBoxRight.getChildren().add(mealListView);
			vBoxRight.getChildren().add(delMealItemButton);
			vBoxRight.getChildren().add(mealSummaryLabel);
			vBoxRight.getChildren().add(calorieHBox);
			vBoxRight.getChildren().add(fatHBox);
			vBoxRight.getChildren().add(carbHBox);
			vBoxRight.getChildren().add(fiberHBox);
			vBoxRight.getChildren().add(proteinHBox);
			vBoxRight.getChildren().add(calculateSummaryButton);
			
			// Drop down menu - MenuBar
			FileChooser fileChooser = new FileChooser();
			dropMenu = new MenuBar();
			Menu menuFile = new Menu("File");
			Menu menuHelp = new Menu("Help");
			MenuItem menuFoodList = new MenuItem("Open FoodList File...");
			MenuItem menuSaveList = new MenuItem("Save FoodList...");
			MenuItem menuHelpItem = new MenuItem("Show Instructions");
			
			menuFile.getItems().add(menuFoodList);
			menuFile.getItems().add(menuSaveList);
			menuHelp.getItems().add(menuHelpItem);
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV File", "*.csv");
			FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("TXT File", "*.txt");
			fileChooser.getExtensionFilters().addAll(extFilter,extFilter2);
			
			menuSaveList.setOnAction(e ->  {
				File saveFile = fileChooser.showSaveDialog(primaryStage);
				
				if(saveFile == null) {
					//If they didn't pick a file, don't proceed!
					return;
				}
				
				if(foodObservableList != null) {
					
					//make a new foodData class so that we can
					//save exclusively what is in the observableList
					//instead of all of the food that has been loaded ever
					FoodData savedData = new FoodData();
					
					for(FoodItem food : foodObservableList) {
						savedData.addFoodItem(food);
					}
					
					savedData.saveFoodItems(saveFile.getAbsolutePath());
				}
				else {
					//if we have no food, we can't save
					displayErrorMessage("There is no food to save!");
				}
			});
			
			/**
			 * Action event for loading a file
			 * 
			 */
			menuFoodList.setOnAction(e -> {
			    File selectedFile = fileChooser.showOpenDialog(primaryStage);
				if(selectedFile != null) {
				    String filePath = selectedFile.getAbsolutePath();
					
				    //make a new food data and load in items
					foodData = new FoodData();
					foodData.loadFoodItems(filePath);
					//set the observable list
					foodObservableList = FXCollections.observableArrayList(foodData.getAllFoodItems());
					
					//filtered lists should be the whole list, nothing to filter right away
					filteredByNutrientList = foodData.getAllFoodItems();
					filteredByNameList = foodData.getAllFoodItems();
					//want our food list view to be sorted
					foodListView.setItems(foodObservableList.sorted());
					//bind our food count label to the amount of items in the listView
					foodCountLabel.textProperty().bind((Bindings.size(foodObservableList).asString()));
					//clear out all rules
					rulesList.clear();
					nameQuery = null;
					sendToMeal.disableProperty().bind(Bindings.size(foodObservableList).isEqualTo(0));
				}
			});
			
			/**
			 * Event for when the user wants to see instructions
			 */
			menuHelpItem.setOnAction(e -> {
				Stage instructionStage = new Stage();
				BorderPane instructionPane = new BorderPane();
				Label instructionLabel = new Label();
				String instructions = "Load food items from a file by selecting that file from the browsing menu."
						+ " Add food items by clicking on the Add New Food button. You can filter the food items by entering "
						+ "a letter/phrase to search for. You can also filter food items by nutritional content. Enter a rule"
						+ " by entering three things separated by spaces: <nutrient name> <comparator> <value>. The comparators are ="
						+ ",<=, and >=. You can add as many rules as you want by entering each one, then pressing add."
						+ " To clear all queries at once, press the corresponding clear label. To add food items to the meal list, select them and hit send to meal.";
				instructionLabel.setWrapText(true);
				instructionLabel.setText(instructions);
				instructionPane.setTop(instructionLabel);
				Scene instructionScene = new Scene(instructionPane, 350, 350);
				instructionStage.setTitle("Instructions");
				instructionStage.setScene(instructionScene);
				instructionStage.show();
			});
			
			dropMenu.getMenus().addAll(menuFile, menuHelp);
			HBox dropMenuPanel = new HBox(dropMenu);
			
			//We want our selection model to be multiple to allow for ctrl-A
			foodListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			foodListView.setPrefHeight(700);
			foodInput.setPromptText("food");
			nameFilter.setPromptText("Enter name search");
			
			//Create the buttons that display to the right side of
			//the foodListView
			VBox addNewFoodSendToMeal = new VBox();
			Button addFood = new Button("Add New Food");
			sendToMeal = new Button("Send to Meal");
			sendToMeal.setDisable(true);
			Button showRules = new Button("Show Rules");
			Button applyAllQueries = new Button("Apply All Rules");
			Button nameFilterButton = new Button("Apply Name Filter");
			
			applyAllQueries.setDisable(true); //can't do any queries from the beginning
			
			Button removeNameFilterButton = new Button("Remove Name Filter");
			
			addNewFoodSendToMeal.getChildren().addAll(addFood, sendToMeal,showRules,applyAllQueries);
			
			/**
			 * Applies the nutrient queries to the list view
			 * Obtain the list of food that meets all the nutrient queries
			 * This is returned from the BPTree
			 * 
			 */
			applyAllQueries.setOnAction(e -> {
				if(foodData != null && !rulesList.isEmpty()) {
					List<List<FoodItem>> listsToIntersect = new ArrayList<List<FoodItem>>();
					filteredByNutrientList = foodData.filterByNutrients(rulesList);
					
					//intersect the nameList and the nutrientList, we want
					//both queries to apply at the same time
					if(filteredByNameList != null && filteredByNutrientList != null) {
						listsToIntersect.add(filteredByNameList);
						listsToIntersect.add(filteredByNutrientList);
						List<FoodItem> intersection = FoodListOperations.intersectLists(listsToIntersect);
						
						foodObservableList.setAll(intersection);
					}
				}
			});
			
			/**
			 * Filter the foodListView by a text query (name contains)
			 */
			nameFilterButton.setOnAction(e -> {
				String inputText = nameFilter.getText();
				nameQuery = inputText;
				if(foodData != null) {
					filteredByNameList = foodData.filterByName(inputText);
					List<List<FoodItem>> listsToIntersect = new ArrayList<List<FoodItem>>();
					listsToIntersect.add(filteredByNameList);
					listsToIntersect.add(filteredByNutrientList);
					List<FoodItem> intersection = FoodListOperations.intersectLists(listsToIntersect);
					
					foodObservableList.setAll(intersection);
				}
				else {
					//If there is no food data, we can't filter any food!
					displayErrorMessage("There is no food to filter!");
				}
				
			});
			
			/**
			 * When the user wants to remove the name filter, we
			 * change the observable list to focus only on those items filteredByNutrient
			 * we also reset the filteredByName list to be the whole list
			 */
			removeNameFilterButton.setOnAction(e -> {
				nameQuery = null;
				nameFilter.clear(); //clear text since the filter is gone
				if(filteredByNameList != null && foodObservableList != null) {
					filteredByNameList = foodData.getAllFoodItems();
					foodObservableList.setAll(filteredByNutrientList);
				}
			});
			
			sendToMeal.setOnAction((ActionEvent e) -> {
				ObservableList<FoodItem> addItems = foodListView.getSelectionModel().getSelectedItems();
				
				mealObservableList.addAll(addItems);
				
			});
			
			//Bring up a new window with the rulesList
			showRules.setOnAction((ActionEvent e) ->{
				//If the user already has a rulesStage open
				//close the existing one so they can't open infinitely many
				if(rulesStage != null) {
					rulesStage.close();
				}
				rulesStage = new Stage();
				BorderPane rulePane = new BorderPane();
				VBox vbox = new VBox();
				ObservableList<String> observableRules = FXCollections.observableList(rulesList);
				ListView<String> rulesView = new ListView<String>(observableRules);
				rulesView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				rulePane.setTop(vbox);
				Button deleteRule = new Button("Delete Rule");
				vbox.getChildren().addAll(rulesView,deleteRule);
				
				//Delete a rule from the rules listView
				deleteRule.setOnAction(f -> {
					List<String> selectedRules = rulesView.getSelectionModel().getSelectedItems();
					observableRules.removeAll(selectedRules);
					if(observableRules.isEmpty() && foodData != null) {
						filteredByNutrientList = foodData.getAllFoodItems();
						if(nameQuery == null) {
							filteredByNameList = foodData.getAllFoodItems();
						}
						
						foodObservableList.setAll(filteredByNameList);
						applyAllQueries.setDisable(true);
						rulesStage.close();
					}
				});
				
				//Show our rules scene
				Scene ruleScene = new Scene(rulePane, 350, 700);
				rulesStage.setTitle("Rules");
				rulesStage.setScene(ruleScene);
				rulesStage.show();
			});
			
			HBox listViewAddFoodHBox = new HBox();
			listViewAddFoodHBox.getChildren().addAll(foodListView, addNewFoodSendToMeal);
			hbox2.getChildren().addAll(nameFilter,nameFilterButton, removeNameFilterButton);
			addFood.setOnAction((ActionEvent e) -> {
				if(addFoodWindow != null) {
					addFoodWindow.close();
				}
				addFoodWindow = new Stage();
				BorderPane bp2 = new BorderPane();
				VBox vbox2 = new VBox();
				TextField foodName = new TextField();
				TextField foodID = new TextField();
				TextField calorieCount = new TextField();
				TextField fatGrams = new TextField();
				TextField carbGrams = new TextField();
				TextField fiberGrams = new TextField();
				TextField proteinGrams = new TextField();
				Label title = new Label();
				title.setText("Add food item with its nutrients");
				foodID.setPromptText("Enter a unique ID for this food");
				foodName.setPromptText("food name");
				calorieCount.setPromptText("calorie count");
				fatGrams.setPromptText("fat grams");
				carbGrams.setPromptText("carbohydrate grams");
				fiberGrams.setPromptText("fiber grams");
				proteinGrams.setPromptText("protein grams");
				foodName.setFocusTraversable(false);
				calorieCount.setFocusTraversable(false);
				fatGrams.setFocusTraversable(false);
				carbGrams.setFocusTraversable(false);
				fiberGrams.setFocusTraversable(false);
				proteinGrams.setFocusTraversable(false);
			    foodID.setFocusTraversable(false);
				Button submit = new Button("submit");
				submit.setOnAction(r -> {
					 try {
					      FoodItem foodItem = new FoodItem(foodID.getText(),foodName.getText());
					      Double calories = Double.parseDouble(calorieCount.getText());
					      Double fat = Double.parseDouble(fatGrams.getText());
					      Double carbohydrate = Double.parseDouble(carbGrams.getText());
					      Double fiber = Double.parseDouble(fiberGrams.getText());
					      Double protein = Double.parseDouble(proteinGrams.getText());
					      if(calories < 0 || fat < 0 || carbohydrate < 0 || fiber < 0 || protein < 0) {
					    	  displayErrorMessage("Nutrients cannot have negative nutrition values");
					      }
					      else {
						      foodItem.addNutrient("calories", calories);
						      foodItem.addNutrient("fat", fat);
						      foodItem.addNutrient("carbohydrate", carbohydrate);
						      foodItem.addNutrient("fiber", fiber);
						      foodItem.addNutrient("protein", protein);
					      
						      if(foodData == null) {
						    	  foodData = new FoodData();
						    	  							
						      }
						      
						      if(foodObservableList == null) {
						    	  foodObservableList = FXCollections.observableArrayList();
						    	  foodCountLabel.textProperty().bind((Bindings.size(foodObservableList).asString()));
						    	  filteredByNutrientList = new ArrayList<FoodItem>();
								  filteredByNameList = new ArrayList<FoodItem>();
								  filteredByNutrientList.add(foodItem);
								  filteredByNameList.add(foodItem);
								  foodData.addFoodItem(foodItem);
							      foodObservableList.add(foodItem);
								  foodListView.setItems(foodObservableList.sorted());
						      }
						      else {
							      foodData.addFoodItem(foodItem);
							      foodObservableList.add(foodItem);
							      if(!rulesList.isEmpty()) {
							    	  filteredByNutrientList = foodData.filterByNutrients(rulesList);
							      }
							      else {
							    	  filteredByNutrientList = foodData.getAllFoodItems();
							      }
							      if(nameQuery != null) {
							    	  filteredByNameList = foodData.filterByName(nameQuery);
							      }
							      else {
							    	  filteredByNameList = foodData.getAllFoodItems();
							      }
						      }
						      
						      sendToMeal.disableProperty().bind(Bindings.size(foodObservableList).isEqualTo(0));
						      addFoodWindow.close();
					      }
					      
					     }catch(NumberFormatException j){
					    	 displayErrorMessage("You must enter non-negative numeric nutrition values, a name, and a unique id.");
					     }
					 
				});
				vbox2.getChildren().addAll(title,foodID, foodName,calorieCount,fatGrams,
						carbGrams,fiberGrams,proteinGrams,submit);
				bp2.setCenter(vbox2);
				Scene popupScene = new Scene(bp2, 750, 450);
				addFoodWindow.setTitle("Add Food Item");
				addFoodWindow.setScene(popupScene);
				addFoodWindow.show();
			});
			
			
			HBox nutrientQuery = new HBox();
			TextField nutrientQueryText = new TextField();
			nutrientQueryText.setPromptText("<nutrient> <comparator> <value>");
			nutrientQueryText.setPrefWidth(200);
			Button submitNutrientQuery = new Button("Add Rule");
			Button clearNutrientQuery = new Button("Clear All Rules");
			nutrientQuery.getChildren().addAll(nutrientQueryText, submitNutrientQuery, clearNutrientQuery);
			
			submitNutrientQuery.setOnAction(e -> {
				
				if(foodData == null) {
					displayErrorMessage("There is no food to filter!");
					return;
				}
				
				String textInput = nutrientQueryText.getText().toLowerCase();
				nutrientQueryText.clear();
				boolean validInput = true;
				try {
					String[] ruleArray = textInput.split(" ");
					if(ruleArray.length == 3) {
			    		String nutrient = ruleArray[0];
			    		String comparator = ruleArray[1];
			    		Double value = Double.parseDouble(ruleArray[2]);
			    		
			    		if(!(comparator.contentEquals("<=") || comparator.contentEquals("==") ||
			    				comparator.contentEquals(">="))) {
			    			displayErrorMessage("Enter a valid comparator");
			    			validInput = false;
			    		}
			    		else {
			    		
				    		String[] validOptions = {"calories", "fat", "carbohydrate", "fiber", "protein"};
				    		
				    		boolean validString = false;
				    		for(String s : validOptions) {
				    			if(nutrient.equalsIgnoreCase(s)) {
				    				validString = true;
				    				break;
				    			}
				    		}
				    		
				    		if(!validString) {
				    			validInput = false;
				    			displayErrorMessage("You must enter a valid nutrient name");
				    		}
			    		
			    		}
					}
					else {
						validInput = false;
						displayErrorMessage("Incorrect number of arguments. See instructions.");
					}
					
					
				}catch(NumberFormatException f) {
					validInput = false;
					displayErrorMessage("You must enter a number for the nutrient value");
				}
				
				if(validInput) {
					rulesList.add(textInput);
					applyAllQueries.setDisable(false);
				}
			});
			
			/**
			 * when the user wants to clear all nutrient queries, we clear
			 * the rules list and reset the filteredByNutrientList to be the whole food
			 */
			clearNutrientQuery.setOnAction(e -> {
				if(rulesStage != null) {
					rulesStage.close();
				}
				rulesList.clear();
				applyAllQueries.setDisable(true);
				if(foodObservableList != null && filteredByNutrientList != null) {
					filteredByNutrientList = foodData.getAllFoodItems();
					//now we display only the food filtered by name, if any
					foodObservableList.setAll(filteredByNameList);
				}
			});
			
			
			
			Label foodListLabel = new Label("Food List");
			foodCountLabel = new Label();
			foodCountLabel.setText("0");
			Label foodCountDescription = new Label("Food Count");
			HBox foodCountListLabels = new HBox();
			foodListLabel.setStyle("-fx-font: 24 segoeui;");
			foodCountListLabels.getChildren().add(foodCountDescription);
			foodCountListLabels.getChildren().add(foodCountLabel);
			vBoxLeft.getChildren().add(foodListLabel);
			vBoxLeft.getChildren().add(listViewAddFoodHBox);
		    vBoxLeft.getChildren().add(hbox2);
		    vBoxLeft.getChildren().add(nutrientQuery);
		    
		    addNewFoodSendToMeal.getChildren().add(foodCountListLabels);
		    
		  
			//SET THE STYLING AND FORMATTING HERE!!
		    
		    foodCountListLabels.setPadding(new Insets(10,0,0,10));
		    foodCountListLabels.setSpacing(10);
			listViewAddFoodHBox.setPadding(new Insets(0,5,0,10));
			listViewAddFoodHBox.setSpacing(10);
			
			foodListLabel.setPadding(new Insets(10,0,0,10));
			
			hbox2.setPadding(new Insets(10,10,5,10));
			hbox2.setSpacing(10);
			
			addNewFoodSendToMeal.setPadding(new Insets(0,5,5,10));
			addNewFoodSendToMeal.setSpacing(10);
			
			
			nutrientQuery.setPadding(new Insets(10,5,5,10));
			nutrientQuery.setSpacing(10);;
			
			
			vBoxRight.setPadding(new Insets(5,5,5,5));
			vBoxRight.setSpacing(5);
			//spacing and padding end

			//command sets color of list view
			foodListView.setStyle("-fx-control-inner-background: #DCF3FF");	
			 //sets style for background for the entire box
		    
			vBoxLeft.setStyle("-fx-background-color: #7aadff");
		    dropMenuPanel.setStyle("-fx-background-color: #7aadff");
		    vBoxRight.setStyle("-fx-background-color: #7aadff");
		    mealListView.setStyle("-fx-control-inner-background: #DCF3FF");
		    
		    //put file chooser at top, foodList stuff on left
		    //mealList stuff on right
		    
		    root.setTop(dropMenuPanel);
		    root.setLeft(vBoxLeft);
		    root.setRight(vBoxRight);
		    
		    //sets the color of our center
		    HBox centerBox = new HBox();
		    centerBox.setStyle("-fx-background-color: #7aadff");
		    root.setCenter(centerBox);
		    
		    // Setting scene to stage and displaying stage.
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		    primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to display a given string as a popup error message
	 * @param errorText
	 */
	private void displayErrorMessage(String errorText) {
		
		Alert errorAlert = new Alert(AlertType.ERROR);
		
		errorAlert.setHeaderText("Error");
		errorAlert.setContentText(errorText);
		errorAlert.showAndWait();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}