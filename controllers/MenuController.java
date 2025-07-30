package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Car;
import services.CarDataService;
import utils.CarCardBuilder;
import utils.CarValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    ObservableList<Car> carsList;
    GridPane cardGrid;
    final int cols = 5;

    @FXML private Button closeBtn;
    @FXML private AnchorPane mainLayout;
    @FXML private Button minimizeBtn;
    @FXML private TextField searchInput;
    @FXML private ScrollPane contents;
    @FXML private BorderPane mainMenu;
    @FXML private AnchorPane createSection;
    @FXML private Button toCreateSection;
    @FXML private Button toMainMenu;
    @FXML private TextField carMakeInput;
    @FXML private TextField carNameInput;
    @FXML private TextField carYearInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainLayout.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        cardGrid = new GridPane();
        cardGrid.getStyleClass().add("card-grid");
        contents.setContent(cardGrid);

        closeBtn.setOnMouseClicked(e -> System.exit(0));
        minimizeBtn.setOnMouseClicked(e -> {
            Stage stage = (Stage) mainLayout.getScene().getWindow();
            stage.setIconified(true);
        });

        carsList = CarDataService.loadCars();
        generateCards();
    }

    public void generateCards() {
        cardGrid.getChildren().clear();
        for (int i = 0; i < carsList.size(); i++) {
            Car car = carsList.get(i);
            VBox card = CarCardBuilder.buildCard(car, this::deleteData, this::editData);

            int col = i % cols;
            int row = i / cols;
            cardGrid.add(card, col, row);
        }
    }

    public void switchSection(ActionEvent event){
        if (event.getSource() == toCreateSection) {
            mainMenu.setVisible(false);
            createSection.setVisible(true);
        } else if (event.getSource() == toMainMenu) {
            mainMenu.setVisible(true);
            createSection.setVisible(false);
        }
    }

    @FXML
    void saveData(ActionEvent event) {
        String carName = carNameInput.getText();
        String carMake = carMakeInput.getText();
        String strCarYear = carYearInput.getText();

        if (!CarValidator.isValid(carName, carMake, strCarYear)) return;

        int carYear = Integer.parseInt(strCarYear);
        int id = carsList.isEmpty() ? 1 : carsList.get(carsList.size() - 1).getId() + 1;

        Car newCar = new Car(id, carName, carMake, carYear);
        carsList.add(newCar);
        CarDataService.saveCars(carsList);

        carNameInput.clear();
        carMakeInput.clear();
        carYearInput.clear();

        generateCards();
        mainMenu.setVisible(true);
        createSection.setVisible(false);
    }

    public void deleteData(int id) {
        if (!confirmationAlert("delete")) return;
        carsList.removeIf(car -> car.getId() == id);
        CarDataService.saveCars(carsList);
        generateCards();
    }

    public void editData(int id) {

    }

    public boolean confirmationAlert(String action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to " + action + " this data?");
        if (action.equalsIgnoreCase("delete"))
            alert.setContentText("The data will be removed permanently!");
        alert.showAndWait();

        return alert.getResult() == ButtonType.OK;
    }
}
