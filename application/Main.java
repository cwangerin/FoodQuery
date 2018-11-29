package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Main extends Application {
	ListView<String> listView;
	ArrayList<String> food;
	TextField foodInput,calorieInput, nameFilter;
	TableView table;
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Hello World!");
			BorderPane root = new BorderPane();
			VBox vbox = new VBox();
			HBox hbox = new HBox();
			HBox hbox2 = new HBox();
			Scene scene = new Scene(root,1280,720);
			food = new ArrayList();
			listView = new ListView<>();
			//table = new TableView();
			foodInput = new TextField();
			//calorieInput = new TextField();
			nameFilter = new TextField();
			vbox.getChildren().add(getData());
			
			/**
			TableColumn foodColumn = new TableColumn("Food");
			foodColumn.setCellFactory(new PropertyValueFactory<>("food"));
			TableColumn calorieColumn = new TableColumn("Calories");
			calorieColumn.setCellFactory(new PropertyValueFactory<>("calorie"));
			table.setItems(getFood());
			table.getColumns().addAll(foodColumn,calorieColumn);
			**/
			listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
				listView.getItems().add(foodInput.getText());
				foodInput.clear();
			});
			deleteButton.setOnAction((ActionEvent e) -> {
				ObservableList<String> delete = listView.getSelectionModel().getSelectedItems();
				listView.getItems().removeAll(delete);
				//listView.getItems().remove(foodInput.getText());
				//foodInput.clear();
			});
			nameFilterButton.setOnAction((ActionEvent e) -> {
				Iterator iter = listView.getItems().iterator();
				while(iter.hasNext()) {
					String next = (String) iter.next();
					food.add(next);
					if(!next.contains(nameFilter.getText())) {
						iter.remove();
					}
				}
			});
			nameUnfilterButton.setOnAction((ActionEvent e) -> {
				listView.getItems().clear();;
				listView.getItems().addAll(food);
				food.clear();
				//listView.setItems(food);
			});
		    //vbox.getChildren().add(listView);
			vbox.getChildren().add(listView);
		    vbox.getChildren().add(hbox);
		    vbox.getChildren().add(hbox2);
		    vbox.setStyle("-fx-background-color: red");
		    
		    root.setRight(vbox);
		    
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
		text.setPromptText("file");
		
		btn.setOnAction((ActionEvent e) -> {
			String filename = text.getText();
			File file = new File(filename);
			try {
				Scanner sc = new Scanner(file);
				while(sc.hasNextLine()) {
					String line = sc.nextLine();
					listView.getItems().add(line);;
					System.out.println(line);
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
		hbox.getChildren().add(new Label("import:"));
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