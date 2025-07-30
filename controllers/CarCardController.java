package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.Car;

import java.util.function.Consumer;

public class CarCardController {
    @FXML private Label nameLabel;
    @FXML private Label makeLabel;
    @FXML private Label yearLabel;
    @FXML private Button deleteBtn;
    @FXML private Button editBtn;

    public void setData(Car car, Consumer<Integer> onDelete, Runnable onEdit) {
        nameLabel.setText("Car: " + car.getName());
        makeLabel.setText("Make: " + car.getMake());
        yearLabel.setText("Year: " + car.getYear());

        deleteBtn.setOnAction(e -> onDelete.accept(car.getId()));
        editBtn.setOnAction(e -> onEdit.run());
    }
}
