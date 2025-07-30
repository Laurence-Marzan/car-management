package utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Car;

import java.util.function.Consumer;

public class CarCardBuilder
{
    public static VBox buildCard(Car car, Consumer<Integer> onDelete, Runnable onEdit) {
        Label name = new Label("Car: " + car.getName());
        Label make = new Label("Make: " + car.getMake());
        Label year = new Label("Year: " + car.getYear());

        Button delete = new Button("Delete " + car.getId());
        delete.setOnAction(e -> onDelete.accept(car.getId()));

        Button edit = new Button("Edit");
        edit.setOnAction(e -> onEdit.run());

        HBox actions = new HBox(10, delete, edit);
        VBox card = new VBox(name, make, year, actions);
        card.getStyleClass().add("card");

        return card;
    }
}
