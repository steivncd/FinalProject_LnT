package view;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Main;
import model.Pudding;
import service.MenuService;

public class HomePage {

	private Stage stage;
	private BorderPane root = new BorderPane();
	private GridPane gp = new GridPane();
	private Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
	private Label headerLbl = new Label("Add New Menu");
	private Label idLbl = new Label("ID");
	private Label nameLbl = new Label("Name");
	private Label priceLbl = new Label("Price");
	private Label stockLbl = new Label("Stock");
	private TextField idTf = new TextField();
	private TextField nameTf = new TextField();
	private TextField priceTf = new TextField();
	private TextField stockTf = new TextField();
	private TableView<Pudding> table = new TableView<>();
	private TableColumn<Pudding, String> idCol = new TableColumn<>("ID");
	private TableColumn<Pudding, String> nameCol = new TableColumn<>("Name");
	private TableColumn<Pudding, Integer> priceCol = new TableColumn<>("Price");
	private TableColumn<Pudding, Integer> stockCol = new TableColumn<>("Stock");
	private Button addBtn = new Button("Add");
	private Button updBtn = new Button("Update");
	private Button delBtn = new Button("Delete");
	private HBox buttonBox = new HBox(addBtn, updBtn, delBtn);
	
	private ObservableList<Pudding> puddingList = FXCollections.observableArrayList();
	private Pudding selectedPudding;
	
	public HomePage(Stage stage) {
		this.stage = stage;
		this.setComponent();
		this.setStyle();
		this.setTableColumns();
		this.populateTable();
		this.handleButton();
		this.handleTableListener();
	}
	
	@SuppressWarnings("unchecked")
	private void setComponent() {
		gp.add(headerLbl, 0, 0, 2, 1);
		gp.add(idLbl, 0, 1);
		gp.add(idTf, 1, 1);
		gp.add(nameLbl, 0, 2);
		gp.add(nameTf, 1, 2);
		gp.add(priceLbl, 0, 3);
		gp.add(priceTf, 1, 3);
		gp.add(stockLbl, 0, 4);
		gp.add(stockTf, 1, 4);
		gp.add(buttonBox, 0, 5, 2, 1);
		
		table.getColumns().addAll(idCol, nameCol, priceCol, stockCol);
		
		root.setTop(table);
		root.setCenter(gp);
		stage.setScene(scene);
	}
	
	private void setStyle() {
		root.setPadding(new Insets(30));
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(15);
		gp.setVgap(15);
		GridPane.setHalignment(headerLbl, HPos.CENTER);
		GridPane.setHalignment(addBtn, HPos.CENTER);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		buttonBox.setAlignment(Pos.CENTER);
		addBtn.setMinWidth(100);
		updBtn.setMinWidth(100);
		delBtn.setMinWidth(100);
	
	}
	
	private void populateTable() {
		puddingList = MenuService.getAllItems();
		table.setItems(puddingList);
		this.clearSelection();
	}
	
	private void clearSelection() {
		idTf.clear();
		nameTf.clear();
		priceTf.clear();
		stockTf.clear();
	}
	
	private void setTableColumns() {
		idCol.setCellValueFactory(new PropertyValueFactory<Pudding, String>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<Pudding, String>("name"));
		priceCol.setCellValueFactory(new PropertyValueFactory<Pudding, Integer>("price"));
		stockCol.setCellValueFactory(new PropertyValueFactory<Pudding, Integer>("stock"));
	}
	
	private void handleButton() {
		addBtn.setOnAction(event -> {
			String id = idTf.getText();
			String name = nameTf.getText();
			String price = priceTf.getText();
			String stock = stockTf.getText();
			
			if (id.isEmpty() || name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
				alert(AlertType.ERROR, "Error", "Validation Error", "All fields must be filled!");
				return;
			}
			
			for (Pudding pudding : puddingList) {
				if (pudding.getId().equals(id)) {
					alert(AlertType.ERROR, "Error", "Validation Error", "ID must be unique!");
					return;
				}
			}
			
			if (!id.startsWith("PD-")) {
				alert(AlertType.ERROR, "Error", "Validation Error", "ID must start with PD-!");
				return;
			}
			
			try {
				Integer.valueOf(price);
			} catch (Exception e) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Price must be numeric!");
				return;
			}
			
			if (!(Integer.valueOf(price) >= 15000)) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Price cannot be lower than 15000!");
				return;
			}
			
			try {
				Integer.valueOf(stock);
			} catch (Exception e) {
				alert(AlertType.ERROR, "Error", "Validation Error", "Stock must be numeric!");
				return;
			}
			
			MenuService.save(new Pudding(id, name, Integer.valueOf(price), Integer.valueOf(stock)));
			this.populateTable();
			alert(AlertType.INFORMATION, "Message", "Information", "Menu Successfully Added!");
			
		});
		
		updBtn.setOnAction(e -> {
			String id = selectedPudding.getId();
			String name = nameTf.getText();
			String price = priceTf.getText();
			String stock = stockTf.getText();
			
			MenuService.update(new Pudding(id, name, Integer.parseInt(price), Integer.parseInt(stock)));
			this.populateTable();
			alert(AlertType.INFORMATION, "Message", "Information", "Menu Successfully Updated!");
		});
		
		delBtn.setOnAction(e -> {
			MenuService.delete(selectedPudding);
			this.populateTable();
			alert(AlertType.INFORMATION, "Message", "Information", "Menu Successfully Deleted!");
		});
		
	}
	
	private void handleTableListener() {
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				selectedPudding = newValue;
				idTf.setText(newValue.getId());
				nameTf.setText(newValue.getName());
				priceTf.setText(String.valueOf(newValue.getPrice()));
				stockTf.setText(String.valueOf(newValue.getStock()));
			}
		});
	}
	
	private void alert(AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.initOwner(stage);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
}