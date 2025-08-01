package controllers;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import models.Car;

import java.util.function.Consumer;

public class CarCardController {
    @FXML private Label nameLabel;
    @FXML private Label makeLabel;
    @FXML private Label yearLabel;
    @FXML private Button deleteBtn;
    @FXML private Button editBtn;
    @FXML private AnchorPane rootPane;

    private Car currentCar;

    public void setData(Car car, Consumer<Integer> onDelete, Runnable onEdit) {
        this.currentCar = car;

        nameLabel.setText("Car: " + car.getName());
        makeLabel.setText("Make: " + car.getMake());
        yearLabel.setText("Year: " + car.getYear());

        deleteBtn.setOnAction(e -> onDelete.accept(car.getId()));
        editBtn.setOnAction(e -> onEdit.run());
    }

    public void updateCar(Car updatedCar) {
        this.currentCar = updatedCar;

        nameLabel.setText("Car: " + updatedCar.getName());
        makeLabel.setText("Make: " + updatedCar.getMake());
        yearLabel.setText("Year: " + updatedCar.getYear());
    }

    public void highlightUpdate() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), rootPane);
        fadeIn.setFromValue(1.0);
        fadeIn.setToValue(0.5);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), rootPane);
        fadeOut.setFromValue(0.5);
        fadeOut.setToValue(1.0);

        SequentialTransition flash = new SequentialTransition(fadeIn, fadeOut);
        flash.setCycleCount(2);
        flash.play();
    }
}
