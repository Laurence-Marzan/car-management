package utils;

import controllers.CarCardController;
import javafx.scene.layout.AnchorPane;

public class CardBundle {
    public final AnchorPane card;
    public final CarCardController controller;

    public CardBundle(AnchorPane card, CarCardController controller) {
        this.card = card;
        this.controller = controller;
    }
}
