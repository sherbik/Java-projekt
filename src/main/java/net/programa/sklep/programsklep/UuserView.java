package net.programa.sklep.programsklep;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.programa.sklep.programsklep.Database.User;

public class UuserView {
    public Text bal;

    public static void open_form(Stage stage) {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("uuser-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 320, 340);
        } catch (Exception e){
            utils.showDialog(Alert.AlertType.ERROR,  "Error","Error while creating form", e.getMessage(), rs -> null);
            System.exit(-1);
            return;
        }
        UuserView c = fxmlLoader.getController();
        c.bal.setText(String.format("%.2f", User.currentUser.balance));
        stage.setTitle(User.currentUser.login + " main page");
        stage.setScene(scene);
    }

    public void top_up(ActionEvent actionEvent) {
        TopupView.open_form(Application.currentStage);
    }

    public void to_shop(ActionEvent actionEvent) {
        ShopViewController.open_form(Application.currentStage);
    }

    public void logout(ActionEvent actionEvent) {
        User.currentUser = null;
        Controller.open_form(Application.currentStage);
    }
}
