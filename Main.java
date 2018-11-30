package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

public class Main extends Application {
	ListView<String> foodListView;
	ListView<String> mealListView;
	ArrayList<String> food;
	TextField foodInput,calorieInput, nameFilter;
	TableView table;
	MenuBar dropMenu;
	VBox vBoxLeft;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Food Query");
			BorderPane root = new BorderPane();
			VBox vBoxRight = new VBox();
			HBox hbox = new HBox();
			HBox hbox2 = new HBox();
			Scene scene = new Scene(root,1280,720);
			
			food = new ArrayList();
			foodListView = new ListView<>();
			//table = new TableView();
			foodInput = new TextField();
			//calorieInput = new TextField();
			nameFilter = new TextField();
			vBoxRight.getChildren().add(getData());
			
			// Left Vertical Box - Meal List
			vBoxLeft = new VBox();

			Label mealListLabel = new Label("Meal List");
			mealListLabel.setStyle("-fx-font: 24 segoeui;");
			
			mealListView = new ListView<>();
			mealListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			mealListView.setPrefHeight(700);
			mealListView.setPrefWidth(350);
			
			Button delMealItemButton = new Button("Delete");
			
			vBoxLeft.getChildren().add(mealListLabel);
			vBoxLeft.getChildren().add(mealListView);
			vBoxLeft.getChildren().add(delMealItemButton);
			
			// Drop down menu - MenuBar
			FileChooser fileChooser = new FileChooser();
			dropMenu = new MenuBar();
			Menu menuFile = new Menu("File");
			menuFile.setOnShowing(e -> {  }); // TODO: Add events
			menuFile.setOnShown  (e -> {  });
			menuFile.setOnHiding (e -> {  });
			menuFile.setOnHidden (e -> {  });
			MenuItem menuFoodList = new MenuItem("Load Food List");
			menuFoodList.setOnAction(e -> {
			    fileChooser.showOpenDialog(primaryStage);
			});
			menuFile.getItems().add(menuFoodList);
			MenuItem menuMealList = new MenuItem("Load Meal List");
			menuMealList.setOnAction(e -> {
			    fileChooser.showOpenDialog(primaryStage);
			});
			menuFile.getItems().add(menuMealList);
			dropMenu.getMenus().add(menuFile);
			HBox dropMenuPanel = new HBox(dropMenu);
			
			
			/**
			TableColumn foodColumn = new TableColumn("Food");
			foodColumn.setCellFactory(new PropertyValueFactory<>("food"));
			TableColumn calorieColumn = new TableColumn("Calories");
			calorieColumn.setCellFactory(new PropertyValueFactory<>("calorie"));
			table.setItems(getFood());
			table.getColumns().addAll(foodColumn,calorieColumn);
			**/
			foodListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			foodListView.setPrefHeight(700);
			foodInput.setPromptText("food");
			//calorieInput.setPromptText("calorie");
			nameFilter.setPromptText("name filter");
			Button addButton = new Button("Add");
			Button deleteButton = new Button("Delete");
			Button nameFilterButton = new Button("Filter");
			Button nameUnfilterButton = new Button("Unfilter");
			hbox.getChildren().addAll(foodInput, addButton, deleteButton);
			hbox2.getChildren().addAll(nameFilter,nameFilterButton, nameUnfilterButton);
			//hbox.getChildren().addAll(foodInput, calorieInput, addButton, deleteButton);
			addButton.setOnAction((ActionEvent e) -> {
				//if(!(foodInput.getText().equals(""))) {
				//	listView.getItems().add(foodInput.getText());
				//}
				//foodInput.clear();
				Stage addFoodWindow = new Stage();
				BorderPane bp2 = new BorderPane();
				VBox vbox2 = new VBox();
				TextField foodName = new TextField();
				TextField calorieCount = new TextField();
				TextField fatGrams = new TextField();
				TextField carbGrams = new TextField();
				TextField fiberGrams = new TextField();
				TextField proteinGrams = new TextField();
				Label title = new Label();
				title.setText("Add food item with its nutrients");
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
				Button submit = new Button("submit");
				submit.setOnAction(r -> addFoodWindow.close());
				vbox2.getChildren().addAll(title,foodName,calorieCount,fatGrams,
						carbGrams,fiberGrams,proteinGrams,submit);
				bp2.setCenter(vbox2);
				Scene popupScene = new Scene(bp2, 750, 450);
				addFoodWindow.setTitle("Add Food Item");
				addFoodWindow.setScene(popupScene);
				addFoodWindow.show();
			});
			
			deleteButton.setOnAction((ActionEvent e) -> {
				ObservableList<String> delete = foodListView.getSelectionModel().getSelectedItems();
				foodListView.getItems().removeAll(delete);
				//foodListView.getItems().remove(foodInput.getText());
				//foodInput.clear();
			});
			nameFilterButton.setOnAction((ActionEvent e) -> {
				Iterator iter = foodListView.getItems().iterator();
				while(iter.hasNext()) {
					String next = (String) iter.next();
					if(food.contains(next) == false) {
						food.add(next);
					}
					if(!next.contains(nameFilter.getText())) {
						iter.remove();
					}
				}
			});
			nameUnfilterButton.setOnAction((ActionEvent e) -> {
				foodListView.getItems().clear();;
				foodListView.getItems().addAll(food);
				food.clear();
				//foodListView.setItems(food);
			});
		    //vBoxRight.getChildren().add(foodListView);
			vBoxRight.getChildren().add(foodListView);
		    vBoxRight.getChildren().add(hbox);
		    vBoxRight.getChildren().add(hbox2);
		    

			//spacing and padding start
			hbox.setPadding(new Insets(10,5,5,10));
			hbox.setSpacing(10);
			
			hbox2.setPadding(new Insets(10,10,5,10));
			hbox2.setSpacing(10);
			
			vBoxRight.setPadding(new Insets(5,5,5,5));
			
			
			hbox.setPadding(new Insets(10,5,5,10));
			hbox.setSpacing(10);
			
			
			
			vBoxLeft.setPadding(new Insets(5,5,5,5));
			//spacing and padding end

			//command sets color of list view
			foodListView.setStyle("-fx-control-inner-background: #DCF3FF");	
			 //sets style for background for the entire box
		    vBoxRight.setStyle("-fx-background-color: #7aadff");
		    dropMenuPanel.setStyle("-fx-background-color: #7aadff");
		    vBoxLeft.setStyle("-fx-background-color: #7aadff");
		    
		    
		    root.setTop(dropMenuPanel);
		    root.setRight(vBoxRight);
		    root.setLeft(vBoxLeft);
		    
		    
		    //
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
	
	public HBox getData() {
		HBox hbox = new HBox();
		Button btn = new Button("Upload Data");
		TextField text = new TextField();
		text.setPromptText("Food input file");
		
		btn.setOnAction((ActionEvent e) -> {
			String filename = text.getText();
			File file = new File(filename);
			try {
				Scanner sc = new Scanner(file);
				while(sc.hasNextLine()) {
					String line = sc.nextLine();
					foodListView.getItems().add(line);;
					System.out.println(line);
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
		hbox.getChildren().add(new Label("Food List:"));
		hbox.getChildren().add(text);
	    hbox.getChildren().add(btn);
	    hbox.setSpacing(10);
	    return hbox;
	}
	public ObservableList getFood() {
		ObservableList food = FXCollections.observableArrayList();
		return food;
	}
	public static void main(String[] args) {
		launch(args);
	}
}
