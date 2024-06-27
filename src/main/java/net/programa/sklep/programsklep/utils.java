package net.programa.sklep.programsklep;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.function.Function;

public class utils {
    public static void showDialog(Alert.AlertType alertType, String title, String headerText, String contentText, Function<ButtonType, Void> buttonFunction) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait().ifPresent(buttonFunction::apply);
    }
}
