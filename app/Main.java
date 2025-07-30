package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application

{
    public static Stage stage;
    public double x,y;

    @Override
    public void start(Stage mainStage) throws Exception
    {
        stage = mainStage;
        Parent root = FXMLLoader.load(getClass().getResource("/views/Menu.fxml"));
        addDragListeners(root);

        Scene scene = new Scene(root);
        mainStage.initStyle(StageStyle.TRANSPARENT);

        mainStage.setScene(scene);
        mainStage.show();
    }

    private void addDragListeners(Parent root) {
        root.setOnMousePressed(e -> {
            x = e.getSceneX();
            y = e.getSceneY();
        });

        root.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - x);
            stage.setY(e.getScreenY() - y);
            stage.setOpacity(.8);
        });

        root.setOnMouseReleased(e -> stage.setOpacity(1));
    }

    public static void main(String[] args)
    {
        launch();
    }
}
