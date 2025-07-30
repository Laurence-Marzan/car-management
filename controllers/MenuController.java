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

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable
{
    private static final String DATA_FILE = "resources/data.csv";
    ObservableList<Car> carsList;
    GridPane cardGrid;
    final int cols = 5;

    // --------------
    // FXML Variables
    // --------------
    @FXML
    private Button closeBtn;

    @FXML
    private AnchorPane mainLayout;

    @FXML
    private Button minimizeBtn;

    @FXML
    private TextField searchInput;

    @FXML
    private ScrollPane contents;

    // Sections
    @FXML
    private BorderPane mainMenu;
    @FXML
    private AnchorPane createSection;

    // Navigator Buttons
    @FXML
    private Button toCreateSection;
    @FXML
    private Button toMainMenu;

    // Form Fields
    @FXML
    private TextField carMakeInput;
    @FXML
    private TextField carNameInput;
    @FXML
    private TextField carYearInput;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        mainLayout.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        cardGrid = new GridPane();
        cardGrid.getStyleClass().add("card-grid");
        contents.setContent(cardGrid);

        closeBtn.setOnMouseClicked(e -> System.exit(0));
        minimizeBtn.setOnMouseClicked(e -> {
            Stage stage = (Stage) mainLayout.getScene().getWindow();
            stage.setIconified(true);
        });

        carsList = FXCollections.observableArrayList();

        readData();
        generateCards();
    }

    public void readData() {
        carsList.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE));
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    String[] info = line.split(",");
                    Car carObj = new Car(
                            Integer.parseInt(info[0]),
                            info[1],
                            info[2],
                            Integer.parseInt(info[3])
                    );
                    carsList.add(carObj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeData(String filePath, ObservableList<Car> carsList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))){
            bw.write("id,name,make,year\n");
            for (Car car : carsList) {
                bw.write(String.format("%d,%s,%s,%d%n", car.getId(), car.getName(), car.getMake(), car.getYear()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateCards() {
        int dataSize = carsList.size();

        cardGrid.getChildren().clear();

        for (int i = 0;i < dataSize;i++) {
            Label nameLabel = new Label("models.Car: " + carsList.get(i).getName());
            Label makeLabel = new Label("Make: " + carsList.get(i).getMake());
            Label yearLabel = new Label("Year: " + carsList.get(i).getYear());

            HBox actionBtnBox = new HBox(10);
            Button deleteBtn = new Button("Delete " + carsList.get(i).getId());
            Button editBtn = new Button("Edit");
            int finalI = i;
            deleteBtn.setOnMouseClicked(e -> {
                deleteData(carsList.get(finalI).getId());
            });
            actionBtnBox.getChildren().addAll(deleteBtn, editBtn);

            VBox card = new VBox();

            card.getChildren().addAll(nameLabel, makeLabel, yearLabel, actionBtnBox);
            card.getStyleClass().add("card");

            int col = i % cols;
            int row = i / cols;

            cardGrid.add(card, col, row);
        }
    }

    public void switchSection(ActionEvent event){
        if (event.getSource() == toCreateSection) {
            mainMenu.setVisible(false);
            createSection.setVisible(true);
        }
        else if (event.getSource() == toMainMenu) {
            mainMenu.setVisible(true);
            createSection.setVisible(false);
        }
    }

    @FXML
    void saveData(ActionEvent event) {
        if (carNameInput.getText().isEmpty() || carMakeInput.getText().isEmpty() || carYearInput.getText().isEmpty()){
            return;
        }

        String carName = carNameInput.getText();
        String carMake = carMakeInput.getText();
        String strCarYear = carYearInput.getText();

        boolean isValid = inputChecker(carName, carMake, strCarYear);

        if (!isValid) {
            return;
        }

        int carYear = Integer.parseInt(strCarYear);
        int id = carsList.isEmpty() ? 1 : carsList.get(carsList.size() - 1).getId() + 1;

        Car newCar = new Car(id, carName, carMake, carYear);
        carsList.add(newCar);
        writeData(DATA_FILE, carsList);

        carNameInput.clear();
        carMakeInput.clear();
        carYearInput.clear();

        generateCards();

        mainMenu.setVisible(true);
        createSection.setVisible(false);
    }

    public void deleteData(int id) {
        if (!confirmationAlert("delete"))
            return;

        carsList.removeIf(car -> car.getId() == id);
        writeData(DATA_FILE, carsList);
        generateCards();
    }

    public boolean inputChecker(String carName, String carMake, String carYear) {
        boolean isValid = true;

//        if (carYear.) {
//            isValid = false;
//        }

        return isValid;
    }

    public boolean confirmationAlert(String action) {
        boolean confirmed = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to " + action + " this data?");
        if (action.equalsIgnoreCase("delete"))
            alert.setContentText("The data will be removed permanently!");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK)
            confirmed = true;

        return confirmed;
    }
}
