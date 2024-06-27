package net.programa.sklep.programsklep;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import net.programa.sklep.programsklep.Database.User;

public class TopupView {
    public Spinner to_add;

    public static void open_form(Stage stage) {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("topup-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 320, 340);
        } catch (Exception e){
            utils.showDialog(Alert.AlertType.ERROR,  "Error","Error while creating form", e.getMessage(), rs -> null);
            System.exit(-1);
            return;
        }
        stage.setTitle(User.currentUser.login + " top up");
        stage.setScene(scene);
    }


    public void top_up(ActionEvent actionEvent) {
        User.currentUser.balance += Double.parseDouble(to_add.getValue().toString());
        try {
            User.currentUser.update();
        } catch (Exception e) {
            utils.showDialog(Alert.AlertType.ERROR,  "Error","Error while topping up.", e.getMessage(), rs -> null);
        }
        User.currentUser.ShowForm();
    }

    public void back(ActionEvent actionEvent) {
        User.currentUser.ShowForm();
    }
}
