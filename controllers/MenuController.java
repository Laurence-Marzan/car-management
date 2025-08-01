package controllers;

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
import utils.CardBundle;

import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    ObservableList<Car> carsList;
    GridPane cardGrid;
    final int cols = 5;
    private Car carBeingEdited = null;
    private final Map<Integer, CardBundle> carCardMap = new HashMap<>();

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
        carCardMap.clear();

        for (int i = 0; i < carsList.size(); i++) {
            Car car = carsList.get(i);
            CardBundle bundle = CarCardBuilder.buildCard(
                    car,
                    this::deleteData,
                    () -> loadCarIntoForm(findCarById(car.getId()))
            );

            int col = i % cols;
            int row = i / cols;

            cardGrid.add(bundle.card, col, row);
            carCardMap.put(car.getId(), bundle);
        }
    }

    private Car findCarById(int id) {
        for (Car c : carsList) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public void switchSection(ActionEvent event){
        if (event.getSource() == toCreateSection) {
            clearForm();
            carBeingEdited = null;
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

        if (carBeingEdited == null) {
            // Create a new car
            int id = carsList.isEmpty() ? 1 : carsList.get(carsList.size() - 1).getId() + 1;
            Car newCar = new Car(id, carName, carMake, carYear);
            carsList.add(newCar);

            CardBundle bundle = CarCardBuilder.buildCard(newCar, this::deleteData, () -> loadCarIntoForm(newCar));
            int index = carsList.size() - 1;
            int col = index % cols;
            int row = index / cols;
            cardGrid.add(bundle.card, col, row);

            carCardMap.put(newCar.getId(), bundle);
        } else {
            // Edit existing car
            carBeingEdited.setName(carName);
            carBeingEdited.setMake(carMake);
            carBeingEdited.setYear(carYear);

            CardBundle bundle = carCardMap.get(carBeingEdited.getId());
            if (bundle.controller != null) {
                bundle.controller.updateCar(carBeingEdited);
                bundle.controller.highlightUpdate();
            }

            carBeingEdited = null;
        }

        CarDataService.saveCars(carsList);
        clearForm();

        mainMenu.setVisible(true);
        createSection.setVisible(false);
    }

    public void deleteData(int id) {
        if (!confirmationAlert("delete")) return;

        carsList.removeIf(car -> car.getId() == id);

        CardBundle bundle = carCardMap.get(id);
        if (bundle != null) {
            cardGrid.getChildren().remove(bundle.card);
            carCardMap.remove(id);

            // Maintain compact grid
            rearrangeCards();
        }

        CarDataService.saveCars(carsList);
    }

    private void rearrangeCards() {
        cardGrid.getChildren().clear();

        int i = 0;
        for (Car car : carsList) {
            CardBundle bundle = carCardMap.get(car.getId());
            if (bundle != null) {
                int col = i % cols;
                int row = i / cols;
                cardGrid.add(bundle.card, col, row);
                i++;
            }
        }
    }

    private void loadCarIntoForm(Car car) {
        carBeingEdited = car;
        carNameInput.setText(car.getName());
        carMakeInput.setText(car.getMake());
        carYearInput.setText(String.valueOf(car.getYear()));

        mainMenu.setVisible(false);
        createSection.setVisible(true);
    }

    public void clearForm() {
        carNameInput.clear();
        carMakeInput.clear();
        carYearInput.clear();
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
