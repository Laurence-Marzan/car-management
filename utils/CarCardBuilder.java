package utils;

import controllers.CarCardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Car;

import java.io.IOException;
import java.util.function.Consumer;

public class CarCardBuilder
{
    public static AnchorPane buildCard(Car car, Consumer<Integer> onDelete, Runnable onEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(CarCardBuilder.class.getResource("/views/components/CarCard.fxml"));
            AnchorPane card = loader.load();

            CarCardController controller = loader.getController();
            controller.setData(car, onDelete, onEdit);

            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return new AnchorPane(); // fallback
        }
    }
}
